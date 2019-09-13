package de.sopra.javagame.model;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaGameTest {

    private ControllerChan controllerChan;
    private MapTile[][] testMap;
    private String testMapString;
    private List<Pair<PlayerType, Boolean>> players;

    @Before
    public void setUp() throws Exception {
        controllerChan = TestDummy.getDummyControllerChan();
        testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        players = new ArrayList<Pair<PlayerType, Boolean>>() {{
            add(new Pair<>(PlayerType.EXPLORER, false));
            add(new Pair<>(PlayerType.NAVIGATOR, true));
            add(new Pair<>(PlayerType.DIVER, false));
            add(new Pair<>(PlayerType.COURIER, true));
        }};
    }

    @Test
    public void newGame() {
        //teste Erstellen vom Spiel mit korrekten Werten
        Pair<JavaGame, Action> newGame = JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
        JavaGame javaGame = newGame.getLeft();

        Assert.assertEquals("Das neue Spiel sollte den gleichen MapNamen beinhalten", testMapString, javaGame.getMapName());
        Assert.assertEquals("Das neue Spiel sollte die gleiche Map beinhalten", testMap, javaGame.getPreviousAction().getTiles());
        Assert.assertEquals("Das neue Spiel sollte den gleichen Schwierigkeitsgrad haben ", Difficulty.NOVICE, javaGame.getDifficulty());

        Action action = javaGame.getPreviousAction();

        for (int i = 0; i < action.getPlayers().size(); i++) {
            Assert.assertEquals("Kopie sollte gleiche Spieler-Liste halten. Index " + i + " unterscheidet sich.",
                    action.getPlayers().get(i).getType(),
                    action.getPlayers().get(i).getType());
        }
        //Assert.assertEquals("", players, javaGame.getPreviousTurn().getPlayers());

    }
    @Test (expected = NullPointerException.class)
    public void newGameNoMap() {
        //teste Erstellen mit leerer Map
        JavaGame.newGame("emptyMap", null, Difficulty.NOVICE, players);
    }

    @Test(expected = NullPointerException.class)
    public void newGameNoMapName() {
        JavaGame.newGame(null, testMap, Difficulty.NOVICE, players);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newGameEmptyMapName() {
        JavaGame.newGame("", testMap, Difficulty.NOVICE, players);
    }

    @Test(expected = NullPointerException.class)
    public void newGameNoDifficulty() {
        JavaGame.newGame("TestMap", testMap, null, players);
    }

    @Test(expected = NullPointerException.class)
    public void newGameNoPlayers() {
        JavaGame.newGame("TestMap", testMap, Difficulty.NOVICE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newGameTooFewPlayers() {
        //teste Erstellen ohne Spieler
        JavaGame.newGame("TestMap", testMap, Difficulty.NOVICE,
                Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void newGameTooManyPlayers() {
        players.add(new Pair<>(PlayerType.PILOT, false));
        //teste Erstellen mit 5+ Spielern
        JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
    }

    @Test
    public void finishAction() {
        Pair<JavaGame, Action> newGame = JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
        JavaGame javaGame = newGame.getLeft();
        Action currentAction = newGame.getRight();

        Action lastAction = javaGame.getPreviousAction();
        Action nextAction = javaGame.finishAction(currentAction);

        //Assert.assertTrue("Das Java-Game hätte einen neuen currentAction haben sollen",
        //                  controllerChan.getCurrentAction() == nextAction);
        Assert.assertNotSame("Das Java-Game hätte einen neuen Previous Action haben sollen", javaGame.getPreviousAction(), lastAction);
        Assert.assertSame("Das Java-Game hätte einen neuen Previous Action haben sollen", javaGame.getPreviousAction(), currentAction);
        Assert.assertNotSame("Der neu erstellte Action hätte nicht gleich dem vorherigen sein dürfen", currentAction, nextAction);

        //teste ob korrekr redo Stapel zurückgesetzt wird
        controllerChan.getGameFlowController().undo();
        controllerChan.getGameFlowController().undo();
        Assert.assertTrue("There should have been two redo turns", javaGame.canRedo());
        currentAction = controllerChan.getCurrentAction();
        javaGame.finishAction(currentAction);
        Assert.assertFalse("There should have been no redo turns", javaGame.canRedo());
    }

    @Test
    public void calculateScore() {
        String mapString = "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,1,1,1,1,1,1,1,1,1,1,-\n"
                + "-,1,1,1,1,1,1,1,1,1,1,-\n"
                + "-,1,1,1,1,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-,-,-\n";
        //controllerChan.startNewGame(map, players, Difficulty.NOVICE);
        //JavaGame javaGame = controllerChan.getJavaGame();
        Difficulty difficulty = Difficulty.NOVICE;
        int actualDifficulty = (difficulty.getInitialWaterLevel() + 1);
        Pair<JavaGame, Action> newGame = JavaGame.newGame(mapString, testMap, difficulty, players);
        JavaGame javaGame = newGame.getLeft();
        Action action = newGame.getRight();
        int turnCount = 1;

        //teste ob Anfangsscore korrekt berechnet wird
        Assert.assertEquals("Score hätte gleich 100 sein sollen",
                100.0 * turnCount * actualDifficulty,
                javaGame.calculateScore(),
                0.0);


        //teste ob Score für 1 Artefakt korrekt berechnet wird
        action.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        Action nextAction = javaGame.finishAction(action);

        Assert.assertEquals("Der score dieses Spiels hätte 10.100 sein müssen",
                100.0 * turnCount * actualDifficulty + 10000 / turnCount / nextAction.getDiscoveredArtifacts().size(),
                javaGame.calculateScore(),
                0.0);


        //teste ob Score für Game Won korrekt berechnet wird
        Action secondNextAction = javaGame.finishAction(nextAction);

        action.getDiscoveredArtifacts().add(ArtifactType.WATER);
        action.getDiscoveredArtifacts().add(ArtifactType.EARTH);
        action.getDiscoveredArtifacts().add(ArtifactType.AIR);

        secondNextAction.setGameWon(true);
        secondNextAction.setGameEnded(true);
        secondNextAction = javaGame.finishAction(secondNextAction);
        Assert.assertEquals("Der score dieses Spiels hätte 120.000 sein müssen",
                (1.0/turnCount * 10000.0 + (10000/turnCount * secondNextAction.getDiscoveredArtifacts().size())*actualDifficulty) + 100000,
                javaGame.calculateScore(),
                0.0);


        //teste ob Score für cheetah korrekt berechnet wird
        javaGame.markCheetah();
        Assert.assertEquals("Der score dieses Spiels hätte 0 sein müssen", 0, javaGame.calculateScore());

        //teste für ganzes Spiel, ob Score korrekt berechnet wird
        //TODO komplettes Spiel laden und dann damit testen!
    }
}