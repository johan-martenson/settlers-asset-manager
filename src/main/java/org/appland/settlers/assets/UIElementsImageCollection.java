package org.appland.settlers.assets;

import org.appland.settlers.model.Size;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UIElementsImageCollection {
    private final Map<Size, Bitmap> hoverAvailableBuilding;
    private final Map<Size, Bitmap> availableBuilding;

    private Bitmap selectedPointImage;
    private Bitmap hoverPointImage;
    private Bitmap hoverAvailableFlag;
    private Bitmap hoverAvailableMine;
    private Bitmap hoverAvailableHarbor;
    private Bitmap availableFlag;
    private Bitmap availableMine;
    private Bitmap availableHarbor;

    public UIElementsImageCollection() {
        hoverAvailableBuilding = new HashMap<>();
        availableBuilding = new HashMap<>();
    }

    public void addSelectedPointImage(Bitmap image) {
        this.selectedPointImage = image;
    }

    public void addHoverPoint(Bitmap image) {
        this.hoverPointImage = image;
    }

    public void addHoverAvailableFlag(Bitmap image) {
        this.hoverAvailableFlag = image;
    }

    public void addHoverAvailableMine(Bitmap image) {
        this.hoverAvailableMine = image;
    }

    public void addHoverAvailableBuilding(Size size, Bitmap image) {
        this.hoverAvailableBuilding.put(size, image);
    }

    public void addHoverAvailableHarbor(Bitmap image) {
        this.hoverAvailableHarbor = image;
    }

    public void addAvailableFlag(Bitmap image) {
        this.availableFlag = image;
    }

    public void addAvailableMine(Bitmap image) {
        this.availableMine = image;
    }

    public void addAvailableBuilding(Size size, Bitmap image) {
        this.availableBuilding.put(size, image);
    }

    public void addAvailableHarbor(Bitmap image) {
        this.availableHarbor = image;
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {

        /*
         * Layout in three rows:
         *   - Selected point, hover point
         *   - Hover over available - flag, mine, harbor, large, medium, small
         *   - Available - flag, mine, harbor, large, medium, small
         */

        // Calculate the dimension of the image atlas
        int totalWidth = 0;
        int totalHeight = 0;

        int row2Width = hoverAvailableFlag.width + hoverAvailableMine.width + hoverAvailableHarbor.width +
                hoverAvailableBuilding.get(Size.LARGE).width + hoverAvailableBuilding.get(Size.MEDIUM).width +
                hoverAvailableBuilding.get(Size.SMALL).width;

        int row3Width = availableFlag.width + availableMine.width + availableHarbor.width +
                availableBuilding.get(Size.LARGE).width + availableBuilding.get(Size.MEDIUM).width +
                availableBuilding.get(Size.SMALL).width;

        totalWidth = Math.max(row2Width, row3Width);

        int row1Height = Utils.max(selectedPointImage.height, hoverPointImage.height);
        int row2Height = Utils.max(hoverAvailableFlag.height, hoverAvailableMine.height, hoverAvailableHarbor.height,
                hoverAvailableBuilding.get(Size.LARGE).height, hoverAvailableBuilding.get(Size.MEDIUM).height,
                hoverAvailableBuilding.get(Size.SMALL).height);
        int row3Height = Utils.max(availableFlag.height, availableMine.height, availableHarbor.height,
                availableBuilding.get(Size.LARGE).height, availableBuilding.get(Size.MEDIUM).height,
                availableBuilding.get(Size.SMALL).height);

        totalHeight = row1Height + row2Height + row3Height;

        // Create the image atlas
        Bitmap imageAtlas = new Bitmap(totalWidth, totalHeight, palette, TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas

        // Row 1 - selected point and hover point
        int nextStartAtX = 0;

        imageAtlas.copyNonTransparentPixels(selectedPointImage, new Point(nextStartAtX, 0), new Point(0, 0), selectedPointImage.getDimension());
        JSONObject jsonSelectedPoint = new JSONObject();

        jsonSelectedPoint.put("x", nextStartAtX);
        jsonSelectedPoint.put("y", 0);
        jsonSelectedPoint.put("width", selectedPointImage.width);
        jsonSelectedPoint.put("height", selectedPointImage.height);
        jsonSelectedPoint.put("offsetX", selectedPointImage.nx);
        jsonSelectedPoint.put("offsetY", selectedPointImage.ny);

        jsonImageAtlas.put("selectedPoint", jsonSelectedPoint);

        nextStartAtX = nextStartAtX + selectedPointImage.width;

        imageAtlas.copyNonTransparentPixels(hoverPointImage, new Point(nextStartAtX, 0), new Point(0, 0), hoverPointImage.getDimension());

        JSONObject jsonHoverPoint = new JSONObject();

        jsonHoverPoint.put("x", nextStartAtX);
        jsonHoverPoint.put("y", 0);
        jsonHoverPoint.put("width", hoverPointImage.width);
        jsonHoverPoint.put("height", hoverPointImage.height);
        jsonHoverPoint.put("offsetX", hoverPointImage.nx);
        jsonHoverPoint.put("offsetY", hoverPointImage.ny);

        jsonImageAtlas.put("hoverPoint", jsonHoverPoint);

        // Row 2 - Hover over available: flag, mine, harbor, large, medium, small
        Bitmap hoverAvailableBuildingLarge = hoverAvailableBuilding.get(Size.LARGE);
        Bitmap hoverAvailableBuildingMedium = hoverAvailableBuilding.get(Size.MEDIUM);
        Bitmap hoverAvailableBuildingSmall = hoverAvailableBuilding.get(Size.SMALL);

        nextStartAtX = 0;

        imageAtlas.copyNonTransparentPixels(hoverAvailableFlag, new Point(nextStartAtX, row1Height), new Point(0, 0), hoverAvailableFlag.getDimension());

        JSONObject jsonHoverAvailableFlag = new JSONObject();

        jsonHoverAvailableFlag.put("x", nextStartAtX);
        jsonHoverAvailableFlag.put("y", row1Height);
        jsonHoverAvailableFlag.put("width", hoverAvailableFlag.width);
        jsonHoverAvailableFlag.put("height", hoverAvailableFlag.height);
        jsonHoverAvailableFlag.put("offsetX", hoverAvailableFlag.nx);
        jsonHoverAvailableFlag.put("offsetY", hoverAvailableFlag.ny);

        jsonImageAtlas.put("hoverAvailableFlag", jsonHoverAvailableFlag);

        nextStartAtX = nextStartAtX + hoverAvailableFlag.width;

        imageAtlas.copyNonTransparentPixels(hoverAvailableMine, new Point(nextStartAtX, row1Height), new Point(0, 0), hoverAvailableMine.getDimension());

        JSONObject jsonHoverAvailableMine = new JSONObject();

        jsonHoverAvailableMine.put("x", nextStartAtX);
        jsonHoverAvailableMine.put("y", row1Height);
        jsonHoverAvailableMine.put("width", hoverAvailableMine.width);
        jsonHoverAvailableMine.put("height", hoverAvailableMine.height);
        jsonHoverAvailableMine.put("offsetX", hoverAvailableMine.nx);
        jsonHoverAvailableMine.put("offsetY", hoverAvailableMine.ny);

        jsonImageAtlas.put("hoverAvailableMine", jsonHoverAvailableMine);

        nextStartAtX = nextStartAtX + hoverAvailableMine.width;

        imageAtlas.copyNonTransparentPixels(hoverAvailableHarbor, new Point(nextStartAtX, row1Height), new Point(0, 0), hoverAvailableHarbor.getDimension());

        JSONObject jsonHoverAvailableHarbor = new JSONObject();

        jsonHoverAvailableHarbor.put("x", nextStartAtX);
        jsonHoverAvailableHarbor.put("y", row1Height);
        jsonHoverAvailableHarbor.put("width", hoverAvailableHarbor.width);
        jsonHoverAvailableHarbor.put("height", hoverAvailableHarbor.height);
        jsonHoverAvailableHarbor.put("offsetX", hoverAvailableHarbor.nx);
        jsonHoverAvailableHarbor.put("offsetY", hoverAvailableHarbor.ny);

        jsonImageAtlas.put("hoverAvailableHarbor", jsonHoverAvailableHarbor);

        nextStartAtX = nextStartAtX + hoverAvailableHarbor.width;

        imageAtlas.copyNonTransparentPixels(hoverAvailableBuildingLarge, new Point(nextStartAtX, row1Height), new Point(0, 0), hoverAvailableBuildingLarge.getDimension());

        JSONObject jsonHoverAvailableBuildingLarge = new JSONObject();

        jsonHoverAvailableBuildingLarge.put("x", nextStartAtX);
        jsonHoverAvailableBuildingLarge.put("y", row1Height);
        jsonHoverAvailableBuildingLarge.put("width", hoverAvailableBuildingLarge.width);
        jsonHoverAvailableBuildingLarge.put("height", hoverAvailableBuildingLarge.height);
        jsonHoverAvailableBuildingLarge.put("offsetX", hoverAvailableBuildingLarge.nx);
        jsonHoverAvailableBuildingLarge.put("offsetY", hoverAvailableBuildingLarge.ny);

        jsonImageAtlas.put("hoverAvailableBuildingLarge", jsonHoverAvailableBuildingLarge);

        nextStartAtX = nextStartAtX + hoverAvailableBuildingLarge.width;

        imageAtlas.copyNonTransparentPixels(hoverAvailableBuildingMedium, new Point(nextStartAtX, row1Height), new Point(0, 0), hoverAvailableBuildingMedium.getDimension());

        JSONObject jsonHoverAvailableBuildingMedium = new JSONObject();

        jsonHoverAvailableBuildingMedium.put("x", nextStartAtX);
        jsonHoverAvailableBuildingMedium.put("y", row1Height);
        jsonHoverAvailableBuildingMedium.put("width", hoverAvailableBuildingMedium.width);
        jsonHoverAvailableBuildingMedium.put("height", hoverAvailableBuildingMedium.height);
        jsonHoverAvailableBuildingMedium.put("offsetX", hoverAvailableBuildingMedium.nx);
        jsonHoverAvailableBuildingMedium.put("offsetY", hoverAvailableBuildingMedium.ny);

        jsonImageAtlas.put("hoverAvailableBuildingMedium", jsonHoverAvailableBuildingMedium);

        nextStartAtX = nextStartAtX + hoverAvailableBuildingMedium.width;

        imageAtlas.copyNonTransparentPixels(hoverAvailableBuildingSmall, new Point(nextStartAtX, row1Height), new Point(0, 0), hoverAvailableBuildingSmall.getDimension());

        JSONObject jsonHoverAvailableBuildingSmall = new JSONObject();

        jsonHoverAvailableBuildingSmall.put("x", nextStartAtX);
        jsonHoverAvailableBuildingSmall.put("y", row1Height);
        jsonHoverAvailableBuildingSmall.put("width", hoverAvailableBuildingSmall.width);
        jsonHoverAvailableBuildingSmall.put("height", hoverAvailableBuildingSmall.height);
        jsonHoverAvailableBuildingSmall.put("offsetX", hoverAvailableBuildingSmall.nx);
        jsonHoverAvailableBuildingSmall.put("offsetY", hoverAvailableBuildingSmall.ny);

        jsonImageAtlas.put("hoverAvailableBuildingSmall", jsonHoverAvailableBuildingSmall);

        // Row 3 - Available - flag, mine, harbor, large, medium, small
        Bitmap availableBuildingLarge = availableBuilding.get(Size.LARGE);
        Bitmap availableBuildingMedium = availableBuilding.get(Size.MEDIUM);
        Bitmap availableBuildingSmall = availableBuilding.get(Size.SMALL);

        nextStartAtX = 0;
        int nextStartAtY = row1Height + row2Height;

        imageAtlas.copyNonTransparentPixels(availableFlag, new Point(nextStartAtX, nextStartAtY), new Point(0, 0), availableFlag.getDimension());

        JSONObject jsonAvailableFlag = new JSONObject();

        jsonAvailableFlag.put("x", nextStartAtX);
        jsonAvailableFlag.put("y", nextStartAtY);
        jsonAvailableFlag.put("width", availableFlag.width);
        jsonAvailableFlag.put("height", availableFlag.height);
        jsonAvailableFlag.put("offsetX", availableFlag.nx);
        jsonAvailableFlag.put("offsetY", availableFlag.ny);

        jsonImageAtlas.put("availableFlag", jsonAvailableFlag);

        nextStartAtX = nextStartAtX + availableFlag.width;

        imageAtlas.copyNonTransparentPixels(availableMine, new Point(nextStartAtX, nextStartAtY), new Point(0, 0), availableMine.getDimension());

        JSONObject jsonAvailableMine = new JSONObject();

        jsonAvailableMine.put("x", nextStartAtX);
        jsonAvailableMine.put("y", nextStartAtY);
        jsonAvailableMine.put("width", availableMine.width);
        jsonAvailableMine.put("height", availableMine.height);
        jsonAvailableMine.put("offsetX", availableMine.nx);
        jsonAvailableMine.put("offsetY", availableMine.ny);

        jsonImageAtlas.put("availableMine", jsonAvailableMine);

        nextStartAtX = nextStartAtX + availableMine.width;

        imageAtlas.copyNonTransparentPixels(availableHarbor, new Point(nextStartAtX, nextStartAtY), new Point(0, 0), availableHarbor.getDimension());

        JSONObject jsonAvailableHarbor = new JSONObject();

        jsonAvailableHarbor.put("x", nextStartAtX);
        jsonAvailableHarbor.put("y", nextStartAtY);
        jsonAvailableHarbor.put("width", availableHarbor.width);
        jsonAvailableHarbor.put("height", availableHarbor.height);
        jsonAvailableHarbor.put("offsetX", availableHarbor.nx);
        jsonAvailableHarbor.put("offsetY", availableHarbor.ny);

        jsonImageAtlas.put("availableHarbor", jsonAvailableHarbor);

        nextStartAtX = nextStartAtX + availableHarbor.width;

        imageAtlas.copyNonTransparentPixels(availableBuildingLarge, new Point(nextStartAtX, nextStartAtY), new Point(0, 0), availableBuildingLarge.getDimension());

        JSONObject jsonAvailableBuildingLarge = new JSONObject();

        jsonAvailableBuildingLarge.put("x", nextStartAtX);
        jsonAvailableBuildingLarge.put("y", nextStartAtY);
        jsonAvailableBuildingLarge.put("width", availableBuildingLarge.width);
        jsonAvailableBuildingLarge.put("height", availableBuildingLarge.height);
        jsonAvailableBuildingLarge.put("offsetX", availableBuildingLarge.nx);
        jsonAvailableBuildingLarge.put("offsetY", availableBuildingLarge.ny);

        jsonImageAtlas.put("availableBuildingLarge", jsonAvailableBuildingLarge);

        nextStartAtX = nextStartAtX + availableBuildingLarge.width;

        imageAtlas.copyNonTransparentPixels(availableBuildingMedium, new Point(nextStartAtX, nextStartAtY), new Point(0, 0), availableBuildingMedium.getDimension());

        JSONObject jsonAvailableBuildingMedium = new JSONObject();

        jsonAvailableBuildingMedium.put("x", nextStartAtX);
        jsonAvailableBuildingMedium.put("y", nextStartAtY);
        jsonAvailableBuildingMedium.put("width", availableBuildingMedium.width);
        jsonAvailableBuildingMedium.put("height", availableBuildingMedium.height);
        jsonAvailableBuildingMedium.put("offsetX", availableBuildingMedium.nx);
        jsonAvailableBuildingMedium.put("offsetY", availableBuildingMedium.ny);

        jsonImageAtlas.put("availableBuildingMedium", jsonAvailableBuildingMedium);

        nextStartAtX = nextStartAtX + availableBuildingMedium.width;

        imageAtlas.copyNonTransparentPixels(availableBuildingSmall, new Point(nextStartAtX, nextStartAtY), new Point(0, 0), availableBuildingSmall.getDimension());

        JSONObject jsonAvailableBuildingSmall = new JSONObject();

        jsonAvailableBuildingSmall.put("x", nextStartAtX);
        jsonAvailableBuildingSmall.put("y", nextStartAtY);
        jsonAvailableBuildingSmall.put("width", availableBuildingSmall.width);
        jsonAvailableBuildingSmall.put("height", availableBuildingSmall.height);
        jsonAvailableBuildingSmall.put("offsetX", availableBuildingSmall.nx);
        jsonAvailableBuildingSmall.put("offsetY", availableBuildingSmall.ny);

        jsonImageAtlas.put("availableBuildingSmall", jsonAvailableBuildingSmall);


        // Write the image atlas to file
        imageAtlas.writeToFile(toDir + "/image-atlas-ui-elements.png");

        Files.writeString(Paths.get(toDir, "image-atlas-ui-elements.json"), jsonImageAtlas.toJSONString());
    }
}
