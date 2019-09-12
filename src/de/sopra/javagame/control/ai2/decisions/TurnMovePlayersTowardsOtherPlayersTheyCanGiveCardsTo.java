package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

import java.util.List;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * @author Niklas Falke, Julius Korweck
 * @version 11.09.2019
 * @since 10.09.2019
 */
public class TurnMovePlayersTowardsOtherPlayersTheyCanGiveCardsTo extends Decision {
    @Override
    public Decision decide() {

        EnhancedPlayerHand hand = playerHand();

        if (!hand.hasAnyCard(AIR, EARTH, FIRE, WATER)) {
            return null;
        }

        int water = hand.getAmount(WATER);
        int fire = hand.getAmount(FIRE);
        int earth = hand.getAmount(EARTH);
        int air = hand.getAmount(AIR);

        List<Player> allPlayers = control.getAllPlayers();
        List<PlayerType> receivers = player().legalReceivers();
        allPlayers.removeIf(player -> !receivers.contains(player.getType()));

        for (Player player : allPlayers) {

            EnhancedPlayerHand playerHand = hand(player);

            int water2 = playerHand.getAmount(WATER);
            int fire2 = playerHand.getAmount(FIRE);
            int earth2 = playerHand.getAmount(EARTH);
            int air2 = playerHand.getAmount(AIR);

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
