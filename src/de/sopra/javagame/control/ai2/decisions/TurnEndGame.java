package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_HAS_ALL_ARTIFACTS;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 11.09.2019
 * @since 11.09.2019
 */

@DoAfter(act = TURN_ACTION, value = Decision.class)
@PreCondition(allTrue = GAME_HAS_ALL_ARTIFACTS)
public class TurnEndGame extends Decision {
    private EnumSet<PlayerType> people;
    @Override
    public Decision decide() {

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();

        List<Player> allPlayers = control.getAllPlayers();

        for (Player player : allPlayers) {
            people.add(player.getType());
            Point playerPosition = player.getPosition();
            if (!playerPosition.equals(landingSitePosition)) {
                return null;
            }
        }

        return this;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().finishTheGame(control.getTile(PlayerType.PILOT).getLeft(),people);
    }

}
