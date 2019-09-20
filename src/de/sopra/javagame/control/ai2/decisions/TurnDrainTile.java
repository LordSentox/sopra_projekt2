package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;

import java.util.List;

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
    private Point drainTile;

    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        List<Point> drainable = activePlayer.drainablePositions();
        if (!drainable.isEmpty()) {
            drainTile = drainable.get(0);
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        if (player().getType() != PlayerType.ENGINEER) {
            return startActionQueue().drain(drainTile);
        }
        return startActionQueue().engineersDrain(drainTile);
    }

}
