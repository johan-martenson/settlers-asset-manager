package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ImageBoard {

    private final Map<Bitmap, ImageOnBoard> images;

    public ImageBoard() {
        images = new HashMap<>();
    }

    void placeImage(Bitmap image, Point point) {
        images.put(image, new ImageOnBoard(image, point.x, point.y));
    }

    void placeImage(Bitmap image, int x, int y) {
        images.put(image, new ImageOnBoard(image, x, y));
    }

    Bitmap writeBoardToBitmap(Palette palette) {

        // Calculate the needed size of the bitmap
        int width = 0;
        int height = 0;

        for (ImageOnBoard imageOnBoard : images.values()) {
            width = Math.max(width, imageOnBoard.x + imageOnBoard.image.width);
            height = Math.max(height, imageOnBoard.y + imageOnBoard.image.height);
        }

        // Create the bitmap
        Bitmap imageBoard = new Bitmap(width, height, palette, TextureFormat.BGRA);

        // Copy all images onto the board
        for (ImageOnBoard imageOnBoard : images.values()) {
            imageBoard.copyNonTransparentPixels(
                    imageOnBoard.image,
                    new Point(imageOnBoard.x, imageOnBoard.y),
                    new Point(0, 0),
                    imageOnBoard.image.getDimension());
        }

        return imageBoard;
    }

    JSONObject imageLocationToJson(Bitmap image) {
        ImageOnBoard imageOnBoard = images.get(image);

        JSONObject jsonImageLocation = new JSONObject();

        jsonImageLocation.put("x", imageOnBoard.x);
        jsonImageLocation.put("y", imageOnBoard.y);
        jsonImageLocation.put("width", image.width);
        jsonImageLocation.put("height", image.height);
        jsonImageLocation.put("offsetX", image.nx);
        jsonImageLocation.put("offsetY", image.ny);

        return jsonImageLocation;
    }

    private class ImageOnBoard {
        private final int x;
        private final int y;
        private final Bitmap image;

        public ImageOnBoard(Bitmap image, int x, int y) {
            this.x = x;
            this.y = y;

            this.image = image;
        }
    }
}
