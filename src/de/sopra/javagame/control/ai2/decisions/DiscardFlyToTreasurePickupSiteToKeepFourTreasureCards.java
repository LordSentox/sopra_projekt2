package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;

import static de.sopra.javagame.control.ai2.DecisionResult.DISCARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_HELICOPTER_CARD;


/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DISCARD, value = DiscardUseSandbagInsteadOfDiscardingTreasureCard.class)
@PreCondition(allTrue = {PLAYER_HAS_HELICOPTER_CARD, PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS})
public class DiscardFlyToTreasurePickupSiteToKeepFourTreasureCards extends Decision {

    private Point startPoint;
    private Point targetPoint;
    private PlayerType activePlayer;

    @Override
    public Decision decide() {
        activePlayer = control.getActivePlayer().getType();
        startPoint = control.getActivePlayer().getPosition();
        EnhancedPlayerHand activeHand = playerHand();
        //Es wird der erste Tempel gewaehlt, da es zu aufwaendig waere, den optimalen Tempel zum 
        //Finden des Schatzes zu berechnen
        for (ArtifactType type : ArtifactType.values()) {
            if (activeHand.getAmount(type) == FOUR_CARDS) {
                Pair<Pair<Point, MapTile>, Pair<Point, MapTile>> temples = control.getTile(type);
                if (temples.getRight().getRight().getState().ordinal() <
                        temples.getLeft().getRight().getState().ordinal())
                    targetPoint = temples.getRight().getLeft();
                else targetPoint = temples.getLeft().getLeft();
                return this;
            }
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().helicopterCard(startPoint, targetPoint, EnumSet.of(activePlayer));
    }
}
