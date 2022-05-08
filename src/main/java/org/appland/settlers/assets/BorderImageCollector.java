package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BorderImageCollector {
    private final Map<Nation, BorderForNation> borderMap;

    public BorderImageCollector() {
        borderMap = new HashMap<>();

        for (Nation nation : Nation.values()) {
            borderMap.put(nation, new BorderForNation());
        }
    }

    public void addLandBorderImage(Nation nation, Bitmap image) {
        borderMap.get(nation).setLandBorder(image);
    }

    public void addWaterBorderImage(Nation nation, Bitmap image) {
        borderMap.get(nation).setCoastBorder(image);
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {

        // Calculate the dimensions of the image atlas
        int maxHeight = 0;
        int maxWidth = 0;

        for (Nation nation : Nation.values()) {
            BorderForNation borderForNation = borderMap.get(nation);

            maxHeight = Math.max(maxHeight, Math.max(borderForNation.landBorder.height, borderForNation.coastBorder.height));
            maxWidth = Math.max(maxWidth, borderForNation.landBorder.width + borderForNation.coastBorder.width);
        }

        // Create the image atlas
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        int nationIndex = 0;
        for (Nation nation : Nation.values()) {

            JSONObject jsonNation = new JSONObject();

            jsonImageAtlas.put(nation.name().toLowerCase(), jsonNation);

            BorderForNation borderForNation = borderMap.get(nation);

            int x = 0;
            int y = nationIndex * maxHeight;

            imageBoard.placeImage(borderForNation.landBorder, x, y);

            JSONObject jsonLandBorder = imageBoard.imageLocationToJson(borderForNation.landBorder);

            jsonNation.put("landBorder", jsonLandBorder);

            x = borderForNation.landBorder.width;
            y = nationIndex * maxHeight;

            imageBoard.placeImage(borderForNation.coastBorder, x, y);

            JSONObject jsonCoastBorder = imageBoard.imageLocationToJson(borderForNation.coastBorder);

            jsonNation.put("coastBorder", jsonCoastBorder);

            nationIndex = nationIndex + 1;
        }

        // Write to file
        imageBoard.writeBoardToBitmap(palette).writeToFile(toDir + "/image-atlas-border.png");

        Files.writeString(Paths.get(toDir, "image-atlas-border.json"), jsonImageAtlas.toJSONString());
    }

    private class BorderForNation {
        private Bitmap landBorder;
        private Bitmap coastBorder;

        public void setLandBorder(Bitmap image) {
            landBorder = image;
        }

        public void setCoastBorder(Bitmap image) {
            coastBorder = image;
        }
    }
}
