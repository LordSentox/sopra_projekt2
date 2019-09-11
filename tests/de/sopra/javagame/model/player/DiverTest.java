package de.sopra.javagame.model.player;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

public class DiverTest {
    MapTile[][] testMap;
    MapTile[][] testMapNull;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), "UTF-8");
        int[][] testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        this.testMapNull = new MapTile[12][];
    }

    @Test
    public void testLegalMoves() {
        Turn turn = Turn.createInitialTurn(Difficulty.NORMAL, Arrays.asList(new Pair<>(PlayerType.DIVER, false), new Pair<>(PlayerType.COURIER, false)), this.testMap);
        Player diver = turn.getPlayers().get(0);
        Player courier = turn.getPlayers().get(1);
        List<Point> testLegelMovesWithSpecial = diver.legalMoves(true);
        List<Point> testLegelMovesWithoutSpecial = diver.legalMoves(false);
        for (Point target : testLegelMovesWithSpecial){
            MapTile tile = turn.getTile(target);
            Assert.assertNotNull(tile);
            Assert.assertEquals(tile.getState(), MapTileState.DRY);
        }
        
        for (Point target : testLegelMovesWithoutSpecial){
            MapTile tile = turn.getTile(target);
            Assert.assertNotNull(tile);
            Assert.assertEquals(tile.getState(), MapTileState.DRY);
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
