package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

import java.util.List;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnFlyActivePlayerToOrphanedTempleMapForDraining.class)
public class TurnGiveTreasureCardsToPlayersWhoNeedThem extends Decision {
    @Override
    public Decision decide() {

        if (!playerHand().hasAnyCard(WATER, FIRE, EARTH, AIR)) {
            return null;
        }

        int water = playerHand().getAmount(WATER);
        int fire = playerHand().getAmount(FIRE);
        int earth = playerHand().getAmount(EARTH);
        int air = playerHand().getAmount(AIR);

        List<Player> allPlayers = control.getAllPlayers();
        List<PlayerType> receivers = player().legalReceivers();
        allPlayers.removeIf(player -> !receivers.contains(player.getType()));

        for (Player player : allPlayers) {
            EnhancedPlayerHand hand = hand(player);
            int water2 = hand.getAmount(WATER);
            int fire2 = hand.getAmount(FIRE);
            int earth2 = hand.getAmount(EARTH);
            int air2 = hand.getAmount(AIR);

            if (any(
                    all(air > ZERO_CARDS, air < air2, air2 < FOUR_CARDS),
                    all(earth > ZERO_CARDS, earth < earth2, earth2 < FOUR_CARDS),
                    all(fire > ZERO_CARDS, fire < fire2, fire2 < FOUR_CARDS),
                    all(water > ZERO_CARDS, water < water2, water2 < FOUR_CARDS)
            )) {
                return this;
            }
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
