package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.view.textures.TextureLoader;

public class FloodCardView extends CardView {
    public FloodCardView(MapTileProperties properties, int size) {
        super(TextureLoader.getFloodCardTexture(properties), TextureLoader.getFloodCardBack(), size);
    }
}
