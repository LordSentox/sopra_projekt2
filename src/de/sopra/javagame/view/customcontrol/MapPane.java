package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Map;
import de.sopra.javagame.util.MapFull;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.InGameViewController;
import de.sopra.javagame.view.textures.TextureLoader;
import de.sopra.javagame.view.textures.TextureLoader.PlayerTexture;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;


public class MapPane extends GridPane {
    //Dieses pane ist besser als alle anderen

    private static final int TILE_SIZE = 130;
    private final MapPaneTile[][] map;
    private InGameViewController inGameViewController;


    public MapPane() throws IOException {
        super();
        map = new MapPaneTile[Map.SIZE_Y][Map.SIZE_X];

        IntStream.range(0, 21).forEach(i -> this.getColumnConstraints().add(new ColumnConstraints(i % 2 == 0 ? 5 : TILE_SIZE)));
        IntStream.range(0, 15).forEach(i -> this.getRowConstraints().add(new RowConstraints(i % 2 == 0 ? 5 : TILE_SIZE)));
    }

    public void buildMap(MapFull tiles) {

        for (MapPaneTile[] row : map) {
            for (MapPaneTile tile : row) {
                getChildren().remove(tile);
            }
        }
        for (int y = 0; y < Map.SIZE_Y; y++) {
            for (int x = 0; x < Map.SIZE_X; x++) {
                if (tiles.get(x, y) != null) {
                    TileView v = new TileView(tiles.get(x, y), TILE_SIZE);
                    v.setPreserveRatio(true);
                    MapPaneTile pane = new MapPaneTile(inGameViewController, v, new Point(x, y));
                    map[y][x] = pane;
                    this.getChildren().add(pane);
                    pane.toBack();
                    GridPane.setConstraints(pane, x * 2 + 1, y * 2 + 1);
                } else {
                    ImageView v = new ImageView(TextureLoader.getSea1());
                    v.setPreserveRatio(true);
                    v.setFitWidth(TILE_SIZE);
                    v.setFitHeight(TILE_SIZE);
                    MapPaneTile pane = new MapPaneTile(v);
                    map[y][x] = pane;
                    this.getChildren().add(pane);
                    pane.toBack();
                    GridPane.setConstraints(pane, x * 2 + 1, y * 2 + 1);
                }
            }
        }
    }

    /**
     * Mit 0 indiziert
     *
     * @param point
     * @return
     */
    public MapPaneTile getMapStackPane(Point point) {
        return map[point.yPos][point.xPos];
    }

    public void forEach(Consumer<MapPaneTile> consumer) {
        for (MapPaneTile[] tiles : map)
            for (MapPaneTile tile : tiles)
                if (tile != null)
                    consumer.accept(tile);
    }

    public void movePlayer(Point point, PlayerType type) {
        MapPaneTile pane = getMapStackPane(point);
        playerPosition(type).ifPresent(mapPaneTile -> mapPaneTile.unputPlayer(type));
        pane.putPlayer(type);
    }

    /**
     * Gibt die ImageView zurück, die den gegebenen PlayerType enthhält, falls dieser auf diesem Feld existiert
     *
     * @param p
     * @param type der PlayerType, nach dem gesucht werden soll
     * @return Ein Optional, der die gesuchte ImageView enthält. Ein leerer Optional, falls der Player nicht auf diesem Feld steht
     */
    public Optional<ImageView> playerOnTile(Point p, PlayerType type) {
        return getMapStackPane(p).getChildren().stream()
                .filter(child -> child instanceof ImageView)
                .map(child -> (ImageView) child)
                .filter(child -> child.getImage() instanceof PlayerTexture && ((PlayerTexture) child.getImage()).getPlayer() == type)
                .findFirst();
    }


    public Optional<MapPaneTile> playerPosition(PlayerType type) {
        for (int y = 0; y < Map.SIZE_Y; y++) {
            for (int x = 0; x < Map.SIZE_X; x++) {
                MapPaneTile mapPaneTile = getMapStackPane(new Point(x, y));
                if (mapPaneTile.getPlayers().contains(type))
                    return Optional.of(mapPaneTile);
            }
        }
        return Optional.empty();
    }

    public InGameViewController getInGameViewController() {
        return this.inGameViewController;

    }

    public void setIngameViewController(InGameViewController inGameViewController) {
        this.inGameViewController = inGameViewController;

    }

    public void setMapTile(Point position, MapTile tile) {
        MapPaneTile pane = this.getMapStackPane(position);
        
        pane.setState(tile.getState());
    }

}
