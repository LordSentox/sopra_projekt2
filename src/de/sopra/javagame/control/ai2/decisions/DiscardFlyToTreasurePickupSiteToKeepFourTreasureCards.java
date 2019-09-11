package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class DiscardFlyToTreasurePickupSiteToKeepFourTreasureCards extends Decision {
    @Override
    public Decision decide() {
        if (!playerHand().hasHelicopter()) {
            return null;
        }
        int water = playerHand().getAmount(WATER);
        int fire = playerHand().getAmount(FIRE);
        int earth = playerHand().getAmount(EARTH);
        int air = playerHand().getAmount(AIR);
        if (air == 4 || fire == 4 || earth == 4 || water == 4) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
