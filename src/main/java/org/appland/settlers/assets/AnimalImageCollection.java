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

        // Write the image atlas, one row per direction, and collect metadata to write as json
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the images into the image atlas
        Point cursor = new Point(0, 0);
        for (Direction direction : Direction.values()) {

            cursor.x = 0;

            List<Bitmap> directionImages = directionToImageMap.get(direction);
            NormalizedImageList directionNormalizedList = new NormalizedImageList(directionImages);
            List<Bitmap> normalizedDirectionImages = directionNormalizedList.getNormalizedImages();

            imageBoard.placeImageSeries(normalizedDirectionImages, cursor, ImageBoard.LayoutDirection.ROW);

            JSONObject jsonDirectionInfo = imageBoard.imageSeriesLocationToJson(normalizedDirectionImages);

            jsonImageAtlas.put(direction.name().toUpperCase(), jsonDirectionInfo);

            cursor.y = cursor.y + directionNormalizedList.getImageHeight();
        }

        // Write the image atlas
        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImages(Direction direction, List<Bitmap> images) {
        this.directionToImageMap.get(direction).addAll(images);
    }
}
