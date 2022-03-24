package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalImageCollection {
    private final String name;
    private final Map<Direction, List<Bitmap>> directionToImageMap;

    public AnimalImageCollection(String name) {
        this.name = name;
        directionToImageMap = new HashMap<>();

        for (Direction direction : Direction.values()) {
            this.directionToImageMap.put(direction, new ArrayList<>());
        }
    }

    public void addImage(Direction direction, Bitmap workerImage) {
        this.directionToImageMap.get(direction).add(workerImage);
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        // Find the max width and height, and the max number of images over all directions
        Utils.RowLayoutInfo aggregatedLayoutInfo = new Utils.RowLayoutInfo();

        for (Direction direction : Direction.values()) {
            List<Bitmap> images = directionToImageMap.get(direction);

            Utils.RowLayoutInfo layoutInfo = Utils.layoutInfoFromImageSeries(images);

            aggregatedLayoutInfo.aggregate(layoutInfo);
        }

        // Write the image atlas, one row per direction, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(
                aggregatedLayoutInfo.getRowWidth(),
                aggregatedLayoutInfo.getRowHeight() * Direction.values().length,
                palette,
                TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the images into the image atlas
        int directionIndex = 0;
        for (Direction direction : Direction.values()) {

            JSONObject jsonDirectionInfo = new JSONObject();

            jsonImageAtlas.put(direction.name().toUpperCase(), jsonDirectionInfo);

            jsonDirectionInfo.put("startX", 0);
            jsonDirectionInfo.put("startY", directionIndex * aggregatedLayoutInfo.getRowHeight());
            jsonDirectionInfo.put("width", aggregatedLayoutInfo.getImageWidth());
            jsonDirectionInfo.put("height", aggregatedLayoutInfo.getImageHeight());
            jsonDirectionInfo.put("nrImages", directionToImageMap.get(direction).size());
            jsonDirectionInfo.put("offsetX", aggregatedLayoutInfo.maxNx);
            jsonDirectionInfo.put("offsetY", aggregatedLayoutInfo.maxNy);

            int imageIndex = 0;
            for (Bitmap image : directionToImageMap.get(direction)) {
                imageAtlas.copyNonTransparentPixels(
                        image,
                        aggregatedLayoutInfo.getTargetPositionForImageInRow(image, directionIndex, imageIndex),
                        new Point(0, 0),
                        image.getDimension());

                imageIndex = imageIndex + 1;
            }

            directionIndex = directionIndex + 1;
        }

        imageAtlas.writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImages(Direction direction, List<Bitmap> images) {
        this.directionToImageMap.get(direction).addAll(images);
    }
}
