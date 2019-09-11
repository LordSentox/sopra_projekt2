package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.Point;

import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
public class TurnMoveToDrainTile extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        if (!activePlayer.drainablePositions().isEmpty()) {
            return null;
        }
        List<Point> drainablePositionsOneMoveAway = control.getDrainablePositionsOneMoveAway(activePlayer.getPosition(),
                activePlayer.getType());
        if (!drainablePositionsOneMoveAway.isEmpty()) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
