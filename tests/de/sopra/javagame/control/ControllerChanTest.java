package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.player.PlayerType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class ControllerChanTest {
    private ControllerChan controllerChan;
    private boolean[][] testMap;

    @Before
    public void setUp() {
        this.controllerChan = TestDummy.getDummyControllerChan();

        this.testMap = new boolean[12][12];
        for (int y = 0; y < 12; ++y) {
            for (int x = 0; x < 12; ++x) {
                // Erstellen einer 6*4-Insel umgeben von Wasser
                testMap[y][x] = x >= 2 && x < 8 && y >= 2 && y < 6;
            }
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void startNewGame() {
        this.controllerChan.startNewGame(this.testMap, Arrays.asList(PlayerType.COURIER, PlayerType.PILOT), Difficulty.LEGENDARY);
        JavaGame game = this.controllerChan.getJavaGame();

        Assert.assertFalse("Spieler wurden als Cheater abgestempelt, obwohl das Spiel gerade erst gestartet wurde", game.getCheetah());
        Assert.assertEquals("Schwierigkeit wurde nicht korrekt im Spiel gesetzt", Difficulty.LEGENDARY, game.getDifficulty());
    }

    @Test
    public void loadGame() {
    }

    @Test
    public void saveGame() {
    }

    @Test
    public void replayGame() {
    }

    @Test
    public void continueGame() {
    }
}