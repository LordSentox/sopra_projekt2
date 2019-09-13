package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_ANY_ARTIFACT_CARD;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnDrainTile.class)
@PreCondition(allTrue = PLAYER_HAS_ANY_ARTIFACT_CARD)
public class TurnMoveToCollectTreasureWithThreeCards extends Decision {
    @Override
    public Decision decide() {
        EnhancedPlayerHand hand = playerHand();
        MapTile tile = tile();
        if ((hand.getAmount(ArtifactCardType.EARTH) > TWO_CARDS && tile.getProperties().getHidden() == ArtifactType.EARTH) ||
                (hand.getAmount(ArtifactCardType.FIRE) > TWO_CARDS && tile.getProperties().getHidden() == ArtifactType.FIRE) ||
                (hand.getAmount(ArtifactCardType.AIR) > TWO_CARDS && tile.getProperties().getHidden() == ArtifactType.AIR) ||
                (hand.getAmount(ArtifactCardType.WATER) > TWO_CARDS && tile.getProperties().getHidden() == ArtifactType.WATER)) {
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
