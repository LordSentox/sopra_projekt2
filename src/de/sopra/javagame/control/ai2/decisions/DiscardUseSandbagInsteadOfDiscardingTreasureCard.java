package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTileState;


/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DecisionResult.DISCARD, value = DiscardTreasureCardOfWhichareTwoInDiscard.class)
@PreCondition( allTrue=Condition.PLAYER_HAS_SANDBAGS_CARD)
public class DiscardUseSandbagInsteadOfDiscardingTreasureCard extends Decision {
    @Override
    public Decision decide() {
        if (control.anyTile(MapTileState.FLOODED) != null) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
