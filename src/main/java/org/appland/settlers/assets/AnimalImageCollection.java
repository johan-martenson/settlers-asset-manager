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
        int maxHeight = 0;
        int maxWidth = 0;
        int maxImagesPerDirection = 0;

        // Find the max width and height, and the max number of images over all directions
        for (Direction direction : Direction.values()) {
            List<Bitmap> images = directionToImageMap.get(direction);

            for (Bitmap bitmap : images) {
                maxWidth = Math.max(bitmap.getWidth(), maxWidth);
                maxHeight = Math.max(bitmap.getHeight(), maxHeight);
            }

            maxImagesPerDirection = Math.max(maxImagesPerDirection, images.size());
        }

        // Write the image atlas, one row per direction, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(maxWidth * maxImagesPerDirection, maxHeight * Direction.values().length, palette, TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the images into the image atlas
        int i = 0;
        for (Direction direction : Direction.values()) {

            JSONObject jsonDirectionInfo = new JSONObject();

            jsonImageAtlas.put(direction.name().toUpperCase(), jsonDirectionInfo);

            jsonDirectionInfo.put("startX", 0);
            jsonDirectionInfo.put("startY", i * maxHeight);
            jsonDirectionInfo.put("width", maxWidth);
            jsonDirectionInfo.put("height", maxHeight);
            jsonDirectionInfo.put("nrImages", directionToImageMap.get(direction).size());

            int j = 0;
            for (Bitmap image : directionToImageMap.get(direction)) {
                imageAtlas.copyNonTransparentPixels(image, new Point(j * maxWidth, i * maxHeight), new Point(0, 0), image.getDimension());

                j = j + 1;
            }

            i = i + 1;
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
