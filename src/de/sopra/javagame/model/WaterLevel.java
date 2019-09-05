package de.sopra.javagame.model;

/**
 * enthält die Informationen wie hoch der aktuelle Wasserpegel ist und wie viele karten zu ziehen sind und die Information, ob das Spiel bereits verloren ist
 *
 * @author Lisa, Hannah
 */
public class WaterLevel {

    /**
     * Hat am jeweiligen index die Anzahl der zu ziehenden Karten
     */
    private static final int[] DRAW_AMOUNT_BY_LEVEL = new int[]{}; //TODO 
    /**
     * Index mit dem das Array ausgelesen wird
     */
    private int level;

    public WaterLevel() {
        this(Difficulty.NOVICE);
    }

    public WaterLevel(Difficulty difficulty) {
        this.level = difficulty.getInitialWaterLevel();
    }

    /**
     * erhöht das akutelle Wasserlevel um 1
     */
    void increment() {

    }

    /**
     * git zurück, ob das Spiel aufgrund des Wasserstandes bereits verloren ist(Level=10)
     *
     * @return true: Level=10
     * false: level<10
     */
    boolean isGameLost() {
        return false;
    }

    /**
     * liest das Arry am index des jeweiligen Levels aus
     *
     * @return Zahl der zu ziehenden Karten
     */
    int getDrawAmount() {
        return 0;
    }

}
