package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
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

public class PlayerTest {
    MapTile[][] testMap;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), "UTF-8");
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
    }

    @Test
    public void legalMoves() {
    }

    @Test
    public void move() {
    }

    @Test
    public void canMoveOthers() {
    }

    @Test
    public void forcePush() {
        Turn turn = Turn.createInitialTurn(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);

        Player pilot = turn.getPlayers().get(0);
        Player navigator = turn.getPlayers().get(1);

        // Teste einen gültigen force-push des Navigators auf ein Inselfeld der Karte
        turn.nextPlayerActive();
        Point oldPos = new Point(pilot.getPosition()); // Position des Piloten
        Assert.assertTrue("Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush(Direction.UP, pilot));
        Assert.assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.x, pilot.getPosition().x);
        Assert.assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.y - 1, pilot.getPosition().y);

        // Der Spieler soll keine Aktionen mehr haben, damit extraPush auch notwendig ist
        pilot.setActionsLeft(0);

        // Der zweite muss auch noch gültig sein, denn es handelt sich um den extra-push
        oldPos = new Point(pilot.getPosition()); // Position des Piloten
        Assert.assertTrue("Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush(Direction.RIGHT, pilot));
        Assert.assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.x + 1, pilot.getPosition().x);
        Assert.assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.y, pilot.getPosition().y);

        // Da der Spieler keine Aktionen mehr hat muss der dritte forcePush fehlschlagen
        oldPos = new Point(pilot.getPosition());
        Assert.assertFalse("Spieler wurde bewegt, obwohl der Navigator keine Aktion dafür hat", navigator.forcePush(Direction.RIGHT, pilot));
        Assert.assertEquals("Spieler wurde bewegt, obwohl er nicht sollte", oldPos, pilot.getPosition());
    }

    @Test
    public void drainablePositions() {
    }

    @Test
    public void drain() {
    }

    @Test
    public void collectArtifact() {
    }

    @Test
    public void legalReceivers() {
    }

    @Test
    public void getType() {
    }
}