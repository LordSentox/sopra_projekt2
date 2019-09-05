package de.sopra.javagame.model;

import de.sopra.javagame.model.player.Player;

/**
 * 
 * @author Max Bühmann, Melanie Arnds
 * Dieses enum beschreibt die verschiedenen Typen, die eine {@link ArtifactCard} haben kann.
 * AIR, EARTH, FIRE, WATER die Rolle der Elemente.
 * HELICOPTER, SANDBAGS beschreiben Spezialkarten, die der Spieler, genauso wie die Elementkarten, auf die Hand ziehen kann {@link Player}.
 * WATERS_RISE ist eine Karte, deren Effekt sofort ausgeführt wird.
 * 
 */
public enum ArtifactCardType {

    AIR(ArtifactType.AIR),
    EARTH(ArtifactType.EARTH),
    FIRE(ArtifactType.FIRE),
    WATER(ArtifactType.WATER),
    HELICOPTER,
    SANDBAGS,
    WATERS_RISE;
	
	/**
	 * @return toString wandelt den Typen der Karten in einen String um und gibt ihn zurück	 * 
	 */

    private final ArtifactType type;

    ArtifactCardType(ArtifactType type) {
        this.type = type;
    }

    ArtifactCardType() {
        this.type = ArtifactType.NONE;
    }

    public String toString() {
        return null;
    }

    public ArtifactType toArtifactType() {
        return type;
    }

}
