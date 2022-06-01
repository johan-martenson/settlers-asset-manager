package org.appland.settlers.assets.collectors;

import org.appland.settlers.assets.Bitmap;
import org.appland.settlers.assets.Bob;
import org.appland.settlers.assets.BodyType;
import org.appland.settlers.assets.CompassDirection;
import org.appland.settlers.assets.ImageBoard;
import org.appland.settlers.assets.Nation;
import org.appland.settlers.assets.NormalizedImageList;
import org.appland.settlers.assets.Palette;
import org.appland.settlers.assets.PlayerBitmap;
import org.appland.settlers.model.Material;
import org.json.simple.JSONObject;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.appland.settlers.assets.BodyType.FAT;

public class WorkerImageCollection {
    private final String name;
    private final Map<Nation, Map<CompassDirection, List<Bitmap>>> nationToDirectionToImageMap;
    private final Map<CompassDirection, List<Bitmap>> shadowImages;
    private final Map<Material, Map<CompassDirection, Bitmap>> singleCargoImages;
    private final Map<Material, Map<CompassDirection, List<Bitmap>>> multipleCargoImages;

    public WorkerImageCollection(String name) {
        this.name = name;
        nationToDirectionToImageMap = new HashMap<>();

        for (Nation nation : Nation.values()) {

            this.nationToDirectionToImageMap.put(nation, new HashMap<>());

            for (CompassDirection compassDirection : CompassDirection.values()) {
                this.nationToDirectionToImageMap.get(nation).put(compassDirection, new ArrayList<>());
            }
        }

        shadowImages = new HashMap<>();
        singleCargoImages = new HashMap<>();
        multipleCargoImages = new HashMap<>();
    }

    public void addImage(Nation nation, CompassDirection compassDirection, Bitmap workerImage) {
        this.nationToDirectionToImageMap.get(nation).get(compassDirection).add(workerImage);
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        // Write the image atlas, one row per direction, and collect metadata to write as json
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();
        JSONObject jsonImages = new JSONObject();
        JSONObject jsonShadowImages = new JSONObject();

        jsonImageAtlas.put("images", jsonImages);
        jsonImageAtlas.put("shadowImages", jsonShadowImages);

        Point cursor = new Point(0, 0);

        // Write walking animations, per nation and direction
        for (Nation nation : Nation.values()) {

            cursor.x = 0;

            Map<CompassDirection, List<Bitmap>> directionToImageMap = nationToDirectionToImageMap.get(nation);

            JSONObject jsonNationInfo = new JSONObject();

            jsonImages.put(nation.name().toUpperCase(), jsonNationInfo);

            for (CompassDirection compassDirection : CompassDirection.values()) {

                if (directionToImageMap.get(compassDirection).isEmpty()) {
                    continue;
                }

                // Handle each image per nation x direction
                List<Bitmap> workerImages = directionToImageMap.get(compassDirection);
                NormalizedImageList normalizedWorkerList = new NormalizedImageList(workerImages);
                List<Bitmap> normalizedWorkerImages = normalizedWorkerList.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedWorkerImages, cursor, ImageBoard.LayoutDirection.ROW);

                jsonNationInfo.put(compassDirection.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedWorkerImages));

                cursor.y = cursor.y + normalizedWorkerList.getImageHeight();
            }
        }

        // Write shadows, per direction (seems to be the same regardless of nation)
        for (Map.Entry<CompassDirection, List<Bitmap>> entry : shadowImages.entrySet()) {
            CompassDirection compassDirection = entry.getKey();
            List<Bitmap> shadowImagesForDirection = entry.getValue();

            cursor.x = 0;

            NormalizedImageList normalizedShadowListForDirection = new NormalizedImageList(shadowImagesForDirection);
            List<Bitmap> normalizedShadowImagesForDirection = normalizedShadowListForDirection.getNormalizedImages();

            imageBoard.placeImageSeries(normalizedShadowImagesForDirection, cursor, ImageBoard.LayoutDirection.ROW);

            jsonShadowImages.put(compassDirection.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedShadowImagesForDirection));

            cursor.y = cursor.y + normalizedShadowListForDirection.getImageHeight();
        }

        // Write single cargo images (if any)
        if (!singleCargoImages.keySet().isEmpty()) {

            JSONObject jsonSingleCargoImages = new JSONObject();

            jsonImageAtlas.put("singleCargoImages", jsonSingleCargoImages);

            for (Material material : singleCargoImages.keySet()) {

                JSONObject jsonMaterialImage = new JSONObject();

                jsonSingleCargoImages.put(material.name().toUpperCase(), jsonMaterialImage);

                int rowHeight = 0;

                cursor.x = 0;

                for (Map.Entry<CompassDirection, Bitmap> entry : singleCargoImages.get(material).entrySet()) {

                    CompassDirection compassDirection = entry.getKey();
                    Bitmap cargoImageForDirection = entry.getValue();

                    imageBoard.placeImage(cargoImageForDirection, cursor);

                    jsonMaterialImage.put(compassDirection.name().toUpperCase(), imageBoard.imageLocationToJson(cargoImageForDirection));

                    rowHeight = Math.max(rowHeight, cargoImageForDirection.getHeight());

                    cursor.x = cursor.x + cargoImageForDirection.getWidth();
                }

                cursor.y = cursor.y + rowHeight;
            }

        }

        // Write lists of cargo images (if any)
        if (!multipleCargoImages.keySet().isEmpty()) {

            JSONObject jsonMultipleCargoImages = new JSONObject();

            jsonImageAtlas.put("multipleCargoImages", jsonMultipleCargoImages);

            for (Material material : multipleCargoImages.keySet()) {

                if (material == null) {
                    continue;
                }

                JSONObject jsonMaterialImages = new JSONObject();

                jsonMultipleCargoImages.put(material.name().toUpperCase(), jsonMaterialImages);

                cursor.x = 0;

                int rowHeight = 0;

                for (Map.Entry<CompassDirection, List<Bitmap>> entry : multipleCargoImages.get(material).entrySet()) {

                    CompassDirection compassDirection = entry.getKey();
                    List<Bitmap> cargoImagesForDirection = entry.getValue();

                    NormalizedImageList normalizedCargoListForDirection = new NormalizedImageList(cargoImagesForDirection);
                    List<Bitmap> normalizedCargoImagesForDirection = normalizedCargoListForDirection.getNormalizedImages();

                    imageBoard.placeImageSeries(normalizedCargoImagesForDirection, cursor, ImageBoard.LayoutDirection.ROW);

                    jsonMaterialImages.put(compassDirection.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedCargoImagesForDirection));

                    rowHeight = Math.max(rowHeight, normalizedCargoListForDirection.getImageHeight());

                    cursor.y = cursor.y + normalizedCargoListForDirection.getImageHeight();
                }
            }
        }

        // Write the image atlas to disk
        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addShadowImages(CompassDirection compassDirection, List<Bitmap> images) {
        shadowImages.put(compassDirection, images);
    }

    public void addCargoImage(CompassDirection compassDirection, Material material, Bitmap image) {
        if (!singleCargoImages.containsKey(material)) {
            singleCargoImages.put(material, new HashMap<>());
        }

        singleCargoImages.get(material).put(compassDirection, image);
    }

    public void addCargoImages(CompassDirection compassDirection, Material material, Bitmap... images) {
        if (!multipleCargoImages.containsKey(material)) {
            multipleCargoImages.put(material, new HashMap<>());
        }

        multipleCargoImages.get(material).put(compassDirection, Arrays.asList(images));
    }

    public void readCargoImagesFromBob(Material material, BodyType bodyType, int bobId, Bob jobsBob) {
        int fatOffset = 0;

        if (bodyType == FAT) {
            fatOffset = 1;
        }

        multipleCargoImages.put(material, new HashMap<>());

        for (CompassDirection compassDirection : CompassDirection.values()) {

            List<Bitmap> cargoImagesForDirection = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                int link = ((bobId * 8 + i) * 2 + fatOffset) * 6 + compassDirection.ordinal();
                int index = jobsBob.getLinkForIndex(link);

                cargoImagesForDirection.add(jobsBob.getBitmapAtIndex(index));
            }

            multipleCargoImages.get(material).put(compassDirection, cargoImagesForDirection);
        }
    }

    public void readBodyImagesFromBob(BodyType bodyType, Bob carrierBob) {
        for (CompassDirection compassDirection : CompassDirection.values()) {

            List<Bitmap> bodyImagesForDirection = new ArrayList<>();

            for (int animationIndex = 0; animationIndex < 8; animationIndex++) {
                PlayerBitmap body = carrierBob.getBody(bodyType == FAT, compassDirection.ordinal(), animationIndex);

                bodyImagesForDirection.add(body);
            }

            this.nationToDirectionToImageMap.get(Nation.ROMANS).put(compassDirection, bodyImagesForDirection);
        }
    }
}
