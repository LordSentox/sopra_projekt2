package de.sopra.javagame.util;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class HighScore {

    private String name;
    private String mapName;
    private int score;

    public HighScore(String name, String mapName, int score) {
        this.name = name;
        this.mapName = mapName;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getMapName() {
        return mapName;
    }

}