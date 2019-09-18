package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
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
    private Point move;
    private Point firstDrain;
    private Point secondDrain;

    @Override
    public Decision decide() {
        if (!hasValidActions(3)) {
            return null;
        }
        Player activePlayer = control.getActivePlayer();
        if (!(activePlayer.drainablePositions().size() < TWO_POSITIONS)) {
            return null;
        }
        List<Pair<Point, Point>> drainablePositionsOneMoveAway = control.getDrainablePositionsOneMoveAway(
                activePlayer.getPosition(), activePlayer.getType());
        if (drainablePositionsOneMoveAway.size() >= TWO_POSITIONS) {
            for (Pair<Point, Point> path : drainablePositionsOneMoveAway) {
                for (Pair<Point, Point> path2 : drainablePositionsOneMoveAway) {
                    if (path.getLeft().equals(path2.getLeft()) && !path.getRight().equals(path2.getRight())) {
                        move = path.getLeft();
                        firstDrain = path.getRight();
                        secondDrain = path2.getRight();
                    }
                }
            }
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        if (player().getType() == PlayerType.PILOT && needSpecialToMove(player().getPosition(), move)) {
            return startActionQueue().pilotFlyTo(move).drain(firstDrain).drain(secondDrain);
        } else if (player().getType() == PlayerType.DIVER && needSpecialToMove(player().getPosition(), move)) {
            return startActionQueue().diverDiveTo(move).drain(firstDrain).drain(secondDrain);
        } else if (player().getType() == PlayerType.ENGINEER) {
            return startActionQueue().move(move).engineersDrain(firstDrain).engineersDrain(secondDrain);
        } else {
            return startActionQueue().move(move).drain(firstDrain).drain(secondDrain);
        }
    }

}
