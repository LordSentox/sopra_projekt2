package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.player.PlayerType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerImageView extends ImageView{
    
    private PlayerType type;
    
    public PlayerImageView( PlayerType type, Image image){
        super(image);
        this.type = type;
    }

    public PlayerType getType() {
        return type;
    }

    

}
