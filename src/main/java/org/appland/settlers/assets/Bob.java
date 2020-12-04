package org.appland.settlers.assets;

import java.util.Arrays;
import java.util.List;

public class Bob {
    private static final int NUM_BODY_IMAGES = 2 * 6 * 8;

    int numberBodyImages;
    int numberOverlayImages;
    int[] links;
    PlayerBitmap[] playerBitmaps;
    private int size;

    public Bob(int numberOverlayImages, int[] links, PlayerBitmap[] playerBitmaps) {
        this.numberOverlayImages = numberOverlayImages;
        this.links = links;
        this.playerBitmaps = playerBitmaps;
        this.numberBodyImages = playerBitmaps.length - numberOverlayImages;
    }

    public PlayerBitmap[] getBodyBitmaps() {
        PlayerBitmap[] bodyBitmaps = new PlayerBitmap[NUM_BODY_IMAGES];

        for (int i = 0; i < NUM_BODY_IMAGES; i++) {
            bodyBitmaps[i] = playerBitmaps[i];
        }

        return bodyBitmaps;
    }

    public int getNumberBodyImages() {
        return numberBodyImages;
    }

    public int getNumberOverlayImages() {
        return numberOverlayImages;
    }

    public int getNumberLinks() {
        return links.length;
    }

    public int getSize() {
        return size;
    }

    public List<PlayerBitmap> getAllBitmaps() {
        return Arrays.asList(this.playerBitmaps);
    }
}
