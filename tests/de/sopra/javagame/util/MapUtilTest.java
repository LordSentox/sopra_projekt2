package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.map.Map;
import de.sopra.javagame.util.map.MapBlackWhite;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.util.map.MapUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class MapUtilTest {

    @Test
    public void createAndFillMap() {
        Boolean[][] tiles = {
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false,  true,  true,  true, false, false, false},
                {false, false,  true, false,  true,  true,  true,  true,  true,  true, false, false},
                {false, false,  true, false,  true,  true,  true,  true,  true,  true, false, false},
                {false, false,  true,  true,  true, false,  true,  true, false, false, false, false},
                {false, false, false, false,  true,  true, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false}
        };

        MapBlackWhite blackWhite = new MapBlackWhite(tiles);

        MapFull map = MapUtil.createAndFillMap(blackWhite);
        Assert.assertNotNull("Annehmbare Map wurde fälschlicherweise abgelehnt", map);

        // Array, um sicherzustellen, dass jedes MapTile nur ein einziges Mal benutzt wird
        Set<MapTile> tilesUsed = new HashSet<>();
        for (int y = 0; y < Map.SIZE_Y; ++y) {
            for (int x = 0; x < Map.SIZE_X; ++x) {
                if (!blackWhite.get(x, y)) {
                    Assert.assertNull("Wasserfeld wurde mit Inselfeld belegt", map.get(x, y));
                } else {
                    Assert.assertFalse("Ein MapTile wurde zweimal benutzt", tilesUsed.contains(map.get(x, y)));
                    tilesUsed.add(map.get(x, y));
                }
            }
        }

        Assert.assertEquals("Es wurden nicht alle MapTiles bei Belegung der Insel verwendet", 24, tilesUsed.size());

        // Entfernen eines Inselfeldes. Die Map soll immernoch gefüllt werden, aber es muss ein Fehler ausgegeben werden.
        blackWhite.set(false, 1, 1);
        MapFull nonFullMap = MapUtil.createAndFillMap(blackWhite);
    }

    @Test
    public void getPlayerSpawnPoint() throws IOException {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        MapFull map = MapUtil.readFullMapFromString(testMapString);

        Assert.assertEquals("Der Spawnpunkt des Piloten wurde nicht richtig gesetzt.", new Point(5, 2), map.getPlayerSpawnPoint(PlayerType.PILOT));
        Assert.assertEquals("Der Spawnpunkt des Botens wurde nicht richtig gesetzt.", new Point(2, 3), map.getPlayerSpawnPoint(PlayerType.COURIER));
        Assert.assertEquals("Der Spawnpunkt des Abenteurers wurde nicht richtig gesetzt.", new Point(5, 3), map.getPlayerSpawnPoint(PlayerType.EXPLORER));
        Assert.assertEquals("Der Spawnpunkt des Tauchers wurde nicht richtig gesetzt.", new Point(4, 2), map.getPlayerSpawnPoint(PlayerType.DIVER));
        Assert.assertEquals("Der Spawnpunkt des Navigators wurde nicht richtig gesetzt.", new Point(1, 2), map.getPlayerSpawnPoint(PlayerType.NAVIGATOR));
        Assert.assertEquals("Der Spawnpunkt des Ingenieurs wurde nicht richtig gesetzt.", new Point(3, 2), map.getPlayerSpawnPoint(PlayerType.ENGINEER));
        Assert.assertNull("Wenn kein Spieler übergeben wird gibt es keinen Spawnpunkt.", map.getPlayerSpawnPoint(PlayerType.NONE));
    }

    @Test
    public void readBoolMapFromString() throws IOException {
        final String testMapString = new String(Files.readAllBytes(Paths.get("resources/maps/island_of_death.map")), StandardCharsets.UTF_8);
        final MapBlackWhite actual = MapUtil.readBlackWhiteMapFromString(testMapString);
        Assert.assertNotNull(actual);

        final Boolean[][] expected = {
                {false, false, false, false, false, false, false, false, false},
                {false, false, false,  true,  true,  true, false, false, false},
                {false,  true,  true,  true,  true,  true,  true,  true, false},
                {false, false,  true,  true,  true,  true,  true, false, false},
                {false, false, false,  true,  true,  true, false, false, false},
                {false, false, false,  true,  true,  true, false, false, false},
                {false, false, false,  true,  true,  true, false, false, false},
                {false, false, false, false, false, false, false, false, false},
        };

        Assert.assertEquals(expected.length, actual.raw().length);
        for (int y = 0; y < expected.length; ++y) {
            Assert.assertArrayEquals("Fehler beim Einlesen einer Kartenzeile", expected[y], actual.raw()[y]);
        }

        // Wenn eine extended map eingegeben wird, soll null zurückgegeben werden
        final String fullMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        Assert.assertNull(MapUtil.readBlackWhiteMapFromString(fullMapString));
    }
}