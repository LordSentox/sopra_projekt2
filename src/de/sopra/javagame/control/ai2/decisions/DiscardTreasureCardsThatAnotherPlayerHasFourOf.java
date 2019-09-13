package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;

import java.util.EnumSet;
import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.DISCARD;
import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
@DoAfter(act = DISCARD, value = DiscardTreasureCardsOfCollectedTreasures.class)
public class DiscardTreasureCardsThatAnotherPlayerHasFourOf extends Decision {
private ArtifactCardType discarded; 
    @Override
    public Decision decide() {
        List<Player> allPlayers = control.getAllPlayers();
        allPlayers.removeIf(player -> player.getType() == player().getType());
        EnumSet<ArtifactCardType> onlyTypes = EnumSet.complementOf(EnumSet.of(HELICOPTER, SANDBAGS, WATERS_RISE));
        for (ArtifactCard activeCard : playerHand().getCards(onlyTypes)) {
            for (Player player : allPlayers) {
                EnhancedPlayerHand hand = hand(player);
                if (hand.getCardsInHand() < FOUR_CARDS) {
                    break;
                }
                int water = hand.getAmount(WATER);
                int fire = hand.getAmount(FIRE);
                int earth = hand.getAmount(EARTH);
                int air = hand.getAmount(AIR);
                if(all(water > 3, activeCard.getType() == ArtifactCardType.WATER)) {
                    discarded=activeCard.getType();
                    return this;
                }    
                if(all(fire > 3, activeCard.getType() == ArtifactCardType.FIRE)){
                    discarded=activeCard.getType();
                    return this;
                }
                if(all(earth > 3, activeCard.getType() == ArtifactCardType.EARTH)){
                    discarded=activeCard.getType();
                    return this;
                }
                if(all(air > 3, activeCard.getType() == ArtifactCardType.AIR)){
                    discarded=activeCard.getType();
                    return this;
                }    
            }
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().discard(discarded);
    }

}