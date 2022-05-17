package org.appland.settlers.assets.collectors;

import org.appland.settlers.assets.Bitmap;
import org.appland.settlers.assets.Direction;
import org.appland.settlers.assets.ImageBoard;
import org.appland.settlers.assets.ImageWithShadow;
import org.appland.settlers.assets.Palette;
import org.json.simple.JSONObject;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShipImageCollection {

    private final Map<Direction, ImageWithShadow> images;

    public ShipImageCollection() {
        images = new HashMap<>();
    }

    public void addShipImageWithShadow(Direction direction, Bitmap image, Bitmap shadowImage) {
        this.images.put(direction, new ImageWithShadow(image, shadowImage));
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {

        // Create the image atlas
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        Point cursor = new Point(0, 0);

        for (Direction direction : images.keySet()) {

            cursor.x = 0;

            ImageWithShadow imageWithShadow = images.get(direction);

            JSONObject jsonDirection = new JSONObject();

            jsonImageAtlas.put(direction.name().toUpperCase(), jsonDirection);

            // Place the image
            imageBoard.placeImage(imageWithShadow.image, cursor);

            jsonDirection.put("image", imageBoard.imageLocationToJson(imageWithShadow.image));

            cursor.x = cursor.x + imageWithShadow.image.getWidth();

            // Place the shadow image
            imageBoard.placeImage(imageWithShadow.shadowImage, cursor);

            jsonDirection.put("shadowImage", imageBoard.imageLocationToJson(imageWithShadow.shadowImage));

            cursor.y = cursor.y + Math.max(imageWithShadow.image.getHeight(), imageWithShadow.shadowImage.getHeight());

            // Write the image atlas to file
            imageBoard.writeBoardToBitmap(palette).writeToFile(toDir + "/image-atlas-ship.png");

            Files.writeString(Paths.get(toDir, "image-atlas-ship.json"), jsonImageAtlas.toJSONString());
        }
    }
}
