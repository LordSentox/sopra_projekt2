package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnMoveToDrainTile.class)
public class TurnMoveTowardsMiddle extends Decision {
    @Override
    public Decision decide() {
        Point position = aiController.getActivePlayer().getPosition();
        //FIXME calculation of middle is not precise
        if (any(position.xPos != 6, position.yPos != 6)) {
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
