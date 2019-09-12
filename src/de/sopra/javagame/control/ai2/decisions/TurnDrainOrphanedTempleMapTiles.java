package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
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
 * @since 09.09.2019
 */
public class TurnDrainOrphanedTempleMapTiles extends Decision {

    @Override
    public Decision decide() {

        //TODO prüfe ob aktiver Spieler auf Tempel steht

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

            //old way of if above
//            if (!((northernNeighbour == null || northernNeighbour.getState() == GONE)
//                    && (northEasternNeighbour == null || northEasternNeighbour.getState() == GONE)
//                    && (easternNeighbour == null || easternNeighbour.getState() == GONE)
//                    && (southEasternNeighbour == null || southEasternNeighbour.getState() == GONE)
//                    && (southernNeighbour == null || southernNeighbour.getState() == GONE)
//                    && (southWesternNeighbour == null || southWesternNeighbour.getState() == GONE)
//                    && (westernNeighbour == null || westernNeighbour.getState() == GONE)
//                    && (northWesternNeighbour == null || northWesternNeighbour.getState() == GONE))) {
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
