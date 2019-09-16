package de.sopra.javagame.view.customcontrol;

import java.util.LinkedList;
import java.util.List;

import de.sopra.javagame.model.player.Pilot;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.customcontrol.ActionPicker.ActionButton;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PlayerImageView extends ImageView implements EventHandler<MouseEvent>{
    
    private PlayerType type;
    private ActionPicker picker;
    private MapPaneTile tile;
    
    public PlayerImageView( MapPaneTile tile, PlayerType type, Image image){
        super(image);
        this.type = type;
        this.tile = tile;
        this.picker = new ActionPicker(this, MouseButton.NONE);
    }

    public PlayerType getType() {
        return type;
    }

    @Override
    public void handle(MouseEvent event) {
        Player activePlayer = tile.getControl().getGameWindow().getControllerChan().getCurrentAction().getActivePlayer();
        boolean hasSpecial = activePlayer.getType() != PlayerType.PILOT || ((Pilot) activePlayer).hasSpecialMove();
        List<ActionButton> buttons = new LinkedList<>();
        if(event.getButton() == MouseButton.PRIMARY) {
            tile.getControl().getGameWindow().getControllerChan().getActivePlayerController().showMovements(false);
            tile.getControl().getGameWindow().getControllerChan().getActivePlayerController().showDrainOptions();
            tile.getControl().setSpecialActive(false);
        }
        else { 
            
            if(event.getButton() == MouseButton.SECONDARY) {
                //wenn der Spieler der aktive Spieler ist
                if(activePlayer.getType() == type) {
                    if(tile.canCollectTreasure(type))
                        buttons.add(ActionButton.COLLECT_ARTIFACT);
                    if(hasSpecial)
                        buttons.add(ActionButton.SPECIAL); 
                }
                else {
                    Player clickedPlayer = tile.getControl().getGameWindow().getControllerChan().getCurrentAction().getPlayer(type);
                    if(tile.getPlayers().contains(activePlayer.getType()) || activePlayer.getType() == PlayerType.COURIER)
                        buttons.add(ActionButton.GIVE_CARD); 
                    if(activePlayer.getType() == PlayerType.NAVIGATOR)
                        buttons.add(ActionButton.SPECIAL);
                }
           
                if(buttons.size() > 0) {
                    picker.setDelegatingPlayer(activePlayer.getType());
                    picker.setMapPaneTile(tile);
                    picker.setMovingPlayer(type);
                    picker.init(buttons.toArray(new ActionButton[buttons.size()]));
                    picker.show(event); 
                }
            }
        }
    }
    

}
