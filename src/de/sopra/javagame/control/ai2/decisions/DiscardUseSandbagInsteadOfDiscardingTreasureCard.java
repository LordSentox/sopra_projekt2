package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.DISCARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_SANDBAGS_CARD;


/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DISCARD, value = DiscardTreasureCardOfWhichareTwoInDiscard.class)
@PreCondition(allTrue = PLAYER_HAS_SANDBAGS_CARD)
public class DiscardUseSandbagInsteadOfDiscardingTreasureCard extends Decision {
    private Point drainable;
    @Override
    public Decision decide() {
        MapTile tile= control.anyTile(MapTileState.FLOODED);
        if (tile != null) {
            drainable= MapUtil.getPositionForTile(action().getTiles(), tile.getProperties());
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().sandbagCard(drainable);
    }
}