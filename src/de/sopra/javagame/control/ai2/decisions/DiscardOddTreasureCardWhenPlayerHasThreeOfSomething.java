package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act=DecisionResult.DISCARD,value=DiscardUseSandbagToKeepFourTreasureCards.class)
public class DiscardOddTreasureCardWhenPlayerHasThreeOfSomething extends Decision {
    @Override
    public Decision decide() {
        EnhancedPlayerHand activeHand = playerHand();
        int water = activeHand.getAmount(WATER);
        int fire = activeHand.getAmount(FIRE);
        int earth = activeHand.getAmount(EARTH);
        int air = activeHand.getAmount(AIR);
        if (any(air == THREE_CARDS, earth == THREE_CARDS, fire == THREE_CARDS, water == THREE_CARDS)) {
            if (any(air <= TWO_CARDS, earth <= TWO_CARDS, fire <= TWO_CARDS, water <= TWO_CARDS)) {
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