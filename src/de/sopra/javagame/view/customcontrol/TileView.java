package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTileState;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileView extends ImageView{
    
    private final Image dryImage, floodedImage, goneImage;
    
    public TileView(int id, String pack) {
        this.dryImage = new Image(getClass().getResource("/textures/" + pack + "/island_" + (id < 9 ? "0"+id : ""+id) + ".png").toExternalForm(), 130, 130, true, true);
        this.floodedImage = new Image(getClass().getResource("/textures/" + pack + "/floodedisland_" + (id < 9 ? "0"+id : ""+id) + ".png").toExternalForm(), 130, 130, true, true);
        this.goneImage = new Image(getClass().getResource("/textures/" + pack + "/floodedisland_additional_3.png").toExternalForm(), 130, 130, true, true);
        this.setImage(dryImage);
    }
    
    public void showImage(MapTileState mapTileState){
        switch (mapTileState) {
        case DRY: this.setImage(dryImage);
            break;
        case FLOODED: this.setImage(floodedImage);
            break;
        case GONE: this.setImage(goneImage);
            break;

        }
        
    }
}
