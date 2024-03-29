package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_HAS_ALL_ARTIFACTS;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = TURN_ACTION, value = TurnFlyActivePlayerToLandingSiteForDraining.class)
@PreCondition(allTrue = GAME_HAS_ALL_ARTIFACTS)
public class TurnMoveToLandingSiteForDeparture extends Decision {
    private Point moveTowards;

    @Override
    public Decision decide() {
        Action action = control.getCurrentAction();

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();

        Player player = action.getActivePlayer();
        Point playerPosition = player.getPosition();
        if (!landingSitePosition.equals(playerPosition)) {
            moveTowards = control.getClosestPointInDirectionOf(player().legalMoves(true), control.getTile(PlayerType.PILOT).getLeft(), player().getType());
            return this;
        }

        return null;
    }

    @Override
    public ActionQueue act() {
        if (player().getType() == PlayerType.PILOT && needSpecialToMove(player().getPosition(), moveTowards)) {
            return startActionQueue().pilotFlyTo(moveTowards);
        } else if (player().getType() == PlayerType.DIVER && needSpecialToMove(player().getPosition(), moveTowards)) {
            return startActionQueue().diverDiveTo(moveTowards);
        } else {
            return startActionQueue().move(moveTowards);
        }
    }

}
