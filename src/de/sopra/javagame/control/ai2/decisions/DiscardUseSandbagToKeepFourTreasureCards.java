package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.MapTileState;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DecisionResult.DISCARD, value = DiscardFlyToTreasurePickupSiteToKeepFourTreasureCards.class)
public class DiscardUseSandbagToKeepFourTreasureCards extends Decision {
    @Override
    public Decision decide() {
        if (control.anyTile(MapTileState.FLOODED) != null) {
            EnhancedPlayerHand activeHand = playerHand();
            if (!activeHand.hasCard(SANDBAGS)) {
                return null;
            }
            int water = activeHand.getAmount(WATER);
            int fire = activeHand.getAmount(FIRE);
            int earth = activeHand.getAmount(EARTH);
            int air = activeHand.getAmount(AIR);
            if (any(air == FOUR_CARDS, fire == FOUR_CARDS, earth == FOUR_CARDS, water == FOUR_CARDS)) {
                return this;
            }
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
