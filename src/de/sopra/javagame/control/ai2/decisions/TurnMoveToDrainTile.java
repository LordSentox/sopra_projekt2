package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnMoveToDrainCorePositions.class)
public class TurnMoveToDrainTile extends Decision {
    private Point move;
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        if (!activePlayer.drainablePositions().isEmpty()) {
            return null;
        }
        List<Pair<Point,Point>> drainablePositionsOneMoveAway = control.getDrainablePositionsOneMoveAway(activePlayer.getPosition(),
                activePlayer.getType());
        if (!drainablePositionsOneMoveAway.isEmpty()) {
            move = drainablePositionsOneMoveAway.get(0).getLeft();
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().move(move);
    }

}
