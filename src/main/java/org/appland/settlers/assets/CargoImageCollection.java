package org.appland.settlers.assets;

import org.appland.settlers.model.Material;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CargoImageCollection {
    private final Map<Material, Bitmap> cargos;
    private final Map<Nation, Map<Material, Bitmap>> nationCargos;

    public CargoImageCollection() {
        nationCargos = new HashMap<>();
        cargos = new HashMap<>();

        for (Nation nation : Nation.values()) {
            nationCargos.put(nation, new HashMap<>());
        }
    }

    public void addCargoImage(Material material, Bitmap image) {
        cargos.put(material, image);
    }

    public void addCargoImageForNation(Nation nation, Material material, Bitmap image) {
        nationCargos.get(nation).put(material, image);
    }

    public void writeImageAtlas(String toDir, Palette palette) throws IOException {

        // Calculate the dimensions for the image atlas
        Utils.RowLayoutInfo aggregatedLayout = new Utils.RowLayoutInfo();

        aggregatedLayout.aggregate(Utils.layoutInfoFromImageSeries(cargos.values()));

        for (Nation nation : Nation.values()) {
            aggregatedLayout.aggregate(Utils.layoutInfoFromImageSeries(nationCargos.get(nation).values()));
        }

        // Create the image atlas
        int totalWidth = aggregatedLayout.getImageWidth() * 2;
        int totalHeight = aggregatedLayout.getRowHeight() * cargos.size();

        Bitmap imageAtlas = new Bitmap(totalWidth, totalHeight, palette, TextureFormat.BGRA);

        JSONObject jsonImageAtlas = new JSONObject();

        // Fill in the image atlas
        JSONObject jsonGeneric = new JSONObject();
        JSONObject jsonNationSpecific = new JSONObject();

        jsonImageAtlas.put("generic", jsonGeneric);
        jsonImageAtlas.put("nationSpecific", jsonNationSpecific);

        int imageIndex = 0;
        int nextYAt = 0;
        for (Map.Entry<Material, Bitmap> entry : cargos.entrySet()) {

            Material material = entry.getKey();
            Bitmap image = entry.getValue();

            Point offset = aggregatedLayout.getImageAtlasOffsetForImage(image);
            Point center = aggregatedLayout.getDrawOffset();

            int x = 0;
            int y = nextYAt;

            imageAtlas.copyNonTransparentPixels(
                    image,
                    new Point(x + offset.x, y + offset.y),
                    new Point(0, 0),
                    image.getDimension()
            );

            JSONObject jsonCargoImage = new JSONObject();

            jsonGeneric.put(material.name().toUpperCase(), jsonCargoImage);

            jsonCargoImage.put("x", x);
            jsonCargoImage.put("y", y);
            jsonCargoImage.put("width", image.getWidth());
            jsonCargoImage.put("height", image.getHeight());
            jsonCargoImage.put("offsetX", center.x);
            jsonCargoImage.put("offsetY", center.y);

            nextYAt = nextYAt + aggregatedLayout.getRowHeight();

            imageIndex = imageIndex + 1;
        }

        nextYAt = 0;

        for (Nation nation : Nation.values()) {

            JSONObject jsonNation = new JSONObject();

            jsonNationSpecific.put(nation.name().toLowerCase(), jsonNation);

            for (Map.Entry<Material, Bitmap> entry : nationCargos.get(nation).entrySet()) {

                Material material = entry.getKey();
                Bitmap image = entry.getValue();

                int x = aggregatedLayout.getImageWidth();
                int y = nextYAt;

                Point offset = aggregatedLayout.getImageAtlasOffsetForImage(image);
                Point center = aggregatedLayout.getDrawOffset();

                imageAtlas.copyNonTransparentPixels(
                        image,
                        new Point(x + offset.x, y + offset.y),
                        new Point(0, 0),
                        image.getDimension()
                );

                JSONObject jsonCargoImage = new JSONObject();

                jsonNation.put(material.name().toUpperCase(), jsonCargoImage);

                jsonCargoImage.put("x", x);
                jsonCargoImage.put("y", y);
                jsonCargoImage.put("width", image.getWidth());
                jsonCargoImage.put("height", image.getHeight());
                jsonCargoImage.put("offsetX", center.x);
                jsonCargoImage.put("offsetY", center.y);

                nextYAt = nextYAt + aggregatedLayout.getRowHeight();

                imageIndex = imageIndex + 1;
            }
        }

        // Write the image atlas to file
        imageAtlas.writeToFile(toDir + "/image-atlas-cargos.png");

        Files.writeString(Paths.get(toDir, "image-atlas-cargos.json"), jsonImageAtlas.toJSONString());
    }
}
