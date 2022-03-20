package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.awt.*;
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
        Bitmap imageAtlas = new Bitmap(maxWidth, maxHeight * Nation.values().length, palette, TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        int nationIndex = 0;
        for (Nation nation : Nation.values()) {

            JSONObject jsonNation = new JSONObject();

            jsonImageAtlas.put(nation.name().toUpperCase(), jsonNation);

            BorderForNation borderForNation = borderMap.get(nation);

            int x = 0;
            int y = nationIndex * maxHeight;

            imageAtlas.copyNonTransparentPixels(
                    borderForNation.landBorder,
                    new Point(x, y),
                    new Point(0, 0),
                    borderForNation.landBorder.getDimension()
            );

            JSONObject jsonLandBorder = new JSONObject();

            jsonNation.put("landBorder", jsonLandBorder);

            jsonLandBorder.put("startX", x);
            jsonLandBorder.put("startY", y);
            jsonLandBorder.put("width", maxWidth);
            jsonLandBorder.put("height", maxHeight);

            x = borderForNation.landBorder.width;
            y = nationIndex * maxHeight;

            imageAtlas.copyNonTransparentPixels(
                    borderForNation.coastBorder,
                    new Point(x, y),
                    new Point(0, 0),
                    borderForNation.coastBorder.getDimension()
            );

            JSONObject jsonCoastBorder = new JSONObject();

            jsonNation.put("coastBorder", jsonCoastBorder);

            jsonCoastBorder.put("startX", x);
            jsonCoastBorder.put("startY", y);
            jsonCoastBorder.put("width", maxWidth);
            jsonCoastBorder.put("height", maxHeight);

            nationIndex = nationIndex + 1;
        }

        // Write to file
        imageAtlas.writeToFile(toDir + "/image-atlas-border.png");

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
