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
        if(all(water <= fire, water <= earth, water <= air)){
            discarded= WATER;
            return this;
        }
        if(all(fire <= water, fire <= earth, fire <= air)){
            discarded= FIRE;
            return this;
        }
        if(all(earth <= fire, earth <= water, earth <= air)){
            discarded= EARTH;
            return this;
        }
        if(all(air <= fire, air <= earth, air <= water)){
            discarded= AIR;
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().discard(discarded);
    }
}
