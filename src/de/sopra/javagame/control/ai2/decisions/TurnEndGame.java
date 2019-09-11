package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;

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
        EnumSet<ArtifactType> discoveredArtifacts = turn().getDiscoveredArtifacts();

        if (discoveredArtifacts.size() != FOUR_ARTIFACTS) {
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
