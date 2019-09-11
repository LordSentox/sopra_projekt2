package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.MapTileState;

import static de.sopra.javagame.model.ArtifactCardType.SANDBAGS;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class DiscardUseSandbagInsteadOfDiscardingTreasureCard extends Decision {
    @Override
    public Decision decide() {
        if (control.anyTile(MapTileState.FLOODED) != null) {
            if (playerHand().hasCard(SANDBAGS)) {
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
