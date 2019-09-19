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
    private boolean tradeable;
    private PlayerType ownerType;
    private ActionPicker ap;

    public ArtifactCardView(ArtifactCardType type, int size, int index) {
        super(TextureLoader.getArtifactCardTexture(type), TextureLoader.getArtifactCardBack(), size);
        this.type = type;
        this.handCardIndex = index;
        this.selected = false;
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        this.ap = new ActionPicker(this, MouseButton.NONE);
        ap.setCardIndex(handCardIndex);
        ap.setArtifactCardType(type);
    }

    public ArtifactCardType getType() {
        return type;
    }

    public int getHandCardIndex() {
        return handCardIndex;
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

    void setSelected(boolean selected) {
        this.selected = selected;
        updateHighlight();
    }

    public void setOwner(PlayerType owner) {
        this.ownerType = owner;
    }

    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    public boolean isTradeable() {
        return tradeable;
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
        System.out.println("DEBUG: "+this.handCardIndex + "+" + this.getType() + "#" + this.ownerType);
        if (isFrontShown()) {
            if (!(handCardIndex == -1)) {
                if (tradeable && type.isTransferable()) {
                    controller.getGameWindow().getControllerChan().getActivePlayerController().transferCard(handCardIndex, controller.getTargetPlayer().getType());
                    controller.resetTargetPlayer();
                    controller.setTransferActive(false);
                    return;
                }
                ap.setDelegatingPlayer(ownerType);
                controller.setTargetPlayer(ownerType);
                List<ActionButton> buttons = new LinkedList<>();
                if (type.equals(ArtifactCardType.HELICOPTER) || type.equals(ArtifactCardType.SANDBAGS))
                    buttons.add(ActionButton.PLAY_CARD);
                if (controller.getGameWindow().getControllerChan().getCurrentAction().getPlayer(ownerType).getHand().size() >= 5)
                    buttons.add(ActionButton.DISCARD);
                if (buttons.size() > 0) {
                    ap.init(buttons.toArray(new ActionButton[buttons.size()]));
                    ap.show(event);
                }
            }

        }
    }

    @Override
    public Node node() {
        return this;
    }
}
