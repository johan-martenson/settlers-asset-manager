package org.appland.settlers.assets;

import org.appland.settlers.model.Size;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignImageCollection {
    private final Map<SignType, Map<Size, Bitmap>> signTypeToImageMap;
    private final List<SignType> SIGN_TYPES = Arrays.asList(SignType.values());

    public SignImageCollection() {
        signTypeToImageMap = new HashMap<>();

        Arrays.asList(SignType.values()).forEach(signType -> this.signTypeToImageMap.put(signType, new HashMap<>()));
    }

    public void addImage(SignType signType, Size size, Bitmap image) {
        this.signTypeToImageMap.get(signType).put(size, image);
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {
        // Calculate the dimensions of the image atlas
        int maxWidth = 0;
        int maxHeight = 0;

        for (SignType signType : SIGN_TYPES) {
            for (Size size : signTypeToImageMap.get(signType).keySet()) {
                Bitmap image = signTypeToImageMap.get(signType).get(size);

                maxWidth = Math.max(maxWidth, image.width);
                maxHeight = Math.max(maxHeight, image.height);
            }
        }

        // Create the image atlas and meta-data
        Bitmap imageAtlas = new Bitmap(
                Size.values().length * maxWidth,
                SIGN_TYPES.size() * maxHeight,
                palette,
                TextureFormat.BGRA
        );

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        int materialIndex = 0;
        for (SignType signType : SIGN_TYPES) {

            JSONObject jsonMaterial = new JSONObject();

            jsonImageAtlas.put(signType.name().toLowerCase(), jsonMaterial);

            int sizeIndex = 0;
            for (Size size : signTypeToImageMap.get(signType).keySet()) {
                Bitmap image = signTypeToImageMap.get(signType).get(size);

                if (image == null) {
                    continue;
                }

                imageAtlas.copyNonTransparentPixels(
                        image,
                        new Point(sizeIndex * maxWidth, materialIndex * maxHeight),
                        new Point(0, 0),
                        image.getDimension()
                );

                JSONObject jsonSign = new JSONObject();

                jsonMaterial.put(size.name().toUpperCase(), jsonSign);

                jsonSign.put("x", sizeIndex * maxWidth);
                jsonSign.put("y", materialIndex * maxHeight);
                jsonSign.put("width", image.width);
                jsonSign.put("height", image.height);
                jsonSign.put("offsetX", image.nx);
                jsonSign.put("offsetY", image.ny);

                sizeIndex = sizeIndex + 1;
            }

            materialIndex = materialIndex + 1;
        }

        // Write the image atlas image to file
        imageAtlas.writeToFile(toDir + "/image-atlas-signs.png");

        // Write the meta-data
        Files.writeString(Paths.get(toDir, "image-atlas-signs.json"), jsonImageAtlas.toJSONString());
    }
}
