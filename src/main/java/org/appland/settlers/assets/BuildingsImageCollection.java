package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BuildingsImageCollection {

    private final Map<Nation, Map<String, BuildingImages>> buildingMap;
    private final Map<Nation, SpecialImages> specialImagesMap;

    public BuildingsImageCollection() {
        this.buildingMap = new HashMap<>();
        this.specialImagesMap = new HashMap<>();

        for (Nation nation : Nation.values()) {
            this.buildingMap.put(nation, new HashMap<>());
            this.specialImagesMap.put(nation, new SpecialImages());
        }
    }

    public void addBuildingForNation(Nation nation, String building, Bitmap image) {
        Map<String, BuildingImages> buildingsForNation = this.buildingMap.get(nation);

        if (!buildingsForNation.containsKey(building)) {
            buildingsForNation.put(building, new BuildingImages());
        }

        BuildingImages buildingImages = buildingsForNation.get(building);

        buildingImages.addReadyBuildingImage(image);
    }

    public void addBuildingUnderConstructionForNation(Nation nation, String building, Bitmap image) {
        Map<String, BuildingImages> buildingsForNation = this.buildingMap.get(nation);

        if (!buildingsForNation.containsKey(building)) {
            buildingsForNation.put(building, new BuildingImages());
        }

        BuildingImages buildingImages = buildingsForNation.get(building);

        buildingImages.addUnderConstructionBuildingImage(image);
    }

    public void addConstructionPlanned(Nation nation, Bitmap image) {
        this.specialImagesMap.get(nation).addConstructionPlannedImage(image);
    }

    public void addConstructionJustStarted(Nation nation, Bitmap image) {
        this.specialImagesMap.get(nation).addConstructionJustStartedImage(image);
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        // Calculate the size of the image atlas and create it
        int maxNationWidth = 0;
        int maxNationHeight = 0;

        for (Nation nation: Nation.values()) {

            int currentNationHeight = 0;
            for (Map.Entry<String, BuildingImages> entry : this.buildingMap.get(nation).entrySet()) {
                BuildingImages images = entry.getValue();

                maxNationWidth = Math.max(maxNationWidth, images.buildingReadyImage.width + images.buildingUnderConstruction.width);
                currentNationHeight = currentNationHeight + Math.max(images.buildingReadyImage.height, images.buildingUnderConstruction.height);
            }

            maxNationHeight = Math.max(maxNationHeight, currentNationHeight);
        }

        Bitmap imageAtlas = new Bitmap(maxNationWidth * Nation.values().length, maxNationHeight, palette, TextureFormat.BGRA);

        // Fill in the image atlas and fill in the meta-data
        int startNextNationAtX = 0;

        JSONObject jsonImageAtlas = new JSONObject();

        for (Nation nation : Nation.values()) {

            JSONObject jsonBuildings = new JSONObject();

            jsonImageAtlas.put(nation.name().toLowerCase(), jsonBuildings);

            int startNextBuildingAtY = 0;
            int widthCurrentNation = 0;

            for (Map.Entry<String, BuildingImages> entry : this.buildingMap.get(nation).entrySet()) {
                String building = entry.getKey();
                BuildingImages images = entry.getValue();

                JSONObject jsonBuilding = new JSONObject();

                jsonBuildings.put(building, jsonBuilding);

                jsonBuilding.put("y", startNextBuildingAtY);

                int currentBuildingMaxHeight = 0;
                int currentBuildingWidth = 0;
                int placeNextBuildingAtX = 0;

                // Copy the building ready image
                if (images.buildingReadyImage != null) {
                    imageAtlas.copyNonTransparentPixels(images.buildingReadyImage,
                            new Point(startNextNationAtX, startNextBuildingAtY),
                            new Point(0, 0),
                            images.buildingReadyImage.getDimension());

                    jsonBuilding.put("readyAtX", startNextNationAtX);
                    jsonBuilding.put("readyWidth", images.buildingReadyImage.width);
                    jsonBuilding.put("readyHeight", images.buildingReadyImage.height);
                    jsonBuilding.put("readyOffsetX", images.buildingReadyImage.nx);
                    jsonBuilding.put("readyOffsetY", images.buildingReadyImage.ny);

                    currentBuildingMaxHeight = Math.max(currentBuildingMaxHeight, images.buildingReadyImage.height);
                    currentBuildingWidth = currentBuildingWidth + images.buildingReadyImage.width;
                    placeNextBuildingAtX = placeNextBuildingAtX + images.buildingReadyImage.width;
                }

                // Copy the under construction image
                if (images.buildingUnderConstruction != null) {
                    imageAtlas.copyNonTransparentPixels(images.buildingUnderConstruction,
                            new Point(startNextNationAtX + placeNextBuildingAtX, startNextBuildingAtY),
                            new Point(0, 0),
                            images.buildingUnderConstruction.getDimension());

                    jsonBuilding.put("underConstructionAtX", placeNextBuildingAtX);
                    jsonBuilding.put("underConstructionWidth", images.buildingUnderConstruction.width);
                    jsonBuilding.put("underConstructionHeight", images.buildingUnderConstruction.height);
                    jsonBuilding.put("underConstructionOffsetX", images.buildingUnderConstruction.nx);
                    jsonBuilding.put("underConstructionOffsetY", images.buildingUnderConstruction.ny);

                    currentBuildingMaxHeight = Math.max(currentBuildingMaxHeight, images.buildingUnderConstruction.height);
                    currentBuildingWidth = currentBuildingWidth + images.buildingUnderConstruction.width;
                }

                // Keep track of the max width of the current nation
                widthCurrentNation = Math.max(widthCurrentNation, currentBuildingWidth);

                startNextBuildingAtY = startNextBuildingAtY + currentBuildingMaxHeight;
            }

            startNextNationAtX = startNextNationAtX + widthCurrentNation;
        }

        // Write the image and the meta-data to files
        imageAtlas.writeToFile(directory + "/image-atlas-buildings.png");
        Files.writeString(Paths.get(directory, "image-atlas-buildings.json"), jsonImageAtlas.toJSONString());
    }

    private class BuildingImages {
        private Bitmap buildingReadyImage;
        private Bitmap buildingUnderConstruction;

        BuildingImages() {
            this.buildingReadyImage = null;
            this.buildingUnderConstruction = null;
        }

        public void addReadyBuildingImage(Bitmap image) {
            this.buildingReadyImage = image;
        }

        public void addUnderConstructionBuildingImage(Bitmap image) {
            this.buildingUnderConstruction = image;
        }
    }

    private class SpecialImages {
        private Bitmap constructionPlannedImage;
        private Bitmap constructionJustStartedImage;

        public void addConstructionPlannedImage(Bitmap image) {
            this.constructionPlannedImage = image;
        }

        public void addConstructionJustStartedImage(Bitmap image) {
            this.constructionJustStartedImage = image;
        }
    }
}
