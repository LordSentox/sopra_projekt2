package de.sopra.javagame.model;

import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class TurnTest {
    private MapTile[][] testMap;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), "UTF-8");
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
    }

    @Test
    public void createInitialTurn() {
        Turn turn = Turn.createInitialTurn(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);

        Assert.assertEquals(0, turn.getActivePlayer());
        Assert.assertEquals(28, turn.getArtifactCardStack().size());
        Assert.assertEquals("", turn.getDescription());
        Assert.assertEquals(new HashSet(), turn.getDiscoveredArtifacts());
        Assert.assertEquals(4, turn.getPlayers().size());
        Assert.assertEquals(TurnState.FLOOD, turn.getState());
        Assert.assertArrayEquals(this.testMap, turn.getTiles());
        Assert.assertEquals(0, turn.getWaterLevel().getLevel());

        // Einen weiteren Turn mit den letzten beiden Klassen erstellen
        Turn turn2 = Turn.createInitialTurn(Difficulty.ELITE,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.ENGINEER, false)),
                this.testMap);

        Assert.assertEquals(2, turn2.getPlayers().size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void createInitialTurnIllegalPlayerType() {
        Turn.createInitialTurn(Difficulty.LEGENDARY, Collections.singletonList(new Pair<>(PlayerType.NONE, true)), this.testMap);
    }

    @Test
    public void transferArtifactCard() {
        ArtifactCard fireCard = new ArtifactCard(ArtifactCardType.FIRE);
        ArtifactCard fireCard2 = new ArtifactCard(ArtifactCardType.FIRE);
        ArtifactCard waterCard = new ArtifactCard(ArtifactCardType.WATER);
        ArtifactCard waterCard2 = new ArtifactCard(ArtifactCardType.WATER);
        ArtifactCard earthCard = new ArtifactCard(ArtifactCardType.EARTH);
        ArtifactCard earthCard2 = new ArtifactCard(ArtifactCardType.EARTH);
        ArtifactCard airCard = new ArtifactCard(ArtifactCardType.AIR);
        Turn turn = Turn.createInitialTurn(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);
        Explorer explorer = (Explorer) turn.getPlayer(PlayerType.EXPLORER);
        Navigator navigator = (Navigator) turn.getPlayer(PlayerType.NAVIGATOR);
        explorer.setPosition(MapUtil.getPlayerSpawnPoint(testMap, PlayerType.NAVIGATOR));

        //test mit beide stehen auf dem gleichen Feld, keine Hand zu voll
        explorer.getHand().add(fireCard);
        boolean transferred = turn.transferArtifactCard(fireCard, explorer, navigator);
        Assert.assertTrue("Der Navigator sollte die Karte erhalten haben.",
                            navigator.getHand().contains(fireCard));
        Assert.assertFalse("Die Karte hätte von der Hand des Explorers verschwinden sollen.",
                            explorer.getHand().contains(fireCard));
        Assert.assertTrue("Die Karte hätte übergeben werden müssen.",
                            transferred);

        //test mit beide stehen auf dem gleichen Feld, Explorer Hand zu voll
        explorer.getHand().add(fireCard2);
        explorer.getHand().add(waterCard);
        explorer.getHand().add(waterCard2);
        explorer.getHand().add(earthCard);
        explorer.getHand().add(airCard);
        transferred = turn.transferArtifactCard(fireCard, navigator, explorer);
        Assert.assertFalse("Die Karte hätte von der Hand des Navigators verschwinden sollen.",
                            navigator.getHand().contains(fireCard));
        Assert.assertTrue("Der Navigator sollte die Karte erhalten haben.",
                            explorer.getHand().contains(fireCard));
        Assert.assertTrue("Die Karte hätte übergeben werden müssen.",
                            transferred);

        //test mit Bote
        Courier courier = (Courier) turn.getPlayer(PlayerType.COURIER);
        courier.getHand().add(earthCard2);
        transferred = turn.transferArtifactCard(earthCard2, courier, navigator);
        transferred = turn.transferArtifactCard(fireCard, navigator, explorer);
        Assert.assertFalse("Die Karte hätte von der Hand des Boten verschwinden sollen.",
                courier.getHand().contains(fireCard));
        Assert.assertTrue("Der Navigator sollte die Karte erhalten haben.",
                navigator.getHand().contains(fireCard));
        Assert.assertTrue("Die Karte hätte übergeben werden müssen.",
                transferred);

        //test ohne gleiches Feld und ohne Bote
        Diver diver = (Diver) turn.getPlayer(PlayerType.DIVER);
        explorer.getHand().clear();
        courier.getHand().clear();
        navigator.getHand().clear();
        diver.getHand().add(fireCard);
        transferred = turn.transferArtifactCard(fireCard, diver, explorer);
        Assert.assertFalse("Der Forscher sollte die Karte nicht erhalten haben.",
                explorer.getHand().contains(fireCard));
        Assert.assertTrue("Die Karte hätte nicht von der Hand des Tauchers verschwinden sollen.",
                diver.getHand().contains(fireCard));
        Assert.assertTrue("Die Karte hätte nicht übergeben werden dürfen.",
                transferred);

        //test ohne gleiches Feld und Bote ist reciever
        transferred = turn.transferArtifactCard(fireCard, diver, courier);
        Assert.assertFalse("Der Bote sollte die Karte nicht erhalten haben",
                courier.getHand().contains(fireCard));
        Assert.assertTrue("Die Karte hätte nicht von der Hand des Tauchers verschwinden sollen.",
                diver.getHand().contains(fireCard));
        Assert.assertFalse("Die Karte hätte nicht übergeben werden dürfen.",
                transferred);

    }

    @Test
    public void copy() {
        Turn turn = Turn.createInitialTurn(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);
        Turn turnCopy = turn.copy();

        //teste ob alle Referenzen gleich
        int activePlayerOriginal = turn.getActivePlayer();
        String descriptionOriginal = turn.getDescription();
        EnumSet<ArtifactType> discoveredArtifactsOriginal = turn.getDiscoveredArtifacts();
        List<Player> playersOriginal = turn.getPlayers();
        Enum<TurnState> stateOriginal = turn.getState();
        MapTile[][] tilesOriginal = turn.getTiles();


        int activePlayerCopy = turnCopy.getActivePlayer();
        String descriptionCopy = turnCopy.getDescription();
        EnumSet<ArtifactType> discoveredArtifactsCopy = turnCopy.getDiscoveredArtifacts();
        List<Player> playersCopy = turnCopy.getPlayers();
        Enum<TurnState> stateCopy = turnCopy.getState();
        MapTile[][] tilesCopy = turnCopy.getTiles();

        Assert.assertEquals("Kopie sollte gleichen aktiven Spieler halten", turn.getActivePlayer(), turnCopy.getActivePlayer());
        Assert.assertEquals("Kopie sollte gleiche Beschreibung halten", turn.getDescription(), turnCopy.getDescription());
        Assert.assertEquals("Kopie sollte gleiche gefundene Artefakte halten", turn.getDiscoveredArtifacts(), turnCopy.getDiscoveredArtifacts());
        Assert.assertEquals("Kopie sollte gleiche Spieler-Liste halten", turn.getPlayers(), turnCopy.getPlayers());
        Assert.assertEquals("Kopie sollte gleichen Status haben", turn.getState(), turnCopy.getState());
        Assert.assertEquals("Kopie sollte gleiche Karte halten", turn.getTiles(), turnCopy.getTiles());


        //teste ob Änderungen an Copy unabhängig vom Original sind
        turnCopy.nextPlayerActive();
        turnCopy.changeDescription("Spieler " + turnCopy.getActivePlayer() + " ist an der Reihe. Ziehe Flutkarten.");
        turnCopy.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        turnCopy.setState(TurnState.FLOOD);

        Assert.assertNotEquals("Kopie sollte nicht mehr gleichen aktiven Spieler halten", turn.getActivePlayer(), turnCopy.getActivePlayer());
        Assert.assertNotEquals("Kopie sollte nicht mehr gleiche Beschreibung halten", turn.getDescription(), turnCopy.getDescription());
        Assert.assertNotEquals("Kopie sollte nicht mehr gleiche gefundene Artefakte halten", turn.getDiscoveredArtifacts(), turnCopy.getDiscoveredArtifacts());
        Assert.assertEquals("Kopie sollte gleiche Spieler-Liste halten", turn.getPlayers(), turnCopy.getPlayers());
        Assert.assertNotEquals("Kopie sollte nicht mehr gleichen Status haben", turn.getState(), turnCopy.getState());
        Assert.assertEquals("Kopie sollte gleiche Karte halten", turn.getTiles(), turnCopy.getTiles());
    }
}