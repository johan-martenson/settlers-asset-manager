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

            jsonImageAtlas.put(nation.name().toLowerCase(), jsonNation);

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

            jsonLandBorder.put("x", x);
            jsonLandBorder.put("y", y);
            jsonLandBorder.put("width", borderForNation.landBorder.width);
            jsonLandBorder.put("height", borderForNation.landBorder.height);
            jsonLandBorder.put("offsetX", borderForNation.landBorder.nx);
            jsonLandBorder.put("offsetY", borderForNation.landBorder.ny);

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

            jsonCoastBorder.put("x", x);
            jsonCoastBorder.put("y", y);
            jsonCoastBorder.put("width", borderForNation.coastBorder.width);
            jsonCoastBorder.put("height", borderForNation.coastBorder.height);
            jsonCoastBorder.put("offsetX", borderForNation.coastBorder.nx);
            jsonCoastBorder.put("offsetY", borderForNation.coastBorder.ny);

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
