package de.sopra.javagame.control;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.*;
import org.junit.Assert;

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
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<PlayerType, Boolean>(PlayerType.COURIER, true));
        
        //teste mit zu wenigen Spielern
        controllerChan.startNewGame(testMap, players, Difficulty.NOVICE);
        Assert.assertNull("", controllerChan.getJavaGame());
        
        //teste mit 2 Spielern
        players.add(new Pair<PlayerType, Boolean>(PlayerType.EXPLORER, true));
        controllerChan.startNewGame(testMap, players, Difficulty.NORMAL);
        Assert.assertNotNull("", controllerChan.getJavaGame());
        
        //teste wenn es schon ein Spiel gab
        JavaGame oldGame = controllerChan.getJavaGame();
        boolean[][] testMapNewGame = new boolean[12][12];
        List<Pair<PlayerType, Boolean>> playersNewGame = new ArrayList<>();
        playersNewGame.add(new Pair<PlayerType, Boolean>(PlayerType.NAVIGATOR, true));
        playersNewGame.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
        playersNewGame.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, true));
        controllerChan.startNewGame(testMapNewGame, playersNewGame, Difficulty.ELITE);
        Assert.assertNotNull("", controllerChan.getJavaGame());
        Assert.assertNotEquals("", oldGame, controllerChan.getJavaGame());
        
        //prüfe ob altes Spiel gespeichert
        File saveGame = new File(controllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        Assert.assertTrue("", saveGame.exists());
        
        ControllerChan newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadGame(saveGame);
        JavaGame loadedGame = newControllerChan.getJavaGame();
        Assert.assertEquals("",  oldGame, loadedGame);
        
    }

    @Test
    public void testLoadSaveGame() {
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<PlayerType, Boolean>(PlayerType.NAVIGATOR, true));
        players.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
        players.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, true));
        controllerChan.startNewGame(testMap, players, Difficulty.NORMAL);
        JavaGame oldGame = controllerChan.getJavaGame();
        
        controllerChan.saveGame("mein erstes Spiel");
        File saveGame = new File(controllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        Assert.assertTrue("", saveGame.exists());
        
        
        ControllerChan newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadGame(saveGame);
        JavaGame loadedGame = newControllerChan.getJavaGame();
        Assert.assertEquals("",  oldGame, loadedGame);
        Assert.assertEquals("",  "mein erstes Spiel", newControllerChan.getGameName());
        
        
        
        
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
