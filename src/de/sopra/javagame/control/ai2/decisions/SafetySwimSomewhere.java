package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;

@DoAfter(act = SWIM_TO_SAFETY, value = SafetyTemple.class)
public class SafetySwimSomewhere extends Decision {

    private Point direction;

    @Override
    public Decision decide() {
        direction = control.getActivePlayer().legalMoves(true).get(0);
        return this;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().move(direction);
    }

}
