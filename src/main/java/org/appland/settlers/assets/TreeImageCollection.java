package org.appland.settlers.assets;

import org.appland.settlers.model.Tree;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class TreeImageCollection {
    private final String name;
    private final Map<Tree.TreeType, List<Bitmap>> grownTreeMap;
    private final Map<Tree.TreeType, Map<TreeSize, Bitmap>> growingTreeMap;
    private final Map<Tree.TreeType, List<Bitmap>> treeFalling;

    public TreeImageCollection(String name) {
        this.name = name;
        grownTreeMap = new HashMap<>();

        for (Tree.TreeType treeType : Tree.TreeType.values()) {
            grownTreeMap.put(treeType, new ArrayList<>());
        }

        growingTreeMap = new HashMap<>();
        treeFalling = new HashMap<>();
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        // Find the max width and height, and the max number of images over all tree types
        Utils.RowLayoutInfo grownTreesAggregatedLayoutInfo = new Utils.RowLayoutInfo();
        Utils.RowLayoutInfo growingTreesAggregatedLayoutInfo = new Utils.RowLayoutInfo();
        Utils.RowLayoutInfo fallingTreesAggregatedLayoutInfo = new Utils.RowLayoutInfo();

        for (Tree.TreeType treeType : Tree.TreeType.values()) {

            List<Bitmap> grownTreeImages = this.grownTreeMap.get(treeType);

            if (growingTreeMap.containsKey(treeType)) {
                Collection<Bitmap> growingTreeImages = this.growingTreeMap.get(treeType).values();
                Utils.RowLayoutInfo growingTreeLayoutInfo = Utils.layoutInfoFromImageSeries(growingTreeImages);
                growingTreesAggregatedLayoutInfo.aggregate(growingTreeLayoutInfo);
            }

            if (treeFalling.containsKey(treeType)) {
                List<Bitmap> fallingTreeImages = this.treeFalling.get(treeType);
                Utils.RowLayoutInfo fallingTreeLayoutInfo = Utils.layoutInfoFromImageSeries(fallingTreeImages);
                fallingTreesAggregatedLayoutInfo.aggregate(fallingTreeLayoutInfo);
            }

            Utils.RowLayoutInfo grownTreeLayoutInfo = Utils.layoutInfoFromImageSeries(grownTreeImages);

            grownTreesAggregatedLayoutInfo.aggregate(grownTreeLayoutInfo);
        }

        int totalWidth = grownTreesAggregatedLayoutInfo.getRowWidth() +
                growingTreesAggregatedLayoutInfo.getRowWidth() +
                fallingTreesAggregatedLayoutInfo.getRowWidth();

        int maxRowHeight = Math.max(
                Math.max(
                        grownTreesAggregatedLayoutInfo.getRowHeight(),
                        growingTreesAggregatedLayoutInfo.getRowHeight()
                ),
                fallingTreesAggregatedLayoutInfo.getRowHeight()
        );

        // Write the image atlas, one row per tree, and collect metadata to write as json
        Bitmap imageAtlas = new Bitmap(
                totalWidth,
                maxRowHeight * Tree.TreeType.values().length,
                palette,
                TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        JSONObject jsonGrownTrees = new JSONObject();
        JSONObject jsonGrowingTrees = new JSONObject();
        JSONObject jsonFallingTrees = new JSONObject();

        jsonImageAtlas.put("grownTrees", jsonGrownTrees);
        jsonImageAtlas.put("growingTrees", jsonGrowingTrees);
        jsonImageAtlas.put("fallingTrees", jsonFallingTrees);

        int treeIndex = 0;
        for (Tree.TreeType treeType : Tree.TreeType.values()) {

            List<Bitmap> images = this.grownTreeMap.get(treeType);

            JSONObject jsonTreeInfo = new JSONObject();

            jsonGrownTrees.put(treeType.name().toUpperCase(), jsonTreeInfo);

            jsonTreeInfo.put("startX", 0);
            jsonTreeInfo.put("startY", treeIndex * grownTreesAggregatedLayoutInfo.getRowHeight());
            jsonTreeInfo.put("width", grownTreesAggregatedLayoutInfo.getImageWidth());
            jsonTreeInfo.put("height", grownTreesAggregatedLayoutInfo.getImageHeight());
            jsonTreeInfo.put("nrImages", images.size());
            jsonTreeInfo.put("offsetX", grownTreesAggregatedLayoutInfo.maxNx);
            jsonTreeInfo.put("offsetY", grownTreesAggregatedLayoutInfo.maxNy);

            int imageIndex = 0;
            for (Bitmap image : images) {
                imageAtlas.copyNonTransparentPixels(
                        image,
                        grownTreesAggregatedLayoutInfo.getTargetPositionForImageInRow(image, treeIndex, imageIndex),
                        new Point(0, 0),
                        image.getDimension());

                imageIndex = imageIndex + 1;
            }

            if (growingTreeMap.containsKey(treeType)) {

                JSONObject jsonGrowingTreeType = new JSONObject();

                jsonGrowingTrees.put(treeType.name().toUpperCase(), jsonGrowingTreeType);

                imageIndex = 0;
                for (Map.Entry<TreeSize, Bitmap> entry : growingTreeMap.get(treeType).entrySet()) {
                    TreeSize treeSize = entry.getKey();
                    Bitmap image = entry.getValue();

                    int x = grownTreesAggregatedLayoutInfo.getRowWidth() + imageIndex * growingTreesAggregatedLayoutInfo.getImageWidth();
                    int y = maxRowHeight * treeIndex;

                    JSONObject jsonGrowingTreeImage = new JSONObject();

                    jsonGrowingTreeType.put(treeSize.name().toUpperCase(), jsonGrowingTreeImage);

                    jsonGrowingTreeImage.put("x", x);
                    jsonGrowingTreeImage.put("y", y);
                    jsonGrowingTreeImage.put("width", image.width);
                    jsonGrowingTreeImage.put("height", image.height);
                    jsonGrowingTreeImage.put("offsetX", image.nx);
                    jsonGrowingTreeImage.put("offsetY", image.ny);

                    imageAtlas.copyNonTransparentPixels(
                            image,
                            new Point(x, y),
                            new Point(0, 0),
                            image.getDimension()
                    );

                    imageIndex = imageIndex + 1;
                }
            }

            if (treeFalling.containsKey(treeType)) {

                int startX = grownTreesAggregatedLayoutInfo.getRowWidth() + growingTreesAggregatedLayoutInfo.getRowWidth();
                int startY = maxRowHeight * treeIndex;

                JSONObject jsonFallingTreeImages = new JSONObject();

                jsonFallingTrees.put(treeType.name().toUpperCase(), jsonFallingTreeImages);

                jsonFallingTreeImages.put("startX", startX);
                jsonFallingTreeImages.put("startY", startY);
                jsonFallingTreeImages.put("width", fallingTreesAggregatedLayoutInfo.getImageWidth());
                jsonFallingTreeImages.put("height", fallingTreesAggregatedLayoutInfo.getImageHeight());
                jsonFallingTreeImages.put("nrImages", treeFalling.get(treeType).size());
                jsonFallingTreeImages.put("offsetX", fallingTreesAggregatedLayoutInfo.maxNx);
                jsonFallingTreeImages.put("offsetY", fallingTreesAggregatedLayoutInfo.maxNy);

                imageIndex = 0;
                for (Bitmap image : treeFalling.get(treeType)) {

                    Point unadjusted = grownTreesAggregatedLayoutInfo.getTargetPositionForImageInRow(image, treeIndex, imageIndex);

                    imageAtlas.copyNonTransparentPixels(
                            image,
                            new Point(unadjusted.x + startX, unadjusted.y),
                            new Point(0, 0),
                            image.getDimension()
                    );

                    imageIndex = imageIndex + 1;
                }
            }

            treeIndex = treeIndex + 1;
        }

        imageAtlas.writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImagesForTree(Tree.TreeType treeType, List<Bitmap> imagesFromResourceLocations) {
        this.grownTreeMap.get(treeType).addAll(imagesFromResourceLocations);
    }

    public void addImageForGrowingTree(Tree.TreeType type, TreeSize treeSize, Bitmap image) {
        if (!growingTreeMap.containsKey(type)) {
            growingTreeMap.put(type, new HashMap<>());
        }

        growingTreeMap.get(type).put(treeSize, image);
    }

    public void addImagesForTreeFalling(Tree.TreeType type, List<Bitmap> images) {
        treeFalling.put(type, images);
    }
}
