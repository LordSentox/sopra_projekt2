package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.view.textures.TextureLoader;

public class ArtifactCardView extends CardView {
    private ArtifactCardType type;
    public ArtifactCardView(ArtifactCardType type, int size) {
        super(TextureLoader.getArtifactCardTexture(type), TextureLoader.getArtifactCardBack(), size);
        this.type = type;
    }
    public ArtifactCardType getType() {
        return type;
    }
    
}
