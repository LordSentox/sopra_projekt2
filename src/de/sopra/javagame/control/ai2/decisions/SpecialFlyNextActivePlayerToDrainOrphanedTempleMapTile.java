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

    private Player nextActivePlayer;
    private Point targetTemple;
    @Override
    public Decision decide() {


        List<Pair<Point, MapTile>> templeList = control.getTemples();
        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        return checkTemples(templeList) ? this : null;
    }

    private boolean checkTemples(List<Pair<Point, MapTile>> temples){
        nextActivePlayer = action().getNextPlayer();
        Point nextActivePlayerPosition = nextActivePlayer.getPosition();
        PlayerType nextActivePlayerType = nextActivePlayer.getType();
        for (Pair<Point, MapTile> temple : temples) {
            Point orphanedTemplePoint = temple.getLeft();
            targetTemple = orphanedTemplePoint;
            MapTile orphanedTemple = temple.getRight();
            //prüfe, ob NextPlayer auf betroffenem Tempel steht
            if (orphanedTemplePoint.equals(nextActivePlayerPosition)) {
                continue;
            }
            //prüfe, ob Tempelartefakt bereits geborgen ist
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }
            //prüfe, ob NextActivePlayer zum Tempel hin"gehen" kann
            List<Point> inOneMoveDrainablePositionslist = control.getDrainablePositionsOneMoveAway(orphanedTemplePoint, nextActivePlayerType).stream().map(Pair::getRight).collect(Collectors.toList());
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
        return startActionQueue().helicopterCard(nextActivePlayer.getPosition(), targetTemple, EnumSet.of(nextActivePlayer.getType()));
    }

}
