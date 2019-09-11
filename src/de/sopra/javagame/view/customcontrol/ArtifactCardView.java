package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.util.TextureLoader;

public class ArtifactCardView extends CardView {
    public ArtifactCardView(ArtifactCardType type, int size) {
        super(TextureLoader.getArtifactCardTexture(type), TextureLoader.getArtifactCardBack(), size);
    }
}
