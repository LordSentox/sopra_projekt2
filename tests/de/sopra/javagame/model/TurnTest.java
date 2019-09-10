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

import static org.junit.Assert.*;

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
        Assert.assertEquals(24, turn.getFloodCardStack().size());

        // Einen weiteren Turn mit den letzten beiden Klassen erstellen
        Turn turn2 = Turn.createInitialTurn(Difficulty.ELITE,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.ENGINEER, false)),
                this.testMap);

        Assert.assertEquals(2, turn2.getPlayers().size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void createInitialTurnIllegalPlayerType() {
        Turn.createInitialTurn(Difficulty.LEGENDARY, Arrays.asList(new Pair<>(PlayerType.NONE, true), new Pair<>(PlayerType.NONE, true)), this.testMap);
    }
    
    @Test (expected = NullPointerException.class)
    public void createInitialTurnNoDifficulty() {
        Turn.createInitialTurn(null, Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, true),
                new Pair<>(PlayerType.DIVER, false),
                new Pair<>(PlayerType.COURIER, true)), this.testMap);
    }
    
    @Test
    public void getPlayer() {
        Turn turn = Turn.createInitialTurn(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);

        Assert.assertEquals("", PlayerType.EXPLORER, turn.getPlayer(PlayerType.EXPLORER).getType());
        Assert.assertNull("", turn.getPlayer(PlayerType.PILOT));
        Assert.assertNull("", turn.getPlayer(null));
        
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
        assertTrue("Der Navigator sollte die Karte erhalten haben.",
                navigator.getHand().contains(fireCard));
        assertFalse("Die Karte hätte von der Hand des Explorers verschwinden sollen.",
                explorer.getHand().contains(fireCard));
        assertTrue("Die Karte hätte übergeben werden müssen.",
                transferred);

        //test mit beide stehen auf dem gleichen Feld, Explorer Hand zu voll
        explorer.getHand().add(fireCard2);
        explorer.getHand().add(waterCard);
        explorer.getHand().add(waterCard2);
        explorer.getHand().add(earthCard);
        explorer.getHand().add(airCard);
        transferred = turn.transferArtifactCard(fireCard, navigator, explorer);
        assertFalse("Die Karte hätte von der Hand des Navigators verschwinden sollen.",
                navigator.getHand().contains(fireCard));
        assertTrue("Der Navigator sollte die Karte erhalten haben.",
                explorer.getHand().contains(fireCard));
        assertTrue("Die Karte hätte übergeben werden müssen.",
                transferred);

        //test mit Bote
        Courier courier = (Courier) turn.getPlayer(PlayerType.COURIER);
        courier.getHand().add(earthCard2);
        transferred = turn.transferArtifactCard(earthCard2, courier, navigator);
        Assert.assertFalse("Die Karte hätte von der Hand des Boten verschwinden sollen.",
                courier.getHand().contains(earthCard2));
        Assert.assertTrue("Der Navigator sollte die Karte erhalten haben.",
                navigator.getHand().contains(earthCard2));
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
        Assert.assertFalse("Die Karte hätte nicht übergeben werden dürfen.",
                transferred);

        //test ohne gleiches Feld und Bote ist reciever
        transferred = turn.transferArtifactCard(fireCard, diver, courier);
        assertFalse("Der Bote sollte die Karte nicht erhalten haben",
                courier.getHand().contains(fireCard));
        assertTrue("Die Karte hätte nicht von der Hand des Tauchers verschwinden sollen.",
                diver.getHand().contains(fireCard));
        assertFalse("Die Karte hätte nicht übergeben werden dürfen.",
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

        //inhaltliche Gleichheit
        assertEquals("Kopie sollte gleichen aktiven Spieler halten", activePlayerOriginal, turnCopy.getActivePlayer());
        assertEquals("Kopie sollte gleiche Beschreibung halten", descriptionOriginal, turnCopy.getDescription());
        assertEquals("Kopie sollte gleiche gefundene Artefakte halten", discoveredArtifactsOriginal, turnCopy.getDiscoveredArtifacts());
        for (int i = 0; i < turn.getPlayers().size(); i++) {
            assertEquals("Kopie sollte gleiche Spieler-Liste halten. Index " + i + " unterscheidet sich.",
                    turn.getPlayers().get(i).getType(),
                    turn.getPlayers().get(i).getType());
        }
        assertEquals("Kopie sollte gleichen Status haben", stateOriginal, turnCopy.getState());
        for (int i = 0; i < tilesOriginal.length; i++) {
            for (int j = 0; j < tilesOriginal[i].length; j++) {
                assertEquals("Inhalt der Karte sollte gleich sein", tilesOriginal[i][j], turnCopy.getTiles()[i][j]);
            }
        }

        //Referenzen sollten andere sein
        assertNotSame("Kopie sollte andere gleiche Beschreibung halten", descriptionOriginal, turnCopy.getDescription());
        assertNotSame("Kopie sollte andere gleiche gefundene Artefakte halten", discoveredArtifactsOriginal, turnCopy.getDiscoveredArtifacts());
        assertNotSame("Kopie sollte andere gleich Liste enthalten", playersOriginal, turnCopy.getPlayers());
        assertNotSame("Kopie sollte andere gleiche Karte halten", tilesOriginal, turnCopy.getTiles());


        //teste ob Änderungen an Copy unabhängig vom Original sind
        turnCopy.nextPlayerActive();
        turnCopy.changeDescription("Spieler " + turnCopy.getActivePlayer() + " ist an der Reihe. Ziehe Flutkarten.");
        turnCopy.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        turnCopy.setState(TurnState.DRAW_ARTIFACT_CARD);

        assertNotEquals("Kopie sollte nicht mehr gleichen aktiven Spieler halten", activePlayerOriginal, turnCopy.getActivePlayer());
        assertNotEquals("Kopie sollte nicht mehr gleiche Beschreibung halten", descriptionOriginal, turnCopy.getDescription());
        assertNotEquals("Kopie sollte nicht mehr gleiche gefundene Artefakte halten", discoveredArtifactsOriginal, turnCopy.getDiscoveredArtifacts());
        for (int i = 0; i < turn.getPlayers().size(); i++) {
            assertEquals("Kopie sollte gleiche Spieler-Liste halten. Index " + i + " unterscheidet sich.",
                    turn.getPlayers().get(i).getType(),
                    turn.getPlayers().get(i).getType());
        }
        assertNotEquals("Kopie sollte nicht mehr gleichen Status haben", stateOriginal, turnCopy.getState());
        assertEquals("Kopie sollte gleiche Karte halten", tilesOriginal, turnCopy.getTiles());
    }
}