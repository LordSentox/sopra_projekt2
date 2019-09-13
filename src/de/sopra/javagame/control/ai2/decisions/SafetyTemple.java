package de.sopra.javagame.control.ai2.decisions;

import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;

import de.sopra.javagame.control.ai2.DoAfter;
@DoAfter(act = SWIM_TO_SAFETY, value = SafetyLandingSite.class)
public class SafetyTemple extends Decision{
    @Override
    public Decision decide() {
        if(condition(Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS).isTrue()
        ||condition(Condition.GAME_ANY_LAST_TEMPLE_IN_DANGER).isTrue()) {
            return this;
        }
        return null;
    }
    
    @Override
    public void act() {
        //TODO
    }

}
