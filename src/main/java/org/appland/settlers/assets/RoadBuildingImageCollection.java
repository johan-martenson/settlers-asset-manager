package org.appland.settlers.assets;

import org.json.simple.JSONObject;

import java.awt.*;
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
        int row1Height = Math.max(startPointImage.height, sameLevelConnectionImage.height);

        Utils.RowLayoutInfo aggregatedLayout = new Utils.RowLayoutInfo();

        aggregatedLayout.aggregate(Utils.layoutInfoFromImageSeries(upwardsConnectionImages.values()));
        aggregatedLayout.aggregate(Utils.layoutInfoFromImageSeries(downwardsConnectionImages.values()));

        int totalWidth = Math.max(aggregatedLayout.getRowWidth(), startPointImage.getWidth() + sameLevelConnectionImage.getWidth());
        int totalHeight = row1Height + 2 * aggregatedLayout.getRowHeight();

        // Create the image atlas
        Bitmap imageAtlas = new Bitmap(
                totalWidth,
                totalHeight,
                palette,
                TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        imageAtlas.copyNonTransparentPixels(
                startPointImage,
                new Point(0, 0),
                new Point(0, 0),
                startPointImage.getDimension()
        );

        JSONObject jsonStartPoint = new JSONObject();

        jsonImageAtlas.put("startPoint", jsonStartPoint);

        jsonStartPoint.put("x", 0);
        jsonStartPoint.put("y", 0);
        jsonStartPoint.put("width", startPointImage.getWidth());
        jsonStartPoint.put("height", startPointImage.getHeight());
        jsonStartPoint.put("offsetX", startPointImage.nx);
        jsonStartPoint.put("offsetY", startPointImage.ny);

        imageAtlas.copyNonTransparentPixels(
                startPointImage,
                new Point(0, 0),
                new Point(0, 0),
                startPointImage.getDimension()
        );

        imageAtlas.copyNonTransparentPixels(
                sameLevelConnectionImage,
                new Point(startPointImage.getWidth(), 0),
                new Point(0, 0),
                sameLevelConnectionImage.getDimension()
        );

        JSONObject jsonSameLevelConnection = new JSONObject();

        jsonImageAtlas.put("sameLevelConnection", jsonSameLevelConnection);

        jsonSameLevelConnection.put("x", startPointImage.getWidth());
        jsonSameLevelConnection.put("y", 0);
        jsonSameLevelConnection.put("width", sameLevelConnectionImage.getWidth());
        jsonSameLevelConnection.put("height", sameLevelConnectionImage.getHeight());
        jsonSameLevelConnection.put("offsetX", sameLevelConnectionImage.nx);
        jsonSameLevelConnection.put("offsetY", sameLevelConnectionImage.ny);

        JSONObject jsonUpwardsConnections = new JSONObject();

        jsonImageAtlas.put("upwardsConnections", jsonUpwardsConnections);

        int nextYAt = Math.max(startPointImage.getHeight(), sameLevelConnectionImage.getHeight());

        int imageIndex = 0;
        for (Map.Entry<RoadConnectionDifference, Bitmap> entry : this.upwardsConnectionImages.entrySet()) {

            RoadConnectionDifference difference = entry.getKey();
            Bitmap image = entry.getValue();

            imageAtlas.copyNonTransparentPixels(
                    image,
                    new Point(imageIndex * aggregatedLayout.getImageWidth(), nextYAt),
                    new Point(0, 0),
                    image.getDimension()
            );

            JSONObject jsonUpwardsConnection = new JSONObject();

            jsonUpwardsConnections.put(difference.name().toUpperCase(), jsonUpwardsConnection);

            jsonUpwardsConnection.put("x", imageIndex * aggregatedLayout.getImageWidth());
            jsonUpwardsConnection.put("y", nextYAt);
            jsonUpwardsConnection.put("width", image.getWidth());
            jsonUpwardsConnection.put("height", image.getHeight());
            jsonUpwardsConnection.put("offsetX", image.nx);
            jsonUpwardsConnection.put("offsetY", image.ny);

            imageIndex = imageIndex + 1;
        }

        JSONObject jsonDownwardsConnections = new JSONObject();

        jsonImageAtlas.put("downwardsConnections", jsonDownwardsConnections);

        nextYAt = nextYAt + aggregatedLayout.getRowHeight();

        imageIndex = 0;
        for (Map.Entry<RoadConnectionDifference, Bitmap> entry : this.downwardsConnectionImages.entrySet()) {

            RoadConnectionDifference difference = entry.getKey();
            Bitmap image = entry.getValue();

            imageAtlas.copyNonTransparentPixels(
                    image,
                    new Point(imageIndex * aggregatedLayout.getImageWidth(), nextYAt),
                    new Point(0, 0),
                    image.getDimension()
            );

            JSONObject jsonDownwardsConnection = new JSONObject();

            jsonDownwardsConnections.put(difference.name().toUpperCase(), jsonDownwardsConnection);

            jsonDownwardsConnection.put("x", imageIndex * aggregatedLayout.getImageWidth());
            jsonDownwardsConnection.put("y", nextYAt);
            jsonDownwardsConnection.put("width", image.getWidth());
            jsonDownwardsConnection.put("height", image.getHeight());
            jsonDownwardsConnection.put("offsetX", image.nx);
            jsonDownwardsConnection.put("offsetY", image.ny);

            imageIndex = imageIndex + 1;
        }

        // Write the image atlas to file
        imageAtlas.writeToFile(toDir + "/image-atlas-road-building.png");

        Files.writeString(Paths.get(toDir, "image-atlas-road-building.json"), jsonImageAtlas.toJSONString());
    }
}
