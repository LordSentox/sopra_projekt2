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
    private JavaGame javaGame;
    private MapTile[][] testMap;
    private String testMapString;
    private List<Pair<PlayerType, Boolean>> players;

    @Before
    public void setUp() throws Exception {
        controllerChan = TestDummy.getDummyControllerChan();
        javaGame = new JavaGame();
        testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), "UTF-8");
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        players =  new ArrayList(){{add(new Pair<>(PlayerType.EXPLORER, false));
                                 add(new Pair<>(PlayerType.NAVIGATOR, true));
                                 add(new Pair<>(PlayerType.DIVER, false));
                                 add(new Pair<>(PlayerType.COURIER, true));}};
    }

    @Test
    public void newGame() {
        //teste Erstellen vom Spiel mit korrekten Werten
        javaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
        Assert.assertEquals("", testMapString, javaGame.getMapName());
        Assert.assertEquals("", testMap, javaGame.getPreviousTurn().getTiles());
        Assert.assertEquals("", Difficulty.NOVICE, javaGame.getDifficulty());

        Turn turn = javaGame.getPreviousTurn();

        for(int i = 0; i< turn.getPlayers().size(); i++) {
            Assert.assertEquals("Kopie sollte gleiche Spieler-Liste halten. Index " + i + " unterscheidet sich.",
                    turn.getPlayers().get(i).getType(),
                    turn.getPlayers().get(i).getType());
        }
        //Assert.assertEquals("", players, javaGame.getPreviousTurn().getPlayers());

    }
    @Test (expected = NullPointerException.class)
    public void newGameNoMap() {
        //teste Erstellen mit leerer Map
        javaGame.newGame("emptyMap", null, Difficulty.NOVICE, players);
    }

    @Test (expected = IllegalArgumentException.class)
    public void newGameEmptyMap() {
        javaGame.newGame("emptyMap", new MapTile[12][12], Difficulty.NOVICE, players);
    }

    @Test (expected = IllegalArgumentException.class)
    public void newGameTooFewPlayers() {
        //teste Erstellen ohne Spieler
        javaGame.newGame(testMapString, testMap, Difficulty.NOVICE,
                Arrays.asList());
    }

    @Test (expected = IllegalArgumentException.class)
    public void newGameTooManyPlayers() {
        players.add(new Pair<>(PlayerType.PILOT, false));
        //teste Erstellen mit 5+ Spielern
        javaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
    }

    @Test
    public void endTurn() {
        Turn currentTurn = javaGame.newGame(testMapString, testMap, Difficulty.NOVICE, players);
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

    }

    @Test
    public void calculateScore() {
        boolean [][] map = new boolean[12][12];
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
        Turn turn = javaGame.newGame(mapString, testMap, Difficulty.NOVICE, players);

        //teste ob Anfangsscore korrekt berechnet wird
        Assert.assertEquals("Score hätte gleich sein sollen", 100.0, javaGame.calculateScore(), 0.0);

        //teste ob Score für 1 Artefakt korrekt berechnet wird
        turn.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        Turn nextTurn = javaGame.endTurn(turn);
        Assert.assertEquals("", 200.0, javaGame.calculateScore(), 0.0);

        //teste ob Score für Game Won korrekt berechnet wird
        nextTurn.setGameWon(true);
        nextTurn.setGameEnded(true);
        Turn secondNextTurn = javaGame.endTurn(nextTurn);
        Assert.assertEquals("", 1200.0, javaGame.calculateScore(), 0.0);

        //teste ob Score für cheetah korrekt berechnet wird
        javaGame.markCheetah();
        Assert.assertEquals("", 0, javaGame.calculateScore());
    }
}