package org.appland.settlers.assets;

import org.json.simple.JSONObject;

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

        // Find the max width and height, and the max number of images over all directions
        Utils.RowLayoutInfo aggregatedLayoutInfo = new Utils.RowLayoutInfo();

        for (Nation nation : Nation.values()) {
            Map<Direction, List<Bitmap>> directionToImageMap = nationToDirectionToImageMap.get(nation);

            for (Direction direction : Direction.values()) {
                List<Bitmap> images = directionToImageMap.get(direction);

                Utils.RowLayoutInfo layoutInfo = Utils.layoutInfoFromImageSeries(images);

                aggregatedLayoutInfo.aggregate(layoutInfo);
            }
        }

        // Write the image atlas, one row per direction, and collect metadata to write as json
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        int nationIndex = 0;
        for (Nation nation : Nation.values()) {
            Map<Direction, List<Bitmap>> directionToImageMap = nationToDirectionToImageMap.get(nation);

            JSONObject jsonNationInfo = new JSONObject();

            jsonImageAtlas.put(nation.name().toLowerCase(), jsonNationInfo);

            int directionIndex = 0;
            for (Direction direction : Direction.values()) {

                JSONObject jsonDirectionInfo = new JSONObject();

                jsonNationInfo.put(direction.name().toUpperCase(), jsonDirectionInfo);

                jsonDirectionInfo.put("startX", 0);
                jsonDirectionInfo.put("startY", aggregatedLayoutInfo.getImageHeight() * (directionIndex + nationIndex * 6));
                jsonDirectionInfo.put("width", aggregatedLayoutInfo.getImageWidth());
                jsonDirectionInfo.put("height", aggregatedLayoutInfo.getImageHeight());
                jsonDirectionInfo.put("nrImages", directionToImageMap.get(direction).size());
                jsonDirectionInfo.put("offsetX", aggregatedLayoutInfo.maxNx);
                jsonDirectionInfo.put("offsetY", aggregatedLayoutInfo.maxNy);

                int imageIndex = 0;
                for (Bitmap image : directionToImageMap.get(direction)) {
                    int x = imageIndex * aggregatedLayoutInfo.getImageWidth();
                    int y = directionIndex * aggregatedLayoutInfo.getRowHeight() + nationIndex * (aggregatedLayoutInfo.getRowHeight() * 6);

                    imageBoard.placeImage(image, x, y);

                    imageIndex = imageIndex + 1;
                }

                directionIndex = directionIndex + 1;
            }

            nationIndex = nationIndex + 1;
        }

        // Write the image atlas to disk
        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-" + name.toLowerCase() + ".png");

        Path filePath = Paths.get(directory, "image-atlas-" + name.toLowerCase() + ".json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }
}
