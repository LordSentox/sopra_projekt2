package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
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
import java.util.stream.IntStream;

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
    private MapFull testMap;

    @Before
    public void setUp() throws IOException {
        controllerChan = TestDummy.getDummyControllerChan();
        MapBlackWhite map = new MapBlackWhite();
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < Map.SIZE_X; x++) {
                map.set(true, x, y);
            }
        }
        for (int x = 0; x < 4; x++) {
            map.set(true, x, 3);
        }

        String testMapName = "TestMap";
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        this.testMap = MapUtil.readFullMapFromString(testMapString);
        List<Pair<PlayerType, Boolean>> players = Arrays.asList(new Pair<>(PlayerType.COURIER, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, false));
        controllerChan.startNewGame(testMapName, map, players, Difficulty.NORMAL);
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
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(navigator.getPosition(), new Point(4, 1)), moveablePlayers);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(heliCard));
        Assert.assertEquals("Die Spieler hätten nicht bewegt werden dürfen",
                new Point(4, 1),
                navigator.getPosition());
        Assert.assertEquals("Die Spieler hätten nicht bewegt werden dürfen",
                new Point(4, 1),
                courier.getPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testPlayHelicopterCardGONEMapTile() {
        //teste mit ungültigem Zielfeld(versunkenes maptile)
        IntStream.range(0, 2).forEach(i -> testMap.get(1, 1).flood());

        explorer.getHand().add(heliCard);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(navigator.getPosition(), new Point(2, 2)), moveablePlayers);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
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
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 0, new Pair<>(navigator.getPosition(), new Point(0, 8)), moveablePlayers);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertTrue("Die Spieler hätten nicht bewegt werden dürfen",
                inGameView.getNotifications().contains("Du hattest keine Helikopter Karte!"));
    }

    @Test
    public void testPlayHelicopterCard() {
        action = controllerChan.getCurrentAction();
        //teste mit gültigen Daten und einem Spieler (Helicard wurde abgewofen)
        navigator.getHand().add(heliCard);
        moveablePlayers.clear();
        moveablePlayers.add(PlayerType.EXPLORER);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        inGameCont.playHelicopterCard(PlayerType.NAVIGATOR, 0, new Pair<>(explorer.getPosition(), new Point(1, 1)), moveablePlayers);
        explorer = (Explorer) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.COURIER);
        action = controllerChan.getJavaGame().getPreviousAction();
        Assert.assertFalse("Die Karte hätte gespielt werden müssen.",
                navigator.getHand().contains(heliCard));
        Assert.assertEquals("Der Spieler hätte bewegt werden müssen",
                new Point(1, 1),
                explorer.getPosition());

        //teste mit gültigen Daten und mehreren Spielern (Helicard wurde abgewofen)
        action = controllerChan.getCurrentAction();
        explorer.getHand().add(heliCard);
        moveablePlayers.clear();
        courier.setPosition(navigator.getPosition());
        moveablePlayers.add(PlayerType.NAVIGATOR);
        moveablePlayers.add(PlayerType.COURIER);
        Point startPos = new Point(navigator.getPosition());
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(startPos, new Point(0, 1)), moveablePlayers);
        explorer = (Explorer) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.COURIER);
        action = controllerChan.getJavaGame().getPreviousAction();
        Assert.assertFalse("Die Karte hätte gespielt werden müssen.",
                explorer.getHand().contains(heliCard));
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
                new Point(0, 1),
                navigator.getPosition());
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
                new Point(0, 1),
                courier.getPosition());

        //teste mit Start = Ziel
        action = controllerChan.getCurrentAction();
        explorer.getHand().add(heliCard);
        Point start = navigator.getPosition();
        startPos = new Point(navigator.getPosition());
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(startPos, startPos), moveablePlayers);
        explorer = (Explorer) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.COURIER);
        action = controllerChan.getJavaGame().getPreviousAction();
        Assert.assertFalse("Die Karte hätte gespielt werden müssen.",
                explorer.getHand().contains(heliCard));
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
                start,
                navigator.getPosition());
        Assert.assertEquals("Die Spieler hätten bewegt werden müssen",
               start,
                courier.getPosition());

        //teste mit HeliCard auf HeliPlatz und nicht alle Artefakte gefunden
        action = controllerChan.getCurrentAction();
        Point heliPoint = controllerChan.getCurrentAction().getMap().getPlayerSpawnPoint(PlayerType.PILOT);
        action.getDiscoveredArtifacts().clear();
        explorer.getHand().add(heliCard);
        moveablePlayers.clear();
        moveablePlayers.add(PlayerType.NAVIGATOR);
        moveablePlayers.add(PlayerType.EXPLORER);
        moveablePlayers.add(PlayerType.COURIER);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        navigator.setPosition(heliPoint);
        explorer.setPosition(heliPoint);
        courier.setPosition(heliPoint);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(heliPoint, courier.legalMoves(false).get(0)), moveablePlayers);
        explorer = (Explorer) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.COURIER);
        action = controllerChan.getJavaGame().getPreviousAction();
        Assert.assertFalse("Das Spiel sollte nicht geendet haben",
                action.isGameEnded());
        Assert.assertFalse("Das Spiel hätte nicht gewonnen sein sollen",
                action.isGameWon());

        //teste mit HeliCard auf HeliPlatz und alle Artefakte gefunden
        action = controllerChan.getCurrentAction();
        action.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        action.getDiscoveredArtifacts().add(ArtifactType.WATER);
        action.getDiscoveredArtifacts().add(ArtifactType.EARTH);
        action.getDiscoveredArtifacts().add(ArtifactType.AIR);
        explorer.getHand().add(heliCard);
        heliPoint = controllerChan.getCurrentAction().getMap().getPlayerSpawnPoint(PlayerType.PILOT);
        startPos = new Point (heliPoint);
        moveablePlayers.clear();
        moveablePlayers.add(PlayerType.NAVIGATOR);
        moveablePlayers.add(PlayerType.EXPLORER);
        moveablePlayers.add(PlayerType.COURIER);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        navigator.setPosition(heliPoint);
        explorer.setPosition(heliPoint);
        courier.setPosition(heliPoint);
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 4, new Pair<>(startPos, courier.legalMoves(false).get(0)), moveablePlayers);
        explorer = (Explorer) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.COURIER);
        action = controllerChan.getJavaGame().getPreviousAction();
        Assert.assertTrue("Das Spiel sollte geendet haben",
                action.isGameEnded());
        Assert.assertTrue("Das Spiel hätte gewonnen sein sollen",
                action.isGameWon());

    }

    @Test(expected = IllegalStateException.class)
    public void testPlaySandbagCardNoMapTile() {
        //teste mit ungültigem Zielfeld(kein maptile)
        explorer.getHand().add(sandCard);
        testMap = controllerChan.getCurrentAction().getMap();
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(0, 0));
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertEquals("Die Karte hätte sich nicht ändern dürfen.",
                testMap,
                action.getMap());
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(sandCard));
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaySandbagCardDryMapTile() {
        //teste mit ungültigem Zielfeld(trockenes maptile)

        testMap = controllerChan.getCurrentAction().getMap();
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(1, 1));
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(sandCard));
        Assert.assertEquals("Das MapTile hätte trocken sein sollen",
                MapTileState.DRY,
                testMap.get(1, 1).getState());

    }

    @Test(expected = IllegalStateException.class)
    public void testPlaySandbagCardGoneMapTile() {
        //teste mit ungültigem Zielfeld(versunkenes maptile)
        explorer.getHand().add(sandCard);
        testMap = controllerChan.getCurrentAction().getMap();
        IntStream.range(0, 2).forEach(i -> testMap.get(1, 1).flood());

        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(1, 1));
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertTrue("Die Karte hätte nicht gespielt werden dürfen.",
                explorer.getHand().contains(sandCard));
        Assert.assertEquals("Das MapTile hätte versunken sein sollen",
                testMap.get(1, 1).getState(),
                MapTileState.GONE);
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaySandbagCardNoCard() {
        //teste ohne Sandcard
        explorer.getHand().remove(sandCard);
        testMap = controllerChan.getCurrentAction().getMap();
        testMap.get(2, 1).flood();
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 0, new Point(2, 1));
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertTrue("Das Feld hätte nicht trockengelegt werden dürfen",
                inGameView.getNotifications().contains("Du hattest keine Sandsack Karte!"));
        Assert.assertEquals("Das Feld sollte weiterhin geflutet sein",
                testMap.get(2, 1).getState(),
                MapTileState.FLOODED);
    }

    @Test
    public void testPlaySandbagCard() {
        //teste mit gültigen Daten und einem Spieler (Sandcard wurde abgewofen)
        explorer.getHand().add(sandCard);
        testMap = controllerChan.getCurrentAction().getMap();
        testMap.get(2, 1).flood();
        inGameCont.playSandbagCard(PlayerType.EXPLORER, 4, new Point(2, 1));
        explorer = (Explorer) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.COURIER);
        action = controllerChan.getJavaGame().getPreviousAction();
        Assert.assertFalse("Die Sandsackkarte hätte von der Hand verschwinden müssen",
                explorer.getHand().contains(sandCard));
        Assert.assertEquals("Das Feld sollte jetzt trockengelegt sein",
                testMap.get(2, 1).getState(),
                MapTileState.DRY);

    }

    @Test(expected = IllegalStateException.class)
    public void testDiscardCardTooFew() {
        //teste für zu leere Hand (Anzahl Karten <= 5)
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        inGameCont.discardCard(PlayerType.EXPLORER, 0);
        Assert.assertEquals(handCardsExpected, explorer.getHand());
        Assert.assertTrue("Der Spieler hätte benachrichtigt werden sollen.",
                inGameView.getNotifications().contains("Es darf keine Karte abgeworfen werden!"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDiscardCardTooMany() {
        //teste für Hand mit 6 Karten
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        explorer.getHand().clear();
        handCardsExpected.clear();
        explorer.getHand().add(heliCard);
        handCardsExpected.add(heliCard);
        explorer.getHand().add(sandCard);

        inGameCont.discardCard(PlayerType.EXPLORER, 6);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertEquals(handCardsExpected, explorer.getHand());
    }

    @Test (expected = IllegalStateException.class)
    public void testDiscardCardEmptyHand() {
        //teste für komplett leere Hand (= falscher Index)
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        inGameCont.discardCard(PlayerType.NAVIGATOR, 0);
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getCurrentAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getCurrentAction().getPlayer(PlayerType.COURIER);
        Assert.assertEquals(handCardsExpected, navigator.getHand());
        Assert.assertTrue("Der Spieler hätte benachrichtigt werden sollen.",
                inGameView.getNotifications().contains("Es gab keine Karte zum abwerfen!"));


    }

    @Test
    public void testDiscardCard() {
        explorer = (Explorer) controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER);
        explorer.getHand().clear();
        handCardsExpected.clear();
        explorer.getHand().add(fireCard);
        explorer.getHand().add(sandCard);
        handCardsExpected.add(sandCard);
        explorer.getHand().add(waterCard);
        handCardsExpected.add(waterCard);
        explorer.getHand().add(earthCard);
        handCardsExpected.add(earthCard);
        explorer.getHand().add(airCard);
        handCardsExpected.add(airCard);
        explorer.getHand().add(heliCard);
        handCardsExpected.add(heliCard);

        inGameCont.discardCard(PlayerType.EXPLORER, 0);
        explorer = (Explorer) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.EXPLORER);
        navigator = (Navigator) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.NAVIGATOR);
        courier = (Courier) controllerChan.getJavaGame().getPreviousAction().getPlayer(PlayerType.COURIER);
        action = controllerChan.getJavaGame().getPreviousAction();
        Assert.assertEquals(handCardsExpected, explorer.getHand());
        Assert.assertEquals(5, explorer.getHand().size());
        Assert.assertFalse(explorer.getHand().contains(fireCard));
    }

}
