package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds, Julius Korweck
 * @version 11.09.2019
 * @since 09.09.2019
 */
public class TurnStayOnLandingSiteWaitingForDeparture extends Decision {

    private final int FOUR_ARTIFACTS = 4;

    @Override
    public Decision decide() {
        Action action = control.getCurrentAction();
        EnumSet<ArtifactType> discoveredArtifacts = action.getDiscoveredArtifacts();

        if (discoveredArtifacts.size() != FOUR_ARTIFACTS) {
            return null;
        }

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
