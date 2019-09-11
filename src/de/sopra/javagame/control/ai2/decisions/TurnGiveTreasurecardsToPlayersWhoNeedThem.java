package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

import java.util.List;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class TurnGiveTreasurecardsToPlayersWhoNeedThem extends Decision {
    @Override
    public Decision decide() {

        if (any(playerHand().hasCard(WATER),
                playerHand().hasCard(FIRE),
                playerHand().hasCard(EARTH),
                playerHand().hasCard(AIR))) {
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
            if (air > 0 && air < air2 && air2 < 4) {
                return this;
            } else if (earth > 0 && earth < earth2 && earth2 < 4) {
                return this;
            } else if (fire > 0 && fire < fire2 && fire2 < 4) {
                return this;
            } else if (water > 0 && water < water2 && water2 < 4) {
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
