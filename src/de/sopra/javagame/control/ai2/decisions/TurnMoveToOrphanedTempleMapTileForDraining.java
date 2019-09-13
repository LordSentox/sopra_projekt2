package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_MORE_THAN_1_ACTION_LEFT;
import static de.sopra.javagame.model.MapTileState.FLOODED;
import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnDrainOrphanedTempleMapTiles.class)
@PreCondition(allFalse = PLAYER_HAS_MORE_THAN_1_ACTION_LEFT)
public class TurnMoveToOrphanedTempleMapTileForDraining extends Decision {

    @Override
    public Decision decide() {

        PlayerType activePlayerType = player().getType();

        //Nur für Diver und Pilot relevant
        if (none(activePlayerType == PlayerType.DIVER, activePlayerType == PlayerType.PILOT)) {
            return null;
        }

        List<Pair<Point, MapTile>> templeList = control.getTemples();
        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        return checkTemples(templeList) ? this : null;

    }

    private boolean checkTemples(List<Pair<Point, MapTile>> temples) {
        PlayerType activePlayerType = player().getType();
        Point activePlayerPosition = player().getPosition();
        for (Pair<Point, MapTile> temple : temples) {
            Point orphanedTemplePoint = temple.getLeft();
            MapTile orphanedTemple = temple.getRight();
            //prüfe, ob Player auf betroffenem Tempel steht
            if (orphanedTemplePoint.equals(activePlayerPosition)) {
                continue; 
            }
            //prüfe, ob Tempelatefakt bereits geborgen ist
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();
            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }
            List<Point> inOneMoveDrainablePositionslist = control.getDrainablePositionsOneMoveAway(orphanedTemplePoint, activePlayerType);
            if (!inOneMoveDrainablePositionslist.contains(orphanedTemplePoint)) {
                continue; 
            }
            List<Point> surroundingPoints = surroundingPoints(orphanedTemplePoint, true);
            List<MapTile> surroundingTiles = surroundingPoints.stream().map(control::getTile).collect(Collectors.toList());
            //prüfe, ob Inselfeld Nachbarfelder hat, die nicht GONE oder NULL sind
            if (!checkAll(tile -> tile.getState() == GONE, surroundingTiles)) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
