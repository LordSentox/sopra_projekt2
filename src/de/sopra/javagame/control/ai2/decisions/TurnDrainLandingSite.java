package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_LANDING_SIDE_IN_DANGER;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = TURN_ACTION, value = TurnEndGame.class)
@PreCondition(allTrue = GAME_LANDING_SIDE_IN_DANGER)
public class TurnDrainLandingSite extends Decision {

    @Override
    public Decision decide() {

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();

        List<Point> drainablePositions = player().drainablePositions();
        //pürfe, ob der aktive Spieler die LandingSite trockenlegen kann
        if (drainablePositions.contains(landingSitePosition)) {
            return this;
        }

        return null;
    }

    @Override
    public void act() {
        //TODO
    }

}
