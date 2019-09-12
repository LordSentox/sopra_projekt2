package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 10.09.2019
 * @since 10.09.2019
 */

public class SpecialFlyOutOrphanedPlayers extends Decision {

    @Override
    public Decision decide() {
        List<Player> allPlayers = control.getAllPlayers();

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();

        for (Player player : allPlayers) {
            if (player.getPosition().equals(landingSitePosition)) {
                return null;
            }
            if (player.legalMoves(true).isEmpty()) {
                return this;
            }
        }
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
