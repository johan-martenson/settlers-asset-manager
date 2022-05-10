package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StonesImageCollection {
    private final Map<StoneType, Map<StoneAmount, Bitmap>> stoneMap;

    public StonesImageCollection() {
        stoneMap = new HashMap<>();

        for (StoneType type : StoneType.values()) {
            stoneMap.put(type, new HashMap<>());
        }
    }

    public void addImage(StoneType type, StoneAmount growth, Bitmap image) {
        stoneMap.get(type).put(growth, image);
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {

        // Calculate the size of the image atlas
        int maxWidth = 0;
        int maxHeight = 0;

        for (StoneType StoneType : StoneType.values()) {
            for (StoneAmount StoneAmount : StoneAmount.values()) {

                Bitmap image = stoneMap.get(StoneType).get(StoneAmount);

                maxWidth = Math.max(maxWidth, image.width);
                maxHeight = Math.max(maxHeight, image.height);
            }
        }

        // Create the image atlas
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        int StoneTypeIndex = 0;
        for (Map.Entry<StoneType, Map<StoneAmount, Bitmap>> entryForStoneType : this.stoneMap.entrySet()) {

            JSONObject jsonStoneType = new JSONObject();

            jsonImageAtlas.put(entryForStoneType.getKey().name().toUpperCase(), jsonStoneType);

            int StoneAmountIndex = 0;
            for (StoneAmount StoneAmount : StoneAmount.values()) {

                Bitmap image = entryForStoneType.getValue().get(StoneAmount);

                int x = maxWidth * StoneAmountIndex;
                int y = maxHeight * StoneTypeIndex;

                imageBoard.placeImage(image, x, y);

                JSONObject jsonCropImage = imageBoard.imageLocationToJson(image);

                jsonStoneType.put(StoneAmount.name().toUpperCase(), jsonCropImage);

                StoneAmountIndex = StoneAmountIndex + 1;
            }

            StoneTypeIndex = StoneTypeIndex + 1;
        }

        // Write the image atlas to disk
        imageBoard.writeBoardToBitmap(palette).writeToFile(toDir + "/image-atlas-stones.png");

        Files.writeString(Paths.get(toDir, "image-atlas-stones.json"), jsonImageAtlas.toJSONString());
    }
}
