package de.sopra.javagame.model;

/**
 * Beschreibt die vier verschiedenen Schwierigkeitsstufen des Spiels.
 * In aufsteigender Reihenfolge.
 *
 * @author Hauke Bühmann, Melanie Arnds
 */
public enum Difficulty {

    NOVICE(0),
    NORMAL(1),
    ELITE(2),
    LEGENDARY(3);

    private final int initialWaterLevel;

    Difficulty(int initialWaterLevel) {
        this.initialWaterLevel = initialWaterLevel;
    }

    public int getInitialWaterLevel() {
        return initialWaterLevel;
    }
}
