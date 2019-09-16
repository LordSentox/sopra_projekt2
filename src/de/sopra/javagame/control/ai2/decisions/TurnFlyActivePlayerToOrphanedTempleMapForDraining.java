package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_ANY_PLAYER_HAS_HELICOPTER;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_NO_ACTION_LEFT;
import static de.sopra.javagame.model.ArtifactCardType.*;
import static de.sopra.javagame.model.MapTileState.FLOODED;
import static de.sopra.javagame.model.MapTileState.GONE;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds, Julius Korweck
 * @version 11.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnDrainTempleMapTileOfUndiscoveredArtifact.class)
@PreCondition(allTrue = GAME_ANY_PLAYER_HAS_HELICOPTER, allFalse = PLAYER_NO_ACTION_LEFT)
public class TurnFlyActivePlayerToOrphanedTempleMapForDraining extends Decision {
    private Point targetPoint;
    private EnumSet<PlayerType> dude;
    private Point start;
    @Override
    public Decision decide() {
           
        List<Pair<Point, MapTile>> templeList = control.getTemples();
        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        return checkTemples(templeList) ? this : null;
    }

    private boolean checkTemples(List<Pair<Point, MapTile>> temples){
        Point activePlayerPosition = player().getPosition();
        for (Pair<Point, MapTile> temple : temples) {

            Point orphanedTemplePoint = temple.getLeft();
            MapTile orphanedTemple = temple.getRight();

            //prüfe, ob Player auf betroffenem Tempel steht
            if (orphanedTemplePoint.equals(activePlayerPosition)) {
                continue;
            }

            //prüfe, ob Tempelartefakt bereits geborgen ist
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }

            List<Point> surroundingPoints = surroundingPoints(orphanedTemplePoint, true);
            List<MapTile> surroundingTiles = surroundingPoints.stream().map(control::getTile).collect(Collectors.toList());
            //prüfe, ob Inselfeld Nachbarfelder hat, die nicht GONE oder NULL sind
            if (!checkAll(tile -> tile.getState() == GONE, surroundingTiles)) {
                continue;
            }

            //Prüfe für Sonderfall: --Player steht auf Tempel, kann dort Schatz bergen-- ob Sandsack spielbar
            EnhancedPlayerHand hand = playerHand();

            if (any(all(hand.getAmount(FIRE) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.FIRE),
                    all(hand.getAmount(EARTH) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.EARTH),
                    all(hand.getAmount(WATER) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.WATER),
                    all(hand.getAmount(AIR) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.AIR))
                    && control.anyPlayerHasCard(ArtifactCardType.SANDBAGS)) {
                continue;
            }
            targetPoint=orphanedTemplePoint;
            start= activePlayerPosition;
            dude.add(player().getType());
            return true;
        }
        return false;

    }

    @Override
    public ActionQueue act() {
        return startActionQueue().helicopterCard(start, targetPoint, dude).drain(targetPoint);
    }

}
