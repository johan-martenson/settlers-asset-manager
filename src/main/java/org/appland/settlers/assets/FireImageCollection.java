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

public class FireImageCollection {
    private final Map<FireSize, List<Bitmap>> fireMap;

    public FireImageCollection() {
        fireMap = new HashMap<>();

        for (FireSize fireSize : FireSize.values()) {
            fireMap.put(fireSize, new ArrayList<>());
        }
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {
        int maxHeight = 0;
        int maxWidth = 0;
        int maxImagesPerDirection = 0;

        // Find the max width and height, and the max number of images over all tree types
        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);

            for (Bitmap bitmap : images) {
                maxWidth = Math.max(bitmap.getWidth(), maxWidth);
                maxHeight = Math.max(bitmap.getHeight(), maxHeight);
            }

            maxImagesPerDirection = Math.max(maxImagesPerDirection, images.size());
        }

        // Write the image atlas, one row per tree, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(maxWidth * maxImagesPerDirection, maxHeight * FireSize.values().length, palette, TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        int i = 0;
        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);

            JSONObject jsonFireInfo = new JSONObject();

            jsonImageAtlas.put(fireSize.name().toUpperCase(), jsonFireInfo);

            jsonFireInfo.put("startX", 0);
            jsonFireInfo.put("startY", i * maxHeight);
            jsonFireInfo.put("width", maxWidth);
            jsonFireInfo.put("height", maxHeight);
            jsonFireInfo.put("nrImages", images.size());

            int j = 0;
            for (Bitmap image : images) {
                imageAtlas.copyNonTransparentPixels(image, new Point(j * maxWidth, i * maxHeight), new Point(0, 0), image.getDimension());

                j = j + 1;
            }

            i = i + 1;
        }

        imageAtlas.writeToFile(directory + "/" + "image-atlas-fire.png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-fire.json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImagesForFire(FireSize fireSize, List<Bitmap> imagesFromResourceLocations) {
        this.fireMap.get(fireSize).addAll(imagesFromResourceLocations);
    }
}
