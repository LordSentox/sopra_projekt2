package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;

import de.sopra.javagame.model.ArtifactType;

import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.DISCARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_HELICOPTER_CARD;

import java.util.EnumSet;



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
    private EnumSet<PlayerType> activePlayer;
    @Override
    public Decision decide() {
        activePlayer.add(control.getActivePlayer().getType());
        startPoint=control.getActivePlayer().getPosition();
        EnhancedPlayerHand activeHand = playerHand();
        //Es wird der erste Tempel gewaehlt, da es zu aufwaendig waere, den optimalen Tempel zum 
        //Finden des Schatzes zu berechnen
        if(activeHand.getAmount(ArtifactType.WATER)==4) {
            targetPoint=control.getTile(ArtifactType.WATER).getLeft().getLeft();
            return this;
        }else if(activeHand.getAmount(ArtifactType.EARTH)==4) {
            targetPoint=control.getTile(ArtifactType.EARTH).getLeft().getLeft();
            return this;
        }else if(activeHand.getAmount(ArtifactType.FIRE)==4) {
            targetPoint=control.getTile(ArtifactType.FIRE).getLeft().getLeft();
            return this;
        }else if(activeHand.getAmount(ArtifactType.AIR)==4) {
            targetPoint=control.getTile(ArtifactType.AIR).getLeft().getLeft();
            return this;
        }    
        return null;    
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().helicopterCard(startPoint, targetPoint,activePlayer);
    }
}
