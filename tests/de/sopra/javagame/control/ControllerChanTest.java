package de.sopra.javagame.control;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;

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
    
    @Test
    public void testStartNewGame() {
        fail("Not yet implemented");
    }

    @Test
    public void testLoadGame() {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveGame() {
        fail("Not yet implemented");
    }

    @Test
    public void testReplayGame() {
        fail("Not yet implemented");
    }

    @Test
    public void testContinueGame() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetCurrentTurn() {
        fail("Not yet implemented");
    }

    @Test
    public void testEndTurn() {
        fail("Not yet implemented");
    }

}
