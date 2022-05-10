package org.appland.settlers.assets;

import org.json.simple.JSONObject;

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

        // Find the max width and height, and the max number of images over all flag types
        Utils.RowLayoutInfo aggregatedLayoutInfo = new Utils.RowLayoutInfo();

        for (Nation nation : Nation.values()) {
            for (FlagType flagType : FlagType.values()) {

                List<Bitmap> images = this.flagMap.get(nation).get(flagType);

                Utils.RowLayoutInfo layoutInfo = Utils.layoutInfoFromImageSeries(images);

                aggregatedLayoutInfo.aggregate(layoutInfo);
            }
        }

        // Write the image atlas, one row per flag, and collect metadata to write as json
        ImageBoard imageBoard = new ImageBoard();

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

                int y = aggregatedLayoutInfo.getRowHeight() * (flagIndex + FlagType.values().length * nationIndex);

                jsonFlagInfo.put("startX", 0);
                jsonFlagInfo.put("startY", y);
                jsonFlagInfo.put("width", aggregatedLayoutInfo.getImageWidth());
                jsonFlagInfo.put("height", aggregatedLayoutInfo.getImageHeight());
                jsonFlagInfo.put("nrImages", images.size());
                jsonFlagInfo.put("offsetX", aggregatedLayoutInfo.maxNx);
                jsonFlagInfo.put("offsetY", aggregatedLayoutInfo.maxNy);

                int imageIndex = 0;
                for (Bitmap image : images) {
                    int adjustedX = imageIndex * aggregatedLayoutInfo.getImageWidth() + aggregatedLayoutInfo.maxNx - image.nx;
                    int adjustedY = y + aggregatedLayoutInfo.maxNy - image.ny;

                    imageBoard.placeImage(image, adjustedX, adjustedY);

                    imageIndex = imageIndex + 1;
                }

                flagIndex = flagIndex + 1;
            }

            nationIndex = nationIndex + 1;
        }

        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-flags.png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-flags.json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImageForFlag(Nation nation, FlagType flagType, Bitmap image) {
        this.flagMap.get(nation).get(flagType).add(image);
    }
}
