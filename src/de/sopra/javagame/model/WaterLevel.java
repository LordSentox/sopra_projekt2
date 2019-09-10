package de.sopra.javagame.model;

/**
 * enthält die Informationen wie hoch der aktuelle Wasserpegel ist und wie viele
 * karten zu ziehen sind und die Information, ob das Spiel bereits verloren ist
 *
 * @author Lisa, Hannah
 */
public class WaterLevel implements Copyable<WaterLevel> {

    /**
     * Hat am jeweiligen index die Anzahl der zu ziehenden Karten
     */
    private static final int[] DRAW_AMOUNT_BY_LEVEL = new int[] { 2, 2, 3, 3, 3, 4, 4, 5, 5, 6 }; // TODO
    /**
     * Index mit dem das Array ausgelesen wird Im Code, sollte dieser Wert von 0
     * bis 9 Zählen.
     */
    private int level;

    public WaterLevel() {
        this(Difficulty.NOVICE);
    }

    public WaterLevel(Difficulty difficulty) {
        this.level = difficulty.getInitialWaterLevel();
    }

    public int getLevel() {
        return level;
    }

    /**
     * erhöht das akutelle Wasserlevel um 1
     */
    void increment() {
        if (!isGameLost()) {
            level += 1;
        }
    }

    /**
     * Gibt zurück, ob das Spiel aufgrund des Wasserstandes bereits verloren ist
     * (Level=9)
     *
     * @return true: Level=9 false: level<9
     */
    boolean isGameLost() {
        return level == 9;
    }

    /**
     * liest das Array am Index des jeweiligen Levels aus
     *
     * @return Zahl der zu ziehenden Karten
     */
    int getDrawAmount() {
        if (!isGameLost()) {
            return DRAW_AMOUNT_BY_LEVEL[level];
        } else {
            return 0;
        }
    }

    @Override
    public WaterLevel copy() {
        WaterLevel waterLevel = new WaterLevel();
        waterLevel.level = this.level;
        return waterLevel;
    }
}
