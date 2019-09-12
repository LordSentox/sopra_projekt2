package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

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
@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnDrainTempleMapTileOfUndiscoveredArtifact.class)
public class TurnFlyActivePlayerToOrphanedTempleMapForDraining extends Decision {

    @Override
    public Decision decide() {
        if (!control.anyPlayerHasCard(ArtifactCardType.HELICOPTER)) {
            return null;
        }
        if (!hasValidActions(1)) {
            return null;
        }
        Point activePlayerPosition = player().getPosition();

        List<Pair<Point, MapTile>> templeList = control.getTemples();
        //filter non-flooded tiles
        templeList = templeList.stream().filter(pair -> pair.getRight().getState() == FLOODED).collect(Collectors.toList());

        for (Pair<Point, MapTile> temple : templeList) {

            Point orphanedTemplePoint = temple.getLeft();
            MapTile orphanedTemple = temple.getRight();

            //prüfe, ob Player auf betroffenem Tempel steht
            if (orphanedTemplePoint.equals(activePlayerPosition)) {
                return null;
            }

            //prüfe, ob Tempelatefakt bereits geborgen ist
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

            //Prüfe für Sonderfall: --Player steht auf Tempel, kann dort Schatz bergen-- ob Sandsack spielbar
            EnhancedPlayerHand hand = playerHand();

            if (any(all(hand.getAmount(FIRE) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.FIRE),
                    all(hand.getAmount(EARTH) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.EARTH),
                    all(hand.getAmount(WATER) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.WATER),
                    all(hand.getAmount(AIR) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.AIR))
                    && control.anyPlayerHasCard(ArtifactCardType.SANDBAGS)) {
                return null;
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
