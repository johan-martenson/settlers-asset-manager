package org.appland.settlers.assets;

import javax.imageio.ImageIO;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Bitmap {
    private final int bitsPerPixel;
    protected final int height;
    protected final int width;
    private final TextureFormat format;

    protected byte[] imageData; // uint 8

    private Palette palette;
    private boolean debug = false;

    public Bitmap(int width, int height, Palette palette, TextureFormat format) {
        this.width = width;
        this.height = height;
        this.palette = palette;
        this.format = format;

        if (format == TextureFormat.BGRA) {

            if (debug) {
                System.out.println("    ++++ Set bits per pixel to: " + 4);
            }

            bitsPerPixel = 4;
        } else {

            if (debug) {
                System.out.println("    ++++ Set bits per pixel to: " + 1);
            }

            bitsPerPixel = 1;
        }

        imageData = new byte[width * height * bitsPerPixel];

        if (debug) {
            System.out.println("    ++++ Image size is: " + imageData.length);
            System.out.println("    ++++ Should be: " + width * height * bitsPerPixel);
        }
    }

    public void setPixelByColorIndex(int x, int y, short colorIndex) { // uint 16, uint 16, uint 8

        /* If format is paletted - assign the color index */
        if (format == TextureFormat.PALETTED) {
            imageData[(y * width + x) * bitsPerPixel] = (byte)(colorIndex & 0xFF);

        /* If format is BGRA - look up the real color and assign */
        } else if (format == TextureFormat.BGRA) {

            /* Handle transparency */
            if (palette.isColorIndexTransparent(colorIndex)) {

                //System.out.println("IS TRANSPARENT!");

                imageData[(y * width + x) * bitsPerPixel + 3] = 0;

            /* Look up the color and assign the individual parts */
            } else {
                RGBColor colorRGB = palette.getColorForIndex(colorIndex);

                //System.out.println("         - Color: " + colorRGB);

                imageData[(y * width + x) * bitsPerPixel] = colorRGB.getBlue();
                imageData[(y * width + x) * bitsPerPixel + 1] = colorRGB.getGreen();
                imageData[(y * width + x) * bitsPerPixel + 2] = colorRGB.getRed();
                imageData[(y * width + x) * bitsPerPixel + 3] = (byte)0xFF;
            }
        } else {
            throw new RuntimeException("Cannot set pixel in format " + format);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void writeToFile(String filename) throws IOException {
        int samplesPerPixel = 4;
        int[] bandOffsets = {2, 1, 0, 3}; // BGRA order

        System.out.println(width);
        System.out.println(height);
        System.out.println(width * height * 4);
        System.out.println(imageData.length);

        DataBuffer buffer = new DataBufferByte(imageData, imageData.length);
        WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, samplesPerPixel * width, samplesPerPixel, bandOffsets, null);

        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

        BufferedImage image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);

        if (debug) {
            System.out.println("image: " + image); // Should print: image: BufferedImage@<hash>: type = 0 ...
        }

        ImageIO.write(image, "PNG", new File(filename));
    }

    public void setImageDataFromBuffer(byte[] data) {
        this.imageData = data;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public void setPixelValue(int x, int y, byte red, byte green, byte blue, byte transparency) {
        if (format == TextureFormat.BGRA) {
            int offset = (y * width + x) * this.bitsPerPixel;

            this.imageData[offset] = blue;
            this.imageData[offset + 1] = green;
            this.imageData[offset + 2] = red;
            this.imageData[offset + 3] = transparency;
        } else {
            throw new RuntimeException("Can only set pixel value for BGRA format. Not " + format);
        }
    }

    public byte[] getImageData() {
        return this.imageData;
    }

    public int getBitsPerPixel() {
        return bitsPerPixel;
    }

    public TextureFormat getFormat() {
        return format;
    }

    public Palette getPalette() {
        return palette;
    }

    public byte getBlueAsByte(int x, int y) {
        if (format == TextureFormat.BGRA) {
            return imageData[(y * width + x) * 4];
        } else if (format == TextureFormat.BGR) {
            return imageData[(y * width + x) * 3];
        } else if (format == TextureFormat.PALETTED) {
            return palette.getBlueAsByte(
                imageData[y * width + x]
            );
        }

        throw new RuntimeException("Can't manage format " + format);
    }

    public byte getGreenAsByte(int x, int y) {
        if (format == TextureFormat.BGRA) {
            return imageData[(y * width + x) * 4 + 1];
        } else if (format == TextureFormat.BGR) {
            return imageData[(y * width + x) * 3 + 1];
        } else if (format == TextureFormat.PALETTED) {
            return palette.getGreenAsByte(
                    imageData[y * width + x]
            );
        }

        throw new RuntimeException("Can't manage format " + format);
    }

    public byte getRedAsByte(int x, int y) {
        if (format == TextureFormat.BGRA) {
            return imageData[(y * width + x) * 4 + 2];
        } else if (format == TextureFormat.BGR) {
            return imageData[(y * width + x) * 3 + 2];
        } else if (format == TextureFormat.PALETTED) {
            return palette.getRedAsByte(
                    imageData[y * width + x]
            );
        }

        throw new RuntimeException("Can't manage format " + format);
    }

    public byte getAlphaAsByte(int x, int y) {
        if (format == TextureFormat.BGRA) {
            return imageData[(y * width + x) * 4 + 3];
        } else if (format == TextureFormat.BGR || format == TextureFormat.PALETTED) {
            return (byte)0xFF;
        }

        throw new RuntimeException("Can't manage format " + format);
    }

    public Bitmap getSubBitmap(int x0, int y0, int x1, int y1) {
        int subImageWidth = x1 - x0;
        int subImageHeight = y1 - y0;

        byte[] subImage = new byte[subImageWidth * subImageHeight * 4];

        for (int y = 0; y < subImageHeight; y++) {
            for (int x = 0; x < subImageWidth; x++) {
                byte blue = getBlueAsByte(x0 + x, y0 + y);
                byte green = getGreenAsByte(x0 + x, y0 + y);
                byte red = getRedAsByte(x0 + x, y0 + y);
                byte alpha = getAlphaAsByte(x0 + x, y0 + y);

                subImage[(y * subImageWidth + x) * 4] = blue;
                subImage[(y * subImageWidth + x) * 4 + 1] = green;
                subImage[(y * subImageWidth + x) * 4 + 2] = red;
                subImage[(y * subImageWidth + x) * 4 + 3] = alpha;
            }
        }

        Bitmap subBitmap = new Bitmap(subImageWidth, subImageHeight, palette, TextureFormat.BGRA);

        subBitmap.setImageDataFromBuffer(subImage);

        return subBitmap;
    }

    public Bitmap getDiagonalSubBitmap(int x0, int y0, int x1, int y1) {
        int destinationWidth = (x1 - x0) / 2;
        int destinationHeight = (y1 - y0) / 2;
        int sourceMiddleX = x0 + destinationWidth;
        int sourceMiddleY = y0 + destinationHeight;

        byte[] subImage = new byte[destinationWidth * destinationHeight * 4];

        int dy = 0;

        int sourceStartY = sourceMiddleY;

        /* Walk down the diagonal line left-middle, to bottom-middle */
        for (int sourceStartX = x0; sourceStartX < sourceMiddleX; sourceStartX++) {
            int sx = sourceStartX;
            int sy = sourceStartY;

            int dx = 0;

            /* Walk diagonally up-right */
            for (int i = 0; i < destinationWidth; i++) {
                byte blue = getBlueAsByte(sx, sy);
                byte green = getGreenAsByte(sx, sy);
                byte red = getRedAsByte(sx, sy);
                byte alpha = getAlphaAsByte(sx, sy);

                subImage[(dy * destinationWidth + dx) * 4] = blue;
                subImage[(dy * destinationWidth + dx) * 4 + 1] = green;
                subImage[(dy * destinationWidth + dx) * 4 + 2] = red;
                subImage[(dy * destinationWidth + dx) * 4 + 3] = alpha;

                dx = dx + 1;
                sx = sx + 1;
                sy = sy + 1;
            }

            sourceStartY = sourceStartY - 1;
            dy = dy + 1;
        }

        Bitmap subBitmap = new Bitmap(destinationWidth, destinationHeight, palette, TextureFormat.BGRA);

        subBitmap.setImageDataFromBuffer(subImage);

        return subBitmap;
    }
}
