package de.sopra.javagame.model.player;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.control.ActivePlayerController;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.model.*;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.util.Triple;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.cardstack.CardStackUtil;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.util.map.MapUtil;
import de.sopra.javagame.view.GamePreparationsViewController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExplorerTest {

    private MapFull testMap;
    private CardStack<ArtifactCard> artifactCardStack;
    private CardStack<FloodCard> floodCardStack;
    private Triple<MapFull, CardStack<ArtifactCard>, CardStack<FloodCard>> tournamentTriple;
    private Action action;

    @Before
    public void setUp() throws Exception {
        ControllerChan controllerChan = TestDummy.getDummyControllerChan();
        ActivePlayerController activePlayerController = controllerChan.getActivePlayerController();
        TestDummy.InGameView inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();

        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        this.testMap = MapUtil.readFullMapFromString(testMapString);
        String testArtifactCardString = new String(Files.readAllBytes(Paths.get(GamePreparationsViewController.DEV_ARTIFACT_STACK_FOLDER)), StandardCharsets.UTF_8);
        this.artifactCardStack = CardStackUtil.readArtifactCardStackFromString(testArtifactCardString);
        String testFloodCardString = new String(Files.readAllBytes(Paths.get(GamePreparationsViewController.DEV_FLOOD_STACK_FOLDER)), StandardCharsets.UTF_8);
        this.floodCardStack = CardStackUtil.readFloodCardStackFromString(testFloodCardString);
        tournamentTriple = new Triple<>(testMap, artifactCardStack, floodCardStack);
        
        
        List<Triple<PlayerType, String, Boolean>> players = Arrays.asList(
                new Triple<>(PlayerType.EXPLORER, "", false),
                new Triple<>(PlayerType.COURIER, "", false),
                new Triple<>(PlayerType.NAVIGATOR, "", false),
                new Triple<>(PlayerType.PILOT, "", false));

        Pair<JavaGame, Action> pair = JavaGame.newGame("test", tournamentTriple, Difficulty.NORMAL, players);
        TestDummy.injectJavaGame(controllerChan, pair.getLeft());
        TestDummy.injectCurrentAction(controllerChan, pair.getRight());
        action = pair.getRight();

        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
        action.getActivePlayer().setActionsLeft(3);

    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void constructorTest() {
        Explorer explorer = new Explorer("Bob", new Point(4, 4), action);
        explorer = new Explorer("Bob", new Point(4, 4), action, false);
    }

    @Test
    public void legalMovesTest() {

        Set<Point> points = new HashSet<>();
        points.add(new Point(5, 2));
        points.add(new Point(6, 3));
        Assert.assertEquals("Incorrect legal moves without special ability", points, new HashSet<>(action.getPlayer(PlayerType.EXPLORER).legalMoves(false)));

        points.add(new Point(4, 2));
        points.add(new Point(6, 2));
        Assert.assertEquals("Incorrect legal moves with special ability", points, new HashSet<>(action.getPlayer(PlayerType.EXPLORER).legalMoves(true)));
    }

    @Test
    public void drainablePositionsTest() {
        Set<Point> points = new HashSet<>();
        points.add(new Point(5, 2));
        points.add(new Point(5, 3));
        points.add(new Point(6, 2));

        for (Point point : points) {
            action.getMap().get(point).flood();
        }

        printMap(testMap.raw());

        Assert.assertFalse("Dry tiles included in drainable positions. Only flooded allowed", points.size() < new HashSet<>(action.getPlayer(PlayerType.EXPLORER).drainablePositions()).size());
        Assert.assertEquals("Incorrect drainable positions", points, new HashSet<>(action.getPlayer(PlayerType.EXPLORER).drainablePositions()));
    }

    @Test
    public void copyTest() {
        Explorer courier = (Explorer) action.getActivePlayer();
        Explorer copy = (Explorer) courier.copy();

        Assert.assertEquals("type not equal", courier.type, copy.type);
        Assert.assertEquals("name not equal", courier.name, copy.name);
        Assert.assertEquals("position not equal", courier.position, copy.position);
        Assert.assertEquals("actionsLeft not equal", courier.actionsLeft, copy.actionsLeft);
        Assert.assertEquals("isAI not equal", courier.isAI, copy.isAI);
        Assert.assertEquals("hand not equal", courier.hand, copy.hand);
    }


    private static void printMap(MapTile[][] tiles) {
        for (MapTile[] row : tiles) {
            Arrays.stream(row).map(t -> {
                if (t == null)
                    return "☐";
                else
                    return (t.getState() == MapTileState.DRY ? "d" : (t.getState() == MapTileState.FLOODED ? "f" : "g")) + t.getProperties().getIndex();
            }).forEach(t -> System.out.printf("%4s", t));
            System.out.println();
        }
    }

}
