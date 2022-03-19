package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DecorativeImageCollection {
    private Bitmap mushroomImage;
    private Bitmap miniStoneImage;
    private Bitmap miniStonesImage;
    private Bitmap stoneImage;
    private Bitmap fallenTreeImage;
    private Bitmap standingDeadTree;
    private Bitmap skeletonImage;
    private Bitmap miniSkeletonImage;
    private Bitmap flowersImage;
    private Bitmap bushImage;
    private Bitmap largerStonesImage;
    private Bitmap cactus1Image;
    private Bitmap cactus2Image;
    private Bitmap beachGrassImage;
    private Bitmap smallGrassImage;

    public void addMushroomImage(Bitmap image) {
        this.mushroomImage = image;
    }

    public void addMiniStoneImage(Bitmap image) {
        this.miniStoneImage = image;
    }

    public void addMiniStonesImage(Bitmap image) {
        this.miniStonesImage = image;
    }

    public void addStoneImage(Bitmap image) {
        this.stoneImage = image;
    }

    public void addFallenTreeImage(Bitmap image) {
        this.fallenTreeImage = image;
    }

    public void addStandingDeadTreeImage(Bitmap image) {
        this.standingDeadTree = image;
    }

    public void addSkeletonImage(Bitmap image) {
        this.skeletonImage = image;
    }

    public void addMiniSkeletonImage(Bitmap image) {
        this.miniSkeletonImage = image;
    }

    public void addFlowersImage(Bitmap image) {
        this.flowersImage = image;
    }

    public void addBushImage(Bitmap image) {
        this.bushImage = image;
    }

    public void addLargerStonesImage(Bitmap image) {
        this.largerStonesImage = image;
    }

    public void addCactus1Image(Bitmap image) {
        this.cactus1Image = image;
    }

    public void addCactus2Image(Bitmap image) {
        this.cactus2Image = image;
    }

    public void addBeachGrassImage(Bitmap image) {
        this.beachGrassImage = image;
    }

    public void addSmallGrassImage(Bitmap image) {
        this.smallGrassImage = image;
    }

    public void writeImageAtlas(String dir, Palette palette) throws IOException {

        Bitmap[] imagesInOrder = new Bitmap[] {
            mushroomImage,
            miniStoneImage,
            miniStonesImage,
            stoneImage,
            fallenTreeImage,
            standingDeadTree,
            skeletonImage,
            miniSkeletonImage,
            flowersImage,
            bushImage,
            largerStonesImage,
            cactus1Image,
            cactus2Image,
            beachGrassImage,
            smallGrassImage
        };

        Map<Bitmap, String> imageToTitle = new HashMap<>();

        imageToTitle.put(mushroomImage, "MUSHROOM");
        imageToTitle.put(miniStoneImage, "MINI_STONE");
        imageToTitle.put(miniStonesImage, "MINI_STONES");
        imageToTitle.put(stoneImage, "STONE");
        imageToTitle.put(fallenTreeImage, "FALLEN_TREE");
        imageToTitle.put(standingDeadTree, "STANDING_DEAD_TREE");
        imageToTitle.put(skeletonImage, "SKELETON");
        imageToTitle.put(miniSkeletonImage, "MINI_SKELETON");
        imageToTitle.put(flowersImage, "FLOWERS");
        imageToTitle.put(bushImage, "BUSH");
        imageToTitle.put(largerStonesImage, "LARGER_STONES");
        imageToTitle.put(cactus1Image, "CACTUS_1");
        imageToTitle.put(cactus2Image, "CACTUS_2");
        imageToTitle.put(beachGrassImage, "BEACH_GRASS");
        imageToTitle.put(smallGrassImage, "SMALL_GRASS");

        // Calculate dimensions
        int totalHeight = 0;
        int maxWidth = 0;

        for (Bitmap image : imagesInOrder) {
            totalHeight = totalHeight + image.height;
            maxWidth = Math.max(maxWidth, image.width);
        }

        // Create the image atlas
        Bitmap imageAtlas = new Bitmap(maxWidth, totalHeight, palette, TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        int nextY = 0;

        for (Bitmap image : imagesInOrder) {

            imageAtlas.copyNonTransparentPixels(image, new Point(0, nextY), new Point(0, 0), image.getDimension());

            JSONObject jsonImageInfo = new JSONObject();

            jsonImageInfo.put("x", 0);
            jsonImageInfo.put("y", nextY);
            jsonImageInfo.put("width", image.width);
            jsonImageInfo.put("height", image.height);
            jsonImageInfo.put("offsetX", image.nx);
            jsonImageInfo.put("offsetY", image.ny);

            jsonImageAtlas.put(imageToTitle.get(image), jsonImageInfo);

            nextY = nextY + image.height;
        }

        // Write to file
        imageAtlas.writeToFile(dir + "/image-atlas-decorations.png");

        Files.writeString(Paths.get(dir, "image-atlas-decorations.json"), jsonImageAtlas.toJSONString());
    }
}
