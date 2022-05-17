package org.appland.settlers.assets.collectors;

import org.appland.settlers.assets.Bitmap;
import org.appland.settlers.assets.Decoration;
import org.appland.settlers.assets.ImageBoard;
import org.appland.settlers.assets.Palette;
import org.json.simple.JSONObject;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DecorativeImageCollection {
    private final Map<Decoration, DecorationImage> decorations;

    public DecorativeImageCollection() {
        decorations = new HashMap<>();
    }

    public void addDecorationImage(Decoration decoration, Bitmap image) {
        decorations.put(decoration, new DecorationImage(image));
    }

    public void addDecorationImageWithShadow(Decoration decoration, Bitmap image, Bitmap shadowImage) {
        decorations.put(decoration, new DecorationImage(image, shadowImage));
    }

    public void writeImageAtlas(String dir, Palette palette) throws IOException {

        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        Point cursor = new Point(0, 0);

        for (Map.Entry<Decoration, DecorationImage> entry : this.decorations.entrySet()) {
            Decoration decoration = entry.getKey();
            Bitmap image = entry.getValue().image;
            Bitmap shadowImage = entry.getValue().shadowImage;

            int rowHeight = 0;
            cursor.x = 0;

            JSONObject jsonDecoration = new JSONObject();

            jsonImageAtlas.put(decoration.name().toUpperCase(), jsonDecoration);

            // Decoration image
            imageBoard.placeImage(image, cursor);

            jsonDecoration.put("image", imageBoard.imageLocationToJson(image));

            rowHeight = image.height;

            cursor.x = cursor.x + image.width;

            // Decoration shadow image
            if (shadowImage != null) {
                imageBoard.placeImage(shadowImage, cursor);

                jsonDecoration.put("shadowImage", imageBoard.imageLocationToJson(shadowImage));

                rowHeight = Math.max(rowHeight, shadowImage.height);
            }

            cursor.y = cursor.y + rowHeight;
        }

        // Write to file
        imageBoard.writeBoardToBitmap(palette).writeToFile(dir + "/image-atlas-decorations.png");

        Files.writeString(Paths.get(dir, "image-atlas-decorations.json"), jsonImageAtlas.toJSONString());
    }

    private class DecorationImage {

        private final Bitmap image;
        private final Bitmap shadowImage;

        public DecorationImage(Bitmap image) {
            this.image = image;
            shadowImage = null;
        }

        public DecorationImage(Bitmap image, Bitmap shadowImage) {
            this.image = image;
            this.shadowImage = shadowImage;
        }
    }
}
