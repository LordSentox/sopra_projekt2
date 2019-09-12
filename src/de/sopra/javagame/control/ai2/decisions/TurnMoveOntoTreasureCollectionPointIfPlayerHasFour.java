package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactType;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnGiveTreasureCardsToPlayersWhoNeedThem.class)
@PreCondition(allTrue = PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS)
public class TurnMoveOntoTreasureCollectionPointIfPlayerHasFour extends Decision {
    @Override
    public Decision decide() {
        //FIXME Bewegung fehlt
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
