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
import org.appland.settlers.assets.TextureFormat;
import org.appland.settlers.model.Material;
import org.json.simple.JSONObject;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.appland.settlers.assets.BodyType.FAT;

public class WorkerImageCollection {
    private final String name;
    private final Map<Nation, Map<CompassDirection, List<Bitmap>>> nationSpecificBodyAndHeadImages;
    private final Map<CompassDirection, List<Bitmap>> commonShadowImages;
    private final Map<Material, Map<CompassDirection, List<Bitmap>>> commonCargoImages;
    private final Map<CompassDirection, List<Bitmap>> commonHeadImagesWithoutCargo;
    private final Map<CompassDirection, List<Bitmap>> commonBodyImages;
    private final Map<CompassDirection, List<Bitmap>> commonBodyAndHeadImages;

    public WorkerImageCollection(String name) {
        this.name = name;

        nationSpecificBodyAndHeadImages = new HashMap<>();
        commonHeadImagesWithoutCargo = new HashMap<>();
        commonShadowImages = new HashMap<>();
        commonCargoImages = new HashMap<>();
        commonBodyImages = new HashMap<>();
        commonBodyAndHeadImages = new HashMap<>();
    }

    public void addNationSpecificFullImage(Nation nation, CompassDirection compassDirection, Bitmap workerImage) {
        if (!nationSpecificBodyAndHeadImages.containsKey(nation)) {
            nationSpecificBodyAndHeadImages.put(nation, new HashMap<>());
        }

        Map<CompassDirection, List<Bitmap>> directions = nationSpecificBodyAndHeadImages.get(nation);

        if (!directions.containsKey(compassDirection)) {
            directions.put(compassDirection, new ArrayList<>());
        }

        nationSpecificBodyAndHeadImages.get(nation).get(compassDirection).add(workerImage);
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        /**
         * Write the image atlas, one row per direction, and collect metadata to write as json
         *
         * JSON format:
         *   - common: walking images without cargo, not nation-specific
         *        - fullImages
         *        - cargoImages: walking images with cargo, not nation-specific
         *        - shadowImages: shadow images, same regardless of nation or cargo
         *   - nationSpecific: walking images without cargo, nation-specific
         *        - fullImages
         *        - cargoImages
         */
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();
        JSONObject jsonCommon = new JSONObject();
        JSONObject jsonNationSpecific = new JSONObject();

        jsonImageAtlas.put("common", jsonCommon);
        jsonImageAtlas.put("nationSpecific", jsonNationSpecific);

        Point cursor = new Point(0, 0);

        // Write walking animations where the worker isn't carrying anything and that are not nation-specific
        if (!commonBodyAndHeadImages.isEmpty()) {
            JSONObject jsonImages = new JSONObject();

            jsonCommon.put("fullImages", jsonImages);

            for (Map.Entry<CompassDirection, List<Bitmap>> entry : commonBodyAndHeadImages.entrySet()) {
                CompassDirection compassDirection = entry.getKey();
                List<Bitmap> images = entry.getValue();

                cursor.x = 0;

                NormalizedImageList normalizedImageList = new NormalizedImageList(images);
                List<Bitmap> normalizedImages = normalizedImageList.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedImages, cursor, ImageBoard.LayoutDirection.ROW);

                jsonImages.put(compassDirection.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedImages));

                cursor.y = cursor.y + normalizedImageList.getImageHeight();
            }
        } else {
            JSONObject jsonBodyImages = new JSONObject();

            jsonCommon.put("bodyImages", jsonBodyImages);

            for (Map.Entry<CompassDirection, List<Bitmap>> entry : commonBodyImages.entrySet()) {
                CompassDirection compassDirection = entry.getKey();
                List<Bitmap> images = entry.getValue();

                cursor.x = 0;

                NormalizedImageList normalizedImageList = new NormalizedImageList(images);
                List<Bitmap> normalizedImages = normalizedImageList.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedImages, cursor, ImageBoard.LayoutDirection.ROW);

                jsonBodyImages.put(compassDirection.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedImages));

                cursor.y = cursor.y + normalizedImageList.getImageHeight();
            }
        }

        // Write walking animations, per nation and direction
        if (!nationSpecificBodyAndHeadImages.isEmpty()) {
            JSONObject jsonFullImages = new JSONObject();

            jsonNationSpecific.put("fullImages", jsonFullImages);

            for (Nation nation : Nation.values()) {

                cursor.x = 0;

                Map<CompassDirection, List<Bitmap>> directionToImageMap = nationSpecificBodyAndHeadImages.get(nation);

                JSONObject jsonNationInfo = new JSONObject();

                jsonFullImages.put(nation.name().toUpperCase(), jsonNationInfo);

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
        }

        // Write shadows, per direction (shadows are not nation-specific or are the same regardless of if/what the courier is carrying)
        if (!commonShadowImages.isEmpty()) {
            JSONObject jsonShadowImages = new JSONObject();

            jsonCommon.put("shadowImages", jsonShadowImages);

            for (Map.Entry<CompassDirection, List<Bitmap>> entry : commonShadowImages.entrySet()) {
                CompassDirection compassDirection = entry.getKey();
                List<Bitmap> shadowImagesForDirection = entry.getValue();

                cursor.x = 0;

                NormalizedImageList normalizedShadowListForDirection = new NormalizedImageList(shadowImagesForDirection);
                List<Bitmap> normalizedShadowImagesForDirection = normalizedShadowListForDirection.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedShadowImagesForDirection, cursor, ImageBoard.LayoutDirection.ROW);

                jsonShadowImages.put(compassDirection.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedShadowImagesForDirection));

                cursor.y = cursor.y + normalizedShadowListForDirection.getImageHeight();
            }
        }

        // Write lists of cargo images (if any)
        if (!commonCargoImages.keySet().isEmpty()) {

            JSONObject jsonMultipleCargoImages = new JSONObject();

            jsonCommon.put("cargoImages", jsonMultipleCargoImages);

            for (Material material : commonCargoImages.keySet()) {

                if (material == null) {
                    continue;
                }

                JSONObject jsonMaterialImages = new JSONObject();

                jsonMultipleCargoImages.put(material.name().toUpperCase(), jsonMaterialImages);

                cursor.x = 0;

                int rowHeight = 0;

                for (Map.Entry<CompassDirection, List<Bitmap>> entry : commonCargoImages.get(material).entrySet()) {

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
        commonShadowImages.put(compassDirection, images);
    }

    public void readCargoImagesFromBob(Material material, BodyType bodyType, int bobId, Bob jobsBob) {
        int fatOffset = 0;

        if (bodyType == FAT) {
            fatOffset = 1;
        }

        commonCargoImages.put(material, new HashMap<>());

        for (CompassDirection compassDirection : CompassDirection.values()) {

            List<Bitmap> cargoImagesForDirection = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                int link = ((bobId * 8 + i) * 2 + fatOffset) * 6 + compassDirection.ordinal();
                int index = jobsBob.getLinkForIndex(link);

                cargoImagesForDirection.add(jobsBob.getBitmapAtIndex(index));
            }

            commonCargoImages.get(material).put(compassDirection, cargoImagesForDirection);
        }
    }

    public void readHeadImagesWithoutCargoFromBob(BodyType bodyType, int bobId, Bob jobsBob) {
        int fatOffset = 0;

        if (bodyType == FAT) {
            fatOffset = 1;
        }

        for (CompassDirection compassDirection : CompassDirection.values()) {

            List<Bitmap> cargoImagesForDirection = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                int link = ((bobId * 8 + i) * 2 + fatOffset) * 6 + compassDirection.ordinal();
                int index = jobsBob.getLinkForIndex(link);

                cargoImagesForDirection.add(jobsBob.getBitmapAtIndex(index));
            }

            commonHeadImagesWithoutCargo.put(compassDirection, cargoImagesForDirection);
        }
    }

    public void mergeBodyAndHeadImages(Palette palette) {
        for (Map.Entry<CompassDirection, List<Bitmap>> entry : commonBodyImages.entrySet()) {
            CompassDirection compassDirection = entry.getKey();
            List<Bitmap> bodyImagesForDirection = entry.getValue();
            List<Bitmap> headImagesForDirection = commonHeadImagesWithoutCargo.get(compassDirection);

            List<Bitmap> mergedBodyAndHeadImagesForDirection = new ArrayList<>();

            // Merge the head images with the body images
            for (int i = 0; i < 8; i++) {
                Bitmap bodyImage = bodyImagesForDirection.get(i);
                Bitmap headImage = headImagesForDirection.get(i);

                List<Bitmap> imageList = new ArrayList<>();
                imageList.add(bodyImage);
                imageList.add(headImage);

                NormalizedImageList normalizedImageList = new NormalizedImageList(imageList);
                List<Bitmap> normalizedImages = normalizedImageList.getNormalizedImages();

                Bitmap normalizedBodyImage = normalizedImages.get(0);
                Bitmap normalizedHeadImage = normalizedImages.get(1);

                Bitmap combinedImage = new Bitmap(
                        normalizedImageList.getImageWidth(),
                        normalizedImageList.getImageHeight(),
                        palette,
                        TextureFormat.BGRA);

                combinedImage.copyNonTransparentPixels(
                        normalizedBodyImage,
                        new Point(0, 0),
                        new Point(0, 0),
                        normalizedBodyImage.getDimension()
                );

                combinedImage.copyNonTransparentPixels(
                        normalizedHeadImage,
                        new Point(0, 0),
                        new Point(0, 0),
                        normalizedHeadImage.getDimension()
                );

                combinedImage.setNx(normalizedImageList.nx);
                combinedImage.setNy(normalizedImageList.ny);

                mergedBodyAndHeadImagesForDirection.add(combinedImage);
            }

            commonBodyAndHeadImages.put(compassDirection, mergedBodyAndHeadImagesForDirection);
        }
    }

    public void readBodyImagesFromBob(BodyType bodyType, Bob carrierBob) {
        for (CompassDirection compassDirection : CompassDirection.values()) {

            List<Bitmap> bodyImagesForDirection = new ArrayList<>();

            for (int animationIndex = 0; animationIndex < 8; animationIndex++) {
                PlayerBitmap body = carrierBob.getBody(bodyType == FAT, compassDirection.ordinal(), animationIndex);

                bodyImagesForDirection.add(body);
            }

            commonBodyImages.put(compassDirection, bodyImagesForDirection);
        }
    }
}
