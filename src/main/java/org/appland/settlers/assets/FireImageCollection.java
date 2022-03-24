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

        // Find the max width and height, and the max number of images over all tree types
        Utils.RowLayoutInfo aggregatedLayoutInfo = new Utils.RowLayoutInfo();

        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);

            Utils.RowLayoutInfo layoutInfo = Utils.layoutInfoFromImageSeries(images);

            aggregatedLayoutInfo.aggregate(layoutInfo);
        }

        // Write the image atlas, one row per tree, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(
                aggregatedLayoutInfo.getRowWidth(),
                aggregatedLayoutInfo.getRowHeight() * FireSize.values().length,
                palette,
                TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        int fireSizeIndex = 0;
        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);

            JSONObject jsonFireInfo = new JSONObject();

            jsonImageAtlas.put(fireSize.name().toUpperCase(), jsonFireInfo);

            jsonFireInfo.put("startX", 0);
            jsonFireInfo.put("startY", fireSizeIndex * aggregatedLayoutInfo.getRowHeight());
            jsonFireInfo.put("width", aggregatedLayoutInfo.getImageWidth());
            jsonFireInfo.put("height", aggregatedLayoutInfo.getImageHeight());
            jsonFireInfo.put("nrImages", images.size());
            jsonFireInfo.put("offsetX", aggregatedLayoutInfo.maxNx);
            jsonFireInfo.put("offsetY", aggregatedLayoutInfo.maxNy);

            int imageIndex = 0;
            for (Bitmap image : images) {
                imageAtlas.copyNonTransparentPixels(
                        image,
                        aggregatedLayoutInfo.getTargetPositionForImageInRow(image, fireSizeIndex, imageIndex),
                        new Point(0, 0),
                        image.getDimension()
                );

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
