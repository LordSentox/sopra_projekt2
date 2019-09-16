package de.sopra.javagame.view.customcontrol;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

public class MapPaneTile extends StackPane{
    private final int PLAYERSIZE = 65;
    
    private TileView base;
    private List<PlayerType> playersOnTile = new LinkedList<>();
    private GridPane fourPlayerPane= new GridPane();
    
    public MapPaneTile (TileView base){
        this.getChildren().add(base);
        this.getChildren().add(fourPlayerPane);
        this.base = base;
        this.initGridPane();
    }
    
    public MapPaneTile (ImageView base) {
        this.getChildren().add(base);
        this.base = null;
    }
    
    public void putPlayer(PlayerType type){
        playersOnTile.add(type);
        this.refreshGridPane();
    }
    public void unputPlayer(PlayerType type){
        playersOnTile.remove(type);
        this.refreshGridPane();
    }
    public void refreshGridPane(){
        fourPlayerPane.getChildren().clear();
        AtomicInteger x = new AtomicInteger(0), y = new AtomicInteger(0);
        playersOnTile.forEach(type -> {
            PlayerImageView view = new PlayerImageView(type, TextureLoader.getPlayerCardTexture(type));
            view.setPreserveRatio(true);
            view.setFitHeight(110);
            fourPlayerPane.getChildren().add(view);
            GridPane.setConstraints(view, x.get(), y.get());
            if(!x.compareAndSet(0, 1))
                if(!y.compareAndSet(0, 1))
                    if(!x.compareAndSet(1, 0))
                        if(!y.compareAndSet(1, 0));            
        });
    }
    public void initGridPane(){
        IntStream.range(0, 2).forEach(i -> fourPlayerPane.getColumnConstraints().add(new ColumnConstraints(PLAYERSIZE)));
        IntStream.range(0, 2).forEach(i -> fourPlayerPane.getRowConstraints().add(new RowConstraints(PLAYERSIZE)));
    }

    public TileView getBase() {
        return base;
    }

    public List<PlayerType> getPlayers() {
        return playersOnTile;
    }

}
