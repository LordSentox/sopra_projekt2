package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.util.Point;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
public class TurnMoveTowardsMiddle extends Decision {
    @Override
    public Decision decide() {
        Point position = control.getActivePlayer().getPosition();
        //FIXME calculation of middle is not precise
        if (any(position.xPos != 6, position.yPos != 6)) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
