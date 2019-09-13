package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
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

    /**
     * Prüfe: ist der Spieler einen Schritt entfernt, um den Landeplatz trocken legen zu können
     * kann der Spieler innerhalb seines Zuges trockenlegen
     */

    @Override
    public Decision decide() {

        Point landingSitePosition = control.getTile(PlayerType.PILOT).getLeft();   
        Point playerPosition = player().getPosition();
        PlayerType playerType = player().getType();
        List<Point> drainablePositionslist = control.getDrainablePositionsOneMoveAway(playerPosition, playerType);

        if (drainablePositionslist.contains(landingSitePosition)) {
            return this;
        }    
        
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
