package de.sopra.javagame.view.customcontrol;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardView extends ImageView{
    
    private final Image frontImage, backImage;
    private boolean frontShown = false;
    
    
    public CardView(String pack, String frontImage, String backImage, int size){
        this.frontImage = new Image(getClass().getResource("/textures/" + pack + "/" + frontImage).toExternalForm(), size, 0, true, true);
        this.backImage = new Image(getClass().getResource("/textures/" + pack + "/" + backImage).toExternalForm(), size, 0, true, true);
        this.showBackImage();
        this.getStyleClass().add("CardView");
    }
    
    public void showFrontImage(){
        this.setImage(frontImage);
        this.frontShown = true;
    }
    
    public void showBackImage(){
        this.setImage(backImage);
        this.frontShown = false;
    }
    
    public boolean isFrontShown() {
        return frontShown;
    }
    
}
