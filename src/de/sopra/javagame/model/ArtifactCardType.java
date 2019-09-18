package de.sopra.javagame.model;

import de.sopra.javagame.model.player.Player;

/**
 * @author Max Bühmann, Melanie Arnds
 * Dieses enum beschreibt die verschiedenen Typen, die eine {@link ArtifactCard} haben kann.
 * AIR, EARTH, FIRE, WATER die Rolle der Elemente.
 * HELICOPTER, SANDBAGS beschreiben Spezialkarten, die der Spieler, genauso wie die Elementkarten, auf die Hand ziehen kann {@link Player}.
 * WATERS_RISE ist eine Karte, deren Effekt sofort ausgeführt wird.
 */
public enum ArtifactCardType {
    WATER(5, ArtifactType.WATER, 1),
    FIRE(5, ArtifactType.FIRE, 2),
    EARTH(5, ArtifactType.EARTH, 3),
    AIR(5, ArtifactType.AIR, 4),
    HELICOPTER(3, 5),
    SANDBAGS(2, 6),
    WATERS_RISE(3, 7);

    private final ArtifactType type;
    private final int cardsInDeck;
    private final int cardId;

    ArtifactCardType(int cardsInDeck, ArtifactType type, int cardId) {
        this.type = type;
        this.cardsInDeck = cardsInDeck;
        this.cardId = cardId;
    }

    ArtifactCardType(int cardsInDeck, int cardId) {
        this.type = ArtifactType.NONE;
        this.cardsInDeck = cardsInDeck;
        this.cardId = cardId;
    }

    public static ArtifactCardType getByIndex(int index) {
        return values()[index - 1];
    }

    public ArtifactType toArtifactType() {
        return type;
    }

    public int getCardsInDeck() {
        return cardsInDeck;
    }

    public int getCardId() {
        return cardId;
    }
}
