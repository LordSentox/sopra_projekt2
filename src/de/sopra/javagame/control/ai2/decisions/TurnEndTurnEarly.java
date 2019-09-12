package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnMoveTowardsMiddle.class)
public class TurnEndTurnEarly extends Decision {
    @Override
    public Decision decide() {
        return this;
    }

    @Override
    public void act() {
        //TODO
    }
}
