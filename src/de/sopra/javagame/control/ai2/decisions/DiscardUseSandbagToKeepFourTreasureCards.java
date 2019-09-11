package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.MapTileState;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
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
            if (any(air == 4, fire == 4, earth == 4, water == 4)) {
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
