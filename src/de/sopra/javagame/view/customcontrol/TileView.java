package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileView extends ImageView {

    private final Image dryImage, floodedImage, goneImage;

    public TileView(int id, int size) {
        //System.out.println(id);
        this.dryImage = TextureLoader.getTileTextureDry(MapTileProperties.getByIndex(id));
        this.floodedImage = TextureLoader.getTileTextureFlooded(MapTileProperties.getByIndex(id));
        this.goneImage = TextureLoader.getGone();
        this.setImage(dryImage);
        setPreserveRatio(true);
        setFitHeight(size);
        setFitWidth(size);
    }

    public void showImage(MapTileState mapTileState) {
        switch (mapTileState) {
            case DRY:
                this.setImage(dryImage);
                break;
            case FLOODED:
                this.setImage(floodedImage);
                break;
            case GONE:
                this.setImage(goneImage);
                break;

        }

    }
}
