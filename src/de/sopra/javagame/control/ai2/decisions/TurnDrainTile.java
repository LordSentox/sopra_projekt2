package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.player.Player;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */

@DoAfter(act = TURN_ACTION, value = TurnMoveIfMovingCouldDrainTwoTiles.class)
public class TurnDrainTile extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        if (!activePlayer.drainablePositions().isEmpty()) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
