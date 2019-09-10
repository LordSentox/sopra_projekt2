package de.sopra.javagame.control.ai2;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.ArtifactCardType.HELICOPTER;
import static de.sopra.javagame.model.ArtifactCardType.SANDBAGS;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class EnhancedPlayerHand {

    private List<ArtifactCard> hand;

    EnhancedPlayerHand(List<ArtifactCard> cards) {
        this.hand = cards;
    }

    public List<ArtifactCard> getCards() {
        return hand;
    }

    public List<ArtifactCard> getCards(EnumSet<ArtifactCardType> onlyTypes) {
        return hand.stream()
                .filter(card -> onlyTypes.contains(card.getType()))
                .collect(Collectors.toList());
    }

    public int getCardsInHand() {
        return hand.size();
    }

    public int getAmount(ArtifactCardType type) {
        Optional<Integer> reduction = hand.stream()
                .map(card -> card.getType() == type ? 1 : 0)
                .reduce(Integer::sum);
        return reduction.isPresent() ? reduction.get() : 0;
    }

    public boolean hasCard(ArtifactCardType type) {
        return getAmount(type) > 0;
    }

    public boolean hasHelicopter() {
        return hasCard(HELICOPTER);
    }

    public boolean hasSandBags() {
        return hasCard(SANDBAGS);
    }

    public static EnhancedPlayerHand ofPlayer(Player player) {
        return ofHand(player.getHand());
    }

    public static EnhancedPlayerHand ofHand(List<ArtifactCard> cards) {
        return new EnhancedPlayerHand(cards);
    }

}