package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
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

        if(hasValidActions(0)){
            return null;
        }

        Point activePlayerPosition = player().getPosition();

        List<Pair<Point, MapTile>> templeList = control.getTemples();

        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        for (Pair<Point, MapTile> temple : templeList) {

            Point orphanedTemplePoint = temple.getLeft();
            MapTile orphanedTemple = temple.getRight();

            if(!activePlayerPosition.equals(orphanedTemplePoint)){
                return null;
            }

            if (orphanedTemple.getState() != MapTileState.FLOODED) {
                continue;
            }

            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }

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

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
