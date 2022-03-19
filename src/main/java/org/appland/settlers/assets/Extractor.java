package org.appland.settlers.assets;

import org.appland.settlers.model.Tree;
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

import static org.appland.settlers.assets.Direction.*;
import static org.appland.settlers.model.Size.*;

public class Extractor {

    private static final String AFRICAN_GRAPHICS_Z = "DATA/MBOB/AFR_Z.LST";
    private static final String JAPANESE_GRAPHICS_Z = "DATA/MBOB/JAP_Z.LST";
    private static final String ROMAN_GRAPHICS_Z = "DATA/MBOB/ROM_Z.LST";
    private static final String VIKING_GRAPHICS_Z = "DATA/MBOB/VIK_Z.LST";

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
    private static final int HEADQUARTER_INDEX = 60;
    private static final int BARRACKS_INDEX = 63;
    private static final int GUARDHOUSE_INDEX = 68;
    private static final int WATCHTOWER_INDEX = 73;
    private static final int FORTRESS_INDEX = 78;
    private static final int GRANITE_MINE_INDEX = 83;
    private static final int COAL_MINE_INDEX = 87;
    private static final int IRON_MINE_RESOURCE = 91;
    private static final int GOLD_MINE_INDEX = 95;
    private static final int LOOKOUT_TOWER_INDEX = 99;
    private static final int CATAPULT_INDEX = 104;
    private static final int WOODCUTTER_INDEX = 108;
    private static final int FISHERY_INDEX = 113;
    private static final int QUARRY_INDEX = 118;
    private static final int FORESTER_HUT_INDEX = 123;
    private static final int SLAUGHTER_HOUSE_INDEX = 128;
    private static final int HUNTER_HUT_INDEX = 133;
    private static final int BREWERY_INDEX = 138;
    private static final int ARMORY_INDEX = 143;
    private static final int METALWORKS_INDEX = 148;
    private static final int IRON_SMELTER_INDEX = 153;
    private static final int PIG_FARM_INDEX = 158;
    private static final int STOREHOUSE_INDEX = 163;
    private static final int MILL_NO_FAN_INDEX = 168;
    private static final int BAKERY_INDEX = 173;
    private static final int SAWMILL_INDEX = 178;
    private static final int MINT_INDEX = 183;
    private static final int WELL_INDEX = 188;
    private static final int SHIPYARD_INDEX = 193;
    private static final int FARM_INDEX = 198;
    private static final int DONKEY_BREEDER_INDEX = 203;
    private static final int HARBOR_INDEX = 208;
    private static final int CONSTRUCTION_PLANNED_INDEX = 212;
    private static final int CONSTRUCTION_JUST_STARTED_INDEX = 214;
    private static final int SELECTED_POINT = 1;
    private static final int HOVER_POINT = 3;
    private static final int HOVER_AVAILABLE_FLAG = 5;
    private static final int HOVER_AVAILABLE_MINE = 6;
    private static final int HOVER_AVAILABLE_SMALL_BUILDING = 7;
    private static final int HOVER_AVAILABLE_MEDIUM_BUILDING = 8;
    private static final int HOVER_AVAILABLE_LARGE_BUILDING = 9;
    private static final int HOVER_AVAILABLE_HARBOR = 10;
    private static final int AVAILABLE_FLAG = 11;
    private static final int AVAILABLE_SMALL_BUILDING = 12;
    private static final int AVAILABLE_MEDIUM_BUILDING = 13;
    private static final int AVAILABLE_LARGE_BUILDING = 14;
    private static final int AVAILABLE_MINE = 15;
    private static final int AVAILABLE_HARBOR = 16;
    private static final int IRON_SIGN_SMALL_UP_RIGHT = 372;
    private static final int IRON_SIGN_MEDIUM_UP_RIGHT = 373;
    private static final int IRON_SIGN_LARGE_UP_RIGHT = 374;
    private static final int GOLD_SIGN_SMALL_UP_RIGHT = 375;
    private static final int GOLD_SIGN_MEDIUM_UP_RIGHT = 376;
    private static final int GOLD_SIGN_LARGE_UP_RIGHT = 377;
    private static final int COAL_SIGN_SMALL_UP_RIGHT = 378;
    private static final int COAL_SIGN_MEDIUM_UP_RIGHT = 379;
    private static final int COAL_SIGN_LARGE_UP_RIGHT = 380;
    private static final int GRANITE_SIGN_SMALL_UP_RIGHT = 381;
    private static final int GRANITE_SIGN_MEDIUM_UP_RIGHT = 382;
    private static final int GRANITE_SIGN_LARGE_UP_RIGHT = 383;
    private static final int WATER_SIGN_LARGE_UP_RIGHT = 384;
    private static final int NOTHING_SIGN_UP_RIGHT = 385;
    private static final int DEAD_TREE = 288;
    private static final int FALLEN_DEAD_TREE = 287;
    private static final int TREE_ANIMATION_TYPE_1 = 26;
    private static final int TREE_ANIMATION_TYPE_2 = 41;
    private static final int TREE_ANIMATION_TYPE_3 = 56;
    private static final int TREE_ANIMATION_TYPE_4 = 71;
    private static final int TREE_ANIMATION_TYPE_5 = 86;
    private static final int TREE_ANIMATION_TYPE_6 = 101;
    private static final int TREE_ANIMATION_TYPE_7 = 109;
    private static final int TREE_ANIMATION_TYPE_8 = 124;
    private static final int TREE_ANIMATION_TYPE_9 = 139;
    private static final int WILD_ANIMAL_FRAMES_PER_ANIMATION = 8;
    private static final int FOX_WALKING_SOUTH = 601;
    private static final int RABBIT_WALKING_SOUTH = 525;
    private static final int STAG_WALKING_SOUTH = 699;
    private static final int DEER_WALKING_SOUTH = 644;
    private static final int SHEEP_WALKING_SOUTH = 818;
    private static final int ICE_BEAR_WALKING_SOUTH = 475;
    private static final int DEER_2_WALKING_SOUTH = 761;
    private static final int DUCK = 754;
    private static final int MINI_FIRE_ANIMATION = 419;
    private static final int SMALL_FIRE_ANIMATION = 1031;
    private static final int MEDIUM_FIRE_ANIMATION = 1039;
    private static final int LARGE_FIRE_ANIMATION = 1047;
    private static final int CROP_TYPE_1_NEWLY_PLANTED = 311;
    private static final int CROP_TYPE_1_LITTLE_GROWTH = 312;
    private static final int CROP_TYPE_1_MORE_GROWTH = 313;
    private static final int CROP_TYPE_1_FULLY_GROWN = 314;
    private static final int CROP_TYPE_1_JUST_HARVESTED = 315;
    private static final int CROP_TYPE_2_NEWLY_PLANTED = 316;
    private static final int CROP_TYPE_2_LITTLE_GROWTH = 317;
    private static final int CROP_TYPE_2_MORE_GROWTH = 318;
    private static final int CROP_TYPE_2_FULLY_GROWN = 319;
    private static final int CROP_TYPE_2_JUST_HARVESTED = 320;
    private static final int STONE_TYPE_1_MINI = 298;
    private static final int STONE_TYPE_1_LITTLE = 299;
    private static final int STONE_TYPE_1_LITTLE_MORE = 300;
    private static final int STONE_TYPE_1_MIDDLE = 301;
    private static final int STONE_TYPE_1_ALMOST_FULL = 302;
    private static final int STONE_TYPE_1_FULL = 303;
    private static final int STONE_TYPE_2_MINI = 304;
    private static final int STONE_TYPE_2_LITTLE = 305;
    private static final int STONE_TYPE_2_LITTLE_MORE = 306;
    private static final int STONE_TYPE_2_MIDDLE = 307;
    private static final int STONE_TYPE_2_ALMOST_FULL = 308;
    private static final int STONE_TYPE_2_FULL = 309;
    private static final int DECORATIVE_MUSHROOM = 283;
    private static final int DECORATIVE_MINI_STONE = 284;
    private static final int DECORATIVE_MINI_STONES = 285;
    private static final int DECORATIVE_STONE = 286;
    private static final int DECORATIVE_FALLEN_TREE = 287;
    private static final int DECORATIVE_STANDING_DEAD_TREE = 288;
    private static final int DECORATIVE_SKELETON = 289;
    private static final int DECORATIVE_MINI_SKELETON = 290;
    private static final int DECORATIVE_FLOWERS = 291;
    private static final int DECORATIVE_BUSH = 292;
    private static final int DECORATIVE_LARGER_STONES = 293;
    private static final int DECORATIVE_CACTUS_1 = 294;
    private static final int DECORATIVE_CACTUS_2 = 295;
    private static final int DECORATIVE_BEACH_GRASS = 296;
    private static final int DECORATIVE_SMALL_GRASS = 297;
    private static final int DONKEY_EAST_START = 785;
    private static final int DONKEY_SOUTH_EAST_START = 793;
    private static final int DONKEY_SOUTH_WEST_START = 801;
    private static final int DONKEY_WEST_START = 809;
    private static final int DONKEY_NORTH_WEST_START = 817;
    private static final int DONKEY_NORTH_EAST_START = 825;

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

        /* Populate roman buildings */
        extractor.populateRomanBuildings(fromDir, toDir);

        /* Populate nature and gui elements */
        extractor.populateNatureAndUIElements(fromDir, toDir);

        /* Create animations for all workers */
        extractor.populateWorkers(fromDir, toDir);

        /* Extract flag animation */
        extractor.populateFlags(fromDir, toDir);
    }

    private void populateFlags(String fromDir, String toDir) throws InvalidFormatException, UnknownResourceTypeException, InvalidHeaderException, IOException {
        Map<Nation, List<GameResource>> nationGraphics = new HashMap<>();

        nationGraphics.put(Nation.AFRICANS, assetManager.loadLstFile(fromDir + "/" + AFRICAN_GRAPHICS_Z, defaultPalette));
        nationGraphics.put(Nation.JAPANESE, assetManager.loadLstFile(fromDir + "/" + JAPANESE_GRAPHICS_Z, defaultPalette));
        nationGraphics.put(Nation.ROMANS, assetManager.loadLstFile(fromDir + "/" + ROMAN_GRAPHICS_Z, defaultPalette));
        nationGraphics.put(Nation.VIKINGS, assetManager.loadLstFile(fromDir + "/" + VIKING_GRAPHICS_Z, defaultPalette));

        FlagImageCollection flagImageCollection = new FlagImageCollection();

        for (Nation nation : Nation.values()) {
            List<GameResource> nationResources = nationGraphics.get(nation);

            for (FlagType flagType : FlagType.values()) {
                for (int animationStep = 0; animationStep < 8; animationStep++) {
                    int nr = animationStep + 4 + 16 * flagType.ordinal();

                    GameResource flagGameResource = nationResources.get(nr);
                    GameResource shadowGameResource = nationResources.get(nr + 8);

                    Bitmap flagImage = Utils.getBitmapFromGameResource(flagGameResource);
                    Bitmap shadowImage = Utils.getBitmapFromGameResource(shadowGameResource);

                    // TODO: combine the flag image with the shadow image

                    if (flagImage == null) {
                        System.out.println("Failed to load flag for: " + nation + ", " + flagType + ", " + animationStep);

                        continue;
                    }

                    // Collect the flag images into an image atlas
                    flagImageCollection.addImageForFlag(nation, flagType, flagImage);
                }
            }
        }

        // Write the image atlas to file - one image file and one json file with meta data
        flagImageCollection.writeImageAtlas(toDir + "/", defaultPalette);
    }

    private void populateWorkers(String fromDir, String toDir) throws InvalidFormatException, UnknownResourceTypeException, InvalidHeaderException, IOException {

        /* Load worker image parts */
        String jobsFilename = fromDir + "DATA/BOBS/JOBS.BOB";

        List<GameResource> workerGameResources = assetManager.loadLstFile(jobsFilename, defaultPalette);

        System.out.println("Loaded worker images");
        System.out.println(workerGameResources);

        if (workerGameResources.size() != 1) {
            throw new RuntimeException("Wrong size of game resources in bob file. Must be 1, but was: " + workerGameResources.size());
        }

        if (! (workerGameResources.get(0) instanceof BobGameResource)) {
            throw new RuntimeException("Element must be Bob game resource. Was: " + workerGameResources.get(0).getClass().getName());
        }

        BobGameResource bobGameResource = (BobGameResource) workerGameResources.get(0);

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

        workerDetailsMap.put(JobType.HELPER, new WorkerDetails(false, 0));
        workerDetailsMap.put(JobType.WOODCUTTER, new WorkerDetails(false, 5));
        workerDetailsMap.put(JobType.FISHER, new WorkerDetails(false, 12));
        workerDetailsMap.put(JobType.FORESTER, new WorkerDetails(false, 8));
        workerDetailsMap.put(JobType.CARPENTER, new WorkerDetails(false, 6));
        workerDetailsMap.put(JobType.STONEMASON, new WorkerDetails(false, 7));
        workerDetailsMap.put(JobType.HUNTER, new WorkerDetails(false, 20));
        workerDetailsMap.put(JobType.FARMER, new WorkerDetails(false, 13));
        workerDetailsMap.put(JobType.MILLER, new WorkerDetails(true, 16));
        workerDetailsMap.put(JobType.BAKER, new WorkerDetails(true, 17));
        workerDetailsMap.put(JobType.BUTCHER, new WorkerDetails(false, 15));
        workerDetailsMap.put(JobType.MINER, new WorkerDetails(false, 10));
        workerDetailsMap.put(JobType.BREWER, new WorkerDetails(true, 3));
        workerDetailsMap.put(JobType.PIG_BREEDER, new WorkerDetails(false, 14));
        workerDetailsMap.put(JobType.DONKEY_BREEDER, new WorkerDetails(false, 24));
        workerDetailsMap.put(JobType.IRON_FOUNDER, new WorkerDetails(false, 11));
        workerDetailsMap.put(JobType.MINTER, new WorkerDetails(false, 9));
        workerDetailsMap.put(JobType.METALWORKER, new WorkerDetails(false, 18));
        workerDetailsMap.put(JobType.ARMORER, new WorkerDetails(true, 4));
        workerDetailsMap.put(JobType.BUILDER, new WorkerDetails(false, 23));
        workerDetailsMap.put(JobType.PLANER, new WorkerDetails(false, 22));
        workerDetailsMap.put(JobType.PRIVATE, new WorkerDetails(false, -30));
        workerDetailsMap.put(JobType.PRIVATE_FIRST_CLASS, new WorkerDetails(false, -31));
        workerDetailsMap.put(JobType.SERGEANT, new WorkerDetails(false, -32));
        workerDetailsMap.put(JobType.OFFICER, new WorkerDetails(false, -33));
        workerDetailsMap.put(JobType.GENERAL, new WorkerDetails(false, -34));
        workerDetailsMap.put(JobType.GEOLOGIST, new WorkerDetails(false, 26));
        workerDetailsMap.put(JobType.SHIP_WRIGHT, new WorkerDetails(false, 25));
        workerDetailsMap.put(JobType.SCOUT, new WorkerDetails(false, -35));
        workerDetailsMap.put(JobType.PACK_DONKEY, new WorkerDetails(false, 37));
        workerDetailsMap.put(JobType.BOAT_CARRIER, new WorkerDetails(false, 37));
        workerDetailsMap.put(JobType.CHAR_BURNER, new WorkerDetails(false, 37));

        /* Composite the worker images and animations */
        Map<JobType, RenderedWorker> renderedWorkers = assetManager.renderWorkerImages(bobGameResource.getBob(), workerDetailsMap);

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

            /* Write the worker's image atlas */
            workerImageCollection.writeImageAtlas(toDir + "/", defaultPalette);
        }
    }

    private String getDirectionString(Direction direction) {
        String directionString = "unknown";

        switch (direction) {
            case EAST: directionString = "east";
                break;
            case SOUTH_EAST: directionString = "south-east";
                break;
            case SOUTH_WEST: directionString = "south-west";
                break;
            case WEST: directionString = "west";
                break;
            case NORTH_WEST: directionString = "north-west";
                break;
            case NORTH_EAST: directionString = "north-east";
                break;
        }
        return directionString;
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
        List<GameResource> gameResourceList = assetManager.loadLstFile(fromDir + "/" + MAP_FILE, defaultPalette);

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
        LBMGameResource greenlandGameResource = (LBMGameResource) assetManager.loadGameResourcesFromLBMFile(fromDir + "/" + GREENLAND_TEXTURE_FILE, defaultPalette);
        LBMGameResource winterGameResource = (LBMGameResource) assetManager.loadGameResourcesFromLBMFile(fromDir + "/" + WINTER_TEXTURE_FILE, defaultPalette);

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

        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.MINI, getImageFromResourceLocation(gameResourceList, STONE_TYPE_1_MINI));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.LITTLE, getImageFromResourceLocation(gameResourceList, STONE_TYPE_1_LITTLE));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.LITTLE_MORE, getImageFromResourceLocation(gameResourceList, STONE_TYPE_1_LITTLE_MORE));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.MIDDLE, getImageFromResourceLocation(gameResourceList, STONE_TYPE_1_MIDDLE));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.ALMOST_FULL, getImageFromResourceLocation(gameResourceList, STONE_TYPE_1_ALMOST_FULL));
        stonesImageCollection.addImage(StoneType.TYPE_1, StoneAmount.FULL, getImageFromResourceLocation(gameResourceList, STONE_TYPE_1_FULL));

        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.MINI, getImageFromResourceLocation(gameResourceList, STONE_TYPE_2_MINI));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.LITTLE, getImageFromResourceLocation(gameResourceList, STONE_TYPE_2_LITTLE));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.LITTLE_MORE, getImageFromResourceLocation(gameResourceList, STONE_TYPE_2_LITTLE_MORE));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.MIDDLE, getImageFromResourceLocation(gameResourceList, STONE_TYPE_2_MIDDLE));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.ALMOST_FULL, getImageFromResourceLocation(gameResourceList, STONE_TYPE_2_ALMOST_FULL));
        stonesImageCollection.addImage(StoneType.TYPE_2, StoneAmount.FULL, getImageFromResourceLocation(gameResourceList, STONE_TYPE_2_FULL));

        stonesImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract UI elements */
        UIElementsImageCollection uiElementsImageCollection = new UIElementsImageCollection();

        uiElementsImageCollection.addSelectedPointImage(getImageFromResourceLocation(gameResourceList, SELECTED_POINT));
        uiElementsImageCollection.addHoverPoint(getImageFromResourceLocation(gameResourceList, HOVER_POINT));
        uiElementsImageCollection.addHoverAvailableFlag(getImageFromResourceLocation(gameResourceList, HOVER_AVAILABLE_FLAG));
        uiElementsImageCollection.addHoverAvailableMine(getImageFromResourceLocation(gameResourceList, HOVER_AVAILABLE_MINE));
        uiElementsImageCollection.addHoverAvailableBuilding(SMALL, getImageFromResourceLocation(gameResourceList, HOVER_AVAILABLE_SMALL_BUILDING));
        uiElementsImageCollection.addHoverAvailableBuilding(MEDIUM, getImageFromResourceLocation(gameResourceList, HOVER_AVAILABLE_MEDIUM_BUILDING));
        uiElementsImageCollection.addHoverAvailableBuilding(LARGE, getImageFromResourceLocation(gameResourceList, HOVER_AVAILABLE_LARGE_BUILDING));
        uiElementsImageCollection.addHoverAvailableHarbor(getImageFromResourceLocation(gameResourceList, HOVER_AVAILABLE_HARBOR));
        uiElementsImageCollection.addAvailableFlag(getImageFromResourceLocation(gameResourceList, AVAILABLE_FLAG));
        uiElementsImageCollection.addAvailableMine(getImageFromResourceLocation(gameResourceList, AVAILABLE_MINE));
        uiElementsImageCollection.addAvailableBuilding(SMALL, getImageFromResourceLocation(gameResourceList, AVAILABLE_SMALL_BUILDING));
        uiElementsImageCollection.addAvailableBuilding(MEDIUM, getImageFromResourceLocation(gameResourceList, AVAILABLE_MEDIUM_BUILDING));
        uiElementsImageCollection.addAvailableBuilding(LARGE, getImageFromResourceLocation(gameResourceList, AVAILABLE_LARGE_BUILDING));
        uiElementsImageCollection.addAvailableHarbor(getImageFromResourceLocation(gameResourceList, AVAILABLE_HARBOR));

        uiElementsImageCollection.writeImageAtlas(toDir, defaultPalette);

        /*  Extract the crops */
        CropImageCollection cropImageCollection = new CropImageCollection();

        cropImageCollection.addImage(CropType.TYPE_1, CropGrowth.NEWLY_PLANTED, getImageFromResourceLocation(gameResourceList, CROP_TYPE_1_NEWLY_PLANTED));
        cropImageCollection.addImage(CropType.TYPE_1, CropGrowth.SMALL, getImageFromResourceLocation(gameResourceList, CROP_TYPE_1_LITTLE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_1, CropGrowth.LARGER, getImageFromResourceLocation(gameResourceList, CROP_TYPE_1_MORE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_1, CropGrowth.FULLY_GROWN, getImageFromResourceLocation(gameResourceList, CROP_TYPE_1_FULLY_GROWN));
        cropImageCollection.addImage(CropType.TYPE_1, CropGrowth.NEWLY_HARVESTED, getImageFromResourceLocation(gameResourceList, CROP_TYPE_1_JUST_HARVESTED));

        cropImageCollection.addImage(CropType.TYPE_2, CropGrowth.NEWLY_PLANTED, getImageFromResourceLocation(gameResourceList, CROP_TYPE_2_NEWLY_PLANTED));
        cropImageCollection.addImage(CropType.TYPE_2, CropGrowth.SMALL, getImageFromResourceLocation(gameResourceList, CROP_TYPE_2_LITTLE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_2, CropGrowth.LARGER, getImageFromResourceLocation(gameResourceList, CROP_TYPE_2_MORE_GROWTH));
        cropImageCollection.addImage(CropType.TYPE_2, CropGrowth.FULLY_GROWN, getImageFromResourceLocation(gameResourceList, CROP_TYPE_2_FULLY_GROWN));
        cropImageCollection.addImage(CropType.TYPE_2, CropGrowth.NEWLY_HARVESTED, getImageFromResourceLocation(gameResourceList, CROP_TYPE_2_JUST_HARVESTED));

        cropImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract signs */
        SignImageCollection signImageCollection = new SignImageCollection();

        signImageCollection.addImage(SignType.IRON, SMALL, getImageFromResourceLocation(gameResourceList, IRON_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.IRON, MEDIUM, getImageFromResourceLocation(gameResourceList, IRON_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.IRON, LARGE, getImageFromResourceLocation(gameResourceList, IRON_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.COAL, SMALL, getImageFromResourceLocation(gameResourceList, COAL_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.COAL, MEDIUM, getImageFromResourceLocation(gameResourceList, COAL_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.COAL, LARGE, getImageFromResourceLocation(gameResourceList, COAL_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.STONE, SMALL, getImageFromResourceLocation(gameResourceList, GRANITE_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.STONE, MEDIUM, getImageFromResourceLocation(gameResourceList, GRANITE_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.STONE, LARGE, getImageFromResourceLocation(gameResourceList, GRANITE_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.GOLD, SMALL, getImageFromResourceLocation(gameResourceList, GOLD_SIGN_SMALL_UP_RIGHT));
        signImageCollection.addImage(SignType.GOLD, MEDIUM, getImageFromResourceLocation(gameResourceList, GOLD_SIGN_MEDIUM_UP_RIGHT));
        signImageCollection.addImage(SignType.GOLD, LARGE, getImageFromResourceLocation(gameResourceList, GOLD_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.WATER, LARGE, getImageFromResourceLocation(gameResourceList, WATER_SIGN_LARGE_UP_RIGHT));

        signImageCollection.addImage(SignType.NOTHING, LARGE, getImageFromResourceLocation(gameResourceList, NOTHING_SIGN_UP_RIGHT));

        signImageCollection.writeImageAtlas(toDir, defaultPalette);

        /* Extract nature elements */
        Map<Integer, String> imagesToFileMap = new HashMap<>();

        imagesToFileMap.put(FALLEN_DEAD_TREE, natureDir + "/fallen-dead-tree.png");
        imagesToFileMap.put(DEAD_TREE, natureDir + "/dead-tree.png");

        /* Extract mini fire animation */
        FireImageCollection fireImageCollection = new FireImageCollection();

        fireImageCollection.addImagesForFire(FireSize.MINI, getImagesFromResourceLocations(gameResourceList, MINI_FIRE_ANIMATION, 8));
        fireImageCollection.addImagesForFire(FireSize.SMALL, getImagesFromResourceLocations(gameResourceList, SMALL_FIRE_ANIMATION, 8));
        fireImageCollection.addImagesForFire(FireSize.MEDIUM, getImagesFromResourceLocations(gameResourceList, MEDIUM_FIRE_ANIMATION, 8));
        fireImageCollection.addImagesForFire(FireSize.LARGE, getImagesFromResourceLocations(gameResourceList, LARGE_FIRE_ANIMATION, 8));

        fireImageCollection.writeImageAtlas(toDir, defaultPalette);

        // Collect tree images
        TreeImageCollection treeImageCollection = new TreeImageCollection("trees");

        /* Extract animation for tree type 1 in wind -- cypress (?) */
        treeImageCollection.addImagesForTree(Tree.TreeType.CYPRESS, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_1, 8));

        /* Extract animation for tree type 2 in wind -- birch, for sure */
        treeImageCollection.addImagesForTree(Tree.TreeType.BIRCH, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_2, 8));

        /* Extract animation for tree type 3 in wind -- oak */
        treeImageCollection.addImagesForTree(Tree.TreeType.OAK, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_3, 8));

        /* Extract animation for tree type 4 in wind -- short palm */
        treeImageCollection.addImagesForTree(Tree.TreeType.PALM_1, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_4, 8));

        /* Extract animation for tree type 5 in wind -- tall palm */
        treeImageCollection.addImagesForTree(Tree.TreeType.PALM_2, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_5, 8));

        /* Extract animation for tree type 6 in wind -- fat palm - pine apple*/
        treeImageCollection.addImagesForTree(Tree.TreeType.PINE_APPLE, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_6, 8));

        /* Extract animation for tree type 7 in wind -- pine */
        treeImageCollection.addImagesForTree(Tree.TreeType.PINE, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_7, 8));

        /* Extract animation for tree type 8 in wind -- cherry */
        treeImageCollection.addImagesForTree(Tree.TreeType.CHERRY, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_8, 8));

        /* Extract animation for tree type 9 in wind -- fir (?) */
        treeImageCollection.addImagesForTree(Tree.TreeType.FIR, getImagesFromResourceLocations(gameResourceList, TREE_ANIMATION_TYPE_9, 8));

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

        for (Direction direction : Direction.values()) {

            String directionString = getDirectionString(direction);

            /* Ice bear */
            for (int i = 0; i < WILD_ANIMAL_FRAMES_PER_ANIMATION; i++) {
                int index = ICE_BEAR_WALKING_SOUTH + WILD_ANIMAL_FRAMES_PER_ANIMATION * direction.ordinal() + i;

                Bitmap image = getImageFromResourceLocation(gameResourceList, index);
                iceBearImageCollection.addImage(direction, image);
            }

            /* Fox */
            for (int i = 0; i < WILD_ANIMAL_FRAMES_PER_ANIMATION; i++) {
                int index = FOX_WALKING_SOUTH + WILD_ANIMAL_FRAMES_PER_ANIMATION * direction.ordinal() + i;

                Bitmap image = getImageFromResourceLocation(gameResourceList, index);
                foxImageCollection.addImage(direction, image);
            }

            /* Rabbit */
            for (int i = 0; i < WILD_ANIMAL_FRAMES_PER_ANIMATION; i++) {
                int index = RABBIT_WALKING_SOUTH + WILD_ANIMAL_FRAMES_PER_ANIMATION * direction.ordinal() + i;

                Bitmap image = getImageFromResourceLocation(gameResourceList, index);
                rabbitImageCollection.addImage(direction, image);
            }

            /* Stag */
            for (int i = 0; i < WILD_ANIMAL_FRAMES_PER_ANIMATION; i++) {
                int index = STAG_WALKING_SOUTH + WILD_ANIMAL_FRAMES_PER_ANIMATION * direction.ordinal() + i;

                Bitmap image = getImageFromResourceLocation(gameResourceList, index);
                stagImageCollection.addImage(direction, image);
            }

            /* Deer */
            for (int i = 0; i < WILD_ANIMAL_FRAMES_PER_ANIMATION; i++) {
                int index = DEER_WALKING_SOUTH + WILD_ANIMAL_FRAMES_PER_ANIMATION * direction.ordinal() + i;

                Bitmap image = getImageFromResourceLocation(gameResourceList, index);
                deerImageCollection.addImage(direction, image);
            }

            /* Sheep */
            for (int i = 0; i < 2; i++) {
                int index = SHEEP_WALKING_SOUTH + 2 * direction.ordinal() + i;

                Bitmap image = getImageFromResourceLocation(gameResourceList, index);
                sheepImageCollection.addImage(direction, image);
            }

            /* Deer 2 (horse?) */
            for (int i = 0; i < WILD_ANIMAL_FRAMES_PER_ANIMATION; i++) {
                int index = DEER_2_WALKING_SOUTH + WILD_ANIMAL_FRAMES_PER_ANIMATION * direction.ordinal() + i;

                Bitmap image = getImageFromResourceLocation(gameResourceList, index);
                deer2ImageCollection.addImage(direction, image);
            }
        }

        /* Extract duck */
        duckImageCollection.addImage(EAST, getImageFromResourceLocation(gameResourceList, DUCK));
        duckImageCollection.addImage(SOUTH_EAST, getImageFromResourceLocation(gameResourceList, DUCK + 1));
        duckImageCollection.addImage(SOUTH_WEST, getImageFromResourceLocation(gameResourceList, DUCK + 2));
        duckImageCollection.addImage(WEST, getImageFromResourceLocation(gameResourceList, DUCK + 3));
        duckImageCollection.addImage(NORTH_WEST, getImageFromResourceLocation(gameResourceList, DUCK + 4));
        duckImageCollection.addImage(NORTH_EAST, getImageFromResourceLocation(gameResourceList, DUCK + 5));

        writeFilesFromMap(gameResourceList, imagesToFileMap);

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

        donkeyImageCollection.addImages(EAST, getImagesFromResourceLocations(mapbobs0, DONKEY_EAST_START, 8));
        donkeyImageCollection.addImages(SOUTH_EAST, getImagesFromResourceLocations(mapbobs0, DONKEY_SOUTH_EAST_START, 8));
        donkeyImageCollection.addImages(SOUTH_WEST, getImagesFromResourceLocations(mapbobs0, DONKEY_SOUTH_WEST_START, 8));
        donkeyImageCollection.addImages(WEST, getImagesFromResourceLocations(mapbobs0, DONKEY_WEST_START, 8));
        donkeyImageCollection.addImages(NORTH_WEST, getImagesFromResourceLocations(mapbobs0, DONKEY_NORTH_WEST_START, 8));
        donkeyImageCollection.addImages(NORTH_EAST, getImagesFromResourceLocations(mapbobs0, DONKEY_NORTH_EAST_START, 8));

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

        decorativeImageCollection.addMushroomImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_MUSHROOM));
        decorativeImageCollection.addMiniStoneImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_MINI_STONE));
        decorativeImageCollection.addMiniStonesImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_MINI_STONES));
        decorativeImageCollection.addStoneImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_STONE));
        decorativeImageCollection.addFallenTreeImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_FALLEN_TREE));
        decorativeImageCollection.addStandingDeadTreeImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_STANDING_DEAD_TREE));
        decorativeImageCollection.addSkeletonImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_SKELETON));
        decorativeImageCollection.addMiniSkeletonImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_MINI_SKELETON));
        decorativeImageCollection.addFlowersImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_FLOWERS));
        decorativeImageCollection.addBushImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_BUSH));
        decorativeImageCollection.addLargerStonesImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_LARGER_STONES));
        decorativeImageCollection.addCactus1Image(getImageFromResourceLocation(gameResourceList, DECORATIVE_CACTUS_1));
        decorativeImageCollection.addCactus2Image(getImageFromResourceLocation(gameResourceList, DECORATIVE_CACTUS_2));
        decorativeImageCollection.addBeachGrassImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_BEACH_GRASS));
        decorativeImageCollection.addSmallGrassImage(getImageFromResourceLocation(gameResourceList, DECORATIVE_SMALL_GRASS));

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
        List<GameResource> gameResourceList = assetManager.loadLstFile(fromDir + "/" + ROMAN_FILE, defaultPalette);

        /* Create the roman buildings directory */
        Utils.createDirectory(toDir + "/" + ROMAN_BUILDINGS_DIRECTORY);

        Map<Integer, String> imagesToFileMap = new HashMap<>();

        String BUILDINGS_DIR = toDir + "/" + ROMAN_BUILDINGS_DIRECTORY;

        /* Write the flag to the out directory */
        GameResource flagResource = gameResourceList.get(FLAG_INDEX); // TODO: make this an animation instead!
        PlayerBitmapResource flagRLEBitmapResource = (PlayerBitmapResource) flagResource;
        flagRLEBitmapResource.getBitmap().writeToFile(toDir + "/" + ROMAN_BUILDINGS_DIRECTORY + "/flag.png");

        /* Write the buildings to the out directory */

        imagesToFileMap.put(HEADQUARTER_INDEX, BUILDINGS_DIR+ "/headquarter.png");
        imagesToFileMap.put(BARRACKS_INDEX, BUILDINGS_DIR + "/barracks.png");
        imagesToFileMap.put(BARRACKS_INDEX + 2, BUILDINGS_DIR + "/barracks-under-construction.png");
        imagesToFileMap.put(GUARDHOUSE_INDEX, BUILDINGS_DIR + "/guardhouse.png");
        imagesToFileMap.put(GUARDHOUSE_INDEX + 2, BUILDINGS_DIR + "/guardhouse-under-construction.png");
        imagesToFileMap.put(WATCHTOWER_INDEX, BUILDINGS_DIR + "/watchtower.png");
        imagesToFileMap.put(WATCHTOWER_INDEX + 2, BUILDINGS_DIR + "/watchtower-under-construction.png");
        imagesToFileMap.put(FORTRESS_INDEX, BUILDINGS_DIR + "/fortress.png");
        imagesToFileMap.put(FORTRESS_INDEX + 2, BUILDINGS_DIR + "/fortress-under-construction.png");
        imagesToFileMap.put(GRANITE_MINE_INDEX, BUILDINGS_DIR + "/granite-mine.png");
        imagesToFileMap.put(GRANITE_MINE_INDEX + 2, BUILDINGS_DIR + "/granite-mine-under-construction.png");
        imagesToFileMap.put(COAL_MINE_INDEX, BUILDINGS_DIR + "/coal-mine.png");
        imagesToFileMap.put(COAL_MINE_INDEX + 2, BUILDINGS_DIR + "/coal-mine-under-construction.png");
        imagesToFileMap.put(IRON_MINE_RESOURCE, BUILDINGS_DIR + "/iron-mine.png");
        imagesToFileMap.put(IRON_MINE_RESOURCE + 2, BUILDINGS_DIR + "/iron-mine-under-construction.png");
        imagesToFileMap.put(GOLD_MINE_INDEX, BUILDINGS_DIR + "/gold-mine.png");
        imagesToFileMap.put(GOLD_MINE_INDEX + 2, BUILDINGS_DIR + "/gold-mine-under-construction.png");
        imagesToFileMap.put(LOOKOUT_TOWER_INDEX, BUILDINGS_DIR + "/lookout-tower.png");
        imagesToFileMap.put(LOOKOUT_TOWER_INDEX + 2, BUILDINGS_DIR + "/lookout-tower-under-construction.png");
        imagesToFileMap.put(CATAPULT_INDEX, BUILDINGS_DIR + "/catapult.png");
        imagesToFileMap.put(CATAPULT_INDEX + 2, BUILDINGS_DIR + "/catapult-under-construction.png");
        imagesToFileMap.put(WOODCUTTER_INDEX, BUILDINGS_DIR + "/woodcutter.png");
        imagesToFileMap.put(WOODCUTTER_INDEX + 2, BUILDINGS_DIR + "/woodcutter-under-construction.png");
        imagesToFileMap.put(FISHERY_INDEX, BUILDINGS_DIR + "/fishery.png");
        imagesToFileMap.put(FISHERY_INDEX + 2, BUILDINGS_DIR + "/fishery-under-construction.png");
        imagesToFileMap.put(QUARRY_INDEX, BUILDINGS_DIR + "/quarry.png");
        imagesToFileMap.put(QUARRY_INDEX + 2, BUILDINGS_DIR + "/quarry-under-construction.png");
        imagesToFileMap.put(FORESTER_HUT_INDEX, BUILDINGS_DIR + "/forester-hut.png");
        imagesToFileMap.put(FORESTER_HUT_INDEX + 2, BUILDINGS_DIR + "/forester-hut-under-construction.png");
        imagesToFileMap.put(SLAUGHTER_HOUSE_INDEX, BUILDINGS_DIR + "/slaughter-house.png");
        imagesToFileMap.put(SLAUGHTER_HOUSE_INDEX + 2, BUILDINGS_DIR + "/slaughter-house-under-construction.png");
        imagesToFileMap.put(HUNTER_HUT_INDEX, BUILDINGS_DIR + "/hunter-hut.png");
        imagesToFileMap.put(HUNTER_HUT_INDEX + 2, BUILDINGS_DIR + "/hunter-hut-under-construction.png");
        imagesToFileMap.put(BREWERY_INDEX, BUILDINGS_DIR + "/brewery.png");
        imagesToFileMap.put(BREWERY_INDEX + 2, BUILDINGS_DIR + "/brewery-under-construction.png");
        imagesToFileMap.put(ARMORY_INDEX, BUILDINGS_DIR + "/armory.png");
        imagesToFileMap.put(ARMORY_INDEX + 2, BUILDINGS_DIR + "/armory-under-construction.png");
        imagesToFileMap.put(METALWORKS_INDEX, BUILDINGS_DIR + "/metalworks.png");
        imagesToFileMap.put(METALWORKS_INDEX + 2, BUILDINGS_DIR + "/metalworks-under-construction.png");
        imagesToFileMap.put(IRON_SMELTER_INDEX, BUILDINGS_DIR + "/iron-smelter.png");
        imagesToFileMap.put(IRON_SMELTER_INDEX + 2, BUILDINGS_DIR + "/iron-smelter-under-construction.png");
        imagesToFileMap.put(PIG_FARM_INDEX, BUILDINGS_DIR + "/pig-farm.png");
        imagesToFileMap.put(PIG_FARM_INDEX + 2, BUILDINGS_DIR + "/pig-farm-under-construction.png");
        imagesToFileMap.put(STOREHOUSE_INDEX, BUILDINGS_DIR + "/storehouse.png");
        imagesToFileMap.put(STOREHOUSE_INDEX + 2, BUILDINGS_DIR + "/storehouse-under-construction.png");
        imagesToFileMap.put(MILL_NO_FAN_INDEX, BUILDINGS_DIR + "/mill-no-fan.png");
        imagesToFileMap.put(MILL_NO_FAN_INDEX + 2, BUILDINGS_DIR + "/mill-no-fan-under-construction.png");
        imagesToFileMap.put(BAKERY_INDEX, BUILDINGS_DIR + "/bakery.png");
        imagesToFileMap.put(BAKERY_INDEX + 2, BUILDINGS_DIR + "/bakery-under-construction.png");
        imagesToFileMap.put(SAWMILL_INDEX, BUILDINGS_DIR + "/sawmill.png");
        imagesToFileMap.put(SAWMILL_INDEX + 2, BUILDINGS_DIR + "/sawmill-under-construction.png");
        imagesToFileMap.put(MINT_INDEX, BUILDINGS_DIR + "/mint.png");
        imagesToFileMap.put(MINT_INDEX + 2, BUILDINGS_DIR + "/mint-under-construction.png");
        imagesToFileMap.put(WELL_INDEX, BUILDINGS_DIR + "/well.png");
        imagesToFileMap.put(WELL_INDEX + 2, BUILDINGS_DIR + "/well-under-construction.png");
        imagesToFileMap.put(SHIPYARD_INDEX, BUILDINGS_DIR + "/shipyard.png");
        imagesToFileMap.put(SHIPYARD_INDEX + 2, BUILDINGS_DIR + "/shipyard-under-construction.png");
        imagesToFileMap.put(FARM_INDEX, BUILDINGS_DIR + "/farm.png");
        imagesToFileMap.put(FARM_INDEX + 2, BUILDINGS_DIR + "/farm-under-construction.png");
        imagesToFileMap.put(DONKEY_BREEDER_INDEX, BUILDINGS_DIR + "/donkey-breeder.png");
        imagesToFileMap.put(DONKEY_BREEDER_INDEX + 2, BUILDINGS_DIR + "/donkey-breeder-under-construction.png");
        imagesToFileMap.put(HARBOR_INDEX, BUILDINGS_DIR + "/harbor.png");
        imagesToFileMap.put(HARBOR_INDEX + 2, BUILDINGS_DIR + "/harbor-under-construction.png");
        imagesToFileMap.put(CONSTRUCTION_PLANNED_INDEX, BUILDINGS_DIR + "/construction-planned-sign.png");
        imagesToFileMap.put(CONSTRUCTION_JUST_STARTED_INDEX, BUILDINGS_DIR + "/construction-started-sign.png");

        writeFilesFromMap(gameResourceList, imagesToFileMap);

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

            buildingsImageCollection.addBuildingForNation(nation, "Headquarter", getImageFromResourceLocation(nationResourceList, HEADQUARTER_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Headquarter", getImageFromResourceLocation(nationResourceList, HEADQUARTER_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Barracks", getImageFromResourceLocation(nationResourceList, BARRACKS_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Barracks", getImageFromResourceLocation(nationResourceList, BARRACKS_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "GuardHouse", getImageFromResourceLocation(nationResourceList, GUARDHOUSE_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "GuardHouse", getImageFromResourceLocation(nationResourceList, GUARDHOUSE_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "WatchTower", getImageFromResourceLocation(nationResourceList, WATCHTOWER_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "WatchTower", getImageFromResourceLocation(nationResourceList, WATCHTOWER_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Fortress", getImageFromResourceLocation(nationResourceList, FORTRESS_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Fortress", getImageFromResourceLocation(nationResourceList, FORTRESS_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "GraniteMine", getImageFromResourceLocation(nationResourceList, GRANITE_MINE_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "GraniteMine", getImageFromResourceLocation(nationResourceList, GRANITE_MINE_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "CoalMine", getImageFromResourceLocation(nationResourceList, COAL_MINE_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "CoalMine", getImageFromResourceLocation(nationResourceList, COAL_MINE_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "IronMine", getImageFromResourceLocation(nationResourceList, IRON_MINE_RESOURCE));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "IronMine", getImageFromResourceLocation(nationResourceList, IRON_MINE_RESOURCE + 2));

            buildingsImageCollection.addBuildingForNation(nation, "GoldMine", getImageFromResourceLocation(nationResourceList, GOLD_MINE_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "GoldMine", getImageFromResourceLocation(nationResourceList, GOLD_MINE_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "LookoutTower", getImageFromResourceLocation(nationResourceList, LOOKOUT_TOWER_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "LookoutTower", getImageFromResourceLocation(nationResourceList, LOOKOUT_TOWER_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Catapult", getImageFromResourceLocation(nationResourceList, CATAPULT_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Catapult", getImageFromResourceLocation(nationResourceList, CATAPULT_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Woodcutter", getImageFromResourceLocation(nationResourceList, WOODCUTTER_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Woodcutter", getImageFromResourceLocation(nationResourceList, WOODCUTTER_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Fishery", getImageFromResourceLocation(nationResourceList, FISHERY_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Fishery", getImageFromResourceLocation(nationResourceList, FISHERY_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Quarry", getImageFromResourceLocation(nationResourceList, QUARRY_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Quarry", getImageFromResourceLocation(nationResourceList, QUARRY_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "ForesterHut", getImageFromResourceLocation(nationResourceList, FORESTER_HUT_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "ForesterHut", getImageFromResourceLocation(nationResourceList, FORESTER_HUT_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "SlaughterHouse", getImageFromResourceLocation(nationResourceList, SLAUGHTER_HOUSE_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "SlaughterHouse", getImageFromResourceLocation(nationResourceList, SLAUGHTER_HOUSE_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "HunterHut", getImageFromResourceLocation(nationResourceList, HUNTER_HUT_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "HunterHut", getImageFromResourceLocation(nationResourceList, HUNTER_HUT_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Brewery", getImageFromResourceLocation(nationResourceList, BREWERY_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Brewery", getImageFromResourceLocation(nationResourceList, BREWERY_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Armory", getImageFromResourceLocation(nationResourceList, ARMORY_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Armory", getImageFromResourceLocation(nationResourceList, ARMORY_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Metalworks", getImageFromResourceLocation(nationResourceList, METALWORKS_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Metalworks", getImageFromResourceLocation(nationResourceList, METALWORKS_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "IronSmelter", getImageFromResourceLocation(nationResourceList, IRON_SMELTER_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "IronSmelter", getImageFromResourceLocation(nationResourceList, IRON_SMELTER_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "PigFarm", getImageFromResourceLocation(nationResourceList, PIG_FARM_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "PigFarm", getImageFromResourceLocation(nationResourceList, PIG_FARM_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Storehouse", getImageFromResourceLocation(nationResourceList, STOREHOUSE_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Storehouse", getImageFromResourceLocation(nationResourceList, STOREHOUSE_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Mill", getImageFromResourceLocation(nationResourceList, MILL_NO_FAN_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Mill", getImageFromResourceLocation(nationResourceList, MILL_NO_FAN_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Bakery", getImageFromResourceLocation(nationResourceList, BAKERY_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Bakery", getImageFromResourceLocation(nationResourceList, BAKERY_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Sawmill", getImageFromResourceLocation(nationResourceList, SAWMILL_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Sawmill", getImageFromResourceLocation(nationResourceList, SAWMILL_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Mint", getImageFromResourceLocation(nationResourceList, MINT_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Mint", getImageFromResourceLocation(nationResourceList, MINT_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Well", getImageFromResourceLocation(nationResourceList, WELL_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Well", getImageFromResourceLocation(nationResourceList, WELL_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Shipyard", getImageFromResourceLocation(nationResourceList, SHIPYARD_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Shipyard", getImageFromResourceLocation(nationResourceList, SHIPYARD_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Farm", getImageFromResourceLocation(nationResourceList, FARM_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Farm", getImageFromResourceLocation(nationResourceList, FARM_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "DonkeyBreeder", getImageFromResourceLocation(nationResourceList, DONKEY_BREEDER_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "DonkeyBreeder", getImageFromResourceLocation(nationResourceList, DONKEY_BREEDER_INDEX + 2));

            buildingsImageCollection.addBuildingForNation(nation, "Harbor", getImageFromResourceLocation(nationResourceList, HARBOR_INDEX));
            buildingsImageCollection.addBuildingUnderConstructionForNation(nation, "Harbor", getImageFromResourceLocation(nationResourceList, HARBOR_INDEX + 2));

            buildingsImageCollection.addConstructionPlanned(nation, getImageFromResourceLocation(nationResourceList, CONSTRUCTION_PLANNED_INDEX));
            buildingsImageCollection.addConstructionJustStarted(nation, getImageFromResourceLocation(nationResourceList, CONSTRUCTION_JUST_STARTED_INDEX + 2));
        }

        buildingsImageCollection.writeImageAtlas(toDir + "/", defaultPalette);
    }

    private List<Bitmap> getImagesFromResourceLocations(List<GameResource> gameResourceList, int startLocation, int amount) {
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
