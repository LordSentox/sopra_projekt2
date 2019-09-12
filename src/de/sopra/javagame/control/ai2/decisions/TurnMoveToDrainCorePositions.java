package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnMoveToCollectTreasureWithThreeCards.class)
public class TurnMoveToDrainCorePositions extends Decision {
    @Override
    public Decision decide() {
        //TODO
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
