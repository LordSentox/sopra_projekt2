package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_ANY_PLAYER_HAS_HELICOPTER;
import static de.sopra.javagame.control.ai2.decisions.Condition.GAME_LANDING_SITE_IS_FLOODED;
import static de.sopra.javagame.model.ArtifactCardType.*;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = TURN_ACTION, value = TurnMoveForDrainingNearbyLandingSite.class)
@PreCondition(allTrue = {GAME_LANDING_SITE_IS_FLOODED, GAME_ANY_PLAYER_HAS_HELICOPTER})
public class TurnFlyActivePlayerToLandingSiteForDraining extends Decision {

    @Override
    public Decision decide() {

        if (!hasValidActions(1)) {
            return null;
        }

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();
        //fliege den Spieler nur, wenn er den Landeplatz nicht sofort trockenlegen kann
        if (player().drainablePositions().contains(landingSitePosition)) {
            return null;
        }
        //Prüfe für Sonderfall: --Player steht auf Tempel, kann dort Schatz bergen-- ob Sandsack spielbar
        EnhancedPlayerHand hand = playerHand();

        if (any(all(hand.getAmount(FIRE) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.FIRE),
                all(hand.getAmount(EARTH) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.EARTH),
                all(hand.getAmount(WATER) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.WATER),
                all(hand.getAmount(AIR) > THREE_CARDS, tile().getProperties().getHidden() == ArtifactType.AIR))
                && control.anyPlayerHasCard(ArtifactCardType.SANDBAGS)) {
            return null;
        }

        return this;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
