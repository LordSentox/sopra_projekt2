package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.util.*;
import de.sopra.javagame.view.MapEditorViewController;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.stream.IntStream;

public class EditorMapPane extends GridPane {
    //Dieses pane ist besser als alle anderen

    private static final int TILE_SIZE = 130;
    private final ImageView[][] mapImageView;
    private MapEditorViewController mapEditorViewController;
    private MapBlackWhite booleanMap;
    private Label showUsedTilesLabel;
    public static final Paint WHITE = Paint.valueOf("#FFFFFF");
    public static final Paint GREEN = Paint.valueOf("#00FF00");
    public static final Paint RED = Paint.valueOf("#FF0000");
    public static final int VALID_MAP_TILE_COUNT = 24;


    public EditorMapPane() throws IOException {
        super();
        mapImageView = new ImageView[Map.SIZE_Y][Map.SIZE_X];

        IntStream.range(0, 21).forEach(i -> this.getColumnConstraints().add(new ColumnConstraints(i % 2 == 0 ? 5 : TILE_SIZE)));
        IntStream.range(0, 15).forEach(i -> this.getRowConstraints().add(new RowConstraints(i % 2 == 0 ? 5 : TILE_SIZE)));

        booleanMap = new MapBlackWhite();
        buildMap(booleanMap);
    }

    public void buildMap(MapBlackWhite booleanTiles) {
        if (booleanTiles == null) {
            booleanTiles = new MapBlackWhite();
        }
        this.booleanMap = booleanTiles;
        for (int y = 0; y < Map.SIZE_Y; y++) {
            for (int x = 0; x < Map.SIZE_X; x++) {
                ImageView v;
                if (this.booleanMap.get(x, y) != null && !this.booleanMap.get(x, y)) {
                    v = new ImageView(TextureLoader.getSea1());
                } else {
                    v = new ImageView(TextureLoader.getSea0());
                }
                v.setPreserveRatio(true);
                v.setFitWidth(TILE_SIZE);
                v.setFitHeight(TILE_SIZE);
                this.getChildren().add(v);
                mapImageView[y][x] = v;
                GridPane.setConstraints(v, x * 2 + 1, y * 2 + 1);
                final int newX = x, newY = y;
                v.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> onTileClicked(event, newX, newY));
            }
        }
        countTiles();
    }

    private void onTileClicked(MouseEvent e, int x, int y) {
        if (e.getButton() == MouseButton.PRIMARY && !booleanMap.get(x, y)) {
            setBooleanTile(new Point(x, y), true);
        } else if (e.getButton() == MouseButton.SECONDARY && booleanMap.get(x, y)) {
            setBooleanTile(new Point(x, y), false);
        }       

        countTiles();
    }

    private void countTiles() {
        int count = (int) getBooleanMap().stream().filter(element -> element).count();
        if (showUsedTilesLabel != null){
        showUsedTilesLabel.setText(count + "/" + VALID_MAP_TILE_COUNT);
        showUsedTilesLabel.setTextFill(count == VALID_MAP_TILE_COUNT ? GREEN : count < VALID_MAP_TILE_COUNT ? WHITE : RED);
        }
    }

    public void setMapEditorViewController(MapEditorViewController mapEditorViewController) {
        this.mapEditorViewController = mapEditorViewController;
        System.out.println("map editor View Controller gesetzt");
    }

    public MapEditorViewController getMapEditorViewController() {
        return mapEditorViewController;
    }

    public void setBooleanTile(Point position, boolean tile) {
        ImageView landView;
        this.booleanMap.set(tile, position);
        if (tile) {
            landView = new ImageView(TextureLoader.getSea0());
        } else {
            landView = new ImageView(TextureLoader.getSea1());
        }
        landView.setPreserveRatio(true);
        landView.setFitWidth(TILE_SIZE);
        landView.setFitHeight(TILE_SIZE);
        this.getChildren().remove(mapImageView[position.yPos][position.xPos]);
        this.getChildren().add(landView);
        mapImageView[position.yPos][position.xPos] = landView;
        GridPane.setConstraints(landView, position.xPos * 2 + 1, position.yPos * 2 + 1);
        final int newX = position.xPos, newY = position.yPos;
        landView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> onTileClicked(event, newX, newY));
        getMapEditorViewController().setSaveButtonDisabled(!MapCheckUtil.checkMapValidity(this.booleanMap));
    }

    public MapBlackWhite getBooleanMap() {
        return booleanMap;
    }

    public void setTileCountLabel(Label showUsedTiles) {
        this.showUsedTilesLabel = showUsedTiles;
        
    }
}
