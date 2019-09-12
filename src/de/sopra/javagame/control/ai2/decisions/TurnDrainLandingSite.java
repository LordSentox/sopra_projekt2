package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class TurnDrainLandingSite extends Decision {

    @Override
    public Decision decide() {

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();
        Point landingSitePosition = informationLandingSite.getLeft();

        List<Point> drainablePositions = player().drainablePositions();

        if (drainablePositions.contains(landingSitePosition)
                && landingSite.getState().equals(MapTileState.FLOODED)) {
            return this;
        }

        return null;
    }

    @Override
    public void act() {
        //TODO
    }

}
