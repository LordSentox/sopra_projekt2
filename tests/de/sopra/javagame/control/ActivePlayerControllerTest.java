package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.TestDummy.InGameView;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ActivePlayerControllerTest {

    private ControllerChan controllerChan;
    private ActivePlayerController activePlayerController;
    private JavaGame javaGame;
    private int[][] testMapNumbers;
    private MapTile[][] testMap;
    private List<Player> moveablePlayers;
    private MapController mapController;
    private Action action;
    private ArtifactCard fireCard;
    private ArtifactCard waterCard;
    private ArtifactCard earthCard;
    private ArtifactCard airCard;
    private ArtifactCard sandCard;
    private ArtifactCard heliCard;
    private Courier courier;
    private Explorer explorer;
    private Navigator navigator;
    private Pilot pilot;
    private CardStack<ArtifactCard> artifactCardStack;
    private List<ArtifactCard> handCardsExpected;
    List<Pair<PlayerType, Boolean>> players;

    private InGameView inGameView;

    public void setUp() throws IOException {
        controllerChan = TestDummy.getDummyControllerChan();
        activePlayerController = controllerChan.getActivePlayerController();
        inGameView = (InGameView) controllerChan.getInGameViewAUI();
        javaGame = controllerChan.getJavaGame();
        action = controllerChan.getCurrentAction();

        boolean [][] tiles = new boolean [12][12];
        for (int y = 1; y < 3; y++) {
            for (int x = 1; x < tiles[y].length-1; x++) {
                tiles[y][x] = true;
            }
        }
        for (int x = 1; x < 5; x++) {
            tiles[3][x] = true;
        }
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), StandardCharsets.UTF_8);
        testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        players = Arrays.asList(
                new Pair<>(PlayerType.COURIER, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, false));
        controllerChan.startNewGame("TestMap", tiles, players, Difficulty.NORMAL);
        mapController = controllerChan.getMapController();

        artifactCardStack = action.getArtifactCardStack();
        List<ArtifactCard> cardList = artifactCardStack.draw(28, false);

        for (ArtifactCard cur : cardList) {
            if (fireCard != null && waterCard != null && earthCard != null && airCard != null && sandCard != null && heliCard != null) {
                break;
            }

            if (cur.getType() == ArtifactCardType.FIRE) {
                fireCard = cur;
            }
            if (cur.getType() == ArtifactCardType.WATER) {
                waterCard = cur;
            }
            if (cur.getType() == ArtifactCardType.EARTH) {
                earthCard = cur;
            }
            if (cur.getType() == ArtifactCardType.AIR) {
                airCard = cur;
            }
            if (cur.getType() == ArtifactCardType.SANDBAGS) {
                sandCard = cur;
            }
            if (cur.getType() == ArtifactCardType.HELICOPTER) {
                heliCard = cur;
            }
        }

        pilot = new Pilot("pilot", new Point(4, 2), action);
        courier = new Courier("courier", new Point(4, 2), action);
        explorer = new Explorer("explorer", new Point(5, 2), action);
        navigator = new Navigator("navigator", new Point(4, 2), action);

        handCardsExpected = new ArrayList<>();
        explorer.getHand().add(fireCard);
        handCardsExpected.add(fireCard);
        explorer.getHand().add(waterCard);
        handCardsExpected.add(waterCard);
        explorer.getHand().add(earthCard);
        handCardsExpected.add(earthCard);
        explorer.getHand().add(airCard);
        handCardsExpected.add(airCard);


        moveablePlayers = new ArrayList<Player>();
        moveablePlayers.add(navigator);
        moveablePlayers.add(courier);
    }

    @Before
    public void setUp2() throws Exception {
        controllerChan = TestDummy.getDummyControllerChan();
        activePlayerController = controllerChan.getActivePlayerController();
        inGameView = (InGameView) controllerChan.getInGameViewAUI();

        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);

        players = Arrays.asList(
                new Pair<>(PlayerType.COURIER, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, false),
                new Pair<>(PlayerType.PILOT, false));

        Pair<JavaGame, Action> pair = JavaGame.newGame("test", testMap, Difficulty.NORMAL, players);
        TestDummy.injectJavaGame(controllerChan, pair.getLeft());
        TestDummy.injectCurrentAction(controllerChan, pair.getRight());
        action = pair.getRight();
        javaGame = controllerChan.getJavaGame();

        inGameView = (InGameView) controllerChan.getInGameViewAUI();
        action.getActivePlayer().setActionsLeft(3);

    }


    @Test
    public void testShowMovements() {
        Player activePlayer = action.getActivePlayer();
        Point playerPos = activePlayer.getPosition();

        //Optionen für Courier

        assertSame(activePlayer.getType(), PlayerType.COURIER);
        Assert.assertEquals("Courier not on correct spawn location", new Point(3, 4), playerPos);
        activePlayerController.showMovements(false);
        List<Point> movementPoints = inGameView.getMovementPoints();
        Assert.assertEquals("More/less than 2 tiles are marked as movement option", 2, movementPoints.size());
        Assert.assertTrue("Point 3,3 is not marked as movement option", movementPoints.contains(new Point(3, 3)));
        Assert.assertTrue("Point 2,4 is not marked as movement option", movementPoints.contains(new Point(2, 4)));

        activePlayerController.showMovements(true);
        movementPoints = inGameView.getMovementPoints();
        Assert.assertEquals("More/less than 2 tiles are marked as movement option after enabling non existant special movement on courier", 2, movementPoints.size());
        Assert.assertTrue("Point 3,3 is not marked as movement option after enabling non existant special movement on courier", movementPoints.contains(new Point(3, 3)));
        Assert.assertTrue("Point 2,4 is not marked as movement option after enabling non existant special movement on courier", movementPoints.contains(new Point(2, 4)));


        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        playerPos = activePlayer.getPosition();


        assertSame(activePlayer.getType(), PlayerType.EXPLORER);
        Assert.assertEquals("Explorer not on correct spawn location", new Point(6, 4), playerPos);
        activePlayerController.showMovements(false);
        movementPoints = inGameView.getMovementPoints();
        Assert.assertEquals("More/less than 2 tiles are marked as movement option", 2, movementPoints.size());
        Assert.assertTrue("Point 6,3 is not marked as movement option", movementPoints.contains(new Point(6, 3)));
        Assert.assertTrue("Point 7,4 is not marked as movement option", movementPoints.contains(new Point(7, 4)));

        activePlayerController.showMovements(true);
        movementPoints = inGameView.getMovementPoints();
        Assert.assertEquals("More/less than 4 tiles are marked as movement option after enabling non existant special movement on courier", 4, movementPoints.size());
        Assert.assertTrue("Point 6,3 is not marked as movement option", movementPoints.contains(new Point(6, 3)));
        Assert.assertTrue("Point 7,4 is not marked as movement option", movementPoints.contains(new Point(7, 4)));
        Assert.assertTrue("Point 5,3 is not marked as movement option", movementPoints.contains(new Point(5, 3)));
        Assert.assertTrue("Point 7,3 is not marked as movement option", movementPoints.contains(new Point(7, 3)));


        action.nextPlayerActive();
        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        playerPos = activePlayer.getPosition();


        assertSame(activePlayer.getType(), PlayerType.PILOT);
        Assert.assertEquals("Pilot not on correct spawn location", new Point(6, 3), playerPos);
        activePlayerController.showMovements(false);
        movementPoints = inGameView.getMovementPoints();
        Assert.assertEquals("More/less than 4 tiles are marked as movement option", 4, movementPoints.size());
        for (Point point : adjacentPoints(playerPos, false)) {
            Assert.assertTrue(String.format("Point %d,%d is not marked as movement option", point.xPos, point.yPos), movementPoints.contains(point));
        }

        activePlayerController.showMovements(true);
        movementPoints = inGameView.getMovementPoints();
        for (int y = 1; y < testMap.length - 1; y++) {
            for (int x = 1; x < testMap[y].length - 1; x++) {
                MapTile tile = testMap[y][x];
                if (tile != null && tile.getState() != MapTileState.FLOODED) {
                    Assert.assertTrue(String.format("Point %d,%d is not marked as movement option", x, y), movementPoints.contains(new Point(x, y)));
                }
            }
        }

    }

    @Test
    public void testShowDrainOptions() {
        Player activePlayer = action.getActivePlayer();
        Point playerPos = activePlayer.getPosition();
        testMap[3][3].flood();
        testMap[3][5].flood();
        testMap[4][7].flood();


        assertSame(activePlayer.getType(), PlayerType.COURIER);
        Assert.assertEquals("Courier not on correct spawn location", new Point(3, 4), playerPos);
        activePlayerController.showDrainOptions();
        List<Point> drainPoints = inGameView.getDrainPoints();
        Assert.assertEquals("More/less than 1 tile is marked as drain option. Only 1 flooded tile is in range", 1, drainPoints.size());
        Assert.assertTrue("Point 3,3 is not marked as drain option", drainPoints.contains(new Point(3, 3)));


        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        playerPos = activePlayer.getPosition();

        assertSame(activePlayer.getType(), PlayerType.EXPLORER);
        activePlayerController.showDrainOptions();
        drainPoints = inGameView.getDrainPoints();
        Assert.assertEquals("More/less than 2 tiles are marked as drain option", 2, drainPoints.size());
        Assert.assertTrue("Point 7,4 is not marked as drain option", drainPoints.contains(new Point(7, 4)));
        Assert.assertTrue("Point 6,3 is not marked as drain option", drainPoints.contains(new Point(6, 3)));

    }

    @Test
    public void testShowSpecialAbility() {
        fail("Not yet implemented");
    }

    @Test
    public void testCancelSpecialAbility() {
        fail("Not yet implemented");
    }

    @Test
    public void testShowTransferable() {
        Player activePlayer = action.getActivePlayer();


        assertSame(activePlayer.getType(), PlayerType.COURIER);
        activePlayerController.showTransferable(PlayerType.EXPLORER);
        assertTrue("Courier can not give cards to all players", inGameView.getTransferable());
        activePlayerController.showTransferable(PlayerType.PILOT);
        assertTrue("Courier can not give cards to all players", inGameView.getTransferable());
        activePlayerController.showTransferable(PlayerType.NAVIGATOR);
        assertTrue("Courier can not give cards to all players", inGameView.getTransferable());

        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        activePlayer.setPosition(action.getPlayer(PlayerType.PILOT).getPosition());

        assertSame(activePlayer.getType(), PlayerType.EXPLORER);
        activePlayerController.showTransferable(PlayerType.COURIER);
        assertFalse("Can give cards to player on other tile", inGameView.getTransferable());
        activePlayerController.showTransferable(PlayerType.PILOT);
        assertTrue("Cannot give cards to player on same tile", inGameView.getTransferable());
        activePlayerController.showTransferable(PlayerType.NAVIGATOR);
        assertFalse("Can give cards to player on other tile", inGameView.getTransferable());

    }

    @Test
    public void testTransferCard() {
        Player activePlayer = action.getActivePlayer();
        activePlayer.getHand().add(new ArtifactCard(ArtifactCardType.FIRE));

        assertSame(activePlayer.getType(), PlayerType.COURIER);

        assertEquals(1, activePlayer.getHand().size());
        assertEquals(ArtifactCardType.FIRE, activePlayer.getHand().get(0).getType());
        assertEquals(0, action.getPlayer(PlayerType.PILOT).getHand().size());
        activePlayerController.transferCard(0, PlayerType.PILOT);
        assertEquals("Card not lost after transfer", 0, activePlayer.getHand().size());
        assertEquals("Card not transfered", 1, action.getPlayer(PlayerType.PILOT).getHand().size());
        assertEquals("Wrong card transfered", ArtifactCardType.FIRE, action.getPlayer(PlayerType.PILOT).getHand().get(0).getType());

    }

    @Test
    public void testCollectArtifact() {
        Player activePlayer = action.getActivePlayer();
        for (int i = 0; i < 4; i++) {
            activePlayer.getHand().add(new ArtifactCard(ArtifactCardType.FIRE));
        }
        assertEquals(4, activePlayer.getHand().size());
        activePlayer.setPosition(new Point(8, 3));
        activePlayerController.collectArtifact();
        assertEquals("Artifact cards not lost after collecting artifact", 0, activePlayer.getHand().size());
        assertTrue("Artifact not collected", action.getDiscoveredArtifacts().contains(ArtifactType.FIRE));
        for (int i = 0; i < 4; i++) {
            activePlayer.getHand().add(new ArtifactCard(ArtifactCardType.AIR));
        }
        activePlayer.setPosition(new Point(3, 4));
        activePlayerController.collectArtifact();
        assertEquals("Artifact cards lost after trying to collect artifact which does not exist on the current tile", 4, activePlayer.getHand().size());
        assertEquals("Artifact collected despite the artifact not existing on this tile", 1, action.getDiscoveredArtifacts().size());

        activePlayer.setPosition(new Point(3, 3));
        activePlayerController.collectArtifact();
        assertEquals("Artifact cards lost after trying to collect artifact which does not exist on the current tile", 4, activePlayer.getHand().size());
        assertFalse("Wrong Artifact collected on this tile", action.getDiscoveredArtifacts().contains(ArtifactType.WATER));
        assertEquals("Artifact collected despite the artifact not existing on this tile", 1, action.getDiscoveredArtifacts().size());
    }

    @Test
    public void testMove() throws Exception {
        Player activePlayer = action.getActivePlayer();

        printMap(testMap);

        System.out.println(activePlayer.getActionsLeft());

        assertEquals(activePlayer.getType(), PlayerType.COURIER);
        assertEquals(activePlayer.getPosition(), new Point(3, 4));
        activePlayerController.move(new Point(2, 3), false);
        assertNotEquals("Courier moved to invalid tile", new Point(2, 3), activePlayer.getPosition());
        assertEquals("Courier used action despite not moving", 3, activePlayer.getActionsLeft());
        activePlayerController.move(new Point(3, 3), false);
        assertEquals("Courier did not move to valid tile", new Point(3, 3), activePlayer.getPosition());
        assertEquals("Courier did not use action after moving", 2, activePlayer.getActionsLeft());

        players = Arrays.asList(
                new Pair<>(PlayerType.DIVER, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, false),
                new Pair<>(PlayerType.PILOT, false));

        Pair<JavaGame, Action> pair = JavaGame.newGame("test", testMap, Difficulty.NORMAL, players);
        TestDummy.injectJavaGame(controllerChan, pair.getLeft());
        TestDummy.injectCurrentAction(controllerChan, pair.getRight());
        action = pair.getRight();
        javaGame = controllerChan.getJavaGame();

        inGameView = (InGameView) controllerChan.getInGameViewAUI();
        action.getActivePlayer().setActionsLeft(3);

        testMap[3][6].flood();
        testMap[3][7].flood();
        testMap[3][7].flood();

        activePlayer = action.getActivePlayer();

        printMap(testMap);

        assertEquals(activePlayer.getType(), PlayerType.DIVER);
        assertEquals(activePlayer.getPosition(), new Point(5, 3));
        activePlayerController.move(new Point(7, 2), false);
        assertNotEquals("Diver moved to invalid tile", new Point(7, 2), activePlayer.getPosition());
        assertEquals("Diver used action despite not moving", 3, activePlayer.getActionsLeft());
        activePlayerController.move(new Point(7, 2), true);
        assertEquals("Diver did not move to valid tile", new Point(7, 2), activePlayer.getPosition());
        assertEquals("Diver did not use action after moving", 2, activePlayer.getActionsLeft());

        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();

        assertEquals(activePlayer.getType(), PlayerType.EXPLORER);
        assertEquals(activePlayer.getPosition(), new Point(6, 4));
        activePlayerController.move(new Point(5, 3), false);
        assertNotEquals("Explorer moved to invalid tile", new Point(5, 3), activePlayer.getPosition());
        assertEquals("Explorer used action despite not moving", 3, activePlayer.getActionsLeft());
        activePlayerController.move(new Point(5, 3), true);
        assertEquals("Explorer did not move to valid tile", new Point(5, 3), activePlayer.getPosition());
        assertEquals("Explorer did not use action after moving with special move", 2, activePlayer.getActionsLeft());

        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();

        assertEquals(activePlayer.getType(), PlayerType.PILOT);
        assertEquals(activePlayer.getPosition(), new Point(6, 3));
        activePlayerController.move(new Point(2, 1), false);
        assertNotEquals("Pilot moved to invalid tile", new Point(2, 1), activePlayer.getPosition());
        assertEquals("Pilot used action despite not moving", 3, activePlayer.getActionsLeft());
        activePlayerController.move(new Point(2, 1), true);
        assertEquals("Pilot did not move to valid tile", new Point(2, 1), activePlayer.getPosition());
        assertEquals("Pilot did not use action after moving", 2, activePlayer.getActionsLeft());
    }

    @Test
    public void testMoveOther() {
        assertEquals(action.getPlayer(PlayerType.COURIER).getPosition(), new Point(3, 4));

        activePlayerController.moveOther(Direction.UP, PlayerType.COURIER);
        System.out.println("actionsLeft = " + action.getActivePlayer().getActionsLeft());
        assertEquals(action.getPlayer(PlayerType.COURIER).getPosition(), new Point(3, 3));

        activePlayerController.moveOther(Direction.RIGHT, PlayerType.COURIER);
        System.out.println("actionsLeft = " + action.getActivePlayer().getActionsLeft());
        assertEquals(action.getPlayer(PlayerType.COURIER).getPosition(), new Point(4, 3));

        activePlayerController.moveOther(Direction.DOWN, PlayerType.COURIER);
        System.out.println("actionsLeft = " + action.getActivePlayer().getActionsLeft());
        assertEquals(action.getPlayer(PlayerType.COURIER).getPosition(), new Point(4, 4));

        activePlayerController.moveOther(Direction.LEFT, PlayerType.COURIER);
        System.out.println("actionsLeft = " + action.getActivePlayer().getActionsLeft());
        assertEquals(action.getPlayer(PlayerType.COURIER).getPosition(), new Point(3, 4));
    }

    @Test
    public void testDrain() throws Exception {
        players = Arrays.asList(
                new Pair<>(PlayerType.NAVIGATOR, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.ENGINEER, false),
                new Pair<>(PlayerType.PILOT, false));

        Pair<JavaGame, Action> pair = JavaGame.newGame("test", testMap, Difficulty.NORMAL, players);
        TestDummy.injectJavaGame(controllerChan, pair.getLeft());
        TestDummy.injectCurrentAction(controllerChan, pair.getRight());
        action = pair.getRight();
        javaGame = controllerChan.getJavaGame();

        inGameView = (InGameView) controllerChan.getInGameViewAUI();
        action.getActivePlayer().setActionsLeft(3);

        testMap[3][7].flood();
        testMap[3][3].flood();
        testMap[3][2].flood();
        testMap[2][4].flood();
        testMap[2][4].flood();
        testMap[2][1].flood();
        testMap[3][5].flood();

        printMap(testMap);

        Player activePlayer = action.getActivePlayer();
        activePlayer.setActionsLeft(3);


        assertEquals(activePlayer.getType(), PlayerType.NAVIGATOR);
        assertEquals(activePlayer.getPosition(), new Point(2, 3));
        activePlayerController.drain(new Point(1, 2));
        assertEquals("Invalid tile was drained by Navigator", MapTileState.FLOODED, testMap[2][1].getState());
        assertEquals("Navigator used action despite not draining tile", 3, activePlayer.getActionsLeft());
        activePlayerController.drain(new Point(2, 3));
        assertEquals("Tile which the Navigator was standing on was not drained by Navigator", MapTileState.DRY, testMap[3][2].getState());
        assertEquals("Navigator did not use action despite draining tile", 2, activePlayer.getActionsLeft());


        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        activePlayer.setActionsLeft(3);

        assertEquals(activePlayer.getType(), PlayerType.EXPLORER);
        assertEquals(activePlayer.getPosition(), new Point(6, 4));
        activePlayerController.drain(new Point(6, 4));
        assertEquals("Dry tile was modified by draining", MapTileState.DRY, testMap[4][6].getState());
        assertEquals("Explorer used action despite not draining tile", 3, activePlayer.getActionsLeft());
        activePlayerController.drain(new Point(7, 3));
        assertEquals("Valid tile was not drained by Explorer", MapTileState.DRY, testMap[3][7].getState());
        assertEquals("Explorer did not use action despite draining tile", 2, activePlayer.getActionsLeft());


        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        activePlayer.setActionsLeft(3);

        assertEquals(activePlayer.getType(), PlayerType.ENGINEER);
        assertEquals(activePlayer.getPosition(), new Point(4, 3));
        activePlayerController.drain(new Point(3, 3));
        assertEquals("Valid tile was not drained by Engineer", MapTileState.DRY, testMap[3][3].getState());

        activePlayerController.drain(new Point(4, 2));
        assertEquals("Engineer drained gone tile", MapTileState.GONE, testMap[2][4].getState());
        assertTrue("Engineer wasted extra drain on gone tile", ((Engineer) activePlayer).hasExtraDrain());

        activePlayerController.drain(new Point(5, 3));
        assertEquals("Engineer did not drain valid tile", MapTileState.DRY, testMap[3][5].getState());
        assertEquals("Engineer did not use correct amount of actions", 2, activePlayer.getActionsLeft());
        assertFalse("Engineer still has extra drain after draining 2 tiles", ((Engineer) activePlayer).hasExtraDrain());

    }

    @Test
    public void testShowTip() {
        fail("Not yet implemented");
    }

    @Test
    public void testEndTurn() {
        fail("Not yet implemented");
    }

    private static List<Point> adjacentPoints(Point point, boolean diagonal) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(point.xPos, point.yPos - 1));
        points.add(new Point(point.xPos, point.yPos + 1));
        points.add(new Point(point.xPos - 1, point.yPos));
        points.add(new Point(point.xPos + 1, point.yPos));

        if (diagonal) {
            points.add(new Point(point.xPos - 1, point.yPos - 1));
            points.add(new Point(point.xPos + 1, point.yPos + 1));
            points.add(new Point(point.xPos - 1, point.yPos + 1));
            points.add(new Point(point.xPos + 1, point.yPos - 1));
        }

        return points;
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
