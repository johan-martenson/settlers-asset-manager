package org.appland.settlers.assets.collectors;

import org.appland.settlers.assets.Bitmap;
import org.appland.settlers.assets.Direction;
import org.appland.settlers.assets.ImageBoard;
import org.appland.settlers.assets.Nation;
import org.appland.settlers.assets.NormalizedImageList;
import org.appland.settlers.assets.Palette;
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

public class WorkerImageCollection {
    private final String name;
    private final Map<Nation, Map<Direction, List<Bitmap>>> nationToDirectionToImageMap;
    private final Map<Direction, List<Bitmap>> shadowImages;
    private final Map<Direction, Bitmap> singleCargoImages;
    private final Map<Direction, List<Bitmap>> multipleCargoImages;

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

            int rowHeight = 0;

            cursor.x = 0;

            JSONObject jsonCargoImages = new JSONObject();

            jsonImageAtlas.put("singleCargoImages", jsonCargoImages);

            for (Direction direction : singleCargoImages.keySet()) {
                Bitmap cargoImageForDirection = singleCargoImages.get(direction);

                imageBoard.placeImage(cargoImageForDirection, cursor);

                jsonCargoImages.put(direction.name().toUpperCase(), imageBoard.imageLocationToJson(cargoImageForDirection));

                rowHeight = Math.max(rowHeight, cargoImageForDirection.getHeight());

                cursor.x = cursor.x + cargoImageForDirection.getWidth();
            }

            cursor.y = cursor.y + rowHeight;
        }

        // Write lists of cargo images (if any)
        if (!multipleCargoImages.keySet().isEmpty()) {

            JSONObject jsonMultipleCargoImages = new JSONObject();

            jsonImageAtlas.put("multipleCargoImages", jsonMultipleCargoImages);

            for (Direction direction : multipleCargoImages.keySet()) {

                cursor.x = 0;

                List<Bitmap> cargoImagesForDirection = multipleCargoImages.get(direction);
                NormalizedImageList normalizedCargoListForDirection = new NormalizedImageList(cargoImagesForDirection);
                List<Bitmap> normalizedCargoImagesForDirection = normalizedCargoListForDirection.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedCargoImagesForDirection, cursor, ImageBoard.LayoutDirection.ROW);

                jsonMultipleCargoImages.put(direction.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedCargoImagesForDirection));

                cursor.y = cursor.y + normalizedCargoListForDirection.getImageHeight();
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

    public void addCargoImage(Direction direction, Bitmap image) {
        singleCargoImages.put(direction, image);
    }

    public void addCargoImages(Direction direction, Bitmap... images) {
        multipleCargoImages.put(direction, Arrays.asList(images));
    }
}
