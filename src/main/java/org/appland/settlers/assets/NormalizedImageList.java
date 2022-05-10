package org.appland.settlers.assets;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class NormalizedImageList {
    private final List<Bitmap> originalImages;
    private final List<Bitmap> normalizedImages;
    public int width;
    public int height;
    public int nx;
    public int ny;

    public NormalizedImageList(List<Bitmap> images) {

        int maxWidth = 0;
        int maxHeight = 0;
        int maxNx = 0;
        int maxNy = 0;
        int minNx = 0;
        int minNy = 0;

        // Calculate the normalized width, height, nx, and ny
        for (Bitmap image : images) {
            maxWidth = Math.max(maxWidth, image.width);
            maxHeight = Math.max(maxHeight, image.height);

            maxNx = Math.max(maxNx, image.nx);
            maxNy = Math.max(maxNy, image.ny);

            minNx = Math.min(minNx, image.nx);
            minNy = Math.min(minNy, image.ny);
        }

        this.width = maxWidth;
        this.height = maxHeight;
        this.nx = maxNx;
        this.ny = maxNy;

        // Store the originals
        this.originalImages = images;

        // Create a list of adjusted images where they all share the same width, height, and offsets
        this.normalizedImages = new ArrayList<>();

        for (int i = 0; i < originalImages.size(); i++) {
            Bitmap originalImage = originalImages.get(i);
            Bitmap normalizedImage = new Bitmap(maxWidth, maxHeight, originalImages.get(0).getPalette(), TextureFormat.BGRA);

            normalizedImage.copyNonTransparentPixels(
                    originalImage,
                    new Point(maxNx - originalImage.nx, maxNy - originalImage.ny),
                    new Point(0, 0),
                    originalImage.getDimension()
            );

            normalizedImage.nx = maxNx;
            normalizedImage.ny = maxNy;

            this.normalizedImages.add(normalizedImage);
        }
    }

    List<Bitmap> getNormalizedImages() {
        return this.normalizedImages;
    }

    public int getImageHeight() {
        return height;
    }

    public int getImageWidth() {
        return width;
    }

    public Point getDrawOffset() {
        return new Point(nx, ny);
    }
}
