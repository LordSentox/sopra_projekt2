package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;

@DoAfter(act = SWIM_TO_SAFETY, value = Decision.class)
public class SafetyLandingSite extends Decision {

    private Point landingSiteDirection;

    @Override
    public Decision decide() {
        if (condition(Condition.GAME_HAS_ALL_ARTIFACTS).isTrue()
                || condition(Condition.GAME_LANDING_SITE_IS_FLOODED).isTrue()) {
            landingSiteDirection = control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                    control.getTile(PlayerType.PILOT).getLeft(),
                    control.getActivePlayer().getType());
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().move(landingSiteDirection);
    }
}
