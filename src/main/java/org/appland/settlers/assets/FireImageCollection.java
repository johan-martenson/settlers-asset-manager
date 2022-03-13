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

        int maxNx = 0;
        int minNx = 0;
        int maxNy = 0;
        int minNy = 0;

        // Find the max width and height, and the max number of images over all tree types
        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);

            for (Bitmap bitmap : images) {
                maxWidth = Math.max(bitmap.getWidth(), maxWidth);
                maxHeight = Math.max(bitmap.getHeight(), maxHeight);

                maxNx = Math.max(maxNx, bitmap.nx);
                maxNy = Math.max(maxNy, bitmap.ny);

                minNx = Math.min(minNx, bitmap.nx);
                minNy = Math.min(minNy, bitmap.ny);
            }

            maxImagesPerDirection = Math.max(maxImagesPerDirection, images.size());
        }

        // Write the image atlas, one row per tree, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(
                (maxWidth + maxNx) * maxImagesPerDirection,
                (maxHeight + maxNy) * FireSize.values().length,
                palette,
                TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        int fireSizeIndex = 0;
        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);

            JSONObject jsonFireInfo = new JSONObject();

            jsonImageAtlas.put(fireSize.name().toUpperCase(), jsonFireInfo);

            jsonFireInfo.put("startX", 0);
            jsonFireInfo.put("startY", fireSizeIndex * (maxHeight + maxNy));
            jsonFireInfo.put("width", maxWidth + maxNx);
            jsonFireInfo.put("height", maxHeight + maxNx);
            jsonFireInfo.put("nrImages", images.size());

            int imageIndex = 0;
            for (Bitmap image : images) {
                imageAtlas.copyNonTransparentPixels(image,
                        new Point(imageIndex * (maxWidth + maxNx) + maxNx - image.nx, fireSizeIndex * (maxHeight + maxNy)  + maxNy - image.ny),
                        new Point(0, 0),
                        new Dimension(image.width, image.height));

                imageIndex = imageIndex + 1;
            }

            fireSizeIndex = fireSizeIndex + 1;
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
