package org.appland.settlers.assets;

import org.appland.settlers.model.Size;
import org.json.simple.JSONObject;

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
        ImageBoard imageBoard = new ImageBoard();

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

                imageBoard.placeImage(image, sizeIndex * maxWidth, materialIndex * maxHeight);

                JSONObject jsonSign = imageBoard.imageLocationToJson(image);

                jsonMaterial.put(size.name().toUpperCase(), jsonSign);

                sizeIndex = sizeIndex + 1;
            }

            materialIndex = materialIndex + 1;
        }

        // Write the image atlas image to file
        imageBoard.writeBoardToBitmap(palette).writeToFile(toDir + "/image-atlas-signs.png");

        Files.writeString(Paths.get(toDir, "image-atlas-signs.json"), jsonImageAtlas.toJSONString());
    }
}
