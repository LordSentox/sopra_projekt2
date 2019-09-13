package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai2.DecisionResult.PLAY_SPECIAL_CARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.*;
import static de.sopra.javagame.model.MapTileState.FLOODED;
import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 12.09.2019
 * @since 12.09.2019
 */
@DoAfter(act = PLAY_SPECIAL_CARD, value = SpecialUseSandbagToDrainOrphanedTempleMapTile.class)
@PreCondition(allTrue = {PLAYER_HAS_HELICOPTER_CARD, PLAYER_NO_ACTION_LEFT, GAME_ANY_LAST_TEMPLE_IN_DANGER})

public class SpecialFlyNextActivePlayerToDrainOrphanedTempleMapTile extends Decision {

    @Override
    public Decision decide() {

        Player nextActivePlayer = action().getNextPlayer();
        Point nextActivePlayerPosition = nextActivePlayer.getPosition();
        PlayerType nextActivePlayerType = nextActivePlayer.getType();
        List<Pair<Point, MapTile>> templeList = aiController.getTemples();
        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        for (Pair<Point, MapTile> temple : templeList) {
            Point orphanedTemplePoint = temple.getLeft();
            MapTile orphanedTemple = temple.getRight();
            //prüfe, ob NextPlayer auf betroffenem Tempel steht
            if (orphanedTemplePoint.equals(nextActivePlayerPosition)) {
                return null;
            }
            //prüfe, ob Tempelartefakt bereits geborgen ist
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }
            //prüfe, ob NextActivePlayer zum Tempel hin"gehen" kann
            List<Point> inOneMoveDrainablePositionslist = aiController.getDrainablePositionsOneMoveAway(orphanedTemplePoint, nextActivePlayerType);
            if (!inOneMoveDrainablePositionslist.contains(orphanedTemplePoint)) {
                return null;
            }
            List<Point> surroundingPoints = surroundingPoints(orphanedTemplePoint, true);
            List<MapTile> surroundingTiles = surroundingPoints.stream().map(aiController::getTile).collect(Collectors.toList());
            //wenn eins nicht GONE ist
            if (!checkAll(tile -> tile.getState() == GONE, surroundingTiles)) {
                continue;
            }
            return this;
        }

        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
