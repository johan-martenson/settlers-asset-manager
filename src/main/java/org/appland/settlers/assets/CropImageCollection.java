package org.appland.settlers.assets;

import org.appland.settlers.model.Crop;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CropImageCollection {
    private final Map<CropType, Map<Crop.GrowthState, Bitmap>> cropMap;

    public CropImageCollection() {
        cropMap = new HashMap<>();

        for (CropType type : CropType.values()) {
            cropMap.put(type, new HashMap<>());
        }
    }

    public void addImage(CropType type, Crop.GrowthState growth, Bitmap image) {
        cropMap.get(type).put(growth, image);
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {

        // Calculate the size of the image atlas
        int maxWidth = 0;
        int maxRowHeight = 0;

        for (CropType cropType : CropType.values()) {
            for (Crop.GrowthState cropGrowth : Crop.GrowthState.values()) {

                Bitmap image = cropMap.get(cropType).get(cropGrowth);

                maxWidth = Math.max(maxWidth, image.width);
                maxRowHeight = Math.max(maxRowHeight, image.height);
            }
        }

        // Create the image atlas
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        int cropTypeIndex = 0;
        for (Map.Entry<CropType, Map<Crop.GrowthState, Bitmap>> entryForCropType : this.cropMap.entrySet()) {

            JSONObject jsonCropType = new JSONObject();

            jsonImageAtlas.put(entryForCropType.getKey().name().toUpperCase(), jsonCropType);

            int cropGrowthIndex = 0;
            for (Crop.GrowthState cropGrowth : Crop.GrowthState.values()) {

                Bitmap image = entryForCropType.getValue().get(cropGrowth);

                int x = maxWidth * cropGrowthIndex;
                int y = maxRowHeight * cropTypeIndex;

                imageBoard.placeImage(image, x, y);

                JSONObject jsonCropImage = new JSONObject();

                jsonCropImage.put("x", x);
                jsonCropImage.put("y", y);
                jsonCropImage.put("width", image.width);
                jsonCropImage.put("height", image.height);
                jsonCropImage.put("offsetX", image.nx);
                jsonCropImage.put("offsetY", image.ny);

                jsonCropType.put(cropGrowth.name().toUpperCase(), jsonCropImage);

                cropGrowthIndex = cropGrowthIndex + 1;
            }

            cropTypeIndex = cropTypeIndex + 1;
        }

        // Write the image atlas to file(s)
        imageBoard.writeBoardToBitmap(palette).writeToFile(toDir + "/image-atlas-crops.png");

        Files.writeString(Paths.get(toDir, "image-atlas-crops.json"), jsonImageAtlas.toJSONString());
    }
}
