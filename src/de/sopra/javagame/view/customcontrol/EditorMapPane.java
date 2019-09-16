package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.*;
import de.sopra.javagame.view.InGameViewController;
import de.sopra.javagame.view.MapEditorViewController;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;

public class EditorMapPane extends GridPane {
    //Dieses pane ist besser als alle anderen

    private static final int TILE_SIZE = 130;
    private final StackPane[][] map;
    private MapEditorViewController mapEditorViewController;
    private MapBlackWhite booleanMap;


    public EditorMapPane() throws IOException {
        super();
        map = new StackPane[Map.SIZE_Y][Map.SIZE_X];

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
                GridPane.setConstraints(v, x * 2 + 1, y * 2 + 1);
                final int newX = x, newY = y;
                v.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> onTileClicked(event, newX, newY));
            }
        }
    }

    private void onTileClicked(MouseEvent e, int x, int y) {
        if (e.getButton() == MouseButton.PRIMARY && !booleanMap.get(x, y)) {
            setBooleanTile(new Point(x, y), true);
        } else if (e.getButton() == MouseButton.SECONDARY && booleanMap.get(x, y)) {
            setBooleanTile(new Point(x, y), false);
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
        this.getChildren().add(landView);
        GridPane.setConstraints(landView, position.xPos * 2 + 1, position.yPos * 2 + 1);
        getMapEditorViewController().setSaveButtonDisabled(!MapCheckUtil.checkMapValidity(this.booleanMap));
    }

    public MapBlackWhite getBooleanMap() {
        return booleanMap;
    }
}
