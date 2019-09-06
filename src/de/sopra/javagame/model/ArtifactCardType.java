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

    AIR(5, ArtifactType.AIR),
    EARTH(5, ArtifactType.EARTH),
    FIRE(5, ArtifactType.FIRE),
    WATER(5, ArtifactType.WATER),
    HELICOPTER(3),
    SANDBAGS(2),
    WATERS_RISE(3);

    private final ArtifactType type;
    private final int cardsInDeck;

    ArtifactCardType(int cardsInDeck, ArtifactType type) {
        this.type = type;
        this.cardsInDeck = cardsInDeck;
    }

    ArtifactCardType(int cardsInDeck) {
        this.type = ArtifactType.NONE;
        this.cardsInDeck = cardsInDeck;
    }

    public ArtifactType toArtifactType() {
        return type;
    }

    public int getCardsInDeck() {
        return cardsInDeck;
    }
}
