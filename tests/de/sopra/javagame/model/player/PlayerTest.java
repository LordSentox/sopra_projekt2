package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PlayerTest {
    MapTile[][] testMap;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
    }

    @Test
    public void legalMoves() {
        fail("Not yet implemented");
    }

    @Test
    public void move() {
        fail("Not yet implemented");
    }

    @Test
    public void canMoveOthers() {
        fail("Not yet implemented");
    }

    @Test
    public void forcePush() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);

        Player pilot = action.getPlayers().get(0);
        Player navigator = action.getPlayers().get(1);

        // Teste einen gültigen force-push des Navigators auf ein Inselfeld der Karte
        action.nextPlayerActive();
        Point oldPos = new Point(pilot.getPosition()); // Position des Piloten
        assertTrue("Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush(Direction.UP, pilot));
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.xPos, pilot.getPosition().xPos);
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.yPos - 1, pilot.getPosition().yPos);

        // Der Spieler soll keine Aktionen mehr haben, damit extraPush auch notwendig ist
        navigator.setActionsLeft(0);

        // Der zweite muss auch noch gültig sein, denn es handelt sich um den extra-push
        oldPos = new Point(pilot.getPosition()); // Position des Piloten
        assertTrue("Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush(Direction.RIGHT, pilot));
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.xPos + 1, pilot.getPosition().xPos);
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.yPos, pilot.getPosition().yPos);

        // Da der Spieler keine Aktionen mehr hat muss der dritte forcePush fehlschlagen
        oldPos = new Point(pilot.getPosition());
        assertFalse("Spieler wurde bewegt, obwohl der Navigator keine Aktion dafür hat", navigator.forcePush(Direction.RIGHT, pilot));
        assertEquals("Spieler wurde bewegt, obwohl er nicht sollte", oldPos, pilot.getPosition());
    }

    @Test
    public void drainablePositions() {
        fail("Not yet implemented");
    }

    @Test
    public void drain() {
        fail("Not yet implemented");
    }

    @Test
    public void collectArtifact() {
        fail("Not yet implemented");
    }

    @Test
    public void legalReceivers() {
        fail("Not yet implemented");
    }

    @Test
    public void getType() {
        fail("Not yet implemented");
    }
}