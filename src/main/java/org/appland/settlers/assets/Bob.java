package org.appland.settlers.assets;

import java.util.Arrays;
import java.util.List;

public class Bob {
    private static final int NUM_BODY_IMAGES = 2 * 6 * 8;

    final int numberBodyImages;
    final int numberOverlayImages;
    final int[] links;
    final PlayerBitmap[] playerBitmaps;
    private int size;

    public Bob(int numberOverlayImages, int[] links, PlayerBitmap[] playerBitmaps) {
        this.numberOverlayImages = numberOverlayImages;
        this.links = links;
        this.playerBitmaps = playerBitmaps;
        this.numberBodyImages = playerBitmaps.length - numberOverlayImages;
    }

    public PlayerBitmap[] getBodyBitmaps() {
        PlayerBitmap[] bodyBitmaps = new PlayerBitmap[NUM_BODY_IMAGES];

        System.arraycopy(playerBitmaps, 0, bodyBitmaps, 0, NUM_BODY_IMAGES);

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

    public List<PlayerBitmap> getAllBitmaps() {
        return Arrays.asList(this.playerBitmaps);
    }

    public PlayerBitmap getBody(boolean fat, long directionIndex, int animationIndex) {
        long bodyIndex;

        if (fat) {
            bodyIndex = (6 + directionIndex) * 8 + animationIndex;
        } else {
            bodyIndex = directionIndex * 8 + animationIndex;
        }

        return playerBitmaps[(int)bodyIndex];
    }

    public PlayerBitmap getOverlay(int id, boolean fat, int directionIndex, int animationStep) {
        int overlayIndex = getOverlayIndex(id, fat, directionIndex, animationStep);

        return playerBitmaps[overlayIndex];
    }

    private int getOverlayIndex(int id, boolean fat, int directionIndex, int animationStep) {
        if (fat) {
            return links[((id * 8 + animationStep) * 2 + 1) * 6 + directionIndex];
        } else {
            return links[(id * 8 + animationStep) * 2 * 6 + directionIndex];
        }
    }
}
