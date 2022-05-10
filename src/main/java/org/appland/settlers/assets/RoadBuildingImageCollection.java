package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RoadBuildingImageCollection {
    private Bitmap startPointImage;
    private Bitmap sameLevelConnectionImage;
    private final Map<RoadConnectionDifference, Bitmap> upwardsConnectionImages;
    private final Map<RoadConnectionDifference, Bitmap> downwardsConnectionImages;

    public RoadBuildingImageCollection() {
        downwardsConnectionImages = new HashMap<>();
        upwardsConnectionImages = new HashMap<>();
    }

    public void addStartPointImage(Bitmap image) {
        this.startPointImage = image;
    }

    public void addSameLevelConnectionImage(Bitmap image) {
        this.sameLevelConnectionImage = image;
    }

    public void addUpwardsConnectionImage(RoadConnectionDifference difference, Bitmap image) {
        this.upwardsConnectionImages.put(difference, image);
    }

    public void addDownwardsConnectionImage(RoadConnectionDifference difference, Bitmap image) {
        this.downwardsConnectionImages.put(difference, image);
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {

        // Calculate dimensions for the image atlas
        // -- layout:
        //     - row 1: start point, same level connection
        //     - row 2: upwards connection small, medium, large
        //     - row 3: downwards connection small, medium, large
        Utils.RowLayoutInfo aggregatedLayout = new Utils.RowLayoutInfo();

        aggregatedLayout.aggregate(Utils.layoutInfoFromImageSeries(upwardsConnectionImages.values()));
        aggregatedLayout.aggregate(Utils.layoutInfoFromImageSeries(downwardsConnectionImages.values()));

        // Create the image atlas
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas

        // Start point
        imageBoard.placeImage(startPointImage, 0, 0);

        JSONObject jsonStartPoint = imageBoard.imageLocationToJson(startPointImage);

        jsonImageAtlas.put("startPoint", jsonStartPoint);

        // Connection on same level
        imageBoard.placeImage(sameLevelConnectionImage, startPointImage.getWidth(), 0);

        JSONObject jsonSameLevelConnection = imageBoard.imageLocationToJson(sameLevelConnectionImage);

        jsonImageAtlas.put("sameLevelConnection", jsonSameLevelConnection);

        // Upwards connections
        JSONObject jsonUpwardsConnections = new JSONObject();

        jsonImageAtlas.put("upwardsConnections", jsonUpwardsConnections);

        int nextYAt = Math.max(startPointImage.getHeight(), sameLevelConnectionImage.getHeight());

        int imageIndex = 0;
        for (Map.Entry<RoadConnectionDifference, Bitmap> entry : this.upwardsConnectionImages.entrySet()) {

            RoadConnectionDifference difference = entry.getKey();
            Bitmap image = entry.getValue();

            imageBoard.placeImage(image, imageIndex * aggregatedLayout.getImageWidth(), nextYAt);

            JSONObject jsonUpwardsConnection = imageBoard.imageLocationToJson(image);

            jsonUpwardsConnections.put(difference.name().toUpperCase(), jsonUpwardsConnection);

            imageIndex = imageIndex + 1;
        }

        // Downwards connections
        JSONObject jsonDownwardsConnections = new JSONObject();

        jsonImageAtlas.put("downwardsConnections", jsonDownwardsConnections);

        nextYAt = nextYAt + aggregatedLayout.getRowHeight();

        imageIndex = 0;
        for (Map.Entry<RoadConnectionDifference, Bitmap> entry : this.downwardsConnectionImages.entrySet()) {

            RoadConnectionDifference difference = entry.getKey();
            Bitmap image = entry.getValue();

            imageBoard.placeImage(image, imageIndex * aggregatedLayout.getImageWidth(), nextYAt);

            JSONObject jsonDownwardsConnection = imageBoard.imageLocationToJson(image);

            jsonDownwardsConnections.put(difference.name().toUpperCase(), jsonDownwardsConnection);

            imageIndex = imageIndex + 1;
        }

        // Write the image atlas to file
        imageBoard.writeBoardToBitmap(palette).writeToFile(toDir + "/image-atlas-road-building.png");

        Files.writeString(Paths.get(toDir, "image-atlas-road-building.json"), jsonImageAtlas.toJSONString());
    }
}
