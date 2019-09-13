package de.sopra.javagame.model.player;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.control.ActivePlayerController;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.model.*;
import de.sopra.javagame.util.MapFull;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
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

    private ControllerChan controllerChan;
    private ActivePlayerController activePlayerController;
    private MapFull testMap;
    private Action action;
    private List<Pair<PlayerType, Boolean>> players;

    private TestDummy.InGameView inGameView;

    @Before
    public void setUp() throws Exception {
        controllerChan = TestDummy.getDummyControllerChan();
        activePlayerController = controllerChan.getActivePlayerController();
        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();

        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        this.testMap = MapUtil.readFullMapFromString(testMapString);

        players = Arrays.asList(
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.COURIER, false),
                new Pair<>(PlayerType.NAVIGATOR, false),
                new Pair<>(PlayerType.PILOT, false));

        Pair<JavaGame, Action> pair = JavaGame.newGame("test", testMap, Difficulty.NORMAL, players);
        TestDummy.injectJavaGame(controllerChan, pair.getLeft());
        TestDummy.injectCurrentAction(controllerChan, pair.getRight());
        action = pair.getRight();

        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
        action.getActivePlayer().setActionsLeft(3);

    }

    @Test
    public void constructorTest() {
        Explorer explorer = new Explorer("Bob", new Point(4, 4), action);
        explorer = new Explorer("Bob", new Point(4, 4), action, false);
    }

    @Test
    public void legalMovesTest() {

        Set<Point> points = new HashSet<>();
        points.add(new Point(6, 3));
        points.add(new Point(7, 4));
        Assert.assertEquals("Incorrect legal moves without special ability", points, new HashSet<>(action.getPlayer(PlayerType.EXPLORER).legalMoves(false)));

        points.add(new Point(5, 3));
        points.add(new Point(7, 3));
        Assert.assertEquals("Incorrect legal moves with special ability", points, new HashSet<>(action.getPlayer(PlayerType.EXPLORER).legalMoves(true)));
    }

    @Test
    public void drainablePositionsTest() {


        action.getTile(6, 3).flood();
        action.getTile(6, 4).flood();
        action.getTile(7, 3).flood();

        printMap(testMap);

        Set<Point> points = new HashSet<>();
        points.add(new Point(6, 3));
        points.add(new Point(7, 3));
        points.add(new Point(6, 4));
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
