package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.view.abstraction.Highlightable;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class ArtifactCardView extends CardView implements EventHandler<MouseEvent>, Highlightable {

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
            highlight();
        } else {
            dehighlight();
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

    @Override
    public Node node() {
        return this;
    }
}
