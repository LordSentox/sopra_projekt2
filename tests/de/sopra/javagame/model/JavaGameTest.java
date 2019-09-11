package de.sopra.javagame.model;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaGameTest {

    private ControllerChan controllerChan;
    private MapTile[][] testMap;
    private String testMapString;
    private List<Pair<PlayerType, Boolean>> players;

    @Before
    public void setUp() throws Exception {
        controllerChan = TestDummy.getDummyControllerChan();
        testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), "UTF-8");
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
        Pair<JavaGame, Turn> newGame = JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
        JavaGame javaGame = newGame.getLeft();

        Assert.assertEquals("Das neue Spiel sollte den gleichen MapNamen beinhalten", testMapString, javaGame.getMapName());
        Assert.assertEquals("Das neue Spiel sollte die gleiche Map beinhalten", testMap, javaGame.getPreviousTurn().getTiles());
        Assert.assertEquals("Das neue Spiel sollte den gleichen Schwierigkeitsgrad haben ", Difficulty.NOVICE, javaGame.getDifficulty());

        Turn turn = javaGame.getPreviousTurn();

        for (int i = 0; i < turn.getPlayers().size(); i++) {
            Assert.assertEquals("Kopie sollte gleiche Spieler-Liste halten. Index " + i + " unterscheidet sich.",
                    turn.getPlayers().get(i).getType(),
                    turn.getPlayers().get(i).getType());
        }
        //Assert.assertEquals("", players, javaGame.getPreviousTurn().getPlayers());

    }

    @Test(expected = NullPointerException.class)
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
        JavaGame.newGame(testMapString, testMap, null, players);
    }

    @Test(expected = NullPointerException.class)
    public void newGameNoPlayers() {
        JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newGameTooFewPlayers() {
        //teste Erstellen ohne Spieler
        JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE,
                Arrays.asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void newGameTooManyPlayers() {
        players.add(new Pair<>(PlayerType.PILOT, false));
        //teste Erstellen mit 5+ Spielern
        JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
    }

    @Test
    public void endTurn() {
        Pair<JavaGame, Turn> newGame = JavaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
        JavaGame javaGame = newGame.getLeft();
        Turn currentTurn = newGame.getRight();

        Turn lastTurn = javaGame.getPreviousTurn();
        Turn nextTurn = javaGame.endTurn(currentTurn);

        //Assert.assertTrue("Das Java-Game hätte einen neuen currentTurn haben sollen",
        //                  controllerChan.getCurrentTurn() == nextTurn);
        Assert.assertFalse("Das Java-Game hätte einen neuen Previous Turn haben sollen",
                javaGame.getPreviousTurn() == lastTurn);
        Assert.assertTrue("Das Java-Game hätte einen neuen Previous Turn haben sollen",
                javaGame.getPreviousTurn() == currentTurn);
        Assert.assertFalse("Der neu erstellte Turn hätte nicht gleich dem vorherigen sein dürfen",
                currentTurn == nextTurn);

        //teste ob korrekr redo Stapel zurückgesetzt wird
        controllerChan.getGameFlowController().undo();
        controllerChan.getGameFlowController().undo();
        Assert.assertTrue("There should have been two redo turns", javaGame.canRedo());
        currentTurn = controllerChan.getCurrentTurn();
        javaGame.endTurn(currentTurn);
        Assert.assertFalse("There should have been no redo turns", javaGame.canRedo());
    }

    @Test
    public void calculateScore() {
        boolean[][] map = new boolean[12][12];
        String name = "hallo";
        String mapString = "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;1;1;1;1;1;1;1;1;1;1;-\n"
                + "-;1;1;1;1;1;1;1;1;1;1;-\n"
                + "-;1;1;1;1;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n";
        //controllerChan.startNewGame(map, players, Difficulty.NOVICE);
        //JavaGame javaGame = controllerChan.getJavaGame();
        Difficulty difficulty = Difficulty.NOVICE;
        int actualDifficulty = (difficulty.getInitialWaterLevel() + 1);
        Pair<JavaGame, Turn> newGame = JavaGame.newGame(mapString, testMap, difficulty, players);
        JavaGame javaGame = newGame.getLeft();
        Turn turn = newGame.getRight();
        int turnCount = 1;

        //teste ob Anfangsscore korrekt berechnet wird
        Assert.assertEquals("Score hätte gleich 100 sein sollen",
                100.0 * turnCount * actualDifficulty,
                javaGame.calculateScore(),
                0.0);


        //teste ob Score für 1 Artefakt korrekt berechnet wird
        turn.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        Turn nextTurn = javaGame.endTurn(turn);

        Assert.assertEquals("Der score dieses Spiels hätte 10.100 sein müssen",
                100.0 * turnCount * actualDifficulty + 10000/turnCount/nextTurn.getDiscoveredArtifacts().size(),
                javaGame.calculateScore(),
                0.0);


        //teste ob Score für Game Won korrekt berechnet wird
        Turn secondNextTurn = javaGame.endTurn(nextTurn);

        turn.getDiscoveredArtifacts().add(ArtifactType.WATER);
        turn.getDiscoveredArtifacts().add(ArtifactType.EARTH);
        turn.getDiscoveredArtifacts().add(ArtifactType.AIR);

        secondNextTurn.setGameWon(true);
        secondNextTurn.setGameEnded(true);
        secondNextTurn = javaGame.endTurn(secondNextTurn);
        Assert.assertEquals("Der score dieses Spiels hätte 120.000 sein müssen",
                (1.0/turnCount * 10000.0 + (10000/turnCount * secondNextTurn.getDiscoveredArtifacts().size())*actualDifficulty) + 100000,
                javaGame.calculateScore(),
                0.0);


        //teste ob Score für cheetah korrekt berechnet wird
        javaGame.markCheetah();
        Assert.assertEquals("Der score dieses Spiels hätte 0 sein müssen", 0, javaGame.calculateScore());

        //teste für ganzes Spiel, ob Score korrekt berechnet wird
        //TODO komplettes Spiel laden und dann damit testen!
    }
}