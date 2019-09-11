package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.player.Player;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class TurnDrainTile extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        if(activePlayer.drainablePositions().size()>0) {
            return this;
        }
        return null;
    }
    
    @Override
    public void act() {
        //TODO
    }    
}
