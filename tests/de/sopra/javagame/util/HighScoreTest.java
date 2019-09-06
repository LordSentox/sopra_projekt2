package de.sopra.javagame.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class HighScoreTest {

    @Test
    public void coverage() {
        HighScore highScore = new HighScore("name", "mapName", 50);
        Assert.assertEquals("name", highScore.getName());
        Assert.assertEquals("mapName", highScore.getMapName());
        Assert.assertEquals(50, highScore.getScore());
    }

}