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

import javax.swing.text.TabExpander;

public class ControllerChanTest {

    private ControllerChan controllerChan;
    private boolean[][] testMap;
    private TestDummy.InGameView inGameView;

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

        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
    }

    @Test
    public void testStartNewGame() {
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<PlayerType, Boolean>(PlayerType.COURIER, true));

        //teste mit zu wenigen Spielern
        controllerChan.startNewGame(testMap, players, Difficulty.NOVICE);
        Assert.assertNull("Es hätte kein Spiel erstellt werden dürfen", controllerChan.getJavaGame());

        //teste mit 2 Spielern
        players.add(new Pair<PlayerType, Boolean>(PlayerType.EXPLORER, true));
        controllerChan.startNewGame(testMap, players, Difficulty.NORMAL);
        Assert.assertNotNull("Es hätte ein Spiel erstellt werden sollen", controllerChan.getJavaGame());
        Assert.assertNotNull("Es hätte einen Explorer geben sollen", controllerChan.getCurrentTurn().getPlayer(PlayerType.EXPLORER));
        Assert.assertNotNull("Es hätte einen Courier geben sollen", controllerChan.getCurrentTurn().getPlayer(PlayerType.COURIER));

        //teste wenn es schon ein Spiel gab
        JavaGame oldGame = controllerChan.getJavaGame();
        boolean[][] testMapNewGame = new boolean[12][12];
        List<Pair<PlayerType, Boolean>> playersNewGame = new ArrayList<>();
        playersNewGame.add(new Pair<PlayerType, Boolean>(PlayerType.NAVIGATOR, true));
        playersNewGame.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
        playersNewGame.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, true));
        controllerChan.startNewGame(testMapNewGame, playersNewGame, Difficulty.ELITE);
        Assert.assertNotNull("Es hätte ein neues Spiel erstellt werden sollen", controllerChan.getJavaGame());
        Assert.assertNotEquals("Das alte Spiel hätte nicht mehr in ControllerChan sein sollen",
                                        oldGame,
                                        controllerChan.getJavaGame());

        //prüfe ob altes Spiel gespeichert
        File saveGame = new File(controllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        Assert.assertTrue("Das alte Spiel hätte nicht gelöscht sondern gespeichert werden sollen", saveGame.exists());

        ControllerChan newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadGame(saveGame);
        JavaGame loadedGame = newControllerChan.getJavaGame();
        Assert.assertEquals("Das geladene Spiel hätte dem vorher noch aktiven alten Spiel entsprechen sollen",  oldGame, loadedGame);

    }

    @Test
    public void testLoadSaveGame() {
        //teste mit korrekten Daten
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<PlayerType, Boolean>(PlayerType.NAVIGATOR, true));
        players.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
        players.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, true));
        controllerChan.startNewGame(testMap, players, Difficulty.NORMAL);
        JavaGame oldGame = controllerChan.getJavaGame();

        controllerChan.saveGame("mein erstes Spiel");
        File saveGame = new File(controllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        Assert.assertTrue("Das Spiel hätte unter dem Pfad "
                                    +  controllerChan.SAVE_GAME_FOLDER
                                    + controllerChan.getGameName()
                                    + " gespeichert werden sollen",
                                    saveGame.exists());

        ControllerChan newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadGame(saveGame);
        JavaGame loadedGame = newControllerChan.getJavaGame();
        Assert.assertEquals("Das geladene Spiel sollte dem vorher gespeicherten entsprechen!",  oldGame, loadedGame);
        Assert.assertEquals("Der Name des geladenen und gesetzten Spiels sollte dem gespeicherten entsprechen!",
                            "mein erstes Spiel",
                                     newControllerChan.getGameName());
        File replayGame = new File(controllerChan.REPLAY_FOLDER + controllerChan.getGameName() + ".replay");
        newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadGame(replayGame);
        Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
                                    inGameView.getNotifications().contains("Die gewählte Datei ist nicht kompatibel oder fehlerhaft."));

        //teste mit beendetem
        controllerChan.getCurrentTurn().setGameEnded(true);
        controllerChan.endTurn();
        controllerChan.saveGame("mein beendetes Spiel");
        saveGame = new File (controllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        replayGame = new File (controllerChan.REPLAY_FOLDER + controllerChan.getGameName() + ".replay");
        newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadGame(saveGame);
        JavaGame savedGame = newControllerChan.getJavaGame();
        newControllerChan.loadGame(replayGame);
        Assert.assertEquals("Gespeichertes Spiel in SaveGames und Replys sollte gleich sein",
                                      savedGame,
                                      newControllerChan.getJavaGame());
        Assert.assertTrue("Im Replay muss alles auf dem Redo-Stack sein", newControllerChan.getJavaGame().canRedo());
        Assert.assertFalse("Für das Replay muss beim Laden der undo-Stack geleert werden", newControllerChan.getJavaGame().canUndo());

        //teste mit leerem JavaGame
        controllerChan = TestDummy.getDummyControllerChan();
        controllerChan.saveGame("nullGame");


        //teste mit leerem namen
        controllerChan.startNewGame(testMap, players, Difficulty.NORMAL);
        controllerChan.saveGame("");
        Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
                                   inGameView.getNotifications().contains("Das Spiel muss einen Namen haben!"));

        saveGame = new File(controllerChan.SAVE_GAME_FOLDER + "" + ".save");
        controllerChan.loadGame(saveGame);
        Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
                                    inGameView.getNotifications().contains("Es muss ein Spiel ausgewählt sein"));

        saveGame = new File (controllerChan.SAVE_GAME_FOLDER + "noGame" + ".save");
        controllerChan.loadGame(saveGame);
        Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
                                    inGameView.getNotifications().contains("Die gewählte Datei ist nicht kompatibel oder fehlerhaft."));

    }

    @Test
    public void testReplayGame() {
        //View-Only!
    }

    @Test
    public void testContinueGame() {
        //TODO was soll hier eigentlich passieren und gibt es etwas nicht view-only testbares?
        fail("Not yet implemented");
    }

    @Test
    public void testEndTurn() {
        //teste mit nicht beendetem Spiel
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<PlayerType, Boolean>(PlayerType.NAVIGATOR, true));
        players.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
        players.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, true));
        controllerChan.startNewGame(testMap, players, Difficulty.LEGENDARY);
        JavaGame currentGame = controllerChan.getJavaGame();
        Turn currentTurn = controllerChan.getCurrentTurn();
        controllerChan.endTurn();

        Assert.assertEquals("Das JavaGame sollte sich nicht ändern", currentGame, controllerChan.getJavaGame());
        Assert.assertEquals("Der vorherige currentTurn sollte auf dem Undo-Stapel liegen",
                                      currentTurn,
                                      controllerChan.getJavaGame().getPreviousTurn());
        Assert.assertFalse("Der Redo-Stapel sollte leer sein", controllerChan.getJavaGame().canRedo());

        //teste mit beendetem Spiel
        controllerChan.getCurrentTurn().setGameEnded(true);
        currentTurn = controllerChan.getCurrentTurn();
        controllerChan.endTurn();

        Assert.assertEquals("Der vorherige currentTurn sollte auf dem Undo-Stapel liegen",
                                         currentTurn,
                                         controllerChan.getJavaGame().getPreviousTurn());
        Assert.assertNull("Das Spiel ist vorbei. Es sollte keinen neuen neuen Turn mehr geben.",
                                    controllerChan.getCurrentTurn());
        Assert.assertFalse("Der Redo-Stapel sollte leer sein", controllerChan.getJavaGame().canRedo());

    }

}
