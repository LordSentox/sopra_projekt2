package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.IntStream;

import static de.sopra.javagame.model.player.PlayerType.PlayerImage;

public class MapPane extends GridPane {
    //Dieses pane ist besser als alle anderen
    
    private final StackPane[][] map;
    
    public MapPane() throws UnsupportedEncodingException, IOException {
        super();
        map = new StackPane[7][10];
        
        IntStream.range(0, 21).forEach(i -> this.getColumnConstraints().add(new ColumnConstraints(i % 2 == 0 ? 5 : 130)));
        IntStream.range(0, 15).forEach(i -> this.getRowConstraints().add(new RowConstraints(i % 2 == 0 ? 5 : 130)));
        
        MapTile[][] tiles = MapUtil.createMapFromNumbers(MapUtil.readNumberMapFromString(new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), StandardCharsets.UTF_8)));
        
        for (int y = 1; y < map.length + 1; y++) {
            for (int x = 1; x < map[y - 1].length + 1; x++) {
                if(tiles[y][x] != null) {
                    TileView v = new TileView(tiles[y][x].getTileIndex(), "default");
                    StackPane pane = new StackPane();
                    map[y - 1][x - 1] = pane;
                    pane.getChildren().add(v);
                    this.getChildren().add(pane);
                    GridPane.setConstraints(pane, x*2-1, y*2-1);
                    final int newX = x, newY = y;
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> onTileClicked(event, v, newX, newY));
                }else{
                    ImageView v = new ImageView(new Image(getClass().getResource("/textures/default/island_additional_2.png").toExternalForm(), 130, 130, true, true));
                    StackPane pane = new StackPane();
                    map[y - 1][x - 1] = pane;
                    pane.getChildren().add(v);
                    this.getChildren().add(pane);
                    GridPane.setConstraints(pane, x*2-1, y*2-1);
                }
            }
        }
    }

    private void onTileClicked(MouseEvent e, TileView v, int x, int y) {
        if (e.getButton() == MouseButton.PRIMARY)
            putPlayer(x, y, PlayerType.DIVER);
        else if (e.getButton() == MouseButton.SECONDARY)
            removePlayer(x, y, PlayerType.DIVER);
    }
    
    /**
     * Mit 1 indiziert
     * @param x
     * @param y
     * @return
     */
    public StackPane getMapStackPane(int x, int y) {
        return map[y - 1][x - 1];
    }
    
    
    public void putPlayer(int x, int y, PlayerType type) {
        StackPane pane = getMapStackPane(x, y);
        Optional<Point> playerPos = playerPosition(type);
        if (playerPos.isPresent()){
            Point pos = playerPos.get();
            removePlayer(pos.xPos, pos.yPos, type);
        }
        pane.getChildren().add(new ImageView(type.getImage(100, 100, true, true)));
    }
    
    public void removePlayer(int x, int y, PlayerType type) {
       playerOnTile(x, y, type).ifPresent(getMapStackPane(x, y).getChildren()::remove);
    }
    
    /**
     * Gibt die ImageView zurück, die den gegebenen PlayerType enthhält, falls dieser auf diesem Feld existiert
     * @param x die x Position
     * @param y die y Position
     * @param type der PlayerType, nach dem gesucht werden soll
     * @return Ein Optional, der die gesuchte ImageView enthält. Ein leerer Optional, falls der Player nicht auf diesem Feld steht
     */
    public Optional<ImageView> playerOnTile(int x, int y, PlayerType type) {
        return getMapStackPane(x, y).getChildren().stream()
            .filter(child -> child instanceof ImageView)
            .map(child -> (ImageView) child)
            .filter(child -> child.getImage() instanceof PlayerImage && ((PlayerImage) child.getImage()).getPlayer() == type)
            .findFirst();
    }


    public Optional<Point> playerPosition(PlayerType type) {
        for (int y = 1; y < map.length + 1; y++) {
            for (int x = 1; x < map[y - 1].length; x++) {
                if (playerOnTile(x, y, type).isPresent()) return Optional.of(new Point(x, y));
            }
        }
        return Optional.empty();
    }
}