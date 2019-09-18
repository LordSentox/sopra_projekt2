package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.view.abstraction.Highlightable;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileView extends ImageView implements Highlightable {

    private final Image dryImage, floodedImage, goneImage;

    private final MapTileProperties type;

    public TileView(MapTile tile, int size) {
        //System.out.println(id);
        type = tile.getProperties();
        this.dryImage = TextureLoader.getTileTextureDry(type);
        this.floodedImage = TextureLoader.getTileTextureFlooded(type);
        this.goneImage = TextureLoader.getGone();
        this.setImage(TextureLoader.getTileTexture(tile));
        setPreserveRatio(true);
        setFitHeight(size);
        setFitWidth(size);
    }

    public TileView(int id, int size) {
        //System.out.println(id);
        type = MapTileProperties.getByIndex(id);
        this.dryImage = TextureLoader.getTileTextureDry(type);
        this.floodedImage = TextureLoader.getTileTextureFlooded(type);
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

    @Override
    public Node node() {
        return this;
    }

}
