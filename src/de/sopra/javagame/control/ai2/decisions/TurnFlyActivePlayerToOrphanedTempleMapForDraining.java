package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.MapTileState.FLOODED;
import static de.sopra.javagame.model.MapTileState.GONE;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds, Julius Korweck
 * @version 11.09.2019
 * @since 10.09.2019
 */
public class TurnFlyActivePlayerToOrphanedTempleMapForDraining extends Decision {

    @Override
    public Decision decide() {
        if (!control.anyPlayerHasCard(ArtifactCardType.HELICOPTER)) {
            return null;
        }

        if (!hasValidActions(1)) {
            return null;
        }

        List<Pair<Point, MapTile>> templeList = control.getTemples();

        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        for (Pair<Point, MapTile> temple : templeList) {
            Point orphanedTemplePoint = temple.getLeft();

            List<Point> surroundingPoints = surroundingPoints(orphanedTemplePoint, true);

            List<MapTile> surroundingTiles = surroundingPoints.stream().map(control::getTile).collect(Collectors.toList());

            //wenn eins nicht GONE ist
            if (!checkAll(tile -> tile.getState() == GONE, surroundingTiles)) {
                continue;
            }

//            if (!((northernNeighbour == null || northernNeighbour.getState() == MapTileState.GONE)
//                    && (northEasternNeighbour == null || northEasternNeighbour.getState() == MapTileState.GONE)
//                    && (easternNeighbour == null || easternNeighbour.getState() == MapTileState.GONE)
//                    && (southEasternNeighbour == null || southEasternNeighbour.getState() == MapTileState.GONE)
//                    && (southernNeighbour == null || southernNeighbour.getState() == MapTileState.GONE)
//                    && (southWesternNeighbour == null || southWesternNeighbour.getState() == MapTileState.GONE)
//                    && (westernNeighbour == null || westernNeighbour.getState() == MapTileState.GONE)
//                    && (northWesternNeighbour == null || northWesternNeighbour.getState() == MapTileState.GONE))) {
//                continue;
//            }

            return this;
        }
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
