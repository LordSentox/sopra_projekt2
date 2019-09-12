package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.Point;

import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnMovePlayersTowardsOtherPlayersTheyCanGiveCardsTo.class)
public class TurnMoveIfMovingCouldDrainTwoTiles extends Decision {

    private final int TWO_POSITIONS = 2;

    @Override
    public Decision decide() {
        if (!hasValidActions(3)) {
            return null;
        }
        Player activePlayer = control.getActivePlayer();
        if (!(activePlayer.drainablePositions().size() < TWO_POSITIONS)) {
            return null;
        }
        List<Point> drainablePositionsOneMoveAway = control.getDrainablePositionsOneMoveAway(
                activePlayer.getPosition(), activePlayer.getType());
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
