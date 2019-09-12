package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;

import java.util.EnumSet;
import java.util.List;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
@DoAfter(act=DecisionResult.DISCARD,value=DiscardTreasureCardsOfCollectedTreasures.class)
public class DiscardTreasureCardsThatAnotherPlayerHasFourOf extends Decision {

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
                if (any(all(water > 3, activeCard.getType() == ArtifactCardType.WATER),
                        all(fire > 3, activeCard.getType() == ArtifactCardType.FIRE),
                        all(earth > 3, activeCard.getType() == ArtifactCardType.EARTH),
                        all(air > 3, activeCard.getType() == ArtifactCardType.AIR))) {
                    return this;
                }
            }
        }
        return null;
    }


    @Override
    public void act() {
        //TODO
    }

}