package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.DISCARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_SANDBAGS_CARD;


/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DISCARD, value = DiscardFlyToTreasurePickupSiteToKeepFourTreasureCards.class)
@PreCondition(allTrue = {PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS, PLAYER_HAS_SANDBAGS_CARD})
public class DiscardUseSandbagToKeepFourTreasureCards extends Decision {
    private Point drainable;

    @Override
    public Decision decide() {
        MapTile tile = control.anyTile(MapTileState.FLOODED);
        if (tile != null) {
            drainable = action().getMap().getPositionForTile(tile.getProperties());
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().sandbagCard(drainable);
    }
}
