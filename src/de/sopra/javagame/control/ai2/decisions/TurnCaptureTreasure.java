package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_CAN_CAPTURE_TREASURE;


/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = TURN_ACTION, value = TurnMoveToLandingSiteForDeparture.class)
@PreCondition(allTrue = PLAYER_CAN_CAPTURE_TREASURE)
public class TurnCaptureTreasure extends Decision {

    @Override
    public Decision decide() {
        return this;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }
}