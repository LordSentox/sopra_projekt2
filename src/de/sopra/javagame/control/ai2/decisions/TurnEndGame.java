package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_HAS_ALL_ARTIFACTS;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 11.09.2019
 * @since 11.09.2019
 */
public class TurnEndGame extends Decision {

    private final int FOUR_ARTIFACTS = 4;

    @Override
    public Decision decide() {

        if (condition(GAME_HAS_ALL_ARTIFACTS).isFalse(this)) {
            return null;
        }

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();

        List<Player> allPlayers = control.getAllPlayers();

        for (Player player : allPlayers) {
            Point playerPosition = player.getPosition();
            if (!playerPosition.equals(landingSitePosition)) {
                return null;
            }
        }

        return this;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
