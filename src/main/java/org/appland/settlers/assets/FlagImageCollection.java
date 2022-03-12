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

public class FlagImageCollection {
    private final Map<Nation, Map<FlagType, List<Bitmap>>> flagMap;

    public FlagImageCollection() {
        flagMap = new HashMap<>();

        for (Nation nation : Nation.values()) {

            flagMap.put(nation, new HashMap<>());

            for (FlagType flagType : FlagType.values()) {
                flagMap.get(nation).put(flagType, new ArrayList<>());
            }
        }
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {
        int maxHeight = 0;
        int maxWidth = 0;
        int maxImagesPerDirection = 0;

        // Find the max width and height, and the max number of images over all flag types
        for (Nation nation : Nation.values()) {
            for (FlagType flagType : FlagType.values()) {

                List<Bitmap> images = this.flagMap.get(nation).get(flagType);

                for (Bitmap bitmap : images) {
                    maxWidth = Math.max(bitmap.getWidth(), maxWidth);
                    maxHeight = Math.max(bitmap.getHeight(), maxHeight);
                }

                maxImagesPerDirection = Math.max(maxImagesPerDirection, images.size());
            }
        }

        // Write the image atlas, one row per flag, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(
                maxWidth * maxImagesPerDirection,
                maxHeight * FlagType.values().length * Nation.values().length,
                palette,
                TextureFormat.BGRA
        );

        JSONObject jsonImageAtlas = new JSONObject();

        int nationIndex = 0;
        for (Nation nation : Nation.values()) {

            JSONObject jsonNationInfo = new JSONObject();

            jsonImageAtlas.put(nation.name().toLowerCase(), jsonNationInfo);

            int flagIndex = 0;
            for (FlagType flagType : FlagType.values()) {

                List<Bitmap> images = this.flagMap.get(nation).get(flagType);

                JSONObject jsonFlagInfo = new JSONObject();

                jsonNationInfo.put(flagType.name().toUpperCase(), jsonFlagInfo);

                jsonFlagInfo.put("startX", 0);
                jsonFlagInfo.put("startY", flagIndex * maxHeight + FlagType.values().length * maxHeight * nationIndex);
                jsonFlagInfo.put("width", maxWidth);
                jsonFlagInfo.put("height", maxHeight);
                jsonFlagInfo.put("nrImages", images.size());

                int imageIndex = 0;
                for (Bitmap image : images) {
                    imageAtlas.copyNonTransparentPixels(image,
                            new Point(
                                    imageIndex * maxWidth,
                                    flagIndex * maxHeight + FlagType.values().length * maxHeight * nationIndex
                            ),
                            new Point(0, 0),
                            image.getDimension());

                    imageIndex = imageIndex + 1;
                }

                flagIndex = flagIndex + 1;
            }

            nationIndex = nationIndex + 1;
        }

        imageAtlas.writeToFile(directory + "/" + "image-atlas-flags.png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-flags.json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImageForFlag(Nation nation, FlagType flagType, Bitmap image) {
        this.flagMap.get(nation).get(flagType).add(image);
    }
}
