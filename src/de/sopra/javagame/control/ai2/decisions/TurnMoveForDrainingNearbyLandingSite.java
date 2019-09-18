package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_LANDING_SITE_IS_FLOODED;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_MORE_THAN_1_ACTION_LEFT;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = TURN_ACTION, value = TurnStayOnLandingSiteWaitingForDeparture.class)
@PreCondition(allTrue = {GAME_LANDING_SITE_IS_FLOODED, PLAYER_HAS_MORE_THAN_1_ACTION_LEFT})
public class TurnMoveForDrainingNearbyLandingSite extends Decision {

    private Point move;

    /**
     * Prüfe: ist der Spieler einen Schritt entfernt, um den Landeplatz trocken legen zu können
     * kann der Spieler innerhalb seines Zuges trockenlegen
     */
    @Override
    public Decision decide() {
        Point landingSitePosition = control.getTile(PlayerType.PILOT).getLeft();
        Point playerPosition = player().getPosition();
        PlayerType playerType = player().getType();

        List<Pair<Point, Point>> drainablePositionslist = control.getDrainablePositionsOneMoveAway(playerPosition, playerType);
        for (Pair<Point, Point> possiblePosition : drainablePositionslist) {
            if (possiblePosition.getRight().equals(landingSitePosition)) {
                move = possiblePosition.getLeft();
                return this;
            }
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        if (player().getType() == PlayerType.PILOT && needSpecialToMove(player().getPosition(), move)) {
            return startActionQueue().pilotFlyTo(move);
        } else if (player().getType() == PlayerType.DIVER && needSpecialToMove(player().getPosition(), move)) {
            return startActionQueue().diverDiveTo(move);
        } else {
            return startActionQueue().move(move);
        }
    }

}
