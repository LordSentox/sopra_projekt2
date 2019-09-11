package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactType;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class TurnCaptureTreasure extends Decision {

    @Override
    public Decision decide() {
        EnhancedPlayerHand hand = playerHand();
        if (any(all(hand.getAmount(FIRE) > 3, tile().getProperties().getHidden() == ArtifactType.FIRE),
                all(hand.getAmount(EARTH) > 3, tile().getProperties().getHidden() == ArtifactType.EARTH),
                all(hand.getAmount(WATER) > 3, tile().getProperties().getHidden() == ArtifactType.WATER),
                all(hand.getAmount(AIR) > 3, tile().getProperties().getHidden() == ArtifactType.AIR))) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}