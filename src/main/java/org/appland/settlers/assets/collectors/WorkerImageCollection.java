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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkerImageCollection {
    private final String name;
    private final Map<Nation, Map<Direction, List<Bitmap>>> nationToDirectionToImageMap;
    private final Map<Direction, List<Bitmap>> shadowImages;

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

        // Write the image atlas to disk
        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addShadowImages(Direction direction, List<Bitmap> images) {
        shadowImages.put(direction, images);
    }
}