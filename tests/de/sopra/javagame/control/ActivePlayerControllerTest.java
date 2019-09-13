package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.TestDummy.InGameView;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
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
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        players = Arrays.asList(
                new Pair<>(PlayerType.COURIER, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, false));
        controllerChan.startNewGame(tiles, players, Difficulty.NORMAL);
        mapController = controllerChan.getMapController();
        
        artifactCardStack = action.getArtifactCardStack();
        List<ArtifactCard> cardList = artifactCardStack.draw(28, false);
        
        for (ArtifactCard cur : cardList){
            if(fireCard != null && waterCard != null && earthCard != null && airCard != null && sandCard != null && heliCard != null){
                break;
            }
            
            if(cur.getType() == ArtifactCardType.FIRE){
                fireCard = cur;
            }
            if(cur.getType() == ArtifactCardType.WATER){
                waterCard = cur;
            }
            if(cur.getType() == ArtifactCardType.EARTH){
                earthCard = cur;
            }
            if(cur.getType() == ArtifactCardType.AIR){
                airCard = cur;
            }
            if(cur.getType() == ArtifactCardType.SANDBAGS){
                sandCard = cur;
            }
            if(cur.getType() == ArtifactCardType.HELICOPTER){
                heliCard = cur;
            }
        }
        
        pilot = new Pilot("pilot", new Point(4,2), action);
        courier = new Courier("courier", new Point(4, 2), action);
        explorer = new Explorer("explorer", new Point(5,2), action);
        navigator = new Navigator("navigator", new Point(4,2), action);
        
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

        action.getPlayer(PlayerType.COURIER).getHand().add(new ArtifactCard(ArtifactCardType.FIRE));
    }
    

    @Test
    public void testShowMovements() {
        Player activePlayer = action.getActivePlayer();
        Point playerPos = activePlayer.getPosition();

        //Optionen für Courier
        if (activePlayer.getType() == PlayerType.COURIER) {
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
        }

        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        playerPos = activePlayer.getPosition();

        if (activePlayer.getType() == PlayerType.EXPLORER) {
            Assert.assertEquals("Explorer not on correct spawn location", new Point(6, 4), playerPos);
            activePlayerController.showMovements(false);
            List<Point> movementPoints = inGameView.getMovementPoints();
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
        }

        action.nextPlayerActive();
        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        playerPos = activePlayer.getPosition();

        if (activePlayer.getType() == PlayerType.PILOT) {
            Assert.assertEquals("Pilot not on correct spawn location", new Point(6, 3), playerPos);
            activePlayerController.showMovements(false);
            List<Point> movementPoints = inGameView.getMovementPoints();
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
    }

    @Test
    public void testShowDrainOptions() {
        Player activePlayer = action.getActivePlayer();
        Point playerPos = activePlayer.getPosition();
        testMap[3][3].flood();
        testMap[3][5].flood();
        testMap[4][7].flood();


        printMap(testMap);

        if (activePlayer.getType() == PlayerType.COURIER) {
            Assert.assertEquals("Courier not on correct spawn location", new Point(3, 4), playerPos);
            activePlayerController.showDrainOptions();
            List<Point> drainPoints = inGameView.getDrainPoints();
            Assert.assertEquals("More/less than 1 tile is marked as drain option. Only 1 flooded tile is in range", 1, drainPoints.size());
            Assert.assertTrue("Point 3,3 is not marked as drain option", drainPoints.contains(new Point(3, 3)));
        }

        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        playerPos = activePlayer.getPosition();

        if (activePlayer.getType() == PlayerType.EXPLORER) {
            activePlayerController.showDrainOptions();
            List<Point> drainPoints = inGameView.getDrainPoints();
            Assert.assertEquals("More/less than 2 tiles are marked as drain option", 2, drainPoints.size());
            Assert.assertTrue("Point 7,4 is not marked as drain option", drainPoints.contains(new Point(7, 4)));
            Assert.assertTrue("Point 6,3 is not marked as drain option", drainPoints.contains(new Point(6, 3)));
        }
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

        if (activePlayer.getType() == PlayerType.COURIER) {
            activePlayerController.showTransferable(PlayerType.EXPLORER);
            assertTrue("Courier can not give cards to all players", inGameView.getTransferable());
            activePlayerController.showTransferable(PlayerType.PILOT);
            assertTrue("Courier can not give cards to all players", inGameView.getTransferable());
            activePlayerController.showTransferable(PlayerType.NAVIGATOR);
            assertTrue("Courier can not give cards to all players", inGameView.getTransferable());
        }

        action.nextPlayerActive();
        activePlayer = action.getActivePlayer();
        activePlayer.setPosition(action.getPlayer(PlayerType.PILOT).getPosition());

        if (activePlayer.getType() == PlayerType.EXPLORER) {
            activePlayerController.showTransferable(PlayerType.COURIER);
            assertFalse("Can give cards to player on other tile", inGameView.getTransferable());
            activePlayerController.showTransferable(PlayerType.PILOT);
            assertTrue("Cannot give cards to player on same tile", inGameView.getTransferable());
            activePlayerController.showTransferable(PlayerType.NAVIGATOR);
            assertFalse("Can give cards to player on other tile", inGameView.getTransferable());
        }
    }

    @Test
    public void testTransferCard() {
        Player activePlayer = action.getActivePlayer();
        if (activePlayer.getType() == PlayerType.COURIER) {
            assertEquals(1, activePlayer.getHand().size());
            assertEquals(ArtifactCardType.FIRE, activePlayer.getHand().get(0).getType());
            assertEquals(0, action.getPlayer(PlayerType.PILOT).getHand().size());
            activePlayerController.transferCard(0, PlayerType.PILOT);
            assertEquals("Card not lost after transfer", 0, activePlayer.getHand().size());
            assertEquals("Card not transfered", 1, action.getPlayer(PlayerType.PILOT).getHand().size());
            assertEquals("Wrong card transfered", ArtifactCardType.FIRE, action.getPlayer(PlayerType.PILOT).getHand().get(0).getType());
        }
    }

    @Test
    public void testCollectArtifact() {
        fail("Not yet implemented");
    }

    @Test
    public void testMove() {
        fail("Not yet implemented");
    }

    @Test
    public void testMoveOther() {
        fail("Not yet implemented");
    }

    @Test
    public void testDrain() {
        fail("Not yet implemented");
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

    private static void printMap(MapTile[][] tiles){
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
