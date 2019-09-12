package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactType;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnGiveTreasureCardsToPlayersWhoNeedThem.class)
public class TurnMoveOntoTreasureCollectionPointIfPlayerHasFour extends Decision {
    @Override
    public Decision decide() {
        ArtifactType type = tile().getProperties().getHidden();
        int amountOfType = playerHand().getAmount(type);

        if (amountOfType == FOUR_CARDS) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
