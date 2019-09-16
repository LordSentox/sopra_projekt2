package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactCardType;

import static de.sopra.javagame.control.ai2.DecisionResult.DISCARD;
import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DISCARD, value = DiscardUseSandbagToKeepFourTreasureCards.class)
public class DiscardOddTreasureCardWhenPlayerHasThreeOfSomething extends Decision {
    private ArtifactCardType discarded;

    @Override
    public Decision decide() {
        EnhancedPlayerHand activeHand = playerHand();
        int water = activeHand.getAmount(WATER);
        int fire = activeHand.getAmount(FIRE);
        int earth = activeHand.getAmount(EARTH);
        int air = activeHand.getAmount(AIR);
        if (any(air == THREE_CARDS, earth == THREE_CARDS, fire == THREE_CARDS, water == THREE_CARDS)) {
            if (air <= TWO_CARDS) {
                discarded = AIR;
                return this;
            } else if (earth <= TWO_CARDS) {
                discarded = EARTH;
                return this;
            } else if (fire <= TWO_CARDS) {
                discarded = FIRE;
                return this;
            } else if (water <= TWO_CARDS) {
                discarded = WATER;
                return this;
            }
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().discard(discarded);
    }
}