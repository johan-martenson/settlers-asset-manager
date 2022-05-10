package org.appland.settlers.assets;

import org.appland.settlers.model.Size;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.appland.settlers.assets.ImageBoard.LayoutDirection.ROW;

public class FireImageCollection {
    private final Map<FireSize, List<Bitmap>> fireMap;
    private final Map<Size, Bitmap> burntDownMap;

    public FireImageCollection() {
        fireMap = new HashMap<>();
        burntDownMap = new HashMap<>();

        for (FireSize fireSize : FireSize.values()) {
            fireMap.put(fireSize, new ArrayList<>());
        }
    }

    public void writeImageAtlas(String directory, Palette palette) throws IOException {

        // Write the image atlas, one row per fire animation size, and a final row with the burnt down images
        ImageBoard imageBoard = new ImageBoard();

        JSONObject jsonImageAtlas = new JSONObject();

        JSONObject jsonFireAnimations = new JSONObject();
        JSONObject jsonBurntDownImages = new JSONObject();

        jsonImageAtlas.put("fires", jsonFireAnimations);
        jsonImageAtlas.put("burntDown", jsonBurntDownImages);

        int y = 0;
        for (FireSize fireSize : FireSize.values()) {

            List<Bitmap> images = this.fireMap.get(fireSize);
            NormalizedImageList normalizedImageList = new NormalizedImageList(images);
            List<Bitmap> normalizedImages = normalizedImageList.getNormalizedImages();

            imageBoard.placeImageSeries(normalizedImages, 0, y, ROW);

            JSONObject jsonFireInfo = imageBoard.imageSeriesLocationToJson(normalizedImages);

            jsonFireAnimations.put(fireSize.name().toUpperCase(), jsonFireInfo);

            y = y + normalizedImageList.getImageHeight();
        }

        int x = 0;
        for (Map.Entry<Size, Bitmap> entry : burntDownMap.entrySet()) {
            Size size = entry.getKey();
            Bitmap image = entry.getValue();

            imageBoard.placeImage(image, x, y);

            JSONObject jsonBurntDownImage = imageBoard.imageLocationToJson(image);

            jsonBurntDownImages.put(size.name().toUpperCase(), jsonBurntDownImage);

            x = x + image.getWidth();
        }

        imageBoard.writeBoardToBitmap(palette).writeToFile(directory + "/" + "image-atlas-fire.png");

        // Write a JSON file that specifies where each image is in pixels
        Path filePath = Paths.get(directory, "image-atlas-fire.json");

        Files.writeString(filePath, jsonImageAtlas.toJSONString());
    }

    public void addImagesForFire(FireSize fireSize, List<Bitmap> imagesFromResourceLocations) {
        this.fireMap.get(fireSize).addAll(imagesFromResourceLocations);
    }

    public void addBurntDownImage(Size size, Bitmap image) {
        this.burntDownMap.put(size, image);
    }
}
