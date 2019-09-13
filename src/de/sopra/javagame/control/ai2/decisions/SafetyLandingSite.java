package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DoAfter;
import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;
@DoAfter(act = SWIM_TO_SAFETY, value = Decision.class)
public class SafetyLandingSite extends Decision{
    @Override
    public Decision decide() {
        if(condition(Condition.GAME_HAS_ALL_ARTIFACTS).isTrue()
        ||condition(Condition.GAME_LANDING_SITE_IS_FLOODED).isTrue()) {
            return this;
        }
        return null;
    }
    
    @Override
    public void act() {
        //TODO
    }
}
