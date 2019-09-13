package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
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

public class InGameUserControllerTest {

    private ControllerChan controllerChan;
    private MapController mapController;
    private InGameUserController inGameCont;
    private TestDummy.InGameView inGameView;
    private JavaGame javaGame;
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
    private CardStack<ArtifactCard> artifactCardStack;
    private List<ArtifactCard> handCardsExpected;
    //FIXME untypisierte liste. im Laufe der tests werden Player und PlayerType Objekte in die Liste eingefuegt
    private List moveablePlayers;
    private int[][] testMapNumbers;
    private MapTile[][] testMap;

    @Before
    public void setUp() throws IOException {
        controllerChan = TestDummy.getDummyControllerChan();
        boolean[][] tiles = new boolean[12][12];
        for (int y = 1; y < 3; y++) {
            for (int x = 1; x < tiles[y].length - 1; x++) {
                tiles[y][x] = true;
            }
        }
        for (int x = 1; x < 5; x++) {
            tiles[3][x] = true;
        }
        String testMapName = "TestMap";
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        testMapNumbers = MapUtil.readFullMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        List<Pair<PlayerType, Boolean>> players = Arrays.asList(new Pair<>(PlayerType.COURIER, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, false));
        controllerChan.startNewGame(testMapName, tiles, players, Difficulty.NORMAL);
        mapController = controllerChan.getMapController();
        inGameCont = controllerChan.getInGameUserController();
        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
        javaGame = controllerChan.getJavaGame();
        action = controllerChan.getCurrentAction();
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

        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        ;

        handCardsExpected = new ArrayList<>();
        explorer.getHand().add(fireCard);
        handCardsExpected.add(fireCard);
        explorer.getHand().add(waterCard);
        handCardsExpected.add(waterCard);
        explorer.getHand().add(earthCard);
        handCardsExpected.add(earthCard);
        explorer.getHand().add(airCard);
        handCardsExpected.add(airCard);
        navigator.setPosition(courier.getPosition());

        moveablePlayers = new ArrayList<Player>();
        moveablePlayers.add(PlayerType.NAVIGATOR);
        moveablePlayers.add(PlayerType.COURIER);

    }

    @Test (expected = IllegalStateException.class)
    public void testPlayHelicopterCardNullMapTile() {
        //teste mit ungültigem Zielfeld(kein maptile)
        explorer.getHand().add(heliCard);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(navigator.getPosition(), new Point(1, 1)), moveablePlayers);
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(heliCard));
        Assert.assertEquals("Die Spieler hätten nicht bewegt werden dürfen",
                new Point(5, 2),
                navigator.getPosition());
        Assert.assertEquals("Die Spieler hätten nicht bewegt werden dürfen",
                new Point(5, 2),
                courier.getPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testPlayHelicopterCardGONEMapTile() {
        //teste mit ungültigem Zielfeld(versunkenes maptile)
        testMap[2][2].flood();
        testMap[2][2].flood();
        explorer.getHand().add(heliCard);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(navigator.getPosition(), new Point(2, 2)), moveablePlayers);
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(heliCard));
        Assert.assertEquals("Die Spieler hätten nicht bewegt werden dürfen",
                new Point(2, 2),
                navigator.getPosition());
        Assert.assertEquals("Die Spieler hätten nicht bewegt werden dürfen",
                new Point(5, 2),
                courier.getPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testPlayHelicopterCardNoHeliCard() {
        //teste ohne helicard
        explorer.getHand().remove(heliCard);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 0, new Pair<>(navigator.getPosition(), new Point(1, 9)), moveablePlayers);
        Assert.assertTrue("Die Spieler hätten nicht bewegt werden dürfen",
                inGameView.getNotifications().contains("Du hattest keine Helikopter Karte!"));
    }

    @Test
    public void testPlayHelicopterCard() {
        Action currentAction = javaGame.getPreviousAction();
        //teste mit gültigen Daten und einem Spieler (Helicard wurde abgewofen)
        testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        navigator.getHand().add(heliCard);
        moveablePlayers.clear();
        moveablePlayers.add(PlayerType.EXPLORER);
        inGameCont.playHelicopterCard(PlayerType.NAVIGATOR, 0, new Pair<>(explorer.getPosition(), new Point(2, 2)), moveablePlayers);
        Assert.assertFalse("Die Karte hätte gespielt werden müssen.",
                navigator.getHand().contains(heliCard));
        Assert.assertEquals("Der Spieler hätte bewegt werden müssen",
                new Point(2, 2),
                explorer.getPosition());

        //teste mit gültigen Daten und mehreren Spielern (Helicard wurde abgewofen)
        explorer.getHand().add(heliCard);
        moveablePlayers.clear();
        moveablePlayers.add(PlayerType.NAVIGATOR);
        moveablePlayers.add(PlayerType.COURIER);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(navigator.getPosition(), new Point(2, 3)), moveablePlayers);
        Assert.assertFalse("Die Karte hätte gespielt werden müssen.",
                explorer.getHand().contains(heliCard));
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
                new Point(2, 3),
                navigator.getPosition());
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
                new Point(2, 3),
                courier.getPosition());

        //teste mit Start = Ziel
        explorer.getHand().add(heliCard);
        Point start = navigator.getPosition();
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(navigator.getPosition(), navigator.getPosition()), moveablePlayers);
        Assert.assertFalse("Die Karte hätte gespielt werden müssen.",
                explorer.getHand().contains(heliCard));
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
                start,
                navigator.getPosition());
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
               start,
                courier.getPosition());

        //teste mit HeliCard auf HeliPlatz und nicht alle Artefakte gefunden
        Point heliPoint = MapUtil.getPlayerSpawnPoint(controllerChan.getCurrentAction().getTiles(), PlayerType.EXPLORER);
        action.getDiscoveredArtifacts().clear();
        explorer.getHand().add(heliCard);
        moveablePlayers.clear();
        moveablePlayers.add(PlayerType.NAVIGATOR);
        moveablePlayers.add(PlayerType.EXPLORER);
        moveablePlayers.add(PlayerType.COURIER);
        navigator.setPosition(heliPoint);
        explorer.setPosition(heliPoint);
        courier.setPosition(heliPoint);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(courier.getPosition(), heliPoint), moveablePlayers);
        Assert.assertFalse("Das Spiel sollte nicht geendet haben",
                currentAction.isGameEnded());
        Assert.assertFalse("Das Spiel hätte nicht gewonnen sein sollen",
                currentAction.isGameWon());

        //teste mit HeliCard auf HeliPlatz und alle Artefakte gefunden
        action.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        action.getDiscoveredArtifacts().add(ArtifactType.WATER);
        action.getDiscoveredArtifacts().add(ArtifactType.EARTH);
        action.getDiscoveredArtifacts().add(ArtifactType.AIR);
        explorer.getHand().add(heliCard);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(courier.getPosition(), heliPoint), moveablePlayers);
        Assert.assertTrue("Das Spiel sollte geendet haben",
                currentAction.isGameEnded());
        Assert.assertTrue("Das Spiel hätte gewonnen sein sollen",
                currentAction.isGameWon());

    }

    @Test
    public void testPlaySandbagCard() {
        explorer.getHand().add(heliCard);
        moveablePlayers.clear();
        moveablePlayers.add(PlayerType.EXPLORER);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(explorer.getPosition(), new Point(4, 2)), moveablePlayers);


        //teste mit ungültigem Zielfeld(kein maptile)
        explorer.getHand().add(sandCard);
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(1, 1));
        //TODO prüfe ob dieser Test funktioniert
        Assert.assertArrayEquals("Die Karte hätte sich nicht ändern dürfen.",
                testMap,
                action.getTiles());
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(sandCard));


        //teste mit ungültigem Zielfeld(trockenes maptile)
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(9, 5));
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(sandCard));
        Assert.assertEquals("Das MapTile hätte trocken sein sollen",
                MapTileState.DRY,
                testMap[8][5].getState());

        //teste mit ungültigem Zielfeld(versunkenes maptile)
        explorer.getHand().add(sandCard);
        testMap[5][9].flood();
        testMap[5][9].flood();
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(9, 5));
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(sandCard));
        Assert.assertEquals("Das MapTile hätte versunken sein sollen",
                testMap[8][5].getState(),
                MapTileState.GONE);

        //teste ohne Sandcard
        explorer.getHand().remove(sandCard);
        testMap[8][5].flood();
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 0, new Point(5, 2));
        Assert.assertTrue("Das Feld hätte nicht trockengelegt werden dürfen",
                inGameView.getNotifications().contains("Du hattest keine Sandsack Karte!"));
        Assert.assertEquals("Das Feld sollte weiterhin geflutet sein",
                testMap[8][5].getState(),
                MapTileState.FLOODED);


        //teste mit gültigen Daten und einem Spieler (Sandcard wurde abgewofen)
        explorer.getHand().add(sandCard);
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(5, 8));
        Assert.assertFalse("Die Sandsackkarte hätte von der Hand verschwinden müssen",
                explorer.getHand().contains(sandCard));
        Assert.assertEquals("Das Feld sollte jetzt trockengelegt sein",
                testMap[8][5].getState(),
                MapTileState.DRY);

    }

    @Test
    public void testDiscardCard() {

        //teste für zu leere Hand (Anzahl Karten <= 5)
        inGameCont.discardCard(PlayerType.EXPLORER, 0);
        Assert.assertEquals(handCardsExpected, explorer.getHand());
        Assert.assertTrue("Der Spieler hätte benachrichtigt werden sollen.",
                inGameView.getNotifications().contains("Es darf keine Karte abgeworfen werden!"));

        //teste für Hand mit 6 Karten
        explorer.getHand().add(heliCard);
        handCardsExpected.add(heliCard);
        explorer.getHand().add(sandCard);

        inGameCont.discardCard(PlayerType.EXPLORER, 6);
        Assert.assertEquals(handCardsExpected, explorer.getHand());

        //teste für komplett leere Hand (= falscher Index)
        inGameCont.discardCard(PlayerType.NAVIGATOR, 0);
        Assert.assertEquals(handCardsExpected, navigator.getHand());
        Assert.assertTrue("Der Spieler hätte benachrichtigt werden sollen.",
                inGameView.getNotifications().contains("Es gab keine Karte zum abwerfen!"));


    }

}
