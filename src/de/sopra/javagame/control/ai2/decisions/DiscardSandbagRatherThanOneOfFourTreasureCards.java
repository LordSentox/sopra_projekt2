package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;

import static de.sopra.javagame.control.ai2.DecisionResult.PLAY_SPECIAL_CARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_SANDBAGS_CARD;


/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = PLAY_SPECIAL_CARD, value = DiscardOddTreasureCardWhenPlayerHasThreeOfSomething.class)
@PreCondition(allTrue = {PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS, PLAYER_HAS_SANDBAGS_CARD})
public class DiscardSandbagRatherThanOneOfFourTreasureCards extends Decision {
    @Override
    public Decision decide() {
        return this;
    }

    @Override
    public void act() {
        //TODO
    }
}