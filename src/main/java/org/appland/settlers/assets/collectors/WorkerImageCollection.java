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

    public WorkerImageCollection(String name) {
        this.name = name;
        nationToDirectionToImageMap = new HashMap<>();

        for (Nation nation : Nation.values()) {

            this.nationToDirectionToImageMap.put(nation, new HashMap<>());

            for (Direction direction : Direction.values()) {
                this.nationToDirectionToImageMap.get(nation).put(direction, new ArrayList<>());
            }
        }
    }

    public void addImage(Nation nation, Direction direction, Bitmap workerImage) {
        this.nationToDirectionToImageMap.get(nation).get(direction).add(workerImage);
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        // Write the image atlas, one row per direction, and collect metadata to write as json
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        Point cursor = new Point(0, 0);

        for (Nation nation : Nation.values()) {

            cursor.x = 0;

            Map<Direction, List<Bitmap>> directionToImageMap = nationToDirectionToImageMap.get(nation);

            JSONObject jsonNationInfo = new JSONObject();

            jsonImageAtlas.put(nation.name().toLowerCase(), jsonNationInfo);


            for (Direction direction : Direction.values()) {

                List<Bitmap> workerImages = directionToImageMap.get(direction);
                NormalizedImageList normalizedWorkerList = new NormalizedImageList(workerImages);
                List<Bitmap> normalizedWorkerImages = normalizedWorkerList.getNormalizedImages();

                imageBoard.placeImageSeries(normalizedWorkerImages, cursor, ImageBoard.LayoutDirection.ROW);

                jsonNationInfo.put(direction.name().toUpperCase(), imageBoard.imageSeriesLocationToJson(normalizedWorkerImages));

                cursor.y = cursor.y + normalizedWorkerList.getImageHeight();
            }
        }

        // Write the image atlas to disk
        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }
}
