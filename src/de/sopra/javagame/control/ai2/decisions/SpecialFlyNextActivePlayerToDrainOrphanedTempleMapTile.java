package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
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
 * @version 12.09.2019
 * @since 12.09.2019
 */

public class SpecialFlyNextActivePlayerToDrainOrphanedTempleMapTile extends Decision {

    @Override
    public Decision decide() {
        if (!control.anyPlayerHasCard(ArtifactCardType.HELICOPTER)) {
            return null;
        }
        if (!hasValidActions(0)) {
            return null;
        }
        //Player nextActivePlayer = control.getNextPlayer();
        //Point nextActivePlayerPosition = nextActivePlayer.getPosition();
        List<Pair<Point, MapTile>> templeList = control.getTemples();
        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        for (Pair<Point, MapTile> temple : templeList) {
            Point orphanedTemplePoint = temple.getLeft();
            MapTile orphanedTemple = temple.getRight();
            //prüfe, ob NextPlayer auf betroffenem Tempel steht
            //if(orphanedTemplePoint.equals(nextActivePlayerPosition)){
            //    return null;
            //}
            //prüfe, ob Tempelartefakt bereits geborgen ist
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }
            //prüfe, ob NextActivePlayer hingehen kann
            //List<Point> inOneMovedrainablePositionslist = control.getDrainablePositionsOneMoveAway(orphanedTemplePoint, ActivePlayerType);
            //if (!inOneMovedrainablePositionslist.contains(orphanedTemplePoint)) {
            //    return null;
            //}
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
