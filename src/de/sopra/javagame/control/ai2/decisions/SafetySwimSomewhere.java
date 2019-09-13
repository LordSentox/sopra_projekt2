package de.sopra.javagame.control.ai2.decisions;
import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;

import de.sopra.javagame.control.ai2.DoAfter;
@DoAfter(act = SWIM_TO_SAFETY, value = SafetyTemple.class)
public class SafetySwimSomewhere extends Decision{
    @Override
    public Decision decide() {        
        return this;
    }

@Override
public void act() {
    //TODO
}

}
