package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileView extends ImageView {

    private final Image dryImage, floodedImage, goneImage;

    private final MapTileProperties type;

    public TileView(int id, int size) {
        //System.out.println(id);
        type = MapTileProperties.getByIndex(id);
        this.dryImage = TextureLoader.getTileTextureDry(type);
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

    public MapTileProperties getType() {
        return type;
    }

    public void highlight() {
        if (!this.getStyleClass().contains("highlightmapTile"))
            this.getStyleClass().add("highlightmapTile");
    }

    public void deHighlight() {
        this.getStyleClass().removeIf(s -> s.equals("highlightmapTile"));
    }
}
