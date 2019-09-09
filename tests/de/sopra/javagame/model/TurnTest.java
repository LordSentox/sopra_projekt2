package de.sopra.javagame.model;

import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.fail;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class TurnTest {
    MapTile[][] testMap;

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
        Assert.assertEquals(this.testMap, turn.getTiles());
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
    public void forcePush() {
        Turn turn = Turn.createInitialTurn(Difficulty.NORMAL,
        Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                new Pair<>(PlayerType.NAVIGATOR, false)),
        this.testMap);

        Player Pilot = turn.getPlayers().get(0);
        Player Navigator = turn.getPlayers().get(1);

        // Teste einen gültigen force-push des Navigators auf ein Inselfeld der Karte
        turn.nextPlayerActive();
        Point oldPos = new Point(Pilot.getPosition()); // Position des Piloten
        Assert.assertTrue("Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", turn.forcePush(Direction.UP, Navigator, Pilot));
        Assert.assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.x, Pilot.getPosition().x);
        Assert.assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.y - 1, Pilot.getPosition().x);
    }

    @Test
    public void transferArtifactCard() {
    }

    @Test
    public void copy() {
        fail("Copy test noch nicht implementiert");
    }
}