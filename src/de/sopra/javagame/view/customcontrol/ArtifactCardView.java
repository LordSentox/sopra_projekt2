package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ArtifactCardView extends CardView implements EventHandler<MouseEvent> {

    private ArtifactCardType type;

    private boolean selected;

    public ArtifactCardView(ArtifactCardType type, int size) {
        super(TextureLoader.getArtifactCardTexture(type), TextureLoader.getArtifactCardBack(), size);
        this.type = type;
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, this);
    }

    public ArtifactCardType getType() {
        return type;
    }

    public void updateHighlight() {
        if (selected) {
            this.getStyleClass().add("highlightmapTile");
        } else {
            this.getStyleClass().removeIf(s -> s.equals("highlightmapTile"));
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void handle(MouseEvent event) {
        selected = !selected;
        updateHighlight();
    }

}
