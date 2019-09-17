package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.view.abstraction.Highlightable;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.scene.Node;

public class FloodCardView extends CardView implements Highlightable {
    public FloodCardView(MapTileProperties properties, int size) {
        super(TextureLoader.getFloodCardTexture(properties), TextureLoader.getFloodCardBack(), size);
    }

    @Override
    public Node node() {
        return this;
    }
}
