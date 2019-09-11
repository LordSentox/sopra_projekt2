package de.sopra.javagame.model;

import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class ActionTest {
    private MapTile[][] testMap;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
    }

    @Test
    public void createInitialAction() {
        Action action = Action.createInitialAction(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);

        Assert.assertEquals(0, action.getActivePlayerIndex());
        Assert.assertEquals(28, action.getArtifactCardStack().size());
        Assert.assertEquals("", action.getDescription());
        Assert.assertEquals(new HashSet(), action.getDiscoveredArtifacts());
        Assert.assertEquals(4, action.getPlayers().size());
        Assert.assertEquals(TurnState.FLOOD, action.getState());
        Assert.assertArrayEquals(this.testMap, action.getTiles());
        Assert.assertEquals(0, action.getWaterLevel().getLevel());
        Assert.assertEquals(24, action.getFloodCardStack().size());

        // Einen weiteren Action mit den letzten beiden Klassen erstellen
        Action action2 = Action.createInitialAction(Difficulty.ELITE,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.ENGINEER, false)),
                this.testMap);

        Assert.assertEquals(2, action2.getPlayers().size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void createInitialActionIllegalPlayerType() {
        Action.createInitialAction(Difficulty.LEGENDARY, Arrays.asList(new Pair<>(PlayerType.NONE, true), new Pair<>(PlayerType.NONE, true)), this.testMap);
    }
    
    @Test (expected = NullPointerException.class)
    public void createInitialActionNoDifficulty() {
        Action.createInitialAction(null, Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, true),
                new Pair<>(PlayerType.DIVER, false),
                new Pair<>(PlayerType.COURIER, true)), this.testMap);
    }
    
    @Test
    public void getPlayer() {
        Action action = Action.createInitialAction(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);

        Assert.assertEquals("Der Spielertyp des geholten Spielers hätte Explorer sein müssen",
                                      PlayerType.EXPLORER,
                                      action.getPlayer(PlayerType.EXPLORER).getType());
        Assert.assertNull("Es hätte keinen Pilot geben dürfen", action.getPlayer(PlayerType.PILOT));
        Assert.assertNull("Es hätte kein Spieler zurückgegeben werden dürfen", action.getPlayer(null));
        
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
        Action action = Action.createInitialAction(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);
        Explorer explorer = (Explorer) action.getPlayer(PlayerType.EXPLORER);
        Navigator navigator = (Navigator) action.getPlayer(PlayerType.NAVIGATOR);
        explorer.setPosition(MapUtil.getPlayerSpawnPoint(testMap, PlayerType.NAVIGATOR));

        //test mit beide stehen auf dem gleichen Feld, keine Hand zu voll
        explorer.getHand().add(fireCard);
        boolean transferred = action.transferArtifactCard(fireCard, explorer, navigator);
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
        transferred = action.transferArtifactCard(fireCard, navigator, explorer);
        assertFalse("Die Karte hätte von der Hand des Navigators verschwinden sollen.",
                navigator.getHand().contains(fireCard));
        assertTrue("Der Navigator sollte die Karte erhalten haben.",
                explorer.getHand().contains(fireCard));
        assertTrue("Die Karte hätte übergeben werden müssen.",
                transferred);

        //test mit Bote
        Courier courier = (Courier) action.getPlayer(PlayerType.COURIER);
        courier.getHand().add(earthCard2);
        transferred = action.transferArtifactCard(earthCard2, courier, navigator);
        Assert.assertFalse("Die Karte hätte von der Hand des Boten verschwinden sollen.",
                courier.getHand().contains(earthCard2));
        Assert.assertTrue("Der Navigator sollte die Karte erhalten haben.",
                navigator.getHand().contains(earthCard2));
        Assert.assertTrue("Die Karte hätte übergeben werden müssen.",
                transferred);

        //test ohne gleiches Feld und ohne Bote
        Diver diver = (Diver) action.getPlayer(PlayerType.DIVER);
        explorer.getHand().clear();
        courier.getHand().clear();
        navigator.getHand().clear();
        diver.getHand().add(fireCard);
        transferred = action.transferArtifactCard(fireCard, diver, explorer);
        Assert.assertFalse("Der Forscher sollte die Karte nicht erhalten haben.",
                explorer.getHand().contains(fireCard));
        Assert.assertTrue("Die Karte hätte nicht von der Hand des Tauchers verschwinden sollen.",
                diver.getHand().contains(fireCard));
        Assert.assertFalse("Die Karte hätte nicht übergeben werden dürfen.",
                transferred);

        //test ohne gleiches Feld und Bote ist reciever
        transferred = action.transferArtifactCard(fireCard, diver, courier);
        assertFalse("Der Bote sollte die Karte nicht erhalten haben",
                courier.getHand().contains(fireCard));
        assertTrue("Die Karte hätte nicht von der Hand des Tauchers verschwinden sollen.",
                diver.getHand().contains(fireCard));
        assertFalse("Die Karte hätte nicht übergeben werden dürfen.",
                transferred);

    }

    @Test
    public void copy() {
        Action action = Action.createInitialAction(Difficulty.NOVICE,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, true),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.COURIER, true)),
                this.testMap);
        Action actionCopy = action.copy();

        //teste ob alle Referenzen gleich
        int activePlayerOriginal = action.getActivePlayerIndex();
        String descriptionOriginal = action.getDescription();
        EnumSet<ArtifactType> discoveredArtifactsOriginal = action.getDiscoveredArtifacts();
        List<Player> playersOriginal = action.getPlayers();
        Enum<TurnState> stateOriginal = action.getState();
        MapTile[][] tilesOriginal = action.getTiles();

        //inhaltliche Gleichheit
        assertEquals("Kopie sollte gleichen aktiven Spieler halten", activePlayerOriginal, actionCopy.getActivePlayerIndex());
        assertEquals("Kopie sollte gleiche Beschreibung halten", descriptionOriginal, actionCopy.getDescription());
        assertEquals("Kopie sollte gleiche gefundene Artefakte halten", discoveredArtifactsOriginal, actionCopy.getDiscoveredArtifacts());
        for (int i = 0; i < action.getPlayers().size(); i++) {
            assertEquals("Kopie sollte gleiche Spieler-Liste halten. Index " + i + " unterscheidet sich.",
                    action.getPlayers().get(i).getType(),
                    action.getPlayers().get(i).getType());
        }
        assertEquals("Kopie sollte gleichen Status haben", stateOriginal, actionCopy.getState());
        for (int i = 0; i < tilesOriginal.length; i++) {
            for (int j = 0; j < tilesOriginal[i].length; j++) {
                assertEquals("Inhalt der Karte sollte gleich sein", tilesOriginal[i][j], actionCopy.getTiles()[i][j]);
            }
        }

        //Referenzen sollten andere sein
        assertNotSame("Kopie sollte andere gleiche Beschreibung halten", descriptionOriginal, actionCopy.getDescription());
        assertNotSame("Kopie sollte andere gleiche gefundene Artefakte halten", discoveredArtifactsOriginal, actionCopy.getDiscoveredArtifacts());
        assertNotSame("Kopie sollte andere gleich Liste enthalten", playersOriginal, actionCopy.getPlayers());
        assertNotSame("Kopie sollte andere gleiche Karte halten", tilesOriginal, actionCopy.getTiles());


        //teste ob Änderungen an Copy unabhängig vom Original sind
        actionCopy.nextPlayerActive();
        actionCopy.changeDescription("Spieler " + actionCopy.getActivePlayerIndex() + " ist an der Reihe. Ziehe Flutkarten.");
        actionCopy.getDiscoveredArtifacts().add(ArtifactType.FIRE);
        actionCopy.setState(TurnState.DRAW_ARTIFACT_CARD);

        assertNotEquals("Kopie sollte nicht mehr gleichen aktiven Spieler halten", activePlayerOriginal, actionCopy.getActivePlayerIndex());
        assertNotEquals("Kopie sollte nicht mehr gleiche Beschreibung halten", descriptionOriginal, actionCopy.getDescription());
        assertNotEquals("Kopie sollte nicht mehr gleiche gefundene Artefakte halten", discoveredArtifactsOriginal, actionCopy.getDiscoveredArtifacts());
        for (int i = 0; i < action.getPlayers().size(); i++) {
            assertEquals("Kopie sollte gleiche Spieler-Liste halten. Index " + i + " unterscheidet sich.",
                    action.getPlayers().get(i).getType(),
                    action.getPlayers().get(i).getType());
        }
        assertNotEquals("Kopie sollte nicht mehr gleichen Status haben", stateOriginal, actionCopy.getState());
        assertEquals("Kopie sollte gleiche Karte halten", tilesOriginal, actionCopy.getTiles());
    }
}