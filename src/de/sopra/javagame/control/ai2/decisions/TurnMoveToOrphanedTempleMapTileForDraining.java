package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
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
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

public class TurnMoveToOrphanedTempleMapTileForDraining extends Decision {

    @Override
    public Decision decide() {

        if (hasValidActions(0, 1)) {
            return null;
        }

        PlayerType activePlayerType = player().getType();
        Point activePlayerPosition = player().getPosition();

        //Nur für Diver und Pilot relevant
        if (none(activePlayerType == PlayerType.DIVER, activePlayerType == PlayerType.PILOT)) {
            return null;
        }

        List<Pair<Point, MapTile>> templeList = control.getTemples();
        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        for (Pair<Point, MapTile> temple : templeList) {
            Point orphanedTemplePoint = temple.getLeft();
            MapTile orphanedTemple = temple.getRight();
            //prüfe, ob Player auf betroffenem Tempel steht
            if(orphanedTemplePoint.equals(activePlayerPosition)){
                return null;
            }
            //prüfe, ob Tempelatefakt bereits geborgen ist
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }

            List<Point> inOneMovedrainablePositionslist = control.getDrainablePositionsOneMoveAway(orphanedTemplePoint, activePlayerType);
            if (!inOneMovedrainablePositionslist.contains(orphanedTemplePoint)) {
                return null;
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
