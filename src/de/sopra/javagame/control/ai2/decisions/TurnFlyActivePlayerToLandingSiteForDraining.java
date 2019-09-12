package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.model.ArtifactCardType.*;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnMoveForDrainingNearbyLandingSite.class)
public class TurnFlyActivePlayerToLandingSiteForDraining extends Decision {

    @Override
    public Decision decide() {
        if (!control.anyPlayerHasCard(ArtifactCardType.HELICOPTER)) {
            return null;
        }

        if (!hasValidActions(1)) {
            return null;
        }

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();
        Point landingSitePosition = informationLandingSite.getLeft();

        if (landingSite.getState() != MapTileState.FLOODED) {
            return null;
        }

        if (player().drainablePositions().contains(landingSitePosition)) {
            return null;
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

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
