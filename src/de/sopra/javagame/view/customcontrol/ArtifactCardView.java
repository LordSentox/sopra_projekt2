package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.InGameViewController;
import de.sopra.javagame.view.abstraction.Highlightable;
import de.sopra.javagame.view.customcontrol.ActionPicker.ActionButton;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.LinkedList;
import java.util.List;

public class ArtifactCardView extends CardView implements EventHandler<MouseEvent>, Highlightable {

    private ArtifactCardType type;
    private int handCardIndex;
    private InGameViewController controller;
    private boolean selected;
    private PlayerType ownerType;
    private ActionPicker ap;

    public ArtifactCardView(ArtifactCardType type, int size, int index) {
        super(TextureLoader.getArtifactCardTexture(type), TextureLoader.getArtifactCardBack(), size);
        this.type = type;
        this.handCardIndex = index;
        this.selected = false;
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        this.ap = new ActionPicker(this, MouseButton.NONE);
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

    public void setOwner(PlayerType owner) {
        this.ownerType = owner;
    }

    public void setInGameViewController(InGameViewController igvc) {
        this.controller = igvc;
        this.ap.setInGameViewController(igvc);
    }

    public InGameViewController getInGameViewController() {
        return this.controller;
    }

    @Override
    public void handle(MouseEvent event) {
        if (isFrontShown()) {
            selected = !selected;
            updateHighlight();
            if(!(handCardIndex == -1)){
            if (!(handCardIndex == -1)) {
                ap.setCardIndex(handCardIndex);
                ap.setArtifactCardType(this.getType());
                ap.setDelegatingPlayer(ownerType);
                controller.setTargetPlayer(ownerType);
                List<ActionButton> buttons = new LinkedList<>();
                if (type.equals(ArtifactCardType.HELICOPTER) || type.equals(ArtifactCardType.SANDBAGS))
                    buttons.add(ActionButton.PLAY_CARD);
                if (buttons.size() > 0) {
                    ap.init(buttons.toArray(new ActionButton[buttons.size()]));
                    ap.show(event);
                    }
                }
            }
        }
    }
    @Override
    public Node node() {
        return this;
    }
}
