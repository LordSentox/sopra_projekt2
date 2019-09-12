package de.sopra.javagame.control.ai2.decisions;

import static de.sopra.javagame.model.ArtifactCardType.AIR;
import static de.sopra.javagame.model.ArtifactCardType.EARTH;
import static de.sopra.javagame.model.ArtifactCardType.FIRE;
import static de.sopra.javagame.model.ArtifactCardType.WATER;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.Decision;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 12.09.2019
 * @since 10.09.2019
 */
public class DiscardRarestTreasureCards extends Decision {
    @Override
    public Decision decide() {
        EnhancedPlayerHand activeHand = playerHand();
        int water = activeHand.getAmount(WATER);
        int fire = activeHand.getAmount(FIRE);
        int earth = activeHand.getAmount(EARTH);
        int air = activeHand.getAmount(AIR);
        if((water<fire && water< earth && water <air ) ||
           (fire<water && fire< earth && fire <air )||
           (earth<fire && earth< water && earth <air )||
           (air<fire && air< earth && air<water )){
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
