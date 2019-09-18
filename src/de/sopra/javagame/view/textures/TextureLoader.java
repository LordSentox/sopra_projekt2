package de.sopra.javagame.view.textures;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.textures.ZipWrapper.ZipEntryList;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.Map;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TextureLoader {

    private static final Queue<String> PACKS = new ArrayDeque<>();

    //Textures
    private static final Map<ArtifactType, Image> ARTIFACT_TEXTURES = new EnumMap<>(ArtifactType.class);
    private static final Map<ArtifactCardType, Image> ARTIFACT_CARD_TEXTURES = new EnumMap<>(ArtifactCardType.class);
    private static final Map<MapTileProperties, Image> FLOOD_CARD_TEXTURES = new EnumMap<>(MapTileProperties.class);
    private static final Map<PlayerType, PlayerTexture> PLAYER_CARD_TEXTURES = new EnumMap<>(PlayerType.class);
    private static final Map<PlayerType, PlayerTexture> PLAYER_ICON_TEXTURES = new EnumMap<>(PlayerType.class);
    private static final Map<MapTileProperties, TileTexture> TILE_TEXTURES_DRY = new EnumMap<>(MapTileProperties.class);
    private static final Map<MapTileProperties, TileTexture> TILE_TEXTURES_FLOODED = new EnumMap<>(MapTileProperties.class);


    private static Image background;
    private static Image turnSpinner;
    private static Image spinnerMarker;
    private static Image water;
    private static Image sea0;
    private static Image sea1;
    private static Image gone;
    private static Image artifactCardBack;
    private static Image floodCardBack;
    private static Image giveCard;
    private static Image findArtifact;
    private static Image special;
    private static Image drain;
    private static Image move;
    private static Image waterTile;

    static {
        PACKS.add("default.zip");
        refreshTextures();
    }

    public static void addPack(String packName) throws FileNotFoundException {
        String packFileName = packName + ".zip";
        if (getResource(packFileName) == null) throw new FileNotFoundException(packFileName);
        PACKS.add(packFileName);
        loadPack(packFileName);
    }

//    private static void removePack(String packName) {
//        if (packName.equals("default")) throw new IllegalArgumentException("Default textures cannot be removed");
//        if (PACKS.remove(packName + ".zip")) refreshTextures();
//    }

    private static void refreshTextures() {
        PACKS.forEach(TextureLoader::loadPack);
    }

    private static void loadPack(String packFileName) {
        try {
            ZipFile zipFile = new ZipFile(new File(getResource(packFileName).toURI()));
            ZipWrapper pack = new ZipWrapper(zipFile);

            ZipEntryList artifacts = pack.entriesIn("textures/artifacts/");
            ZipEntryList artifactCards = pack.entriesIn("textures/cards/artifacts/");
            ZipEntryList floodCards = pack.entriesIn("textures/cards/flood/");
            ZipEntryList misc = pack.entriesIn("textures/misc/");
            ZipEntryList playerCards = pack.entriesIn("textures/players/cards/");
            ZipEntryList playerIcons = pack.entriesIn("textures/players/icons/");
            ZipEntryList tilesDry = pack.entriesIn("textures/tiles/dry/");
            ZipEntryList tilesFlooded = pack.entriesIn("textures/tiles/flooded/");
            ZipEntryList tilesExtra = pack.entriesIn("textures/tiles/extra/");

            background = new Image(misc.inputStreamByName("background.png"), 1920, 1200, false, true);
            turnSpinner = new Image(misc.inputStreamByName("turnSpinner.png"));
            spinnerMarker = new Image(misc.inputStreamByName("spinnerMarker.png"));
            water = new Image(misc.inputStreamByName("water.png"));
            giveCard = new Image(misc.inputStreamByName("give-card.png"));
            findArtifact = new Image(misc.inputStreamByName("find-artifact.png"));
            special = new Image(misc.inputStreamByName("special.png"));
            drain = new Image(misc.inputStreamByName("drain.png"));
            move = new Image(misc.inputStreamByName("move.png"));
            waterTile = new Image(misc.inputStreamByName("water-tile.png"));
            sea0 = new Image(tilesExtra.inputStreamByName("sea_0.png"));
            sea1 = new Image(tilesExtra.inputStreamByName("sea_1.png"));
            gone = new Image(tilesExtra.inputStreamByName("flooded.png"));

            for (int i = 0; i < ArtifactType.values().length; i++) {

                ArtifactType type = ArtifactType.values()[i];
                if (type != ArtifactType.NONE) {
                    ARTIFACT_TEXTURES.put(type, new Image(artifacts.inputStreamByName(String.format("artifact_%d.png", i))));
                }
            }

            for (int i = 0; i < ArtifactCardType.values().length; i++) {
                ARTIFACT_CARD_TEXTURES.put(ArtifactCardType.values()[i], new Image(artifactCards.inputStreamByName(String.format("artifact_%d.png", i))));
            }
            artifactCardBack = new Image(artifactCards.inputStreamByName("artifact_back.png"));

            for (int i = 0; i < MapTileProperties.values().length; i++) {
                FLOOD_CARD_TEXTURES.put(MapTileProperties.values()[i], new Image(floodCards.inputStreamByName(String.format("floodcard_%02d.png", i))));
            }
            floodCardBack = new Image(floodCards.inputStreamByName("floodcard_back.png"));

            for (ZipEntry playerEntry : playerCards) {
                String name = playerEntry.getName();
                String textureName = name.substring(name.lastIndexOf("/") + 1);
                PlayerType type = PlayerType.valueOf(textureName.substring(0, textureName.length() - 4).toUpperCase());
                PLAYER_CARD_TEXTURES.put(type, new PlayerTexture(playerCards.inputStreamByName(textureName), type));
            }

            for (ZipEntry playerEntry : playerIcons) {
                String name = playerEntry.getName();
                String textureName = name.substring(name.lastIndexOf("/") + 1);
                PlayerType type = PlayerType.valueOf(textureName.substring(0, textureName.length() - 4).toUpperCase());
                PLAYER_ICON_TEXTURES.put(type, new PlayerTexture(playerIcons.inputStreamByName(textureName), type));
            }

            for (int i = 0; i < MapTileProperties.values().length; i++) {
                MapTileProperties properties = MapTileProperties.values()[i];
                TILE_TEXTURES_DRY.put(properties, new TileTexture(tilesDry.inputStreamByName(String.format("island_%02d.png", i)), properties));
            }

            for (int i = 0; i < MapTileProperties.values().length; i++) {
                MapTileProperties properties = MapTileProperties.values()[i];
                TILE_TEXTURES_FLOODED.put(properties, new TileTexture(tilesFlooded.inputStreamByName(String.format("floodedisland_%02d.png", i)), properties));
            }

        } catch (ZipWrapper.EntryNotFoundException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

//    private static List<String> getPacks() {
//        return Collections.unmodifiableList(PACKS.stream().map(s -> s.substring(s.length() - 4)).collect(Collectors.toList()));
//    }

    public static Image getArtifactTexture(ArtifactType type) {
        return ARTIFACT_TEXTURES.get(type);
    }

    public static Image getArtifactCardTexture(ArtifactCardType type) {
        return ARTIFACT_CARD_TEXTURES.get(type);
    }

    public static Image getFloodCardTexture(MapTileProperties type) {
        return FLOOD_CARD_TEXTURES.get(type);
    }

    public static PlayerTexture getPlayerCardTexture(PlayerType type) {
        return PLAYER_CARD_TEXTURES.get(type);
    }

    public static PlayerTexture getPlayerIconTexture(PlayerType type) {
        return PLAYER_ICON_TEXTURES.get(type);
    }

    public static TileTexture getTileTextureDry(MapTileProperties properties) {
        return TILE_TEXTURES_DRY.get(properties);
    }

    public static TileTexture getTileTextureFlooded(MapTileProperties properties) {
        return TILE_TEXTURES_FLOODED.get(properties);
    }

    public static Image getTileTexture(MapTile tile) {
        return tile.getState() == MapTileState.DRY ? getTileTextureDry(tile.getProperties()) : (tile.getState() == MapTileState.FLOODED ? getTileTextureFlooded(tile.getProperties()) : getGone());
    }

    public static Image getBackground() {
        return background;
    }

    public static Image getTurnSpinner() {
        return turnSpinner;
    }

    public static Image getSpinnerMarker() {
        return spinnerMarker;
    }

    public static Image getWater() {
        return water;
    }

    public static Image getSea0() {
        return sea0;
    }

    public static Image getSea1() {
        return sea1;
    }

    public static Image getGone() {
        return gone;
    }

    public static Image getArtifactCardBack() {
        return artifactCardBack;
    }

    public static Image getFloodCardBack() {
        return floodCardBack;
    }

    public static Image getGiveCard() {
        return giveCard;
    }

    public static Image getFindArtifact() {
        return findArtifact;
    }

    public static Image getSpecial() {
        return special;
    }

    public static Image getDrain() {
        return drain;
    }

    public static Image getMove() {
        return move;
    }

    public static Image getWaterTile() {
        return waterTile;
    }

    private static URL getResource(String name) {
        return TextureLoader.class.getResource("/" + name);
    }

    public static class PlayerTexture extends Image {

        private final PlayerType player;

        public PlayerTexture(InputStream inputStream, PlayerType player) {
            super(inputStream);
            this.player = player;
        }

        public PlayerTexture(InputStream inputStream, double requestedWidth, double requestedHeight, boolean preserveRatio, PlayerType player) {
            super(inputStream, requestedWidth, requestedHeight, preserveRatio, true);
            this.player = player;
        }

        private PlayerTexture(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, PlayerType player) {
            super(url, requestedWidth, requestedHeight, preserveRatio, true);
            this.player = player;
        }

        private PlayerTexture(String url, PlayerType player) {
            super(url);
            this.player = player;
        }

        public PlayerType getPlayer() {
            return player;
        }
    }

    public static class TileTexture extends Image {

        private final MapTileProperties properties;

        public TileTexture(InputStream inputStream, MapTileProperties properties) {
            super(inputStream);
            this.properties = properties;
        }

        public TileTexture(InputStream inputStream, double requestedWidth, double requestedHeight, boolean preserveRatio, MapTileProperties properties) {
            super(inputStream, requestedWidth, requestedHeight, preserveRatio, true);
            this.properties = properties;
        }

        private TileTexture(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, MapTileProperties properties) {
            super(url, requestedWidth, requestedHeight, preserveRatio, true);
            this.properties = properties;
        }

        private TileTexture(String url, MapTileProperties properties) {
            super(url);
            this.properties = properties;
        }

        public MapTileProperties getProperties() {
            return properties;
        }
    }
}
