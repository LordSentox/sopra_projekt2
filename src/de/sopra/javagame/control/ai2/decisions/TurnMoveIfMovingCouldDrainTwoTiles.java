package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.player.Player;
/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
public class TurnMoveIfMovingCouldDrainTwoTiles extends Decision {
    @Override
    public Decision decide() {
        if(!hasValidActions(3)) {
            return null;
        }
        Player activePlayer = control.getActivePlayer();
        if(!(activePlayer.drainablePositions().size()<2)) {
            return null;
        }
            if(control.getDrainablePositionsOneMoveAway(activePlayer.getPosition(),
               activePlayer.getType()).size()>1){
                return this;
            }
        return null;
    }
    
    @Override
    public void act() {
        //TODO
    }
}
