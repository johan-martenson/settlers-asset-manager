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

public class WorkerImageCollection {
    private final String name;
    private final Map<Nation, Map<Direction, List<Bitmap>>> nationToDirectionToImageMap;

    public WorkerImageCollection(String name) {
        this.name = name;
        nationToDirectionToImageMap = new HashMap<>();

        for (Nation nation : Nation.values()) {

            this.nationToDirectionToImageMap.put(nation, new HashMap<>());

            for (Direction direction : Direction.values()) {
                this.nationToDirectionToImageMap.get(nation).put(direction, new ArrayList<>());
            }
        }
    }

    public void addImage(Nation nation, Direction direction, Bitmap workerImage) {
        this.nationToDirectionToImageMap.get(nation).get(direction).add(workerImage);
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {
        int maxHeight = 0;
        int maxWidth = 0;
        int maxImagesPerDirection = 0;

        // Find the max width and height, and the max number of images over all directions
        for (Nation nation : Nation.values()) {
            Map<Direction, List<Bitmap>> directionToImageMap = nationToDirectionToImageMap.get(nation);

            for (Direction direction : Direction.values()) {
                List<Bitmap> images = directionToImageMap.get(direction);

                for (Bitmap bitmap : images) {
                    maxWidth = Math.max(bitmap.getWidth(), maxWidth);
                    maxHeight = Math.max(bitmap.getHeight(), maxHeight);
                }

                maxImagesPerDirection = Math.max(maxImagesPerDirection, images.size());
            }
        }

        // Write the image atlas, one row per direction, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(maxWidth * maxImagesPerDirection, maxHeight * Direction.values().length * Nation.values().length, palette, TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        int n = 0;
        for (Nation nation : Nation.values()) {
            Map<Direction, List<Bitmap>> directionToImageMap = nationToDirectionToImageMap.get(nation);

            JSONObject jsonNationInfo = new JSONObject();

            jsonImageAtlas.put(nation.name().toLowerCase(), jsonNationInfo);

            int i = 0;
            for (Direction direction : Direction.values()) {

                JSONObject jsonDirectionInfo = new JSONObject();

                jsonNationInfo.put(direction.name().toUpperCase(), jsonDirectionInfo);

                jsonDirectionInfo.put("startX", 0);
                jsonDirectionInfo.put("startY", i * maxHeight + n * (maxHeight * 6));
                jsonDirectionInfo.put("width", maxWidth);
                jsonDirectionInfo.put("height", maxHeight);
                jsonDirectionInfo.put("nrImages", directionToImageMap.get(direction).size());

                int j = 0;
                for (Bitmap image : directionToImageMap.get(direction)) {
                    imageAtlas.copyNonTransparentPixels(image, new Point(j * maxWidth, i * maxHeight + n * (maxHeight * 6)), new Point(0, 0), image.getDimension());

                    j = j + 1;
                }

                i = i + 1;
            }

            n = n + 1;
        }

        imageAtlas.writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }
}
