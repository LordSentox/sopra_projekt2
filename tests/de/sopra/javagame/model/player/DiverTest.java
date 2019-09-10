package de.sopra.javagame.model.player;

import static org.junit.Assert.*;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;

public class DiverTest {
    MapTile[][] testMap;
    MapTile[][] testMaps;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), "UTF-8");
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        this.testMaps = new MapTile[12][];
    }

    @Test
    public void testLegalMoves() {
        Turn turn = Turn.createInitialTurn(Difficulty.NORMAL, Arrays.asList(new Pair<>(PlayerType.DIVER, false), new Pair<>(PlayerType.COURIER, false)), this.testMap);
        Player diver = turn.getPlayers().get(0);
        Player courier = turn.getPlayers().get(1);
        List<Point> testLegelMovesWithSpecial = diver.legalMoves(true);
        List<Point> testLegelMovesWithoutSpecial = diver.legalMoves(false);
        for (Point target : testLegelMovesWithSpecial){
            Assert.assertTrue(turn.getTile(target.x, target.y).getState() == MapTileState.DRY);
            Assert.assertFalse(turn.getTile(target.x, target.y) == null);
        }
        
        for (Point target : testLegelMovesWithoutSpecial){
            Assert.assertTrue(turn.getTile(target).getState() == MapTileState.DRY);
            Assert.assertFalse(turn.getTile(target) == null);
        }
        
        
        Turn turnnull = Turn.createInitialTurn(Difficulty.NORMAL, Arrays.asList(new Pair<>(PlayerType.DIVER, false), new Pair<>(PlayerType.COURIER, false)), this.testMaps);
        Player diverb = turnnull.getPlayers().get(0);
        Player courierb = turnnull.getPlayers().get(1);
        List<Point> testLegelMovesWithSpecialb = diver.legalMoves(true);
        List<Point> testLegelMovesWithoutSpecialb = diver.legalMoves(false);
        Assert.assertEquals(0, testLegelMovesWithoutSpecialb.size());
        Assert.assertEquals(0, testLegelMovesWithSpecialb.size());
    }

    @Test
    public void testDiverStringPointTurn() {
        fail("Not yet implemented");
    }

    @Test
    public void testDiverStringPointTurnBoolean() {
        fail("Not yet implemented");
    }

    @Test
    public void testCopy() {
        fail("Not yet implemented");
    }

}
