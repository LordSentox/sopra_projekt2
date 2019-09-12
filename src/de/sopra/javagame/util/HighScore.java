package de.sopra.javagame.util;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class HighScore implements Comparable<HighScore>{

    private String name;
    private String mapName;
    private int score;
    private String replayName;

    public HighScore(String name, String mapName, int score, String replayName) {
        this.name = name;
        this.mapName = mapName;
        this.score = score;
        this.replayName = replayName;
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

	public String getReplayname() {
		return replayName;
	}
    
    public int compareTo(HighScore other) {
        return Integer.compareUnsigned(score, other.score);
    }

}