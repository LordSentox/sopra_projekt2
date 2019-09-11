package de.sopra.javagame.control.ai;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.player.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.ArtifactCardType.*;

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
        return reduction.orElse(0);
    }

    public int getAmount(ArtifactType type) {
        switch (type) {

            case AIR:
                return getAmount(AIR);
            case EARTH:
                return getAmount(EARTH);
            case FIRE:
                return getAmount(FIRE);
            case WATER:
                return getAmount(WATER);
            default:
                return 0;
        }
    }

    public boolean hasCard(ArtifactCardType... types) {
        for (ArtifactCardType type : types)
            if (getAmount(type) == 0)
                return false;
        return true;
    }

    public boolean hasAnyCard(ArtifactCardType... types) {
        for (ArtifactCardType type : types)
            if (getAmount(type) > 0)
                return true;
        return true;
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