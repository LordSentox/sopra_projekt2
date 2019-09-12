package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;


/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DecisionResult.PLAY_SPECIAL_CARD, value = DiscardOddTreasureCardWhenPlayerHasThreeOfSomething.class)
@PreCondition( allTrue={Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS,Condition.PLAYER_HAS_SANDBAGS_CARD})
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