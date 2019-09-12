package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;

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
    @Override
    public Decision decide() {
        EnhancedPlayerHand activeHand = playerHand();
        int water = activeHand.getAmount(WATER);
        int fire = activeHand.getAmount(FIRE);
        int earth = activeHand.getAmount(EARTH);
        int air = activeHand.getAmount(AIR);
        if (any(all(water < fire, water < earth, water < air),
                all(fire < water, fire < earth, fire < air),
                all(earth < fire, earth < water, earth < air),
                all(air < fire, air < earth, air < water))) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
