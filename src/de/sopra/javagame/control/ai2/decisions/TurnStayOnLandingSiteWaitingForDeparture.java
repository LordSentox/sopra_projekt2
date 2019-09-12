package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds, Julius Korweck
 * @version 11.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnDrainLandingSite.class)
@PreCondition( allTrue= Condition.GAME_HAS_ALL_ARTIFACTS)
public class TurnStayOnLandingSiteWaitingForDeparture extends Decision {

 
    @Override
    public Decision decide() {
       
        Point playerPosition = player().getPosition();

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();

        if (landingSitePosition.equals(playerPosition)) {
            return this;

        }
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }


}
