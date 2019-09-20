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
 * @version 12.09.2019
 * @since 10.09.2019
 */

@DoAfter(act = DISCARD, value = DiscardSandbagRatherThanOneOfFourTreasureCards.class)
public class DiscardRarestTreasureCards extends Decision {
    private ArtifactCardType discarded;

    @Override
    public Decision decide() {
        EnhancedPlayerHand activeHand = playerHand();
        int water = activeHand.getAmount(WATER);
        int fire = activeHand.getAmount(FIRE);
        int earth = activeHand.getAmount(EARTH);
        int air = activeHand.getAmount(AIR);
        if (all(water <= fire, water <= earth, water <= air, water > ZERO_CARDS)) {
            discarded = WATER;
            return this;
        }
        if (all(fire <= water, fire <= earth, fire <= air, fire > ZERO_CARDS)) {
            discarded = FIRE;
            return this;
        }
        if (all(earth <= fire, earth <= water, earth <= air, earth > ZERO_CARDS)) {
            discarded = EARTH;
            return this;
        }
        if (all(air <= fire, air <= earth, air <= water, air > ZERO_CARDS)) {
            discarded = AIR;
            return this;
        }
        //discard first any
        discarded = activeHand.getCards().get(activeHand.getCardsInHand() - 1).getType();
        return this;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().discard(discarded);
    }
}
