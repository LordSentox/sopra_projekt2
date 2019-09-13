package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;

import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;

@DoAfter(act = SWIM_TO_SAFETY, value = SafetyTemple.class)
public class SafetySwimSomewhere extends Decision {
    @Override
    public Decision decide() {
        return this;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
