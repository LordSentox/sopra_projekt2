package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai2.DecisionResult.PLAY_SPECIAL_CARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.*;
import static de.sopra.javagame.model.MapTileState.FLOODED;
import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds, Julius Korweck
 * @version 11.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = PLAY_SPECIAL_CARD, value = SpecialUseSandbagToDrainLandingSite.class)
@PreCondition(allTrue = {PLAYER_HAS_SANDBAGS_CARD, PLAYER_NO_ACTION_LEFT, GAME_ANY_LAST_TEMPLE_IN_DANGER})
//FIXME ist GAME_ANY_LAST_TEMPLE_IN_DANGER richtig?
public class SpecialUseSandbagToDrainOrphanedTempleMapTile extends Decision {

    @Override
    public Decision decide() {
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
            return this;
        }
        return null;
    }

    //alte version
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

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
