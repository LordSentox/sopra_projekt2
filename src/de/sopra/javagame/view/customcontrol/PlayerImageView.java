package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.player.Pilot;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.abstraction.Highlightable;
import de.sopra.javagame.view.customcontrol.ActionPicker.ActionButton;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.LinkedList;
import java.util.List;

public class PlayerImageView extends ImageView implements EventHandler<MouseEvent>, Highlightable {

    private final PlayerType type;
    private final ActionPicker picker;
    private final MapPaneTile tile;

    public PlayerImageView(MapPaneTile tile, PlayerType type, Image image) {
        super(image);
        this.type = type;
        this.tile = tile;
        this.picker = new ActionPicker(this, MouseButton.NONE);
    }

    public PlayerType getType() {
        return type;
    }

    private boolean shallShowSpecial() {
        Player activePlayer = tile.getControl().getGameWindow().getControllerChan().getCurrentAction().getActivePlayer();
        if (activePlayer.getType() != type) {
            //wenn der gewählte Spieler nicht der aktive ist und der aktive der Navigator ist
            return activePlayer.getType() == PlayerType.NAVIGATOR && tile.getControl().isSpecialActive();
        }
        switch (activePlayer.getType()) {
            case COURIER:
            case DIVER:
            case ENGINEER:
            case EXPLORER:
                return true;
            case PILOT:
                return ((Pilot) activePlayer).hasSpecialMove();
        }
        return false;
    }

    @Override
    public void handle(MouseEvent event) {
        Player activePlayer = tile.getControl().getGameWindow().getControllerChan().getCurrentAction().getActivePlayer();
        boolean hasSpecial = shallShowSpecial();
        List<ActionButton> buttons = new LinkedList<>();
        //Mit der linken Maustaste soll die Special deaktiviert und
        if (event.getButton() == MouseButton.PRIMARY && activePlayer.getType() == type) {
            tile.getControl().getGameWindow().getControllerChan().getInGameUserController().showMovements(type, false);
            tile.getControl().getGameWindow().getControllerChan().getActivePlayerController().showDrainOptions();
            tile.getControl().setSpecialActive(false);
        } else {

            if (event.getButton() == MouseButton.SECONDARY) {
                //wenn der Spieler der aktive Spieler ist
                if (activePlayer.getType() == type) {
                    if (tile.canCollectTreasure(type))
                        buttons.add(ActionButton.COLLECT_ARTIFACT);
                } else {
                    //Player clickedPlayer = tile.getControl().getGameWindow().getControllerChan().getCurrentAction().getPlayer(type);
                    if (tile.getPlayers().size() >= 2 || activePlayer.getType() == PlayerType.COURIER){
                        buttons.add(ActionButton.GIVE_CARD);
                        picker.setMapPaneTile(tile);
                        picker.setMovingPlayer(type);;
                        picker.setDelegatingPlayer(tile.getControl().getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType());
                    }
                }
                if (hasSpecial)
                    buttons.add(ActionButton.SPECIAL);

                if (buttons.size() > 0) {
                    picker.setDelegatingPlayer(activePlayer.getType());
                    picker.setMapPaneTile(tile);
                    picker.setMovingPlayer(type);
                    picker.init(buttons.toArray(new ActionButton[0]));
                    picker.show(event);
                }
            }
        }
    }


    @Override
    public Node node() {
        return this;
    }
}
