package org.appland.settlers.assets;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ImageBoard {

    private final List<ImageOnBoard> images;

    public ImageBoard() {
        images = new ArrayList<>();
    }

    void placeImage(Bitmap image, Point point) {
        images.add(new ImageOnBoard(image, point.x, point.y));
    }

    void placeImage(Bitmap image, int x, int y) {
        images.add(new ImageOnBoard(image, x, y));
    }

    Bitmap writeBoardToBitmap(Palette palette) {

        // Calculate the needed size of the bitmap
        int width = 0;
        int height = 0;

        for (ImageOnBoard imageOnBoard : images) {
            width = Math.max(width, imageOnBoard.x + imageOnBoard.image.width);
            height = Math.max(height, imageOnBoard.y + imageOnBoard.image.height);
        }

        // Create the bitmap
        Bitmap imageBoard = new Bitmap(width, height, palette, TextureFormat.BGRA);

        // Copy all images onto the board
        for (ImageOnBoard imageOnBoard : images) {
            imageBoard.copyNonTransparentPixels(
                    imageOnBoard.image,
                    new Point(imageOnBoard.x, imageOnBoard.y),
                    new Point(0, 0),
                    imageOnBoard.image.getDimension());
        }

        return imageBoard;
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
