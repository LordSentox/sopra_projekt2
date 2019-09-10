package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

import java.util.List;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class MovePlayersTowardsOtherPlayersTheyCanGiveCardsTo extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        boolean hasArtifact = false;
        for (ArtifactCard treasure : activeHand) {
            if (treasure.getType() == ArtifactCardType.AIR ||
                    treasure.getType() == ArtifactCardType.EARTH ||
                    treasure.getType() == ArtifactCardType.WATER ||
                    treasure.getType() == ArtifactCardType.FIRE) {
                hasArtifact = true;
            }
        }
        if (!hasArtifact) {
            return null;
        }
        int water = 0;
        int fire = 0;
        int earth = 0;
        int air = 0;
        for (ArtifactCard card : activeHand) {
            if (card.getType() == ArtifactCardType.AIR) {
                air++;
            } else if (card.getType() == ArtifactCardType.EARTH) {
                earth++;
            } else if (card.getType() == ArtifactCardType.FIRE) {
                fire++;
            } else if (card.getType() == ArtifactCardType.WATER) {
                water++;
            }
        }
        List<Player> allPlayers = control.getAllPlayers();
        List<PlayerType> receivers = activePlayer.legalReceivers();
        for (Player player : allPlayers) {
            if (receivers.contains(player.getType())) {
                allPlayers.remove(player);
            }
        }
        for (Player player : allPlayers) {
            List<ArtifactCard> hand = player.getHand();
            int water2 = 0;
            int fire2 = 0;
            int earth2 = 0;
            int air2 = 0;
            for (ArtifactCard card : hand) {
                if (card.getType() == ArtifactCardType.AIR) {
                    air2++;
                } else if (card.getType() == ArtifactCardType.EARTH) {
                    earth2++;
                } else if (card.getType() == ArtifactCardType.FIRE) {
                    fire2++;
                } else if (card.getType() == ArtifactCardType.WATER) {
                    water2++;
                }
            }
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
