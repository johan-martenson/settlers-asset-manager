package org.appland.settlers.assets;

import org.appland.settlers.model.Tree;
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

public class TreeImageCollection {
    private final String name;
    private final Map<Tree.TreeType, List<Bitmap>> treeMap;

    public TreeImageCollection(String name) {
        this.name = name;
        treeMap = new HashMap<>();

        for (Tree.TreeType treeType : Tree.TreeType.values()) {
            treeMap.put(treeType, new ArrayList<>());
        }
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        // Find the max width and height, and the max number of images over all tree types
        Utils.RowLayoutInfo aggregatedLayoutInfo = new Utils.RowLayoutInfo();

        for (Tree.TreeType treeType : Tree.TreeType.values()) {

            List<Bitmap> images = this.treeMap.get(treeType);
            Utils.RowLayoutInfo layoutInfo = Utils.layoutInfoFromImageSeries(images);

            aggregatedLayoutInfo.aggregate(layoutInfo);
        }

        // Write the image atlas, one row per tree, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(
                aggregatedLayoutInfo.getRowWidth(),
                aggregatedLayoutInfo.getRowHeight() * Tree.TreeType.values().length,
                palette,
                TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        int treeIndex = 0;
        for (Tree.TreeType treeType : Tree.TreeType.values()) {

            List<Bitmap> images = this.treeMap.get(treeType);

            JSONObject jsonTreeInfo = new JSONObject();

            jsonImageAtlas.put(treeType.name().toUpperCase(), jsonTreeInfo);

            jsonTreeInfo.put("startX", 0);
            jsonTreeInfo.put("startY", treeIndex * aggregatedLayoutInfo.getRowHeight());
            jsonTreeInfo.put("width", aggregatedLayoutInfo.getImageWidth());
            jsonTreeInfo.put("height", aggregatedLayoutInfo.getImageHeight());
            jsonTreeInfo.put("nrImages", images.size());
            jsonTreeInfo.put("offsetX", aggregatedLayoutInfo.maxNx);
            jsonTreeInfo.put("offsetY", aggregatedLayoutInfo.maxNy);

            int imageIndex = 0;
            for (Bitmap image : images) {
                imageAtlas.copyNonTransparentPixels(
                        image,
                        aggregatedLayoutInfo.getTargetPositionForImageInRow(image, treeIndex, imageIndex),
                        new Point(0, 0),
                        image.getDimension());

                imageIndex = imageIndex + 1;
            }

            treeIndex = treeIndex + 1;
        }

        imageAtlas.writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImagesForTree(Tree.TreeType treeType, List<Bitmap> imagesFromResourceLocations) {
        this.treeMap.get(treeType).addAll(imagesFromResourceLocations);
    }
}
