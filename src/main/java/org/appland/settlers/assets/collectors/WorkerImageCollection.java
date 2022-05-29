package org.appland.settlers.assets.collectors;

import org.appland.settlers.assets.Bitmap;
import org.appland.settlers.assets.Bob;
import org.appland.settlers.assets.BodyType;
import org.appland.settlers.assets.Direction;
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
    private final Map<Nation, Map<Direction, List<Bitmap>>> nationToDirectionToImageMap;
    private final Map<Direction, List<Bitmap>> shadowImages;
    private final Map<Material, Map<Direction, Bitmap>> singleCargoImages;
    private final Map<Material, Map<Direction, List<Bitmap>>> multipleCargoImages;

    public WorkerImageCollection(String name) {
        this.name = name;
        nationToDirectionToImageMap = new HashMap<>();

        for (Nation nation : Nation.values()) {

            this.nationToDirectionToImageMap.put(nation, new HashMap<>());

            for (Direction direction : Direction.values()) {
                this.nationToDirectionToImageMap.get(nation).put(direction, new ArrayList<>());
            }
        }

        shadowImages = new HashMap<>();
        singleCargoImages = new HashMap<>();
        multipleCargoImages = new HashMap<>();
    }

    public void addImage(Nation nation, Direction direction, Bitmap workerImage) {
        this.nationToDirectionToImageMap.get(nation).get(direction).add(workerImage);
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

            Map<Direction, List<Bitmap>> directionToImageMap = nationToDirectionToImageMap.get(nation);

            JSONObject jsonNationInfo = new JSONObject();

            jsonImages.put(nation.name().toUpperCase(), jsonNationInfo);

            for (Direction direction : Direction.values()) {

                if (directionToImageMap.get(direction).isEmpty()) {
                    continue;
                }

                // Handle each image per nation x direction
                List<Bitmap> workerImages = directionToImageMap.get(direction);
                NormalizedImageList normalizedWorkerList = new NormalizedImageList(workerImages);
                List<Bitmap> normalizedWorkerImages = normalizedWorkerList.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedWorkerImages, cursor, ImageBoard.LayoutDirection.ROW);

                jsonNationInfo.put(direction.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedWorkerImages));

                cursor.y = cursor.y + normalizedWorkerList.getImageHeight();
            }
        }

        // Write shadows, per direction (seems to be the same regardless of nation)
        for (Direction direction : shadowImages.keySet()) {
            cursor.x = 0;

            List<Bitmap> shadowImagesForDirection = shadowImages.get(direction);
            NormalizedImageList normalizedShadowListForDirection = new NormalizedImageList(shadowImagesForDirection);
            List<Bitmap> normalizedShadowImagesForDirection = normalizedShadowListForDirection.getNormalizedImages();

            imageBoard.placeImageSeries(normalizedShadowImagesForDirection, cursor, ImageBoard.LayoutDirection.ROW);

            jsonShadowImages.put(direction.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedShadowImagesForDirection));

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

                for (Direction direction : singleCargoImages.get(material).keySet()) {
                    Bitmap cargoImageForDirection = singleCargoImages.get(material).get(direction);

                    imageBoard.placeImage(cargoImageForDirection, cursor);

                    jsonMaterialImage.put(direction.name().toUpperCase(), imageBoard.imageLocationToJson(cargoImageForDirection));

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

                for (Direction direction : multipleCargoImages.get(material).keySet()) {

                    List<Bitmap> cargoImagesForDirection = multipleCargoImages.get(material).get(direction);
                    NormalizedImageList normalizedCargoListForDirection = new NormalizedImageList(cargoImagesForDirection);
                    List<Bitmap> normalizedCargoImagesForDirection = normalizedCargoListForDirection.getNormalizedImages();

                    imageBoard.placeImageSeries(normalizedCargoImagesForDirection, cursor, ImageBoard.LayoutDirection.ROW);

                    jsonMaterialImages.put(direction.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedCargoImagesForDirection));

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

    public void addShadowImages(Direction direction, List<Bitmap> images) {
        shadowImages.put(direction, images);
    }

    public void addCargoImage(Direction direction, Material material, Bitmap image) {
        if (!singleCargoImages.containsKey(material)) {
            singleCargoImages.put(material, new HashMap<>());
        }

        singleCargoImages.get(material).put(direction, image);
    }

    public void addCargoImages(Direction direction, Material material, Bitmap... images) {
        if (!multipleCargoImages.containsKey(material)) {
            multipleCargoImages.put(material, new HashMap<>());
        }

        multipleCargoImages.get(material).put(direction, Arrays.asList(images));
    }

    public void readCargoImagesFromBob(Material material, BodyType bodyType, int bobId, Bob jobsBob) {
        int fatOffset = 0;

        if (bodyType == FAT) {
            fatOffset = 1;
        }

        multipleCargoImages.put(material, new HashMap<>());

        for (Direction direction : Direction.values()) {

            List<Bitmap> cargoImagesForDirection = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                int link = ((bobId * 8 + i) * 2 + fatOffset) * 6 + direction.ordinal();
                int index = jobsBob.getLinkForIndex(link);

                cargoImagesForDirection.add(jobsBob.getBitmapAtIndex(index));
            }

            multipleCargoImages.get(material).put(direction, cargoImagesForDirection);
        }
    }

    public void readBodyImagesFromBob(BodyType bodyType, Bob carrierBob) {
        for (Direction direction : Direction.values()) {

            List<Bitmap> bodyImagesForDirection = new ArrayList<>();

            for (int animationIndex = 0; animationIndex < 8; animationIndex++) {
                PlayerBitmap body = carrierBob.getBody(bodyType == FAT, direction.ordinal(), animationIndex);

                bodyImagesForDirection.add(body);
            }

            this.nationToDirectionToImageMap.get(Nation.ROMANS).put(direction, bodyImagesForDirection);
        }
    }
}
