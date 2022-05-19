package org.appland.settlers.assets;

import org.appland.settlers.assets.collectors.AnimalImageCollection;
import org.appland.settlers.assets.collectors.BorderImageCollector;
import org.appland.settlers.assets.collectors.BuildingsImageCollection;
import org.appland.settlers.assets.collectors.CargoImageCollection;
import org.appland.settlers.assets.collectors.CropImageCollection;
import org.appland.settlers.assets.collectors.DecorativeImageCollection;
import org.appland.settlers.assets.collectors.FireImageCollection;
import org.appland.settlers.assets.collectors.FlagImageCollection;
import org.appland.settlers.assets.collectors.RoadBuildingImageCollection;
import org.appland.settlers.assets.collectors.ShipImageCollection;
import org.appland.settlers.assets.collectors.SignImageCollection;
import org.appland.settlers.assets.collectors.StonesImageCollection;
import org.appland.settlers.assets.collectors.TreeImageCollection;
import org.appland.settlers.assets.collectors.UIElementsImageCollection;
import org.appland.settlers.assets.collectors.WorkerImageCollection;
import org.appland.settlers.assets.gamefiles.AfrZLst;
import org.appland.settlers.assets.gamefiles.BootBobsLst;
import org.appland.settlers.assets.gamefiles.JapZLst;
import org.appland.settlers.assets.gamefiles.JobsBob;
import org.appland.settlers.assets.gamefiles.Map0ZLst;
import org.appland.settlers.assets.gamefiles.MapBobs0Lst;
import org.appland.settlers.assets.gamefiles.MapBobsLst;
import org.appland.settlers.assets.gamefiles.RomYLst;
import org.appland.settlers.assets.gamefiles.RomZLst;
import org.appland.settlers.assets.gamefiles.VikZLst;
import org.appland.settlers.model.Crop;
import org.appland.settlers.model.Material;
import org.appland.settlers.model.Tree;
import org.appland.settlers.model.TreeSize;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.appland.settlers.assets.Direction.EAST;
import static org.appland.settlers.assets.Direction.NORTH_EAST;
import static org.appland.settlers.assets.Direction.NORTH_WEST;
import static org.appland.settlers.assets.Direction.SOUTH_EAST;
import static org.appland.settlers.assets.Direction.SOUTH_WEST;
import static org.appland.settlers.assets.Direction.WEST;
import static org.appland.settlers.model.Size.LARGE;
import static org.appland.settlers.model.Size.MEDIUM;
import static org.appland.settlers.model.Size.SMALL;

public class Extractor {

    private static final String ROMAN_FILE = "DATA/MBOB/ROM_Y.LST";
    private static final String MAP_FILE = "DATA/MAPBOBS.LST";
    private static final String GREENLAND_TEXTURE_FILE = "GFX/TEXTURES/TEX5.LBM";
    private static final String WINTER_TEXTURE_FILE = "GFX/TEXTURES/TEX7.LBM";

    private static final String DEFAULT_PALETTE = "/home/johan/projects/settlers-image-manager/src/main/resources/default-palette.act";

    private static final String ROMAN_BUILDINGS_DIRECTORY = "roman-buildings";
    private static final String UI_ELEMENTS_DIRECTORY = "ui-elements";
    private static final String NATURE_DIRECTORY = "nature";
    private static final String SIGNS_DIRECTORY = "signs";
    private static final String TERRAIN_SUB_DIRECTORY = "terrain";
    private static final String GREENLAND_DIRECTORY = "greenland";
    private static final String WINTER_DIRECTORY = "winter";

    private static final int FLAG_INDEX = 4;
    private static final int DEAD_TREE = 288;
    private static final int FALLEN_DEAD_TREE = 287;
    private static final int LAND_BORDER_ICON = 0;
    private static final int COAST_BORDER_ICON = 2;

    @Option(name = "--from-dir", usage = "Asset directory to load from")
    static String fromDir;

    @Option(name = "--to-dir", usage = "Directory to extract assets into")
    static String toDir;

    private final AssetManager assetManager;
    private Palette defaultPalette;

    public static void main(String[] args) throws IOException, InvalidHeaderException, InvalidFormatException, UnknownResourceTypeException, CmdLineException {

        Extractor extractor = new Extractor();

        CmdLineParser parser = new CmdLineParser(extractor);

        parser.parseArgument(args);

        if (!Utils.isDirectory(toDir) || !Utils.isEmptyDirectory(toDir)) {
            System.out.println("Must specify an empty directory to extract assets into: " + toDir);
        }

        /* Get the default palette */
        extractor.loadDefaultPalette();

        /* Extract assets */
        extractor.populateRomanBuildings(fromDir, toDir);

        extractor.populateNatureAndUIElements(fromDir, toDir);

        extractor.populateWorkers(fromDir, toDir);

        extractor.populateFlags(fromDir, toDir);

        extractor.populateBorders(fromDir, toDir);

        extractor.populateShips(fromDir, toDir);
    }

    private void populateShips(String fromDir, String toDir) throws UnknownResourceTypeException, IOException, InvalidHeaderException, InvalidFormatException {
        List<GameResource> bootBobsLst = assetManager.loadLstFile(fromDir + "/" + BootBobsLst.FILENAME, defaultPalette);

        ShipImageCollection shipImageCollection = new ShipImageCollection();

        shipImageCollection.addShipImageWithShadow(
                EAST,
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_EAST),
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_EAST_SHADOW)
        );
        shipImageCollection.addShipImageWithShadow(
                SOUTH_EAST,
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_SOUTH_EAST),
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_SOUTH_EAST_SHADOW)
        );
        shipImageCollection.addShipImageWithShadow(
                SOUTH_WEST,
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_SOUTH_WEST),
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_SOUTH_WEST_SHADOW)
        );
        shipImageCollection.addShipImageWithShadow(
                WEST,
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_WEST),
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_WEST_SHADOW)
        );
        shipImageCollection.addShipImageWithShadow(
                NORTH_WEST,
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_NORTH_WEST),
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_NORTH_WEST_SHADOW)
        );
        shipImageCollection.addShipImageWithShadow(
                NORTH_EAST,
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_NORTH_EAST),
                getImageFromResourceLocation(bootBobsLst, BootBobsLst.SHIP_NORTH_EAST_SHADOW)
        );

        shipImageCollection.writeImageAtlas(toDir, defaultPalette);
    }

    private void populateBorders(String fromDir, String toDir) throws UnknownResourceTypeException, IOException, InvalidHeaderException, InvalidFormatException {
        List<GameResource> afrBobsLst = assetManager.loadLstFile(fromDir + "/DATA/MBOB/AFR_BOBS.LST", defaultPalette);
        List<GameResource> japBobsLst = assetManager.loadLstFile(fromDir + "/DATA/MBOB/JAP_BOBS.LST", defaultPalette);
        List<GameResource> romBobsLst = assetManager.loadLstFile(fromDir + "/DATA/MBOB/ROM_BOBS.LST", defaultPalette);
        List<GameResource> vikBobsLst = assetManager.loadLstFile(fromDir + "/DATA/MBOB/VIK_BOBS.LST", defaultPalette);

        BorderImageCollector borderImageCollector = new BorderImageCollector();

        borderImageCollector.addLandBorderImage(Nation.AFRICANS, getImageFromResourceLocation(afrBobsLst, LAND_BORDER_ICON));
        borderImageCollector.addWaterBorderImage(Nation.AFRICANS, getImageFromResourceLocation(afrBobsLst, COAST_BORDER_ICON));

        borderImageCollector.addLandBorderImage(Nation.JAPANESE, getImageFromResourceLocation(japBobsLst, LAND_BORDER_ICON));
        borderImageCollector.addWaterBorderImage(Nation.JAPANESE, getImageFromResourceLocation(japBobsLst, COAST_BORDER_ICON));

        borderImageCollector.addLandBorderImage(Nation.ROMANS, getImageFromResourceLocation(romBobsLst, LAND_BORDER_ICON));
        borderImageCollector.addWaterBorderImage(Nation.ROMANS, getImageFromResourceLocation(romBobsLst, COAST_BORDER_ICON));

        borderImageCollector.addLandBorderImage(Nation.VIKINGS, getImageFromResourceLocation(vikBobsLst, LAND_BORDER_ICON));
        borderImageCollector.addWaterBorderImage(Nation.VIKINGS, getImageFromResourceLocation(vikBobsLst, COAST_BORDER_ICON));

        borderImageCollector.writeImageAtlas(toDir, defaultPalette);
    }

    private void populateFlags(String fromDir, String toDir) throws InvalidFormatException, UnknownResourceTypeException, InvalidHeaderException, IOException {

        List<GameResource> afrZLst = assetManager.loadLstFile(fromDir + "/" + AfrZLst.FILENAME, defaultPalette);
        List<GameResource> japZLst = assetManager.loadLstFile(fromDir + "/" + JapZLst.FILENAME, defaultPalette);
        List<GameResource> romZLst = assetManager.loadLstFile(fromDir + "/" + RomZLst.FILENAME, defaultPalette);
        List<GameResource> vikZLst = assetManager.loadLstFile(fromDir + "/" + VikZLst.FILENAME, defaultPalette);

        FlagImageCollection flagImageCollection = new FlagImageCollection();

        // Africans
        flagImageCollection.addImagesForFlag(Nation.AFRICANS, FlagType.NORMAL, getImagesFromGameResource(afrZLst, AfrZLst.NORMAL_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.AFRICANS, FlagType.NORMAL, getImagesFromGameResource(afrZLst, AfrZLst.NORMAL_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.AFRICANS, FlagType.MAIN, getImagesFromGameResource(afrZLst, AfrZLst.MAIN_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.AFRICANS, FlagType.MAIN, getImagesFromGameResource(afrZLst, AfrZLst.MAIN_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.AFRICANS, FlagType.MARINE, getImagesFromGameResource(afrZLst, AfrZLst.MARINE_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.AFRICANS, FlagType.MARINE, getImagesFromGameResource(afrZLst, AfrZLst.MARINE_FLAG_SHADOW_ANIMATION, 8));

        // Japanese
        flagImageCollection.addImagesForFlag(Nation.JAPANESE, FlagType.NORMAL, getImagesFromGameResource(japZLst, JapZLst.NORMAL_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.JAPANESE, FlagType.NORMAL, getImagesFromGameResource(japZLst, JapZLst.NORMAL_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.JAPANESE, FlagType.MAIN, getImagesFromGameResource(japZLst, JapZLst.MAIN_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.JAPANESE, FlagType.MAIN, getImagesFromGameResource(japZLst, JapZLst.MAIN_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.JAPANESE, FlagType.MARINE, getImagesFromGameResource(japZLst, JapZLst.MARINE_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.JAPANESE, FlagType.MARINE, getImagesFromGameResource(japZLst, JapZLst.MARINE_FLAG_SHADOW_ANIMATION, 8));

        // Romans
        flagImageCollection.addImagesForFlag(Nation.ROMANS, FlagType.NORMAL, getImagesFromGameResource(romZLst, RomZLst.NORMAL_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.ROMANS, FlagType.NORMAL, getImagesFromGameResource(romZLst, RomZLst.NORMAL_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.ROMANS, FlagType.MAIN, getImagesFromGameResource(romZLst, RomZLst.MAIN_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.ROMANS, FlagType.MAIN, getImagesFromGameResource(romZLst, RomZLst.MAIN_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.ROMANS, FlagType.MARINE, getImagesFromGameResource(romZLst, RomZLst.MARINE_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.ROMANS, FlagType.MARINE, getImagesFromGameResource(romZLst, RomZLst.MARINE_FLAG_SHADOW_ANIMATION, 8));

        // Vikings
        flagImageCollection.addImagesForFlag(Nation.VIKINGS, FlagType.NORMAL, getImagesFromGameResource(vikZLst, VikZLst.NORMAL_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.VIKINGS, FlagType.NORMAL, getImagesFromGameResource(vikZLst, VikZLst.NORMAL_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.VIKINGS, FlagType.MAIN, getImagesFromGameResource(vikZLst, VikZLst.MAIN_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.VIKINGS, FlagType.MAIN, getImagesFromGameResource(vikZLst, VikZLst.MAIN_FLAG_SHADOW_ANIMATION, 8));

        flagImageCollection.addImagesForFlag(Nation.VIKINGS, FlagType.MARINE, getImagesFromGameResource(vikZLst, VikZLst.MARINE_FLAG_ANIMATION, 8));
        flagImageCollection.addImagesForFlagShadow(Nation.VIKINGS, FlagType.MARINE, getImagesFromGameResource(vikZLst, VikZLst.MARINE_FLAG_SHADOW_ANIMATION, 8));

        // Write the image atlas to file
        flagImageCollection.writeImageAtlas(toDir + "/", defaultPalette);
    }

    private void populateWorkers(String fromDir, String toDir) throws InvalidFormatException, UnknownResourceTypeException, InvalidHeaderException, IOException {

        /* Load worker image parts */
        List<GameResource> jobsBob = assetManager.loadLstFile(fromDir + "/" + JobsBob.FILENAME, defaultPalette);
        List<GameResource> map0ZLst = assetManager.loadLstFile(fromDir + "/" + Map0ZLst.FILENAME, defaultPalette);

        if (jobsBob.size() != 1) {
            throw new RuntimeException("Wrong size of game resources in bob file. Must be 1, but was: " + jobsBob.size());
        }

        if (! (jobsBob.get(0) instanceof BobGameResource)) {
            throw new RuntimeException("Element must be Bob game resource. Was: " + jobsBob.get(0).getClass().getName());
        }

        BobGameResource bobGameResource = (BobGameResource) jobsBob.get(0);

        /* Construct the worker details map */
        Map<JobType, WorkerDetails> workerDetailsMap = new HashMap<>();

        // FIXME: assume RANGER == FORESTER

        /*
        * Translate ids:
        *  - 0 (Africans) -> 3
        *  - 1 (Japanese) -> 2
        *  - 2 (Romans)   -> 0
        *  - 3 (Vikings)  -> 1
        * */

        workerDetailsMap.put(JobType.HELPER, new WorkerDetails(false, JobsBob.HELPER_BOB_ID));
        workerDetailsMap.put(JobType.WOODCUTTER, new WorkerDetails(false, JobsBob.WOODCUTTER_BOB_ID));
        workerDetailsMap.put(JobType.FISHER, new WorkerDetails(false, JobsBob.FISHERMAN_BOB_ID));
        workerDetailsMap.put(JobType.FORESTER, new WorkerDetails(false, JobsBob.FORESTER_BOB_ID));
        workerDetailsMap.put(JobType.CARPENTER, new WorkerDetails(false, JobsBob.CARPENTER_BOB_ID));
        workerDetailsMap.put(JobType.STONEMASON, new WorkerDetails(false, JobsBob.STONEMASON_BOB_ID));
        workerDetailsMap.put(JobType.HUNTER, new WorkerDetails(false, JobsBob.HUNTER_BOB_ID));
        workerDetailsMap.put(JobType.FARMER, new WorkerDetails(false, JobsBob.FARMER_BOB_ID));
        workerDetailsMap.put(JobType.MILLER, new WorkerDetails(true, JobsBob.MILLER_BOB_ID));
        workerDetailsMap.put(JobType.BAKER, new WorkerDetails(true, JobsBob.BAKER_BOB_ID));
        workerDetailsMap.put(JobType.BUTCHER, new WorkerDetails(false, JobsBob.BUTCHER_BOB_ID));
        workerDetailsMap.put(JobType.MINER, new WorkerDetails(false, JobsBob.MINER_BOB_ID));
        workerDetailsMap.put(JobType.BREWER, new WorkerDetails(true, JobsBob.BREWER_BOB_ID));
        workerDetailsMap.put(JobType.PIG_BREEDER, new WorkerDetails(false, JobsBob.PIG_BREEDER_BOB_ID));
        workerDetailsMap.put(JobType.DONKEY_BREEDER, new WorkerDetails(false, JobsBob.DONKEY_BREEDER_BOB_ID));
        workerDetailsMap.put(JobType.IRON_FOUNDER, new WorkerDetails(false, JobsBob.IRON_FOUNDER_BOB_ID));
        workerDetailsMap.put(JobType.MINTER, new WorkerDetails(false, JobsBob.MINTER_BOB_ID));
        workerDetailsMap.put(JobType.METALWORKER, new WorkerDetails(false, JobsBob.METALWORKER_BOB_ID));
        workerDetailsMap.put(JobType.ARMORER, new WorkerDetails(true, JobsBob.ARMORER_BOB_ID));
        workerDetailsMap.put(JobType.BUILDER, new WorkerDetails(false, JobsBob.BUILDER_BOB_ID));
        workerDetailsMap.put(JobType.PLANER, new WorkerDetails(false, JobsBob.PLANER_BOB_ID));
        workerDetailsMap.put(JobType.PRIVATE, new WorkerDetails(false, JobsBob.PRIVATE_BOB_ID));
        workerDetailsMap.put(JobType.PRIVATE_FIRST_CLASS, new WorkerDetails(false, JobsBob.PRIVATE_FIRST_CLASS_BOB_ID));
        workerDetailsMap.put(JobType.SERGEANT, new WorkerDetails(false, JobsBob.SERGEANT_BOB_ID));
        workerDetailsMap.put(JobType.OFFICER, new WorkerDetails(false, JobsBob.OFFICER_BOB_ID));
        workerDetailsMap.put(JobType.GENERAL, new WorkerDetails(false, JobsBob.GENERAL_BOB_ID));
        workerDetailsMap.put(JobType.GEOLOGIST, new WorkerDetails(false, JobsBob.GEOLOGIST_BOB_ID));
        workerDetailsMap.put(JobType.SHIP_WRIGHT, new WorkerDetails(false, JobsBob.SHIP_WRIGHT_BOB_ID));
        workerDetailsMap.put(JobType.SCOUT, new WorkerDetails(false, JobsBob.SCOUT_BOB_ID));
        workerDetailsMap.put(JobType.PACK_DONKEY, new WorkerDetails(false, JobsBob.PACK_DONKEY_BOB_ID));
        workerDetailsMap.put(JobType.BOAT_CARRIER, new WorkerDetails(false, JobsBob.BOAT_CARRIER_BOB_ID));
        workerDetailsMap.put(JobType.CHAR_BURNER, new WorkerDetails(false, JobsBob.CHAR_BURNER_BOB_ID));

        /* Composite the worker images and animations */
        Map<JobType, RenderedWorker> renderedWorkers = assetManager.renderWorkerImages(bobGameResource.getBob(), workerDetailsMap);
        Map<JobType, WorkerImageCollection> workerImageCollectors = new HashMap<>();

        for (JobType jobType : JobType.values()) {
            RenderedWorker renderedWorker = renderedWorkers.get(jobType);

            WorkerImageCollection workerImageCollection = new WorkerImageCollection(jobType.name().toLowerCase());

            for (Nation nation : Nation.values()) {
                for (Direction direction : Direction.values()) {

                    StackedBitmaps[] stackedBitmaps = renderedWorker.getAnimation(nation, direction);

                    if (stackedBitmaps == null) {
                        System.out.println("Stacked bitmaps is null");
                        System.out.println(jobType);
                        System.out.println(nation);
                        System.out.println(direction);
                    }

                    for (int i = 0; i < stackedBitmaps.length; i++) {
                        StackedBitmaps frame = stackedBitmaps[i];

                        PlayerBitmap body = frame.getBitmaps().get(0);
                        PlayerBitmap head = frame.getBitmaps().get(1);

                        /* Calculate the dimension */
                        Point origin = new Point(0, 0);
                        Dimension size = new Dimension(0, 0);

                        if (!frame.getBitmaps().isEmpty()) {

                            origin.x = Integer.MIN_VALUE;
                            origin.y = Integer.MIN_VALUE;

                            Point maxPosition = origin;

                            boolean hasPlayer = false;

                            for (Bitmap bitmap : frame.getBitmaps()) {

                                if (bitmap instanceof PlayerBitmap) {
                                    hasPlayer = true;
                                }

                                Area bitmapVisibleArea = bitmap.getVisibleArea();
                                Point bitmapOrigin = bitmap.getOrigin();

                                origin.x = Math.max(origin.x, bitmapOrigin.x);
                                origin.y = Math.max(origin.y, bitmapOrigin.y);

                                maxPosition.x = Math.max(maxPosition.x, bitmapVisibleArea.width - bitmapOrigin.x);
                                maxPosition.y = Math.max(maxPosition.y, bitmapVisibleArea.height - bitmapOrigin.y);
                            }

                            /* Create a bitmap to merge both body and head into */
                            Bitmap merged = new Bitmap(origin.x + maxPosition.x, origin.y + maxPosition.y, defaultPalette, TextureFormat.BGRA);

                            /* Draw the body */
                            Area bodyVisibleArea = body.getVisibleArea();

                            Point bodyToUpperleft = new Point(origin.x - body.getOrigin().x, origin.y - body.getOrigin().y);
                            Point bodyFromUpperLeft = bodyVisibleArea.getUpperLeftCoordinate();

                            merged.copyNonTransparentPixels(body, bodyToUpperleft, bodyFromUpperLeft, bodyVisibleArea.getDimension());

                            /* Draw the head */
                            Area headVisibleArea = head.getVisibleArea();

                            Point headToUpperLeft = new Point(origin.x - head.getOrigin().x, origin.y - head.getOrigin().y);
                            Point headFromUpperLeft = headVisibleArea.getUpperLeftCoordinate();

                            merged.copyNonTransparentPixels(head, headToUpperLeft, headFromUpperLeft, headVisibleArea.getDimension());

                            /* Store the image in the worker image collection */
                            workerImageCollection.addImage(nation, direction, merged);
                        }
                    }
                }
            }

            workerImageCollection.addShadowImages(EAST, getImagesFromGameResource(map0ZLst, Map0ZLst.WALKING_EAST_SHADOW_ANIMATION, 8));
            workerImageCollection.addShadowImages(SOUTH_EAST, getImagesFromGameResource(map0ZLst, Map0ZLst.WALKING_SOUTH_EAST_SHADOW_ANIMATION, 8));
            workerImageCollection.addShadowImages(SOUTH_WEST, getImagesFromGameResource(map0ZLst, Map0ZLst.WALKING_SOUTH_WEST_SHADOW_ANIMATION, 8));
            workerImageCollection.addShadowImages(WEST, getImagesFromGameResource(map0ZLst, Map0ZLst.WALKING_WEST_SHADOW_ANIMATION, 8));
            workerImageCollection.addShadowImages(NORTH_WEST, getImagesFromGameResource(map0ZLst, Map0ZLst.WALKING_NORTH_WEST_SHADOW_ANIMATION, 8));
            workerImageCollection.addShadowImages(NORTH_EAST, getImagesFromGameResource(map0ZLst, Map0ZLst.WALKING_NORTH_EAST_SHADOW_ANIMATION, 8));

            // Store the worker image collector
            workerImageCollectors.put(jobType, workerImageCollection);
        }

        // Add cargo carrying images
        WorkerImageCollection woodcutterImageCollector = workerImageCollectors.get(JobType.WOODCUTTER);
        WorkerImageCollection carpenterImageCollector = workerImageCollectors.get(JobType.CARPENTER);
        WorkerImageCollection fishermanImageCollector = workerImageCollectors.get(JobType.FISHER);
        WorkerImageCollection stonemasonImageCollector = workerImageCollectors.get(JobType.STONEMASON);
        WorkerImageCollection minterImageCollector = workerImageCollectors.get(JobType.MINTER);
        WorkerImageCollection farmerImageCollector = workerImageCollectors.get(JobType.FARMER);
        WorkerImageCollection pigBreederImageCollector = workerImageCollectors.get(JobType.PIG_BREEDER);
        WorkerImageCollection millerImageCollector = workerImageCollectors.get(JobType.MILLER);
        WorkerImageCollection bakerImageCollector = workerImageCollectors.get(JobType.BAKER);

        List<PlayerBitmap> bobBitmaps = bobGameResource.getBob().getAllBitmaps();

        woodcutterImageCollector.addCargoImage(EAST, bobBitmaps.get(JobsBob.WOODCUTTER_CARGO_EAST));
        woodcutterImageCollector.addCargoImage(SOUTH_EAST, bobBitmaps.get(JobsBob.WOODCUTTER_CARGO_SOUTH_EAST));
        woodcutterImageCollector.addCargoImage(SOUTH_WEST, bobBitmaps.get(JobsBob.WOODCUTTER_CARGO_SOUTH_WEST));
        woodcutterImageCollector.addCargoImage(WEST, bobBitmaps.get(JobsBob.WOODCUTTER_CARGO_WEST));
        woodcutterImageCollector.addCargoImage(NORTH_WEST, bobBitmaps.get(JobsBob.WOODCUTTER_CARGO_NORTH_WEST));
        woodcutterImageCollector.addCargoImage(NORTH_EAST, bobBitmaps.get(JobsBob.WOODCUTTER_CARGO_NORTH_EAST));

        carpenterImageCollector.addCargoImage(SOUTH_EAST, bobBitmaps.get(JobsBob.CARPENTER_CARGO_SOUTH_EAST));
        carpenterImageCollector.addCargoImage(NORTH_WEST, bobBitmaps.get(JobsBob.CARPENTER_CARGO_NORTH_WEST));

        stonemasonImageCollector.addCargoImage(EAST, bobBitmaps.get(JobsBob.STONEMASON_CARGO_EAST));
        stonemasonImageCollector.addCargoImage(SOUTH_EAST, bobBitmaps.get(JobsBob.STONEMASON_CARGO_SOUTH_EAST));
        stonemasonImageCollector.addCargoImage(SOUTH_WEST, bobBitmaps.get(JobsBob.STONEMASON_CARGO_SOUTH_WEST));
        stonemasonImageCollector.addCargoImage(WEST, bobBitmaps.get(JobsBob.STONEMASON_CARGO_WEST));
        stonemasonImageCollector.addCargoImage(NORTH_WEST, bobBitmaps.get(JobsBob.STONEMASON_CARGO_NORTH_WEST));
        stonemasonImageCollector.addCargoImage(NORTH_EAST, bobBitmaps.get(JobsBob.STONEMASON_CARGO_NORTH_EAST));

        minterImageCollector.addCargoImage(SOUTH_EAST, bobBitmaps.get(JobsBob.MINTER_CARGO_SOUTH_EAST));
        minterImageCollector.addCargoImage(NORTH_WEST, bobBitmaps.get(JobsBob.MINTER_CARGO_NORTH_WEST));

        // TODO: add miner with each type of ore

        fishermanImageCollector.addCargoImage(EAST, bobBitmaps.get(JobsBob.FISHERMAN_CARGO_EAST));
        fishermanImageCollector.addCargoImage(SOUTH_EAST, bobBitmaps.get(JobsBob.FISHERMAN_CARGO_SOUTH_EAST));
        fishermanImageCollector.addCargoImage(SOUTH_WEST, bobBitmaps.get(JobsBob.FISHERMAN_CARGO_SOUTH_WEST));
        fishermanImageCollector.addCargoImage(WEST, bobBitmaps.get(JobsBob.FISHERMAN_CARGO_WEST));
        fishermanImageCollector.addCargoImage(NORTH_WEST, bobBitmaps.get(JobsBob.FISHERMAN_CARGO_NORTH_WEST));
        fishermanImageCollector.addCargoImage(NORTH_EAST, bobBitmaps.get(JobsBob.FISHERMAN_CARGO_NORTH_EAST));

        farmerImageCollector.addCargoImage(EAST, bobBitmaps.get(JobsBob.FARMER_CARGO_EAST));
        farmerImageCollector.addCargoImage(SOUTH_EAST, bobBitmaps.get(JobsBob.FARMER_CARGO_SOUTH_EAST));
        farmerImageCollector.addCargoImage(SOUTH_WEST, bobBitmaps.get(JobsBob.FARMER_CARGO_SOUTH_WEST));
        farmerImageCollector.addCargoImage(WEST, bobBitmaps.get(JobsBob.FARMER_CARGO_WEST));
        farmerImageCollector.addCargoImage(NORTH_WEST, bobBitmaps.get(JobsBob.FARMER_CARGO_NORTH_WEST));
        farmerImageCollector.addCargoImage(NORTH_EAST, bobBitmaps.get(JobsBob.FARMER_CARGO_NORTH_EAST));

        // TODO: it's suspicious that the amount of images are different per direction and that they are in a strange order in the file. Double check!
        pigBreederImageCollector.addCargoImages(SOUTH_EAST,
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_SOUTH_EAST_ANIMATION_0),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_SOUTH_EAST_ANIMATION_1),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_SOUTH_EAST_ANIMATION_2),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_SOUTH_EAST_ANIMATION_3),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_SOUTH_EAST_ANIMATION_4)
                );
        pigBreederImageCollector.addCargoImages(NORTH_WEST,
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_0),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_1),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_2),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_3),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_4),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_5),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_6),
                bobBitmaps.get(JobsBob.PIG_BREEDER_CARGO_NORTH_WEST_ANIMATION_7)
                );

        // TODO: miller is also suspicious. Different number of images
        millerImageCollector.addCargoImages(SOUTH_EAST,
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_0),
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_1),
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_2),
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_3),
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_4),
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_5),
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_6),
                bobBitmaps.get(JobsBob.MILLER_CARGO_SOUTH_EAST_ANIMATION_7)
        );
        millerImageCollector.addCargoImages(NORTH_WEST,
                bobBitmaps.get(JobsBob.MILLER_CARGO_NORTH_WEST_ANIMATION_0),
                bobBitmaps.get(JobsBob.MILLER_CARGO_NORTH_WEST_ANIMATION_1),
                bobBitmaps.get(JobsBob.MILLER_CARGO_NORTH_WEST_ANIMATION_2),
                bobBitmaps.get(JobsBob.MILLER_CARGO_NORTH_WEST_ANIMATION_3),
                bobBitmaps.get(JobsBob.MILLER_CARGO_NORTH_WEST_ANIMATION_4),
                bobBitmaps.get(JobsBob.MILLER_CARGO_NORTH_WEST_ANIMATION_5),
                bobBitmaps.get(JobsBob.MILLER_CARGO_NORTH_WEST_ANIMATION_6)
        );

        bakerImageCollector.addCargoImages(EAST,
                bobBitmaps.get(JobsBob.BAKER_CARGO_EAST_ANIMATION_0),
                bobBitmaps.get(JobsBob.BAKER_CARGO_EAST_ANIMATION_1),
                bobBitmaps.get(JobsBob.BAKER_CARGO_EAST_ANIMATION_2),
                bobBitmaps.get(JobsBob.BAKER_CARGO_EAST_ANIMATION_3),
                bobBitmaps.get(JobsBob.BAKER_CARGO_EAST_ANIMATION_4)
        );
        bakerImageCollector.addCargoImages(SOUTH_EAST,
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_0),
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_1),
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_2),
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_3),
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_4),
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_5),
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_6),
                bobBitmaps.get(JobsBob.BAKER_CARGO_SOUTH_EAST_ANIMATION_7)
        );
        bakerImageCollector.addCargoImages(WEST,
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_0),
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_1),
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_2),
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_3),
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_4),
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_5),
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_6),
                bobBitmaps.get(JobsBob.BAKER_CARGO_WEST_ANIMATION_7)
        );
        bakerImageCollector.addCargoImages(NORTH_WEST,
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_0),
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_1),
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_2),
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_3),
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_4),
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_5),
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_6),
                bobBitmaps.get(JobsBob.BAKER_CARGO_NORTH_WEST_ANIMATION_7)
        );

        // Write each worker image collection to file
        workerImageCollectors.values().forEach(
                (WorkerImageCollection workerImageCollection) -> {
                    try {
                        workerImageCollection.writeImageAtlas(toDir + "/", defaultPalette);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    /**
     * TEX5.LBM -- contains vegetation textures
     *
     *
     *
     * Layout of data in MAPBOBS.LST
     *
     * 0     Palette
     * 1     Selected point
     * 2     Building road?? Maybe when clicked down on step in road when building?
     * 3     Hover point
     * 4     ?? - some sort of selector
     * 5     Hover over available flag point
     * 6     Hover over available mine point
     * 7     Hover over available small building point
     * 8     Hover over available medium building point
     * 9     Hover over available large building point
     * 10    Hover over available harbor point
     * 11    Available flag
     * 12    Available small building
     * 13    Available medium building
     * 14    Available large building
     * 15    Available mine
     * 16    Available harbor
     * 17    ?? - skull - when to use?
     * 18    ?? - green bar - road building?
     * 19    ?? - green bar - road building?
     * 20    ?? - yellow bar - road building?
     * 21    ?? - red bar - road building?
     * 22    ?? - green bar - road building?
     * 23    ?? - yellow bar - road building?
     * 24    ?? - red bar - road building?
     * 25    ?? - revert arrow - used when?
     * 26-33 Tree animation type 1
     * 34    Tree mini type 1
     * 35    Tree small type 1
     * 36    Tree medium type 1
     * 37-40 Tree falling animation type 1
     * 41-48 Tree animation type 2
     * 49    Tree mini type 2
     * 50    Tree small type 2
     * 51    Tree medium type 2
     * 52-55 Tree falling animation type 2
     * 56-63 Tree animation type 3
     * 64    Tree mini type 3
     * 65    Tree small type 3
     * 66    Tree medium type 3
     * 67-70 Tree falling animation type 3
     * 71-78 Tree animation type 4
     * 79    Tree mini type 4
     * 80    Tree small type 4
     * 81      Tree medium type 4
     * 82-85   Tree falling animation type 4
     * 86-93   Tree animation type 5
     * 94      Tree mini type 5
     * 95      Tree small type 5
     * 96      Tree medium type 5
     * 97-100  Tree falling animation type 5
     * 101-108 Tree animation type 6 (cannot be cut down?)
     * 109-116 Tree animation type 7
     * 117     Tree mini type 7
     * 118     Tree small type 7
     * 119     Tree medium type 7
     * 120-123 Tree falling animation type 7
     * 124-131 Tree animation type 8
     * 132     Tree mini type 8
     * 133     Tree small type 8
     * 134     Tree medium type 8
     * 135-138 Tree falling type 8
     * 139-146 Tree animation type 9
     * 147     Tree mini type 9
     * 148     Tree small type 9
     * 149     Tree medium type 9
     * 150-153 Tree falling animation type 9
     * 154-161 Tree shadow type ? (lying or standing?)
     * 162     Tree mini shadow type ?
     * 163     Tree small shadow type ?
     * 164     Tree medium shadow type ?
     * 165-168 Tree falling animation shadow type ?
     *
     * 169-281 More tree shadows -- figure out which ones belong to which tree
     *
     * 282     ??
     * 283     Mushroom
     * 284     Mini decorative stone
     * 285     Mini decorative stones
     * 286     Small decorative stone
     * 287     Fallen dead decorative tree
     * 288     Standing dead decorative tree
     * 289     Skeleton decorative
     * 290     Mini decorative skeleton
     * 291     Flowers decorative
     * 292     Bush decorative
     * 293     Larger set of stones (can be extracted?)
     * 294     Cactus decorative
     * 295     Cactus decorative
     * 296     Beach grass decorative
     * 297     Small grass decorative
     * 298     Minish stones left over (decorative?)
     * 299     Minish stones left over (decorative?)
     * 300     Mini stones left over type 1
     * 301     Smallish stones left over type 1
     * 302     Medium stones left over type 1
     * 303     large stones left over type 1
     * 304     Mini stones left over type 2
     * 305     More stones left over type 2
     * 306     More stones left over type 2
     * 307     More stones left over type 2
     * 308     More stones left over type 2
     * 309     Maximum stones left over type 2
     * 310     Fallen tree left over
     * 311-314 Crops - newly planted to fully grown - type 1
     * 315     Crops - just harvested
     * 316-319 Crops newly planted to fully grown - type 2
     * 320     Crops - just harvested
     * 321     Bush - decorative
     * 322     Bush - decorative
     * 323     Bush - decorative
     * 324     Grass - decorative
     * 325     Grass - decorative
     * 326     Small skeleton
     * 327     Smaller skeleton
     *
     * 328-332 ???
     * 333-371 Unknown shadows???
     *
     * 372-374     Iron sign, small-medium-large, up-right
     * 375-377     Gold sign, small-medium-large, up-right
     * 378-380     Coal sign, small-medium-large, up-right
     * 381-383     Granite sign, small-medium-large, up-right
     * 384         Water sign, large(?), up-right
     * 385         Nothing found sign, up-right
     *
     * 386-418     Unknown shadows???
     *
     * 419-426     Small fire animation
     *
     * 475-482     Ice bear walking animation, right
     * 483-490     Ice bear walking animation, down-right
     * 491-498     Ice bear walking animation, down-left
     * 499-506     Ice bear walking animation, left
     * 507-514     Ice bear walking animation, up-left
     * 515-522     Ice bear walking animation, up-right
     *
     * 523         Dead animal???
     *
     * 524         ??
     *
     * 525-536     Rabbit running animation, right
     * 537-548     Rabbit running animation, left
     * 549-554     Rabbit running animation, up-left
     * 555-561     Rabbit running animation, up-right
     *
     * 562-600     More rabbits??
     *
     * 601-606     Fox running animation, right
     * 607-612     Fox running animation, down-right
     * 613-618     Fox running animation, down-left
     * 619-624     Fox running animation, left
     * 625-630     Fox running animation, up-left
     * 631-636     Fox running animation, up-right
     * 637         Fox killed
     * 638-643     Fox shadows
     *
     * 644-651     Deer running animation, right
     * 652-659     Deer running animation, down-right
     * 660-667     Deer running animation, down-left
     * 668-675     Deer running animation, left
     * 676-683     Deer running animation, up-left
     * 684-691     Deer running animation, up-right
     * 692         Deer killed
     * 693-698     Deer shadows
     *
     * 699-706     Rain deer running animation, right
     * 707-714     Rain deer running animation, down-right
     * 715-722     Rain deer running animation, down-left
     * 723-730     Rain deer running animation, left
     * 731-738     Rain deer running animation, up-left
     * 739-746     Rain deer running animation, up-right
     * 747         Rain deer killed
     * 748-753     Rain deer shadows
     *
     * 754-        Duck, east, south-east, south-west, west, north-west, north-east,
     *
     * 761-808     Horse animated ...
     *
     * 812-817     Donkey bags ...
     *
     * 818-829     Sheep animated ...
     *
     * 832-843     Pig animated ...
     *
     * 844-891     Smaller pig (?) animated
     *
     * ...
     *
     * 895-927     Tools and materials - small for workers to cary around
     *
     * 928-964     Tools and materials - for drawing in house information views - needs/has/produces
     *
     * 965-994     Tools and materials for headquarter/storehouse inventory view
     *
     * 995-1030    Very small tools and materials. For what??
     *
     * 1031-1038   Small building fire
     * 1039-1046   Medium building fire
     * 1047-1054   Large building fire
     * 1055        Small building fire done
     * 1056        Medium building fire done
     * 1056        Large building fire done
     * 1057-1081   Fire shadows
     * 1082-1087   Catapult shot landing
     *
     * 
     *
     * @param fromDir
     * @param toDir
     */
    private void populateNatureAndUIElements(String fromDir, String toDir) throws InvalidFormatException, UnknownResourceTypeException, InvalidHeaderException, IOException {

        /* Load from the map asset file */
        List<GameResource> mapbobsLst = assetManager.loadLstFile(fromDir + "/" + MAP_FILE, defaultPalette);

        /* Create the out directories */
        String uiDir = toDir + "/" + UI_ELEMENTS_DIRECTORY;
        String natureDir = toDir + "/" + NATURE_DIRECTORY;
        String signDir = toDir + "/" + SIGNS_DIRECTORY;
        String terrainDir = natureDir + "/" + TERRAIN_SUB_DIRECTORY;
        String greenlandDir = terrainDir + "/" + GREENLAND_DIRECTORY;
        String winterDir = terrainDir + "/" + WINTER_DIRECTORY;

        Utils.createDirectory(uiDir);
        Utils.createDirectory(natureDir);
        Utils.createDirectory(natureDir + "/animals");
        Utils.createDirectory(signDir);
        Utils.createDirectory(terrainDir);
        Utils.createDirectory(greenlandDir);
        Utils.createDirectory(winterDir);

        /* Extract the terrains */
        LBMGameResource greenlandGameResource = (LBMGameResource) assetManager.loadLBMFile(fromDir + "/" + GREENLAND_TEXTURE_FILE, defaultPalette);
        LBMGameResource winterGameResource = (LBMGameResource) assetManager.loadLBMFile(fromDir + "/" + WINTER_TEXTURE_FILE, defaultPalette);

        Bitmap greenlandTextureBitmap = greenlandGameResource.getLbmFile().getBitmap();
        Bitmap winterTextureBitmap = winterGameResource.getLbmFile().getBitmap();

        greenlandTextureBitmap.writeToFile(greenlandDir + "/greenland-texture.png");
        winterTextureBitmap.writeToFile(winterDir + "/winter-texture.png");

        /* Create the greenland world */
        extractGreenlandTerrain(greenlandDir, greenlandTextureBitmap);

        /* Create the winter world */
        extractWinterTerrain(winterDir, winterTextureBitmap);

        /* Extract the stones */
        StonesImageCollection stonesImageCollection = new StonesImageCollection();

        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.MINI, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_MINI));
        stonesImageCollection.addShadowImage(StoneType.TYPE_1, StoneAmount.MINI, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_MINI_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.LITTLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_LITTLE));
        stonesImageCollection.addShadowImage(StoneType.TYPE_1, StoneAmount.LITTLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_LITTLE_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.LITTLE_MORE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_LITTLE_MORE));
        stonesImageCollection.addShadowImage(StoneType.TYPE_1, StoneAmount.LITTLE_MORE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_LITTLE_MORE_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.MIDDLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_MIDDLE));
        stonesImageCollection.addShadowImage(StoneType.TYPE_1, StoneAmount.MIDDLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_MIDDLE_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.ALMOST_FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_ALMOST_FULL));
        stonesImageCollection.addShadowImage(StoneType.TYPE_1, StoneAmount.ALMOST_FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_ALMOST_FULL_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_FULL));
        stonesImageCollection.addShadowImage(StoneType.TYPE_1, StoneAmount.FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_1_FULL_SHADOW));

        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.MINI, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_MINI));
        stonesImageCollection.addShadowImage(StoneType.TYPE_2, StoneAmount.MINI, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_MINI_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.LITTLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_LITTLE));
        stonesImageCollection.addShadowImage(StoneType.TYPE_2, StoneAmount.LITTLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_LITTLE_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.LITTLE_MORE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_LITTLE_MORE));
        stonesImageCollection.addShadowImage(StoneType.TYPE_2, StoneAmount.LITTLE_MORE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_LITTLE_MORE_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.MIDDLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_MIDDLE));
        stonesImageCollection.addShadowImage(StoneType.TYPE_2, StoneAmount.MIDDLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_MIDDLE_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.ALMOST_FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_ALMOST_FULL));
        stonesImageCollection.addShadowImage(StoneType.TYPE_2, StoneAmount.ALMOST_FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_ALMOST_FULL_SHADOW));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_FULL));
        stonesImageCollection.addShadowImage(StoneType.TYPE_2, StoneAmount.FULL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_TYPE_2_FULL_SHADOW));

        stonesImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract UI elements */
        UIElementsImageCollection uiElementsImageCollection = new UIElementsImageCollection();

        uiElementsImageCollection.addSelectedPointImage(getImageFromResourceLocation(mapbobsLst, MapBobsLst.SELECTED_POINT));
        uiElementsImageCollection.addHoverPoint(getImageFromResourceLocation(mapbobsLst, MapBobsLst.HOVER_POINT));
        uiElementsImageCollection.addHoverAvailableFlag(getImageFromResourceLocation(mapbobsLst, MapBobsLst.HOVER_AVAILABLE_FLAG));
        uiElementsImageCollection.addHoverAvailableMine(getImageFromResourceLocation(mapbobsLst, MapBobsLst.HOVER_AVAILABLE_MINE));
        uiElementsImageCollection.addHoverAvailableBuilding(SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.HOVER_AVAILABLE_SMALL_BUILDING));
        uiElementsImageCollection.addHoverAvailableBuilding(MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.HOVER_AVAILABLE_MEDIUM_BUILDING));
        uiElementsImageCollection.addHoverAvailableBuilding(LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.HOVER_AVAILABLE_LARGE_BUILDING));
        uiElementsImageCollection.addHoverAvailableHarbor(getImageFromResourceLocation(mapbobsLst, MapBobsLst.HOVER_AVAILABLE_HARBOR));
        uiElementsImageCollection.addAvailableFlag(getImageFromResourceLocation(mapbobsLst, MapBobsLst.AVAILABLE_FLAG));
        uiElementsImageCollection.addAvailableMine(getImageFromResourceLocation(mapbobsLst, MapBobsLst.AVAILABLE_MINE));
        uiElementsImageCollection.addAvailableBuilding(SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.AVAILABLE_SMALL_BUILDING));
        uiElementsImageCollection.addAvailableBuilding(MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.AVAILABLE_MEDIUM_BUILDING));
        uiElementsImageCollection.addAvailableBuilding(LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.AVAILABLE_LARGE_BUILDING));
        uiElementsImageCollection.addAvailableHarbor(getImageFromResourceLocation(mapbobsLst, MapBobsLst.AVAILABLE_HARBOR));

        uiElementsImageCollection.writeImageAtlas(toDir, defaultPalette);

        /*  Extract the crops */
        CropImageCollection cropImageCollection = new CropImageCollection();

        cropImageCollection.addImage(CropType.TYPE_1, Crop.GrowthState.JUST_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_NEWLY_PLANTED));
        cropImageCollection.addImage(CropType.TYPE_1, Crop.GrowthState.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_LITTLE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_1, Crop.GrowthState.ALMOST_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_MORE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_1, Crop.GrowthState.FULL_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_FULLY_GROWN));
        cropImageCollection.addImage(CropType.TYPE_1, Crop.GrowthState.HARVESTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_JUST_HARVESTED));

        cropImageCollection.addImage(CropType.TYPE_2, Crop.GrowthState.JUST_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_NEWLY_PLANTED));
        cropImageCollection.addImage(CropType.TYPE_2, Crop.GrowthState.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_LITTLE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_2, Crop.GrowthState.ALMOST_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_MORE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_2, Crop.GrowthState.FULL_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_FULLY_GROWN));
        cropImageCollection.addImage(CropType.TYPE_2, Crop.GrowthState.HARVESTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_JUST_HARVESTED));

        cropImageCollection.addShadowImage(CropType.TYPE_1, Crop.GrowthState.JUST_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_NEWLY_PLANTED_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_1, Crop.GrowthState.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_LITTLE_GROWTH_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_1, Crop.GrowthState.ALMOST_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_MORE_GROWTH_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_1, Crop.GrowthState.FULL_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_FULLY_GROWN_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_1, Crop.GrowthState.HARVESTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_1_JUST_HARVESTED_SHADOW));

        cropImageCollection.addShadowImage(CropType.TYPE_2, Crop.GrowthState.JUST_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_NEWLY_PLANTED_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_2, Crop.GrowthState.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_LITTLE_GROWTH_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_2, Crop.GrowthState.ALMOST_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_MORE_GROWTH_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_2, Crop.GrowthState.FULL_GROWN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_FULLY_GROWN_SHADOW));
        cropImageCollection.addShadowImage(CropType.TYPE_2, Crop.GrowthState.HARVESTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CROP_TYPE_2_JUST_HARVESTED_SHADOW));

        cropImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract the cargo images that workers carry */
        CargoImageCollection cargoImageCollection = new CargoImageCollection();

        cargoImageCollection.addCargoImage(Material.BEER, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BEER_CARGO));
        cargoImageCollection.addCargoImage(Material.TONGS, getImageFromResourceLocation(mapbobsLst, MapBobsLst.TONG_CARGO));
        cargoImageCollection.addCargoImage(Material.AXE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.AXE_CARGO));
        cargoImageCollection.addCargoImage(Material.SAW, getImageFromResourceLocation(mapbobsLst, MapBobsLst.SAW_CARGO));
        cargoImageCollection.addCargoImage(Material.PICK_AXE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PICK_AXE_CARGO));
        cargoImageCollection.addCargoImage(Material.SHOVEL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.SHOVEL_CARGO));
        cargoImageCollection.addCargoImage(Material.CRUCIBLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CRUCIBLE_CARGO)); //???
        cargoImageCollection.addCargoImage(Material.FISHING_ROD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FISHING_ROD_CARGO));
        cargoImageCollection.addCargoImage(Material.SCYTHE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.SCYTHE_CARGO));
        // - empty bucket at 904

        cargoImageCollection.addCargoImage(Material.WATER, getImageFromResourceLocation(mapbobsLst, MapBobsLst.WATER_BUCKET_CARGO));
        cargoImageCollection.addCargoImage(Material.CLEAVER, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CLEAVER_CARGO));
        cargoImageCollection.addCargoImage(Material.ROLLING_PIN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROLLING_PIN_CARGO));
        cargoImageCollection.addCargoImage(Material.BOW, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BOW_CARGO));
        cargoImageCollection.addCargoImage(Material.BOAT, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BOAT_CARGO));
        cargoImageCollection.addCargoImage(Material.SWORD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.SWORD_CARGO));
        // - anvil at 911

        cargoImageCollection.addCargoImage(Material.FLOUR, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FLOUR_CARGO));
        cargoImageCollection.addCargoImage(Material.FISH, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FISH_CARGO));
        cargoImageCollection.addCargoImage(Material.BREAD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BREAD_CARGO));
        cargoImageCollection.addCargoImageForNation(Nation.ROMANS, Material.SHIELD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROMAN_SHIELD_CARGO));
        cargoImageCollection.addCargoImage(Material.WOOD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.WOOD_CARGO));
        cargoImageCollection.addCargoImage(Material.PLANK, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PLANK_CARGO));
        cargoImageCollection.addCargoImage(Material.STONE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.STONE_CARGO));
        cargoImageCollection.addCargoImageForNation(Nation.VIKINGS, Material.SHIELD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.VIKING_SHIELD_CARGO));
        cargoImageCollection.addCargoImageForNation(Nation.AFRICANS, Material.SHIELD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.AFRICAN_SHIELD_CARGO));
        cargoImageCollection.addCargoImage(Material.WHEAT, getImageFromResourceLocation(mapbobsLst, MapBobsLst.WHEAT_CARGO));
        cargoImageCollection.addCargoImage(Material.COIN, getImageFromResourceLocation(mapbobsLst, MapBobsLst.COIN_CARGO));
        cargoImageCollection.addCargoImage(Material.GOLD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.GOLD_CARGO));
        cargoImageCollection.addCargoImage(Material.IRON, getImageFromResourceLocation(mapbobsLst, MapBobsLst.IRON_CARGO));
        cargoImageCollection.addCargoImage(Material.COAL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.COAL_CARGO));
        cargoImageCollection.addCargoImage(Material.MEAT, getImageFromResourceLocation(mapbobsLst, MapBobsLst.MEAT_CARGO));
        cargoImageCollection.addCargoImage(Material.PIG, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PIG_CARGO));
        cargoImageCollection.addCargoImageForNation(Nation.JAPANESE, Material.SHIELD, getImageFromResourceLocation(mapbobsLst, MapBobsLst.JAPANESE_SHIELD_CARGO));

        cargoImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract signs */
        SignImageCollection signImageCollection = new SignImageCollection();

        signImageCollection.addImage(SignType.IRON, SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.IRON_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.IRON, MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.IRON_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.IRON, LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.IRON_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.COAL, SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.COAL_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.COAL, MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.COAL_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.COAL, LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.COAL_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.STONE, SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.GRANITE_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.STONE, MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.GRANITE_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.STONE, LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.GRANITE_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.GOLD, SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.GOLD_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.GOLD, MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.GOLD_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.GOLD, LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.GOLD_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.WATER, LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.WATER_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.NOTHING, LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.NOTHING_SIGN_UP_RIGHT));

        signImageCollection.addShadowImage(getImageFromResourceLocation(mapbobsLst, MapBobsLst.SIGN_SHADOW));

        signImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract road building icons */
        RoadBuildingImageCollection roadBuildingImageCollection = new RoadBuildingImageCollection();

        roadBuildingImageCollection.addStartPointImage(getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_START_POINT));
        roadBuildingImageCollection.addSameLevelConnectionImage(getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_SAME_LEVEL_CONNECTION));

        roadBuildingImageCollection.addUpwardsConnectionImage(RoadConnectionDifference.LITTLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_LITTLE_HIGHER_CONNECTION));
        roadBuildingImageCollection.addUpwardsConnectionImage(RoadConnectionDifference.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_MEDIUM_HIGHER_CONNECTION));
        roadBuildingImageCollection.addUpwardsConnectionImage(RoadConnectionDifference.HIGH, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_MUCH_HIGHER_CONNECTION));

        roadBuildingImageCollection.addDownwardsConnectionImage(RoadConnectionDifference.LITTLE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_LITTLE_LOWER_CONNECTION));
        roadBuildingImageCollection.addDownwardsConnectionImage(RoadConnectionDifference.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_MEDIUM_LOWER_CONNECTION));
        roadBuildingImageCollection.addDownwardsConnectionImage(RoadConnectionDifference.HIGH, getImageFromResourceLocation(mapbobsLst, MapBobsLst.ROAD_BUILDING_MUCH_LOWER_CONNECTION));

        roadBuildingImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract nature elements */
        Map<Integer, String> imagesToFileMap = new HashMap<>();

        imagesToFileMap.put(FALLEN_DEAD_TREE, natureDir + "/fallen-dead-tree.png");
        imagesToFileMap.put(DEAD_TREE, natureDir + "/dead-tree.png");

        /* Extract fire animation */
        FireImageCollection fireImageCollection = new FireImageCollection();

        fireImageCollection.addImagesForFire(FireSize.MINI, getImagesFromGameResource(mapbobsLst, MapBobsLst.MINI_FIRE_ANIMATION, 8));
        fireImageCollection.addImagesForFireWithShadow(
                FireSize.SMALL,
                getImagesFromGameResource(mapbobsLst, MapBobsLst.SMALL_FIRE_ANIMATION, 8),
                getImagesFromGameResource(mapbobsLst, MapBobsLst.SMALL_FIRE_SHADOW_ANIMATION, 8)
        );
        fireImageCollection.addImagesForFireWithShadow(
                FireSize.MEDIUM,
                getImagesFromGameResource(mapbobsLst, MapBobsLst.MEDIUM_FIRE_ANIMATION, 8),
                getImagesFromGameResource(mapbobsLst, MapBobsLst.MEDIUM_FIRE_SHADOW_ANIMATION, 8)
        );
        fireImageCollection.addImagesForFireWithShadow(
                FireSize.LARGE,
                getImagesFromGameResource(mapbobsLst, MapBobsLst.LARGE_FIRE_ANIMATION, 8),
                getImagesFromGameResource(mapbobsLst, MapBobsLst.LARGE_FIRE_SHADOW_ANIMATION, 8)
        );

        fireImageCollection.addBurntDownImage(SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.SMALL_BURNT_DOWN));
        fireImageCollection.addBurntDownImage(MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.MEDIUM_BURNT_DOWN));
        fireImageCollection.addBurntDownImage(LARGE, getImageFromResourceLocation(mapbobsLst, MapBobsLst.LARGE_BURNT_DOWN));

        fireImageCollection.writeImageAtlas(toDir, defaultPalette);

        // Collect tree images
        TreeImageCollection treeImageCollection = new TreeImageCollection("trees");

        /* Extract animation for tree type 1 in wind -- cypress (?) */
        treeImageCollection.addImagesForTree(Tree.TreeType.CYPRESS, getImagesFromGameResource(mapbobsLst, MapBobsLst.CYPRESS_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.CYPRESS, getImagesFromGameResource(mapbobsLst, MapBobsLst.CYPRESS_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.CYPRESS, getImagesFromGameResource(mapbobsLst, MapBobsLst.CYPRESS_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.CYPRESS, getImagesFromGameResource(mapbobsLst, MapBobsLst.CYPRESS_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.CYPRESS, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CYPRESS_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.CYPRESS, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CYPRESS_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.CYPRESS, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CYPRESS_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.CYPRESS, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CYPRESS_SHADOW_SMALLEST));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.CYPRESS, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CYPRESS_SHADOW_SMALL));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.CYPRESS, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CYPRESS_SHADOW_MEDIUM));

        /* Extract animation for tree type 2 in wind -- birch, for sure */
        treeImageCollection.addImagesForTree(Tree.TreeType.BIRCH, getImagesFromGameResource(mapbobsLst, MapBobsLst.BIRCH_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.BIRCH, getImagesFromGameResource(mapbobsLst, MapBobsLst.BIRCH_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.BIRCH, getImagesFromGameResource(mapbobsLst, MapBobsLst.BIRCH_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.BIRCH, getImagesFromGameResource(mapbobsLst, MapBobsLst.BIRCH_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.BIRCH, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BIRCH_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.BIRCH, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BIRCH_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.BIRCH, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BIRCH_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.BIRCH, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BIRCH_SHADOW_SMALLEST));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.BIRCH, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BIRCH_SHADOW_SMALL));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.BIRCH, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.BIRCH_SHADOW_MEDIUM));

        /* Extract animation for tree type 3 in wind -- oak */
        treeImageCollection.addImagesForTree(Tree.TreeType.OAK, getImagesFromGameResource(mapbobsLst, MapBobsLst.OAK_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.OAK, getImagesFromGameResource(mapbobsLst, MapBobsLst.OAK_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.OAK, getImagesFromGameResource(mapbobsLst, MapBobsLst.OAK_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.OAK, getImagesFromGameResource(mapbobsLst, MapBobsLst.OAK_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.OAK, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.OAK_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.OAK, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.OAK_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.OAK, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.OAK_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.OAK, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.OAK_SHADOW_SMALLEST));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.OAK, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.OAK_SHADOW_SMALL));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.OAK, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.OAK_SHADOW_MEDIUM));

        /* Extract animation for tree type 4 in wind -- short palm */
        treeImageCollection.addImagesForTree(Tree.TreeType.PALM_1, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_1_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.PALM_1, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_1_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.PALM_1, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_1_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.PALM_1, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_1_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PALM_1, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_1_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PALM_1, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_1_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PALM_1, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_1_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PALM_1, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_1_SHADOW_SMALLEST));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PALM_1, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_1_SHADOW_SMALL));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PALM_1, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_1_SHADOW_ALMOST_GROWN));

        /* Extract animation for tree type 5 in wind -- tall palm */
        treeImageCollection.addImagesForTree(Tree.TreeType.PALM_2, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_2_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.PALM_2, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_2_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.PALM_2, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_2_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.PALM_2, getImagesFromGameResource(mapbobsLst, MapBobsLst.PALM_2_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PALM_2, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_2_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PALM_2, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_2_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PALM_2, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_2_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PALM_2, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_2_SHADOW_SMALLEST));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PALM_2, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_2_SHADOW_SMALL));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PALM_2, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PALM_2_SHADOW_ALMOST_GROWN));

        /* Extract animation for tree type 6 in wind -- fat palm - pine apple */
        treeImageCollection.addImagesForTree(Tree.TreeType.PINE_APPLE, getImagesFromGameResource(mapbobsLst, MapBobsLst.PINE_APPLE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.PINE_APPLE, getImagesFromGameResource(mapbobsLst, MapBobsLst.PINE_APPLE_SHADOW_ANIMATION, 8));

        /* Extract animation for tree type 7 in wind -- pine */
        treeImageCollection.addImagesForTree(Tree.TreeType.PINE, getImagesFromGameResource(mapbobsLst, MapBobsLst.PINE_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.PINE, getImagesFromGameResource(mapbobsLst, MapBobsLst.PINE_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.PINE, getImagesFromGameResource(mapbobsLst, MapBobsLst.PINE_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.PINE, getImagesFromGameResource(mapbobsLst, MapBobsLst.PINE_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PINE, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PINE_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PINE, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PINE_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.PINE, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PINE_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PINE, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PINE_SMALLEST_SHADOW));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PINE, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PINE_SMALL_SHADOW));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.PINE, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.PINE_ALMOST_GROWN_SHADOW));

        /* Extract animation for tree type 8 in wind -- cherry */
        treeImageCollection.addImagesForTree(Tree.TreeType.CHERRY, getImagesFromGameResource(mapbobsLst, MapBobsLst.CHERRY_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.CHERRY, getImagesFromGameResource(mapbobsLst, MapBobsLst.CHERRY_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.CHERRY, getImagesFromGameResource(mapbobsLst, MapBobsLst.CHERRY_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.CHERRY, getImagesFromGameResource(mapbobsLst, MapBobsLst.CHERRY_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.CHERRY, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CHERRY_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.CHERRY, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CHERRY_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.CHERRY, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CHERRY_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.CHERRY, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CHERRY_SMALLEST_SHADOW));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.CHERRY, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CHERRY_SMALL_SHADOW));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.CHERRY, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.CHERRY_ALMOST_GROWN_SHADOW));

        /* Extract animation for tree type 9 in wind -- fir (?) */
        treeImageCollection.addImagesForTree(Tree.TreeType.FIR, getImagesFromGameResource(mapbobsLst, MapBobsLst.FIR_TREE_ANIMATION, 8));
        treeImageCollection.addImagesForTreeShadow(Tree.TreeType.FIR, getImagesFromGameResource(mapbobsLst, MapBobsLst.FIR_TREE_SHADOW_ANIMATION, 8));

        treeImageCollection.addImagesForTreeFalling(Tree.TreeType.FIR, getImagesFromGameResource(mapbobsLst, MapBobsLst.FIR_FALLING, 4));
        treeImageCollection.addImagesForTreeFallingShadow(Tree.TreeType.FIR, getImagesFromGameResource(mapbobsLst, MapBobsLst.FIR_FALLING_SHADOW, 4));

        treeImageCollection.addImageForGrowingTree(Tree.TreeType.FIR, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FIR_SMALLEST));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.FIR, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FIR_SMALL));
        treeImageCollection.addImageForGrowingTree(Tree.TreeType.FIR, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FIR_ALMOST_GROWN));

        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.FIR, TreeSize.NEWLY_PLANTED, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FIR_SMALLEST_SHADOW));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.FIR, TreeSize.SMALL, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FIR_SMALL_SHADOW));
        treeImageCollection.addImageForGrowingTreeShadow(Tree.TreeType.FIR, TreeSize.MEDIUM, getImageFromResourceLocation(mapbobsLst, MapBobsLst.FIR_ALMOST_GROWN_SHADOW));

        treeImageCollection.writeImageAtlas(natureDir, defaultPalette);

        /* Extract animal animations */
        AnimalImageCollection iceBearImageCollection = new AnimalImageCollection("ice-bear");
        AnimalImageCollection foxImageCollection = new AnimalImageCollection("fox");
        AnimalImageCollection rabbitImageCollection = new AnimalImageCollection("rabbit");
        AnimalImageCollection stagImageCollection = new AnimalImageCollection("stag");
        AnimalImageCollection deerImageCollection = new AnimalImageCollection("deer");
        AnimalImageCollection sheepImageCollection = new AnimalImageCollection("sheep");
        AnimalImageCollection deer2ImageCollection = new AnimalImageCollection("deer2");
        AnimalImageCollection duckImageCollection = new AnimalImageCollection("duck");

        /* Ice bear */
        iceBearImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.ICE_BEAR_WALKING_NORTH_EAST_ANIMATION, 6));
        iceBearImageCollection.addImages(EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.ICE_BEAR_WALKING_EAST_ANIMATION, 6));
        iceBearImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.ICE_BEAR_WALKING_SOUTH_EAST_ANIMATION, 6));
        iceBearImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.ICE_BEAR_WALKING_SOUTH_WEST_ANIMATION, 6));
        iceBearImageCollection.addImages(WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.ICE_BEAR_WALKING_WEST_ANIMATION, 6));
        iceBearImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.ICE_BEAR_WALKING_NORTH_WEST_ANIMATION, 6));

        /* Fox */
        foxImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.FOX_WALKING_NORTH_EAST_ANIMATION, 6));
        foxImageCollection.addImages(EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.FOX_WALKING_EAST_ANIMATION, 6));
        foxImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.FOX_WALKING_SOUTH_EAST_ANIMATION, 6));
        foxImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.FOX_WALKING_SOUTH_WEST_ANIMATION, 6));
        foxImageCollection.addImages(WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.FOX_WALKING_WEST_ANIMATION, 6));
        foxImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.FOX_WALKING_NORTH_WEST_ANIMATION, 6));

        /* Rabbit */
        rabbitImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.RABBIT_WALKING_NORTH_EAST_ANIMATION, 6));
        rabbitImageCollection.addImages(EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.RABBIT_WALKING_EAST_ANIMATION, 6));
        rabbitImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.RABBIT_WALKING_SOUTH_EAST_ANIMATION, 6));
        rabbitImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.RABBIT_WALKING_SOUTH_WEST_ANIMATION, 6));
        rabbitImageCollection.addImages(WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.RABBIT_WALKING_WEST_ANIMATION, 6));
        rabbitImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.RABBIT_WALKING_NORTH_WEST_ANIMATION, 6));

        /* Stag */
        stagImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.STAG_WALKING_NORTH_EAST_ANIMATION, 8));
        stagImageCollection.addImages(EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.STAG_WALKING_EAST_ANIMATION, 8));
        stagImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.STAG_WALKING_SOUTH_EAST_ANIMATION, 8));
        stagImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.STAG_WALKING_SOUTH_WEST_ANIMATION, 8));
        stagImageCollection.addImages(WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.STAG_WALKING_WEST_ANIMATION, 8));
        stagImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.STAG_WALKING_NORTH_WEST_ANIMATION, 8));

        /* Deer */
        deerImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_WALKING_NORTH_EAST_ANIMATION, 8));
        deerImageCollection.addImages(EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_WALKING_EAST_ANIMATION, 8));
        deerImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_WALKING_SOUTH_EAST_ANIMATION, 8));
        deerImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_WALKING_SOUTH_WEST_ANIMATION, 8));
        deerImageCollection.addImages(WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_WALKING_WEST_ANIMATION, 8));
        deerImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_WALKING_NORTH_WEST_ANIMATION, 8));

        /* Sheep */
        sheepImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.SHEEP_WALKING_NORTH_EAST_ANIMATION, 2));
        sheepImageCollection.addImages(EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.SHEEP_WALKING_EAST_ANIMATION, 2));
        sheepImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.SHEEP_WALKING_SOUTH_EAST_ANIMATION, 2));
        sheepImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.SHEEP_WALKING_SOUTH_WEST_ANIMATION, 2));
        sheepImageCollection.addImages(WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.SHEEP_WALKING_WEST_ANIMATION, 2));
        sheepImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.SHEEP_WALKING_NORTH_WEST_ANIMATION, 2));

        /* Deer 2 (horse?) */
        deer2ImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_2_WALKING_NORTH_EAST_ANIMATION, 8));
        deer2ImageCollection.addImages(EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_2_WALKING_EAST_ANIMATION, 8));
        deer2ImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_2_WALKING_SOUTH_EAST_ANIMATION, 8));
        deer2ImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_2_WALKING_SOUTH_WEST_ANIMATION, 8));
        deer2ImageCollection.addImages(WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_2_WALKING_WEST_ANIMATION, 8));
        deer2ImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobsLst, MapBobsLst.DEER_2_WALKING_NORTH_WEST_ANIMATION, 8));

        /* Extract duck */
        duckImageCollection.addImage(EAST, getImageFromResourceLocation(mapbobsLst, MapBobsLst.DUCK_EAST));
        duckImageCollection.addImage(SOUTH_EAST, getImageFromResourceLocation(mapbobsLst, MapBobsLst.DUCK_EAST + 1));
        duckImageCollection.addImage(SOUTH_WEST, getImageFromResourceLocation(mapbobsLst, MapBobsLst.DUCK_EAST + 2));
        duckImageCollection.addImage(WEST, getImageFromResourceLocation(mapbobsLst, MapBobsLst.DUCK_EAST + 3));
        duckImageCollection.addImage(NORTH_WEST, getImageFromResourceLocation(mapbobsLst, MapBobsLst.DUCK_EAST + 4));
        duckImageCollection.addImage(NORTH_EAST, getImageFromResourceLocation(mapbobsLst, MapBobsLst.DUCK_EAST + 5));

        writeFilesFromMap(mapbobsLst, imagesToFileMap);

        iceBearImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);
        foxImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);
        rabbitImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);
        stagImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);
        deerImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);
        sheepImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);
        deer2ImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);
        duckImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);

        /* Extract the donkey */
        List<GameResource> mapbobs0 = assetManager.loadLstFile(fromDir + "/DATA/MAPBOBS0.LST", defaultPalette);

        AnimalImageCollection donkeyImageCollection = new AnimalImageCollection("donkey");

        donkeyImageCollection.addImages(EAST, getImagesFromGameResource(mapbobs0, MapBobs0Lst.DONKEY_EAST_ANIMATION, 8));
        donkeyImageCollection.addImages(SOUTH_EAST, getImagesFromGameResource(mapbobs0, MapBobs0Lst.DONKEY_SOUTH_EAST_ANIMATION, 8));
        donkeyImageCollection.addImages(SOUTH_WEST, getImagesFromGameResource(mapbobs0, MapBobs0Lst.DONKEY_SOUTH_WEST_ANIMATION, 8));
        donkeyImageCollection.addImages(WEST, getImagesFromGameResource(mapbobs0, MapBobs0Lst.DONKEY_WEST_ANIMATION, 8));
        donkeyImageCollection.addImages(NORTH_WEST, getImagesFromGameResource(mapbobs0, MapBobs0Lst.DONKEY_NORTH_WEST_ANIMATION, 8));
        donkeyImageCollection.addImages(NORTH_EAST, getImagesFromGameResource(mapbobs0, MapBobs0Lst.DONKEY_NORTH_EAST_ANIMATION, 8));

        donkeyImageCollection.writeImageAtlas(natureDir + "/animals/", defaultPalette);

        /*  Extract decorative elements */
        /*
         * 283     Mushroom
         * 284     Mini decorative stone
         * 285     Mini decorative stones
         * 286     Small decorative stone
         * 287     Fallen dead decorative tree
         * 288     Standing dead decorative tree
         * 289     Skeleton decorative
         * 290     Mini decorative skeleton
         * 291     Flowers decorative
         * 292     Bush decorative
         * 293     Larger set of stones (can be extracted?)
         * 294     Cactus decorative
         * 295     Cactus decorative
         * 296     Beach grass decorative
         * 297     Small grass decorative
         */
        DecorativeImageCollection decorativeImageCollection = new DecorativeImageCollection();

        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.MUSHROOM,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MUSHROOM),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MUSHROOM_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.MINI_STONE,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MINI_STONE),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MINI_STONE_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.MINI_STONES,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MINI_STONES),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MINI_STONES_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.STONE,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_STONE),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_STONE_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.FALLEN_TREE,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_FALLEN_TREE),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_FALLEN_TREE_SHADOW));
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.STANDING_DEAD_TREE,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_STANDING_DEAD_TREE),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_STANDING_DEAD_TREE_SHADOW)
                );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.SKELETON,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_SKELETON),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_SKELETON_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.MINI_SKELETON,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MINI_SKELETON),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_MINI_SKELETON_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.FLOWERS,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_FLOWERS),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_FLOWERS_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.BUSH,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_BUSH),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_BUSH_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.LARGER_STONES,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_LARGER_STONES),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_LARGER_STONES_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.CACTUS_1,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_CACTUS_1),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_CACTUS_1_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.CACTUS_2,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_CACTUS_2),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_CACTUS_2_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.BEACH_GRASS,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_BEACH_GRASS),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_BEACH_GRASS_SHADOW)
        );
        decorativeImageCollection.addDecorationImageWithShadow(
                Decoration.SMALL_GRASS,
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_SMALL_GRASS),
                getImageFromResourceLocation(mapbobsLst, MapBobsLst.DECORATIVE_SMALL_GRASS_SHADOW)
        );

        decorativeImageCollection.writeImageAtlas(toDir, defaultPalette);
    }

    private void extractWinterTerrain(String winterDir, Bitmap winterTextureBitmap) throws IOException {

        /* Ice water */
        Bitmap iceWaterTexture = winterTextureBitmap.getSubBitmap(0, 0, 30, 30);
        iceWaterTexture.writeToFile(winterDir + "/ice-water.png");

        /* Ice floe - type of water */
        Bitmap iceLand = winterTextureBitmap.getSubBitmap(48, 0, 48 + 32, 32);
        iceLand.writeToFile(winterDir + "/ice-land.png");

        /* Ice water 2 */
        Bitmap iceWaterTexture2 = winterTextureBitmap.getSubBitmap(48 * 2, 0, 48 * 2 + 32, 32);
        iceWaterTexture2.writeToFile(winterDir + "/ice-water-2.png");

        /* Tundra */
        Bitmap tundraTexture = winterTextureBitmap.getSubBitmap(48 * 3, 0, 48 * 3 + 32, 32);
        Bitmap tundraTexture2 = winterTextureBitmap.getSubBitmap(48, 96, 48 + 32, 96 + 32);
        Bitmap tundraTexture3 = winterTextureBitmap.getSubBitmap(48 * 2, 48 * 2, 48 * 2 + 32, 48 * 2 + 32);
        Bitmap tundraTexture4 = winterTextureBitmap.getSubBitmap(48 * 3, 48 * 2, 48 * 3 + 32, 48 * 2 + 32);
        Bitmap tundraTexture5 = winterTextureBitmap.getSubBitmap(0, 48 * 3, 32, 48 * 3 + 32);

        tundraTexture.writeToFile(winterDir + "/tundra.png");
        tundraTexture2.writeToFile(winterDir + "/tundra-2.png");
        tundraTexture3.writeToFile(winterDir + "/tundra-3.png");
        tundraTexture4.writeToFile(winterDir + "/tundra-4.png");
        tundraTexture5.writeToFile(winterDir + "/tundra-5.png");

        /* Mountains */
        Bitmap mountainTexture1 = winterTextureBitmap.getSubBitmap(0, 48, 32, 48 + 32);
        Bitmap mountainTexture2 = winterTextureBitmap.getSubBitmap(48, 48, 48 + 32, 48 + 32);
        Bitmap mountainTexture3 = winterTextureBitmap.getSubBitmap(48 * 2, 48, 48 * 2 + 32, 48 + 32);
        Bitmap mountainTexture4 = winterTextureBitmap.getSubBitmap(48 * 3, 48, 48 * 3 + 32, 48 + 32);

        mountainTexture1.writeToFile(winterDir + "/mountain-1.png");
        mountainTexture2.writeToFile(winterDir + "/mountain-2.png");
        mountainTexture3.writeToFile(winterDir + "/mountain-3.png");
        mountainTexture4.writeToFile(winterDir + "/mountain-4.png");

        /* Taiga */
        Bitmap taigaTexture = winterTextureBitmap.getSubBitmap(0, 48 * 2, 32, 48 * 2 + 32);
        taigaTexture.writeToFile(winterDir + "/taiga.png");

        /* Snow */
        Bitmap snowTexture = winterTextureBitmap.getSubBitmap(48, 48 * 3, 48 + 32, 48 * 3 + 32);
        snowTexture.writeToFile(winterDir + "/snow.png");

        /* Water */ // FIXME
        Bitmap waterTexture = winterTextureBitmap.getSubBitmap(48 * 4, 48, 48 * 4 + 32, 48 + 32);
        waterTexture.writeToFile(winterDir + "/water.png");

        /* Lava */ // FIXME
        Bitmap lavaTexture = winterTextureBitmap.getSubBitmap(48 * 4, 48 * 2, 48 * 4 + 32, 48 * 2 + 32);
        lavaTexture.writeToFile(winterDir + "/lava.png");


        /* Collect roads */

        /* Regular road */
        Bitmap regularRoad = winterTextureBitmap.getSubBitmap(192, 0, 192 + 64, 16);
        regularRoad.writeToFile(winterDir + "/regular-road.png");

        /* Main road */
        Bitmap mainRoad = winterTextureBitmap.getSubBitmap(192, 16, 192 + 64, 16 + 16);
        mainRoad.writeToFile(winterDir + "/main-road.png");

        /* Boat road */
        Bitmap boatRoad = winterTextureBitmap.getSubBitmap(192, 32, 192 + 64, 32 + 16);
        boatRoad.writeToFile(winterDir + "/boat-road.png");

        /* Mountain road */
        Bitmap mountainRoad = winterTextureBitmap.getSubBitmap(192, 160, 192 + 64, 160 + 16);
        mountainRoad.writeToFile(winterDir + "/mountain-road.png");
    }

    private void extractGreenlandTerrain(String greenlandDir, Bitmap textureBitmap) throws IOException {
        /* Snow */
        Bitmap snowTextureBitmap = textureBitmap.getSubBitmap(0, 0, 48, 48);
        snowTextureBitmap.writeToFile(greenlandDir + "/snow.png");

        /* Mountain */
        Bitmap grassTextureBitmap = textureBitmap.getSubBitmap(48, 0, 96, 48);
        grassTextureBitmap.writeToFile(greenlandDir + "/mountain.png");

        /* Swamp */
        Bitmap otherTextureBitmap = textureBitmap.getSubBitmap(96, 0, 144, 48);
        otherTextureBitmap.writeToFile(greenlandDir + "/swamp.png");

        /* Grass - flower meadow */
        Bitmap otherTextureBitmap2 = textureBitmap.getSubBitmap(144, 0, 192, 48);
        otherTextureBitmap2.writeToFile(greenlandDir + "/flower-meadow.png");

        /* Water */
        Bitmap waterTextureBitmap = textureBitmap.getDiagonalSubBitmap(192, 48, 192 + 56, 48 + 56);
        waterTextureBitmap.writeToFile(greenlandDir + "/water.png");

        /* Lava */
        Bitmap lavaTexture = textureBitmap.getDiagonalSubBitmap(192, 48 + 56, 192 + 56, 48 + 56 + 56);
        lavaTexture.writeToFile(greenlandDir + "/lava.png");

        /* Meadows */
        Bitmap meadowTexture1 = textureBitmap.getSubBitmap(48, 48 * 2, 48 * 2, 48 * 3);
        Bitmap meadowTexture2 = textureBitmap.getSubBitmap(48 * 2, 48 * 2, 48 * 3, 48 * 3);
        Bitmap meadowTexture3 = textureBitmap.getSubBitmap(48 * 3, 48 * 2, 48 * 4, 48 * 3);

        meadowTexture1.writeToFile(greenlandDir + "/meadow1.png");
        meadowTexture2.writeToFile(greenlandDir + "/meadow2.png");
        meadowTexture3.writeToFile(greenlandDir + "/meadow3.png");

        /* Savannah */
        Bitmap savannahTexture = textureBitmap.getSubBitmap(0, 48 * 2, 48, 48 * 3);
        savannahTexture.writeToFile(greenlandDir + "/savannah.png");

        /* Deserts */
        Bitmap desertTexture = textureBitmap.getSubBitmap(48, 0, 48 * 2, 48);
        desertTexture.writeToFile(greenlandDir + "/desert.png");

        /* Mountains */
        Bitmap mountainTexture1 = textureBitmap.getSubBitmap(0, 48, 48, 48 * 2);
        Bitmap mountainTexture2 = textureBitmap.getSubBitmap(48, 48, 48 * 2, 48 * 2);
        Bitmap mountainTexture3 = textureBitmap.getSubBitmap(48 * 2, 48, 48 * 3, 48 * 2);
        Bitmap mountainTexture4 = textureBitmap.getSubBitmap(48 * 3, 48, 48 * 4, 48 * 2);

        mountainTexture1.writeToFile(greenlandDir + "/mountain1.png");
        mountainTexture2.writeToFile(greenlandDir + "/mountain2.png");
        mountainTexture3.writeToFile(greenlandDir + "/mountain3.png");
        mountainTexture4.writeToFile(greenlandDir + "/mountain4.png");

        /* Mountain meadow */
        Bitmap mountainMeadowTexture = textureBitmap.getSubBitmap(48, 48 * 3, 48 * 2, 48 * 4);
        mountainMeadowTexture.writeToFile(greenlandDir + "/mountain-meadow.png");

        /* Steppe */
        Bitmap steppeMountainTexture = textureBitmap.getSubBitmap(0, 48 * 3, 48, 48 * 4);
        steppeMountainTexture.writeToFile(greenlandDir + "/steppe.png");

        /* Collect roads */

        /* Regular road */
        Bitmap regularRoad = textureBitmap.getSubBitmap(192, 0, 192 + 64, 16);
        regularRoad.writeToFile(greenlandDir + "/regular-road.png");

        /* Main road */
        Bitmap mainRoad = textureBitmap.getSubBitmap(192, 16, 192 + 64, 16 + 16);
        mainRoad.writeToFile(greenlandDir + "/main-road.png");

        /* Boat road */
        Bitmap boatRoad = textureBitmap.getSubBitmap(192, 32, 192 + 64, 32 + 16);
        boatRoad.writeToFile(greenlandDir + "/boat-road.png");

        /* Mountain road */
        Bitmap mountainRoad = textureBitmap.getSubBitmap(192, 160, 192 + 64, 160 + 16);
        mountainRoad.writeToFile(greenlandDir + "/mountain-road.png");
    }

    private void loadDefaultPalette() throws IOException {
        defaultPalette = assetManager.loadPaletteFromFile(DEFAULT_PALETTE);
    }

    Extractor() {
        assetManager = new AssetManager();
    }

    /**
     * Layout of data in ROM_Y.LST
     *
     *
     * 0 ...
     * 4-11        Flag animation
     * 12-19       Corresponding shadows
     * 20-27       Main road flag animation
     * 28-35       Corresponding shadows
     * 36-43       Sea flag animation
     * 44-51       Corresponding flag animation
     * 51-59       ??
     * 60          Headquarter
     * 61          ??
     * 62          Headquarter open door
     * 63          Barracks
     * 64          ??
     * 65          Barracks under construction
     * 66          ??
     * 67          Barracks open door
     * 68          Guardhouse
     * 69          ??
     * 70          Guardhouse under construction
     * 71          ??
     * 72          Guardhouse open door
     * 73          Watch tower
     * 74          ??
     * 75          Watch tower under construction
     * 76          Watch tower under construction shadow (??)
     * 77          Watch tower open door
     * 78          Fortress
     * 79          ??
     * 80          Fortress under construction
     * 81          Fortress under construction shadow (??)
     * 82          Fortress open door
     * 83          Granite mine
     * 84          Granite mine shadow (??)
     * 85          Granite mine under construction
     * 86          Granite mine under construction shadow (??)
     * 87          Coal mine
     * 88          Coal mine shadow (??)
     * 89          Coal mine under construction
     * 90          Coal mine under construction shadow (??)
     * 91          Iron mine
     * 92          Iron mine shadow (??)
     * 93          Iron mine under construction
     * 94          Iron mine under construction shadow (??)
     * 95          Gold mine
     * 96          Gold mine shadow (??)
     * 97          Gold mine under construction
     * 98          Gold mine under construction shadow (??)
     * 99          Lookout tower
     * 100         Lookout tower shadow (??)
     * 101         Lookout tower under construction
     * 102         Lookout tower under construction shadow (??)
     * 103         Lookout tower open door
     * 104         Catapult
     * 105         Catapult shadow (??)
     * 106         Catapult under construction
     * 107         Catapult open door
     * 108         Woodcutter
     * 109         Woodcutter shadow (??)
     * 110         Woodcutter under construction
     * 111         Woodcutter under construction shadow (??)
     * 112         Woodcutter open door
     * 113         Fishery
     * 114         Fishery shadow
     * 115         Fishery under construction
     * 116         Fishery under construction shadow
     * 117         Fishery open door
     * 118         Quarry
     * 119         Quarry shadow
     * 120         Quarry under construction
     * 121         Quarry under construction shadow
     * 122         Quarry open door
     * 123         Forester hut
     * 124         Forester hut shadow
     * 125         Forester hut under construction
     * 126         Forester hut under construction shadow
     * 127         Forester hut open door
     * 128         Slaughter house
     * 129         Slaughter house shadow
     * 130         Slaughter house under construction
     * 131         Slaughter house under construction shadow
     * 132         Slaughter house open door
     * 133         Hunter hut
     * 134         Hunter hut shadow
     * 135         Hunter hut under construction
     * 136         Hunter hut under construction shadow
     * 137         Hunter hut open door
     * 138         Brewery
     * 139         Brewery shadow
     * 140         Brewery under construction
     * 141         Brewery under construction shadow
     * 142         Brewery open door
     * 143         Armory
     * 144         Armory shadow
     * 145         Armory under construction
     * 146         Armory under construction shadow
     * 147         Armory open door
     * 148         Metalworks
     * 149         Metalworks shadow
     * 150         Metalworks under construction
     * 151         Metalworks under construction shadow
     * 152         Metalworks open door
     * 153         Iron Smelter
     * 154         Iron Smelter shadow
     * 155         Iron Smelter under construction
     * 156         Iron Smelter under construction shadow
     * 157         Iron Smelter open door
     * 158         Pig farm
     * 159         Pig farm shadow
     * 160         Pig farm under construction
     * 161         Pig farm under construction shadow
     * 162         Pig farm open door
     * 163         Store house
     * 164         Store house shadow
     * 165         Store house under construction
     * 166         Store house under construction shadow
     * 167         Store house open door
     * 168         Mill - no fan
     * 169         Mill - no fan shadow
     * 170         Mill - no fan under construction
     * 171         Mill - no fan under construction shadow
     * 172         Mill - open door
     * 173         Bakery
     * 174         Bakery shadow
     * 175         Bakery under construction
     * 176         Bakery under construction shadow
     * 177         Bakery open door
     * 178         Sawmill
     * 179         Sawmill shadow
     * 180         Sawmill under construction
     * 181         Sawmill under construction shadow
     * 182         Sawmill open door
     * 183         Mint
     * 184         Mint shadow
     * 185         Mint under construction
     * 186         Mint under construction shadow
     * 187         Mint open door
     * 188         Well
     * 189         Well shadow
     * 190         Well under construction
     * 191         Well under construction shadow
     * 192         Well open door
     * 193         Shipyard
     * 194         Shipyard shadow
     * 195         Shipyard under construction
     * 196         Shipyard under construction shadow
     * 197         Shipyard open door
     * 198         Farm
     * 199         Farm shadow
     * 200         Farm under construction
     * 201         Farm under construction shadow
     * 202         Farm open door
     * 203         Donkey breeder
     * 204         Donkey breeder shadow
     * 205         Donkey breeder under construction
     * 206         Donkey breeder under construction shadow
     * 207         Donkey breeder open door
     * 208         Harbor
     * 209         Harbor shadow
     * 210         Harbor under construction
     * 211         Harbor under construction shadow
     * 212         Construction planned sign
     * 213         Construction planned sign shadow
     * 214         Construction just started
     * 215         Construction just started shadow
     * 216         Mill fan not spinning
     * 217-223     Pairs of mill fan+shadow
     * 224-227     Unknown fire
     *
     *
     *
     *
     * @param fromDir
     * @param toDir
     * @throws InvalidFormatException
     * @throws UnknownResourceTypeException
     * @throws InvalidHeaderException
     * @throws IOException
     */
    private void populateRomanBuildings(String fromDir, String toDir) throws InvalidFormatException, UnknownResourceTypeException, InvalidHeaderException, IOException {

        /* Load from the roman asset file */
        List<GameResource> romYLst = assetManager.loadLstFile(fromDir + "/" + ROMAN_FILE, defaultPalette);

        /* Create the roman buildings directory */
        Utils.createDirectory(toDir + "/" + ROMAN_BUILDINGS_DIRECTORY);

        Map<Integer, String> imagesToFileMap = new HashMap<>();

        String BUILDINGS_DIR = toDir + "/" + ROMAN_BUILDINGS_DIRECTORY;

        /* Write the buildings to the out directory */
        imagesToFileMap.put(RomYLst.HEADQUARTER, BUILDINGS_DIR + "/headquarter.png");
        imagesToFileMap.put(RomYLst.BARRACKS, BUILDINGS_DIR + "/barracks.png");
        imagesToFileMap.put(RomYLst.BARRACKS + 2, BUILDINGS_DIR + "/barracks-under-construction.png");
        imagesToFileMap.put(RomYLst.GUARDHOUSE, BUILDINGS_DIR + "/guardhouse.png");
        imagesToFileMap.put(RomYLst.GUARDHOUSE + 2, BUILDINGS_DIR + "/guardhouse-under-construction.png");
        imagesToFileMap.put(RomYLst.WATCHTOWER, BUILDINGS_DIR + "/watchtower.png");
        imagesToFileMap.put(RomYLst.WATCHTOWER + 2, BUILDINGS_DIR + "/watchtower-under-construction.png");
        imagesToFileMap.put(RomYLst.FORTRESS, BUILDINGS_DIR + "/fortress.png");
        imagesToFileMap.put(RomYLst.FORTRESS + 2, BUILDINGS_DIR + "/fortress-under-construction.png");
        imagesToFileMap.put(RomYLst.GRANITE_MINE, BUILDINGS_DIR + "/granite-mine.png");
        imagesToFileMap.put(RomYLst.GRANITE_MINE + 2, BUILDINGS_DIR + "/granite-mine-under-construction.png");
        imagesToFileMap.put(RomYLst.COAL_MINE, BUILDINGS_DIR + "/coal-mine.png");
        imagesToFileMap.put(RomYLst.COAL_MINE + 2, BUILDINGS_DIR + "/coal-mine-under-construction.png");
        imagesToFileMap.put(RomYLst.IRON_MINE_RESOURCE, BUILDINGS_DIR + "/iron-mine.png");
        imagesToFileMap.put(RomYLst.IRON_MINE_RESOURCE + 2, BUILDINGS_DIR + "/iron-mine-under-construction.png");
        imagesToFileMap.put(RomYLst.GOLD_MINE, BUILDINGS_DIR + "/gold-mine.png");
        imagesToFileMap.put(RomYLst.GOLD_MINE + 2, BUILDINGS_DIR + "/gold-mine-under-construction.png");
        imagesToFileMap.put(RomYLst.LOOKOUT_TOWER, BUILDINGS_DIR + "/lookout-tower.png");
        imagesToFileMap.put(RomYLst.LOOKOUT_TOWER + 2, BUILDINGS_DIR + "/lookout-tower-under-construction.png");
        imagesToFileMap.put(RomYLst.CATAPULT, BUILDINGS_DIR + "/catapult.png");
        imagesToFileMap.put(RomYLst.CATAPULT + 2, BUILDINGS_DIR + "/catapult-under-construction.png");
        imagesToFileMap.put(RomYLst.WOODCUTTER, BUILDINGS_DIR + "/woodcutter.png");
        imagesToFileMap.put(RomYLst.WOODCUTTER + 2, BUILDINGS_DIR + "/woodcutter-under-construction.png");
        imagesToFileMap.put(RomYLst.FISHERY, BUILDINGS_DIR + "/fishery.png");
        imagesToFileMap.put(RomYLst.FISHERY + 2, BUILDINGS_DIR + "/fishery-under-construction.png");
        imagesToFileMap.put(RomYLst.QUARRY, BUILDINGS_DIR + "/quarry.png");
        imagesToFileMap.put(RomYLst.QUARRY + 2, BUILDINGS_DIR + "/quarry-under-construction.png");
        imagesToFileMap.put(RomYLst.FORESTER_HUT, BUILDINGS_DIR + "/forester-hut.png");
        imagesToFileMap.put(RomYLst.FORESTER_HUT + 2, BUILDINGS_DIR + "/forester-hut-under-construction.png");
        imagesToFileMap.put(RomYLst.SLAUGHTER_HOUSE, BUILDINGS_DIR + "/slaughter-house.png");
        imagesToFileMap.put(RomYLst.SLAUGHTER_HOUSE + 2, BUILDINGS_DIR + "/slaughter-house-under-construction.png");
        imagesToFileMap.put(RomYLst.HUNTER_HUT, BUILDINGS_DIR + "/hunter-hut.png");
        imagesToFileMap.put(RomYLst.HUNTER_HUT + 2, BUILDINGS_DIR + "/hunter-hut-under-construction.png");
        imagesToFileMap.put(RomYLst.BREWERY, BUILDINGS_DIR + "/brewery.png");
        imagesToFileMap.put(RomYLst.BREWERY + 2, BUILDINGS_DIR + "/brewery-under-construction.png");
        imagesToFileMap.put(RomYLst.ARMORY, BUILDINGS_DIR + "/armory.png");
        imagesToFileMap.put(RomYLst.ARMORY + 2, BUILDINGS_DIR + "/armory-under-construction.png");
        imagesToFileMap.put(RomYLst.METALWORKS, BUILDINGS_DIR + "/metalworks.png");
        imagesToFileMap.put(RomYLst.METALWORKS + 2, BUILDINGS_DIR + "/metalworks-under-construction.png");
        imagesToFileMap.put(RomYLst.IRON_SMELTER, BUILDINGS_DIR + "/iron-smelter.png");
        imagesToFileMap.put(RomYLst.IRON_SMELTER + 2, BUILDINGS_DIR + "/iron-smelter-under-construction.png");
        imagesToFileMap.put(RomYLst.PIG_FARM, BUILDINGS_DIR + "/pig-farm.png");
        imagesToFileMap.put(RomYLst.PIG_FARM + 2, BUILDINGS_DIR + "/pig-farm-under-construction.png");
        imagesToFileMap.put(RomYLst.STOREHOUSE, BUILDINGS_DIR + "/storehouse.png");
        imagesToFileMap.put(RomYLst.STOREHOUSE + 2, BUILDINGS_DIR + "/storehouse-under-construction.png");
        imagesToFileMap.put(RomYLst.MILL_NO, BUILDINGS_DIR + "/mill-no-fan.png");
        imagesToFileMap.put(RomYLst.MILL_NO + 2, BUILDINGS_DIR + "/mill-no-fan-under-construction.png");
        imagesToFileMap.put(RomYLst.BAKERY, BUILDINGS_DIR + "/bakery.png");
        imagesToFileMap.put(RomYLst.BAKERY + 2, BUILDINGS_DIR + "/bakery-under-construction.png");
        imagesToFileMap.put(RomYLst.SAWMILL, BUILDINGS_DIR + "/sawmill.png");
        imagesToFileMap.put(RomYLst.SAWMILL + 2, BUILDINGS_DIR + "/sawmill-under-construction.png");
        imagesToFileMap.put(RomYLst.MINT, BUILDINGS_DIR + "/mint.png");
        imagesToFileMap.put(RomYLst.MINT + 2, BUILDINGS_DIR + "/mint-under-construction.png");
        imagesToFileMap.put(RomYLst.WELL, BUILDINGS_DIR + "/well.png");
        imagesToFileMap.put(RomYLst.WELL + 2, BUILDINGS_DIR + "/well-under-construction.png");
        imagesToFileMap.put(RomYLst.SHIPYARD, BUILDINGS_DIR + "/shipyard.png");
        imagesToFileMap.put(RomYLst.SHIPYARD + 2, BUILDINGS_DIR + "/shipyard-under-construction.png");
        imagesToFileMap.put(RomYLst.FARM, BUILDINGS_DIR + "/farm.png");
        imagesToFileMap.put(RomYLst.FARM + 2, BUILDINGS_DIR + "/farm-under-construction.png");
        imagesToFileMap.put(RomYLst.DONKEY_BREEDER, BUILDINGS_DIR + "/donkey-breeder.png");
        imagesToFileMap.put(RomYLst.DONKEY_BREEDER + 2, BUILDINGS_DIR + "/donkey-breeder-under-construction.png");
        imagesToFileMap.put(RomYLst.HARBOR, BUILDINGS_DIR + "/harbor.png");
        imagesToFileMap.put(RomYLst.HARBOR + 2, BUILDINGS_DIR + "/harbor-under-construction.png");
        imagesToFileMap.put(RomYLst.CONSTRUCTION_PLANNED, BUILDINGS_DIR + "/construction-planned-sign.png");
        imagesToFileMap.put(RomYLst.CONSTRUCTION_JUST_STARTED_INDEX, BUILDINGS_DIR + "/construction-started-sign.png");

        writeFilesFromMap(romYLst, imagesToFileMap);

        // Create the image atlas
        Map<Nation, String> nationsAndBobFiles = new HashMap<>();

        nationsAndBobFiles.put(Nation.ROMANS, "DATA/MBOB/ROM_Y.LST");
        nationsAndBobFiles.put(Nation.JAPANESE, "DATA/MBOB/JAP_Y.LST");
        nationsAndBobFiles.put(Nation.AFRICANS, "DATA/MBOB/AFR_Y.LST");
        nationsAndBobFiles.put(Nation.VIKINGS, "DATA/MBOB/VIK_Y.LST");

        BuildingsImageCollection buildingsImageCollection = new BuildingsImageCollection();

        for (Entry<Nation, String> entry : nationsAndBobFiles.entrySet()) {
            Nation nation = entry.getKey();
            String filename = fromDir + "/" + entry.getValue();

            List<GameResource> nationResourceList = assetManager.loadLstFile(filename, defaultPalette);

            buildingsImageCollection.addBuildingForNation(nation, "Headquarter", getImageFromResourceLocation(nationResourceList, RomYLst.HEADQUARTER));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Headquarter", getImageFromResourceLocation(nationResourceList, RomYLst.HEADQUARTER_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Barracks", getImageFromResourceLocation(nationResourceList, RomYLst.BARRACKS));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Barracks", getImageFromResourceLocation(nationResourceList, RomYLst.BARRACKS_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Barracks", getImageFromResourceLocation(nationResourceList, RomYLst.BARRACKS + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Barracks", getImageFromResourceLocation(nationResourceList, RomYLst.BARRACKS_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "GuardHouse", getImageFromResourceLocation(nationResourceList, RomYLst.GUARDHOUSE));
            buildingsImageCollection.addBuildingShadowForNation(nation, "GuardHouse", getImageFromResourceLocation(nationResourceList, RomYLst.GUARDHOUSE_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "GuardHouse", getImageFromResourceLocation(nationResourceList, RomYLst.GUARDHOUSE + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "GuardHouse", getImageFromResourceLocation(nationResourceList, RomYLst.GUARDHOUSE_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "WatchTower", getImageFromResourceLocation(nationResourceList, RomYLst.WATCHTOWER));
            buildingsImageCollection.addBuildingShadowForNation(nation, "WatchTower", getImageFromResourceLocation(nationResourceList, RomYLst.WATCHTOWER_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "WatchTower", getImageFromResourceLocation(nationResourceList, RomYLst.WATCHTOWER + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "WatchTower", getImageFromResourceLocation(nationResourceList, RomYLst.WATCHTOWER_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Fortress", getImageFromResourceLocation(nationResourceList, RomYLst.FORTRESS));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Fortress", getImageFromResourceLocation(nationResourceList, RomYLst.FORTRESS_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Fortress", getImageFromResourceLocation(nationResourceList, RomYLst.FORTRESS + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Fortress", getImageFromResourceLocation(nationResourceList, RomYLst.FORTRESS_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "GraniteMine", getImageFromResourceLocation(nationResourceList, RomYLst.GRANITE_MINE));
            buildingsImageCollection.addBuildingShadowForNation(nation, "GraniteMine", getImageFromResourceLocation(nationResourceList, RomYLst.GRANITE_MINE_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "GraniteMine", getImageFromResourceLocation(nationResourceList, RomYLst.GRANITE_MINE + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "GraniteMine", getImageFromResourceLocation(nationResourceList, RomYLst.GRANITE_MINE_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "CoalMine", getImageFromResourceLocation(nationResourceList, RomYLst.COAL_MINE));
            buildingsImageCollection.addBuildingShadowForNation(nation, "CoalMine", getImageFromResourceLocation(nationResourceList, RomYLst.COAL_MINE_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "CoalMine", getImageFromResourceLocation(nationResourceList, RomYLst.COAL_MINE + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "CoalMine", getImageFromResourceLocation(nationResourceList, RomYLst.COAL_MINE_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "IronMine", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_MINE_RESOURCE));
            buildingsImageCollection.addBuildingShadowForNation(nation, "IronMine", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_MINE_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "IronMine", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_MINE_RESOURCE + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "IronMine", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_MINE_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "GoldMine", getImageFromResourceLocation(nationResourceList, RomYLst.GOLD_MINE));
            buildingsImageCollection.addBuildingShadowForNation(nation, "GoldMine", getImageFromResourceLocation(nationResourceList, RomYLst.GOLD_MINE_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "GoldMine", getImageFromResourceLocation(nationResourceList, RomYLst.GOLD_MINE + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "GoldMine", getImageFromResourceLocation(nationResourceList, RomYLst.GOLD_MINE_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "LookoutTower", getImageFromResourceLocation(nationResourceList, RomYLst.LOOKOUT_TOWER));
            buildingsImageCollection.addBuildingShadowForNation(nation, "LookoutTower", getImageFromResourceLocation(nationResourceList, RomYLst.LOOKOUT_TOWER_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "LookoutTower", getImageFromResourceLocation(nationResourceList, RomYLst.LOOKOUT_TOWER + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "LookoutTower", getImageFromResourceLocation(nationResourceList, RomYLst.LOOKOUT_TOWER_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Catapult", getImageFromResourceLocation(nationResourceList, RomYLst.CATAPULT));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Catapult", getImageFromResourceLocation(nationResourceList, RomYLst.CATAPULT_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Catapult", getImageFromResourceLocation(nationResourceList, RomYLst.CATAPULT + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Catapult", getImageFromResourceLocation(nationResourceList, RomYLst.CATAPULT_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Woodcutter", getImageFromResourceLocation(nationResourceList, RomYLst.WOODCUTTER));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Woodcutter", getImageFromResourceLocation(nationResourceList, RomYLst.WOODCUTTER_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Woodcutter", getImageFromResourceLocation(nationResourceList, RomYLst.WOODCUTTER + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Woodcutter", getImageFromResourceLocation(nationResourceList, RomYLst.WOODCUTTER_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Fishery", getImageFromResourceLocation(nationResourceList, RomYLst.FISHERY));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Fishery", getImageFromResourceLocation(nationResourceList, RomYLst.FISHERY_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Fishery", getImageFromResourceLocation(nationResourceList, RomYLst.FISHERY + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Fishery", getImageFromResourceLocation(nationResourceList, RomYLst.FISHERY_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Quarry", getImageFromResourceLocation(nationResourceList, RomYLst.QUARRY));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Quarry", getImageFromResourceLocation(nationResourceList, RomYLst.QUARRY_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Quarry", getImageFromResourceLocation(nationResourceList, RomYLst.QUARRY + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Quarry", getImageFromResourceLocation(nationResourceList, RomYLst.QUARRY_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "ForesterHut", getImageFromResourceLocation(nationResourceList, RomYLst.FORESTER_HUT));
            buildingsImageCollection.addBuildingShadowForNation(nation, "ForesterHut", getImageFromResourceLocation(nationResourceList, RomYLst.FORESTER_HUT_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "ForesterHut", getImageFromResourceLocation(nationResourceList, RomYLst.FORESTER_HUT + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "ForesterHut", getImageFromResourceLocation(nationResourceList, RomYLst.FORESTER_HUT_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "SlaughterHouse", getImageFromResourceLocation(nationResourceList, RomYLst.SLAUGHTER_HOUSE));
            buildingsImageCollection.addBuildingShadowForNation(nation, "SlaughterHouse", getImageFromResourceLocation(nationResourceList, RomYLst.SLAUGHTER_HOUSE_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "SlaughterHouse", getImageFromResourceLocation(nationResourceList, RomYLst.SLAUGHTER_HOUSE + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "SlaughterHouse", getImageFromResourceLocation(nationResourceList, RomYLst.SLAUGHTER_HOUSE_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "HunterHut", getImageFromResourceLocation(nationResourceList, RomYLst.HUNTER_HUT));
            buildingsImageCollection.addBuildingShadowForNation(nation, "HunterHut", getImageFromResourceLocation(nationResourceList, RomYLst.HUNTER_HUT_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "HunterHut", getImageFromResourceLocation(nationResourceList, RomYLst.HUNTER_HUT + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "HunterHut", getImageFromResourceLocation(nationResourceList, RomYLst.HUNTER_HUT_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Brewery", getImageFromResourceLocation(nationResourceList, RomYLst.BREWERY));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Brewery", getImageFromResourceLocation(nationResourceList, RomYLst.BREWERY_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Brewery", getImageFromResourceLocation(nationResourceList, RomYLst.BREWERY + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Brewery", getImageFromResourceLocation(nationResourceList, RomYLst.BREWERY_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Armory", getImageFromResourceLocation(nationResourceList, RomYLst.ARMORY));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Armory", getImageFromResourceLocation(nationResourceList, RomYLst.ARMORY_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Armory", getImageFromResourceLocation(nationResourceList, RomYLst.ARMORY + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Armory", getImageFromResourceLocation(nationResourceList, RomYLst.ARMORY_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Metalworks", getImageFromResourceLocation(nationResourceList, RomYLst.METALWORKS));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Metalworks", getImageFromResourceLocation(nationResourceList, RomYLst.METALWORKS_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Metalworks", getImageFromResourceLocation(nationResourceList, RomYLst.METALWORKS + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Metalworks", getImageFromResourceLocation(nationResourceList, RomYLst.METALWORKS_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "IronSmelter", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_SMELTER));
            buildingsImageCollection.addBuildingShadowForNation(nation, "IronSmelter", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_SMELTER_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "IronSmelter", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_SMELTER + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "IronSmelter", getImageFromResourceLocation(nationResourceList, RomYLst.IRON_SMELTER_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "PigFarm", getImageFromResourceLocation(nationResourceList, RomYLst.PIG_FARM));
            buildingsImageCollection.addBuildingShadowForNation(nation, "PigFarm", getImageFromResourceLocation(nationResourceList, RomYLst.PIG_FARM_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "PigFarm", getImageFromResourceLocation(nationResourceList, RomYLst.PIG_FARM + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "PigFarm", getImageFromResourceLocation(nationResourceList, RomYLst.PIG_FARM_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Storehouse", getImageFromResourceLocation(nationResourceList, RomYLst.STOREHOUSE));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Storehouse", getImageFromResourceLocation(nationResourceList, RomYLst.STOREHOUSE_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Storehouse", getImageFromResourceLocation(nationResourceList, RomYLst.STOREHOUSE + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Storehouse", getImageFromResourceLocation(nationResourceList, RomYLst.STOREHOUSE_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Mill", getImageFromResourceLocation(nationResourceList, RomYLst.MILL_NO));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Mill", getImageFromResourceLocation(nationResourceList, RomYLst.MILL_NO_FAN_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Mill", getImageFromResourceLocation(nationResourceList, RomYLst.MILL_NO + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Mill", getImageFromResourceLocation(nationResourceList, RomYLst.MILL_NO_FAN_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Bakery", getImageFromResourceLocation(nationResourceList, RomYLst.BAKERY));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Bakery", getImageFromResourceLocation(nationResourceList, RomYLst.BAKERY_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Bakery", getImageFromResourceLocation(nationResourceList, RomYLst.BAKERY + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Bakery", getImageFromResourceLocation(nationResourceList, RomYLst.BAKERY_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Sawmill", getImageFromResourceLocation(nationResourceList, RomYLst.SAWMILL));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Sawmill", getImageFromResourceLocation(nationResourceList, RomYLst.SAWMILL_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Sawmill", getImageFromResourceLocation(nationResourceList, RomYLst.SAWMILL + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Sawmill", getImageFromResourceLocation(nationResourceList, RomYLst.SAWMILL_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Mint", getImageFromResourceLocation(nationResourceList, RomYLst.MINT));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Mint", getImageFromResourceLocation(nationResourceList, RomYLst.MINT_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Mint", getImageFromResourceLocation(nationResourceList, RomYLst.MINT + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Mint", getImageFromResourceLocation(nationResourceList, RomYLst.MINT_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Well", getImageFromResourceLocation(nationResourceList, RomYLst.WELL));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Well", getImageFromResourceLocation(nationResourceList, RomYLst.WELL_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Well", getImageFromResourceLocation(nationResourceList, RomYLst.WELL + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Well", getImageFromResourceLocation(nationResourceList, RomYLst.WELL_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Shipyard", getImageFromResourceLocation(nationResourceList, RomYLst.SHIPYARD));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Shipyard", getImageFromResourceLocation(nationResourceList, RomYLst.SHIPYARD_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Shipyard", getImageFromResourceLocation(nationResourceList, RomYLst.SHIPYARD + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Shipyard", getImageFromResourceLocation(nationResourceList, RomYLst.SHIPYARD_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Farm", getImageFromResourceLocation(nationResourceList, RomYLst.FARM));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Farm", getImageFromResourceLocation(nationResourceList, RomYLst.FARM_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Farm", getImageFromResourceLocation(nationResourceList, RomYLst.FARM + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Farm", getImageFromResourceLocation(nationResourceList, RomYLst.FARM_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "DonkeyBreeder", getImageFromResourceLocation(nationResourceList, RomYLst.DONKEY_BREEDER));
            buildingsImageCollection.addBuildingShadowForNation(nation, "DonkeyBreeder", getImageFromResourceLocation(nationResourceList, RomYLst.DONKEY_BREEDER_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "DonkeyBreeder", getImageFromResourceLocation(nationResourceList, RomYLst.DONKEY_BREEDER + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "DonkeyBreeder", getImageFromResourceLocation(nationResourceList, RomYLst.DONKEY_BREEDER_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addBuildingForNation(nation, "Harbor", getImageFromResourceLocation(nationResourceList, RomYLst.HARBOR));
            buildingsImageCollection.addBuildingShadowForNation(nation, "Harbor", getImageFromResourceLocation(nationResourceList, RomYLst.HARBOR_SHADOW));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Harbor", getImageFromResourceLocation(nationResourceList, RomYLst.HARBOR + 2));
            buildingsImageCollection.addBuildingUnderConstructionShadowForNation(nation, "Harbor", getImageFromResourceLocation(nationResourceList, RomYLst.HARBOR_UNDER_CONSTRUCTION_SHADOW));

            buildingsImageCollection.addConstructionPlanned(nation, getImageFromResourceLocation(nationResourceList, RomYLst.CONSTRUCTION_PLANNED));
            buildingsImageCollection.addConstructionPlannedShadow(nation, getImageFromResourceLocation(nationResourceList, RomYLst.CONSTRUCTION_PLANNED_SHADOW));
            buildingsImageCollection.addConstructionJustStarted(nation, getImageFromResourceLocation(nationResourceList, RomYLst.CONSTRUCTION_JUST_STARTED_INDEX));
            buildingsImageCollection.addConstructionJustStartedShadow(nation, getImageFromResourceLocation(nationResourceList, RomYLst.CONSTRUCTION_JUST_STARTED_SHADOW));
        }

        buildingsImageCollection.writeImageAtlas(toDir + "/", defaultPalette);
    }

    private List<Bitmap> getImagesFromGameResource(List<GameResource> gameResourceList, int startLocation, int amount) {
        List<Bitmap> images = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            images.add(getImageFromResourceLocation(gameResourceList, startLocation + i));
        }

        return images;
    }

    private Bitmap getImageFromResourceLocation(List<GameResource> gameResourceList, int location) {
        GameResource gameResource = gameResourceList.get(location);

        switch (gameResource.getType()) {
            case BITMAP_RLE:
                BitmapRLEResource headquarterRLEBitmapResource = (BitmapRLEResource) gameResource;
                return headquarterRLEBitmapResource.getBitmap();

            case PLAYER_BITMAP_RESOURCE:
                PlayerBitmapResource playerBitmapResource = (PlayerBitmapResource) gameResource;
                return playerBitmapResource.getBitmap();

            case BITMAP_RESOURCE:
                BitmapResource bitmapResource = (BitmapResource) gameResource;
                return bitmapResource.getBitmap();

            default:
                throw new RuntimeException("CANNOT HANDLE " + gameResource.getClass());
        }
    }

    private void writeFilesFromMap(List<GameResource> gameResourceList, Map<Integer, String> imagesToFileMap) throws IOException {
        for (Entry<Integer, String> entry : imagesToFileMap.entrySet()) {
            GameResource gameResource = gameResourceList.get(entry.getKey());
            String outFilename = entry.getValue();

            switch (gameResource.getType()) {
                case BITMAP_RLE:
                    BitmapRLEResource headquarterRLEBitmapResource = (BitmapRLEResource) gameResource;
                    headquarterRLEBitmapResource.getBitmap().writeToFile(outFilename);
                    break;

                case PLAYER_BITMAP_RESOURCE:
                    PlayerBitmapResource playerBitmapResource = (PlayerBitmapResource) gameResource;
                    playerBitmapResource.getBitmap().writeToFile(outFilename);
                    break;

                case BITMAP_RESOURCE:
                    BitmapResource bitmapResource = (BitmapResource) gameResource;
                    bitmapResource.getBitmap().writeToFile(outFilename);
                    break;

                default:
                    throw new RuntimeException("CANNOT HANDLE " + gameResource.getClass());
            }
        }
    }
}
