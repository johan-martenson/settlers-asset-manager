package org.appland.settlers.assets;

import org.appland.settlers.model.Tree;
import org.appland.settlers.model.TreeSize;
import org.json.simple.JSONObject;

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

        // Write the image atlas, one row per tree, and collect metadata to write as json
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        JSONObject jsonGrownTrees = new JSONObject();
        JSONObject jsonGrowingTrees = new JSONObject();
        JSONObject jsonFallingTrees = new JSONObject();

        jsonImageAtlas.put("grownTrees", jsonGrownTrees);
        jsonImageAtlas.put("growingTrees", jsonGrowingTrees);
        jsonImageAtlas.put("fallingTrees", jsonFallingTrees);

        int y = 0;
        int x;
        int rowHeight = 0;
        for (Tree.TreeType treeType : Tree.TreeType.values()) {

            x = 0;

            List<Bitmap> images = this.grownTreeMap.get(treeType);
            NormalizedImageList normalizedImageList = new NormalizedImageList(images);
            List<Bitmap> normalizedImages = normalizedImageList.getNormalizedImages();

            imageBoard.placeImageSeries(normalizedImages, x, y, ImageBoard.LayoutDirection.ROW);

            JSONObject jsonTreeInfo = imageBoard.imageSeriesLocationToJson(normalizedImages); //new JSONObject();

            jsonGrownTrees.put(treeType.name().toUpperCase(), jsonTreeInfo);

            x = normalizedImageList.size() * normalizedImageList.getImageWidth();
            rowHeight = normalizedImageList.getImageHeight();

            if (growingTreeMap.containsKey(treeType)) {

                JSONObject jsonGrowingTreeType = new JSONObject();

                jsonGrowingTrees.put(treeType.name().toUpperCase(), jsonGrowingTreeType);

                for (Map.Entry<TreeSize, Bitmap> entry : growingTreeMap.get(treeType).entrySet()) {
                    TreeSize treeSize = entry.getKey();
                    Bitmap image = entry.getValue();

                    imageBoard.placeImage(image, x, y);

                    JSONObject jsonGrowingTreeImage = imageBoard.imageLocationToJson(image);

                    jsonGrowingTreeType.put(treeSize.name().toUpperCase(), jsonGrowingTreeImage);

                    x = x + image.getWidth();
                    rowHeight = Math.max(rowHeight, image.getHeight());
                }
            }

            if (treeFalling.containsKey(treeType)) {

                List<Bitmap> fallingTreeImages = treeFalling.get(treeType);
                NormalizedImageList normalizedImageList1 = new NormalizedImageList(fallingTreeImages);
                List<Bitmap> normalizedFallingTreeImages = normalizedImageList1.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedFallingTreeImages, x, y, ImageBoard.LayoutDirection.ROW);

                JSONObject jsonFallingTreeImages = imageBoard.imageSeriesLocationToJson(normalizedFallingTreeImages); //new JSONObject();

                jsonFallingTrees.put(treeType.name().toUpperCase(), jsonFallingTreeImages);

                rowHeight = Math.max(rowHeight, normalizedImageList1.getImageHeight());
            }

            y = y + rowHeight;
        }

        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/image-atlas-" + name.toLowerCase() + ".png");

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
