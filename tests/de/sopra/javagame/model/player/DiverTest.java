package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.util.Triple;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.util.map.MapUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

public class DiverTest {
    private MapFull testMap;
    private MapFull testMapFull;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        this.testMap = MapUtil.readFullMapFromString(testMapString);

        String testMapStringFull = new String(Files.readAllBytes(Paths.get("resources/maps/test_maps/test_map1.csv")), StandardCharsets.UTF_8);
        this.testMapFull = MapUtil.readFullMapFromString(testMapStringFull);

    }

    @Test
    public void testLegalMoves() {
        Action action = Action.createInitialAction(Difficulty.NORMAL, Arrays.asList(new Triple<>(PlayerType.DIVER, "", false), new Triple<>(PlayerType.COURIER, "", false)), this.testMap);
        Player diver = action.getPlayers().get(0);
        List<Point> testLegelMovesWithSpecial = diver.legalMoves(true);
        List<Point> testLegelMovesWithoutSpecial = diver.legalMoves(false);
        for (Point target : testLegelMovesWithSpecial) {
            MapTile tile = action.getMap().get(target);
            Assert.assertNotNull(tile);
            Assert.assertEquals(tile.getState(), MapTileState.DRY);
        }

        for (Point target : testLegelMovesWithoutSpecial) {
            MapTile tile = action.getMap().get(target);
            Assert.assertNotNull(tile);
            Assert.assertEquals(tile.getState(), MapTileState.DRY);
        }

        Action turnFullMapAction = Action.createInitialAction(Difficulty.NORMAL, Arrays.asList(new Triple<>(PlayerType.DIVER, "", false), new Triple<>(PlayerType.COURIER, "", false)), this.testMapFull);
        diver = turnFullMapAction.getPlayer(PlayerType.DIVER);
        diver.setPosition(testMap.getPlayerSpawnPoint(PlayerType.DIVER));
        List<Point> testLegalMovesWithSpecialb = diver.legalMoves(true);
        List<Point> testLegalMovesWithoutSpecialb = diver.legalMoves(false);
        Assert.assertEquals(4, testLegalMovesWithoutSpecialb.size());
        Assert.assertEquals(0, testLegalMovesWithSpecialb.size());
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
