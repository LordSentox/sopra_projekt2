package de.sopra.javagame.view.customcontrol;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardView extends ImageView {
    
    private final Image frontImage, backImage;
    private boolean frontShown = false;


    protected CardView(Image frontImage, Image backImage, int size){
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.showBackImage();
        this.getStyleClass().add("CardView");
        setPreserveRatio(true);
        setFitWidth(size);
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
