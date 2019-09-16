package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Map;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class ControllerChanTest {

    private ControllerChan controllerChan;
    private MapBlackWhite testMap;
    private TestDummy.InGameView inGameView;

    @Before
    public void setUp() throws IOException {
        this.controllerChan = TestDummy.getDummyControllerChan();
        this.testMap = new MapBlackWhite();
        for (int y = 0; y < Map.SIZE_Y; ++y) {
            for (int x = 0; x < Map.SIZE_X; ++x) {
                // Erstellen einer 6*4-Insel umgeben von Wasser
                testMap.set(x >= 1 && x < 7 && y >= 1 && y < 5, x, y);
            }
        }

        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
    }

    @Test
    public void testStartNewGame() {

        /* MERGED - FROM na-tests branch
         this.controllerChan.startNewGame(this.testMap, Arrays.asList(new Pair(PlayerType.COURIER, true),new Pair(PlayerType.PILOT, false)), Difficulty.LEGENDARY);
        JavaGame game = this.controllerChan.getJavaGame();

        Assert.assertFalse("Spieler wurden als Cheater abgestempelt, obwohl das Spiel gerade erst gestartet wurde", game.getCheetah());
        Assert.assertEquals("Schwierigkeit wurde nicht korrekt im Spiel gesetzt", Difficulty.LEGENDARY, game.getDifficulty());
        TestDummy.InGameView inGameView=  (TestDummy.InGameView)this.controllerChan.getInGameViewAUI();
        for(int i=0;i<12;i++) {
            for(int j=0;j<12;j++) {
                if(testMap[i][j]){
                    Assert.assertNotNull("Map ist falsch",inGameView.getRefreshedMapTiles().get(new Point(i,j)));
                }else{
                    Assert.assertNull("Map ist falsch",inGameView.getRefreshedMapTiles().get(new Point(i,j)));
                }
            }
        }        //TODO: complete
        //Assert.assert("Falsche Spieler",game.getCurrentTurn().);

         */

        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<>(PlayerType.COURIER, true));

        String testMapName = "TestMap";
        //teste mit 2 Spielern
        players.add(new Pair<>(PlayerType.EXPLORER, true));
        controllerChan.startNewGame(testMapName, testMap, players, Difficulty.NORMAL);
        Assert.assertNotNull("Es hätte ein Spiel erstellt werden sollen", controllerChan.getJavaGame());
        Assert.assertNotNull("Es hätte einen Explorer geben sollen", controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER));
        Assert.assertNotNull("Es hätte einen Courier geben sollen", controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER));

        //teste wenn es schon ein Spiel gab

        JavaGame oldGame = controllerChan.getJavaGame();
        /**
         String testMapNewGameName = "Neue Map";
         boolean[][] testMapNewGame = new boolean[12][12];
         List<Pair<PlayerType, Boolean>> playersNewGame = new ArrayList<>();
         playersNewGame.add(new Pair<>(PlayerType.NAVIGATOR, true));
         playersNewGame.add(new Pair<>(PlayerType.PILOT, true));
         playersNewGame.add(new Pair<>(PlayerType.ENGINEER, true));
         controllerChan.startNewGame(testMapNewGameName, testMapNewGame, playersNewGame, Difficulty.ELITE);
         Assert.assertNotNull("Es hätte ein neues Spiel erstellt werden sollen", controllerChan.getJavaGame());
         Assert.assertNotEquals("Das alte Spiel hätte nicht mehr in ControllerChan sein sollen",
         oldGame,
         controllerChan.getJavaGame());
         */

        //prüfe ob altes Spiel gespeichert
        File saveGame = new File(ControllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        Assert.assertTrue("Das alte Spiel hätte nicht gelöscht sondern gespeichert werden sollen", saveGame.exists());

        ControllerChan newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadSaveGame(controllerChan.getGameName());
        JavaGame loadedGame = newControllerChan.getJavaGame();
        Assert.assertEquals("Das geladene Spiel hätte dem vorher noch aktiven alten Spiel entsprechen sollen", oldGame, loadedGame);

    }

    @Test(expected = IllegalStateException.class)
    public void testStartNewGameNotEnoughPlayers() {
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<>(PlayerType.COURIER, true));
        //teste mit zu wenigen Spielern
        String testMapName = "TestMap";
        controllerChan.startNewGame(testMapName, testMap, players, Difficulty.NOVICE);
        Assert.assertNull("Es hätte kein Spiel erstellt werden dürfen", controllerChan.getJavaGame());
    }

    @Test
    public void testLoadSaveGame() {
        //teste mit korrekten Daten
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<>(PlayerType.NAVIGATOR, true));
        players.add(new Pair<>(PlayerType.PILOT, true));
        players.add(new Pair<>(PlayerType.ENGINEER, true));
        String testMapName = "TestMap";
        controllerChan.startNewGame(testMapName, testMap, players, Difficulty.NORMAL);
        JavaGame oldGame = controllerChan.getJavaGame();

        controllerChan.saveGame("mein erstes Spiel");
        File saveGame = new File(controllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        Assert.assertTrue("Das Spiel hätte unter dem Pfad "
                        + controllerChan.SAVE_GAME_FOLDER
                        + controllerChan.getGameName()
                        + " gespeichert werden sollen",
                saveGame.exists());

        ControllerChan newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadSaveGame(controllerChan.getGameName());
        JavaGame loadedGame = newControllerChan.getJavaGame();
        Assert.assertEquals("Das geladene Spiel sollte dem vorher gespeicherten entsprechen!", oldGame, loadedGame);
        Assert.assertEquals("Der Name des geladenen und gesetzten Spiels sollte dem gespeicherten entsprechen!",
                "mein erstes Spiel",
                newControllerChan.getGameName());
        File replayGame = new File(controllerChan.REPLAY_FOLDER + controllerChan.getGameName() + ".replay");
        newControllerChan = TestDummy.getDummyControllerChan();
        //newControllerChan.loadGame(replayGame);
        //Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
            //    inGameView.getNotifications().contains("Die gewählte Datei ist nicht kompatibel oder fehlerhaft."));

        //teste mit beendetem
        controllerChan.getCurrentAction().setGameEnded(true);
        controllerChan.finishAction();
        controllerChan.saveGame("mein beendetes Spiel");
        saveGame = new File(controllerChan.SAVE_GAME_FOLDER + controllerChan.getGameName() + ".save");
        replayGame = new File(controllerChan.REPLAY_FOLDER + controllerChan.getGameName() + ".replay");
        newControllerChan = TestDummy.getDummyControllerChan();
        newControllerChan.loadSaveGame(controllerChan.getGameName());
        JavaGame savedGame = newControllerChan.getJavaGame();

        /*
        newControllerChan.loadGame(replayGame);
        Assert.assertEquals("Gespeichertes Spiel in SaveGames und Replys sollte gleich sein",
                savedGame,
                newControllerChan.getJavaGame());
        Assert.assertTrue("Im Replay muss alles auf dem Redo-Stack sein", newControllerChan.getJavaGame().canRedo());
        Assert.assertFalse("Für das Replay muss beim Laden der undo-Stack geleert werden", newControllerChan.getJavaGame().canUndo());
       */

        //teste mit leerem JavaGame
        controllerChan = TestDummy.getDummyControllerChan();
        controllerChan.saveGame("nullGame");


        //teste mit leerem namen
        controllerChan.startNewGame("TestMap", testMap, players, Difficulty.NORMAL);
        controllerChan.saveGame("");
        Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
                inGameView.getNotifications().contains("Das Spiel muss einen Namen haben!"));

        saveGame = new File(controllerChan.SAVE_GAME_FOLDER + "" + ".save");
        controllerChan.loadSaveGame(controllerChan.getGameName());
        Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
                inGameView.getNotifications().contains("Es muss ein Spiel ausgewählt sein"));

        saveGame = new File(controllerChan.SAVE_GAME_FOLDER + "noGame" + ".save");
        controllerChan.loadSaveGame(controllerChan.getGameName());
        Assert.assertTrue("Es hätte eine Meldung an den Benutzer ausgegeben werden sollen",
                inGameView.getNotifications().contains("Die gewählte Datei ist nicht kompatibel oder fehlerhaft."));

    }

    @Test
    public void testReplayGame() {
        //View-Only! TODO view connection can be tested
        fail("Not yet implemented");
    }

    @Test
    public void testContinueGame() {
        //TODO was soll hier eigentlich passieren und gibt es etwas nicht view-only testbares?
        fail("Not yet implemented");
    }

    @Test
    public void testFinishAction() {
        //teste mit nicht beendetem Spiel
        List<Pair<PlayerType, Boolean>> players = new ArrayList<>();
        players.add(new Pair<>(PlayerType.NAVIGATOR, true));
        players.add(new Pair<>(PlayerType.PILOT, true));
        players.add(new Pair<>(PlayerType.ENGINEER, true));
        controllerChan.startNewGame("TestMap", testMap, players, Difficulty.LEGENDARY);
        JavaGame currentGame = controllerChan.getJavaGame();
        Action currentAction = controllerChan.getCurrentAction();
        controllerChan.finishAction();

        Assert.assertEquals("Das JavaGame sollte sich nicht ändern", currentGame, controllerChan.getJavaGame());
        Assert.assertEquals("Der vorherige currentAction sollte auf dem Undo-Stapel liegen",
                currentAction,
                controllerChan.getJavaGame().getPreviousAction());
        Assert.assertFalse("Der Redo-Stapel sollte leer sein", controllerChan.getJavaGame().canRedo());

        //teste mit beendetem Spiel
        controllerChan.getCurrentAction().setGameEnded(true);
        currentAction = controllerChan.getCurrentAction();
        controllerChan.finishAction();

        Assert.assertEquals("Der vorherige currentAction sollte auf dem Undo-Stapel liegen",
                currentAction,
                controllerChan.getJavaGame().getPreviousAction());
        Assert.assertNull("Das Spiel ist vorbei. Es sollte keine neue Action mehr geben.",
                controllerChan.getCurrentAction());
        Assert.assertFalse("Der Redo-Stapel sollte leer sein", controllerChan.getJavaGame().canRedo());

    }

}