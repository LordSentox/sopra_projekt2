package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

public class TurnFlyActivePlayerToLandingSiteForDraining extends Decision {

    @Override
    public Decision decide() {
        if (!control.anyPlayerHasCard(ArtifactCardType.HELICOPTER)) {
            return null;
        }

        if (!hasValidActions(1)) {
            return null;
        }

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();
        Point landingSitePosition = informationLandingSite.getLeft();

        if (landingSite.getState() != MapTileState.FLOODED) {
            return null;
        }

        if (player().drainablePositions().contains(landingSitePosition)) {
            return null;
        }

        return this;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}