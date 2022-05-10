package org.appland.settlers.assets;

import org.appland.settlers.model.Size;
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
    private final Map<Size, Bitmap> burntDownMap;

    public FireImageCollection() {
        fireMap = new HashMap<>();
        burntDownMap = new HashMap<>();

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

        Utils.RowLayoutInfo burntDownLayout = Utils.layoutInfoFromImageSeries(burntDownMap.values());

        // Write the image atlas, one row per tree, and collect metadata to write as json
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        JSONObject jsonFireAnimations = new JSONObject();
        JSONObject jsonBurntDownImages = new JSONObject();

        jsonImageAtlas.put("fires", jsonFireAnimations);
        jsonImageAtlas.put("burntDown", jsonBurntDownImages);

        int fireSizeIndex = 0;
        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);

            JSONObject jsonFireInfo = new JSONObject();

            jsonFireAnimations.put(fireSize.name().toUpperCase(), jsonFireInfo);

            jsonFireInfo.put("startX", 0);
            jsonFireInfo.put("startY", fireSizeIndex * aggregatedLayoutInfo.getRowHeight());
            jsonFireInfo.put("width", aggregatedLayoutInfo.getImageWidth());
            jsonFireInfo.put("height", aggregatedLayoutInfo.getImageHeight());
            jsonFireInfo.put("nrImages", images.size());
            jsonFireInfo.put("offsetX", aggregatedLayoutInfo.maxNx);
            jsonFireInfo.put("offsetY", aggregatedLayoutInfo.maxNy);

            int imageIndex = 0;
            for (Bitmap image : images) {
                Point point = aggregatedLayoutInfo.getTargetPositionForImageInRow(image, fireSizeIndex, imageIndex);

                imageBoard.placeImage(image, point);

                imageIndex = imageIndex + 1;
            }

            fireSizeIndex = fireSizeIndex + 1;
        }

        int y = FireSize.values().length * aggregatedLayoutInfo.getRowHeight();

        int imageIndex = 0;
        for (Map.Entry<Size, Bitmap> entry : burntDownMap.entrySet()) {
            Size size = entry.getKey();
            Bitmap image = entry.getValue();

            int x = imageIndex * burntDownLayout.getImageWidth();

            imageBoard.placeImage(image, x, y);

            JSONObject jsonBurntDownImage = imageBoard.imageLocationToJson(image);

            jsonBurntDownImages.put(size.name().toUpperCase(), jsonBurntDownImage);

            imageIndex = imageIndex + 1;
        }

        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-fire.png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-fire.json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImagesForFire(FireSize fireSize, List<Bitmap> imagesFromResourceLocations) {
        this.fireMap.get(fireSize).addAll(imagesFromResourceLocations);
    }

    public void addBurntDownImage(Size size, Bitmap image) {
        this.burntDownMap.put(size, image);
    }
}
