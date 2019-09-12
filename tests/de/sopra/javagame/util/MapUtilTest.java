package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static de.sopra.javagame.model.MapTileProperties.*;

public class MapUtilTest {

    @Test
    public void createAndFillMap() {
        boolean[][] tiles = {
                {false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false,  true,  true,  true, false, false, false},
                {false, false,  true, false,  true,  true,  true,  true,  true,  true, false, false},
                {false, false,  true, false,  true,  true,  true,  true,  true,  true, false, false},
                {false, false,  true,  true,  true, false,  true,  true, false, false, false, false},
                {false, false, false, false,  true,  true, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false}
        };

        MapTile[][] map = MapUtil.createAndFillMap(tiles);
        Assert.assertNotNull("Annehmbare Map wurde fälschlicherweise abgelehnt", map);

        // Array, um sicherzustellen, dass jedes MapTile nur ein einziges Mal benutzt wird
        Set<MapTile> tilesUsed = new HashSet<>();
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                if (tiles[y] == null || !tiles[y][x]) {
                    Assert.assertNull("Wasserfeld wurde mit Inselfeld belegt", map[y][x]);
                } else {
                    Assert.assertFalse("Ein MapTile wurde zweimal benutzt", tilesUsed.contains(map[y][x]));
                    tilesUsed.add(map[y][x]);
                }
            }
        }

        Assert.assertEquals("Es wurden nicht alle MapTiles bei Belegung der Insel verwendet", 24, tilesUsed.size());

        // Entfernen eines Inselfeldes. Die Map soll immernoch gefüllt werden, aber es muss ein Fehler ausgegeben werden.
        tiles[2][2] = false;
        MapTile[][] nonFullMap = MapUtil.createAndFillMap(tiles);
    }

    @Test
    public void getPlayerSpawnPoint() {
        int[][] numbers = {
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, 20, 19, -1, -1,  2,  6, -1, -1, -1},
                {-1, -1, 21, 22, 18, 11,  3,  5,  4,  8, -1, -1},
                {-1, -1,  7, 12, 23, 14, 13,  1, 17,  0, -1, -1},
                {-1, -1, -1,  9, 16, -1, -1, 15, 10, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
        };

        MapTile[][] map = MapUtil.createMapFromNumbers(numbers);

        Assert.assertEquals("Der Spawnpunkt des Piloten wurde nicht richtig gesetzt.", new Point(7, 3), MapUtil.getPlayerSpawnPoint(map, PlayerType.PILOT));
        Assert.assertEquals("Der Spawnpunkt des Botens wurde nicht richtig gesetzt.", new Point(4, 4), MapUtil.getPlayerSpawnPoint(map, PlayerType.COURIER));
        Assert.assertEquals("Der Spawnpunkt des Abenteurers wurde nicht richtig gesetzt.", new Point(7, 4), MapUtil.getPlayerSpawnPoint(map, PlayerType.EXPLORER));
        Assert.assertEquals("Der Spawnpunkt des Tauchers wurde nicht richtig gesetzt.", new Point(6, 3), MapUtil.getPlayerSpawnPoint(map, PlayerType.DIVER));
        Assert.assertEquals("Der Spawnpunkt des Navigators wurde nicht richtig gesetzt.", new Point(3, 3), MapUtil.getPlayerSpawnPoint(map, PlayerType.NAVIGATOR));
        Assert.assertEquals("Der Spawnpunkt des Ingenieurs wurde nicht richtig gesetzt.", new Point(5, 3), MapUtil.getPlayerSpawnPoint(map, PlayerType.ENGINEER));
        Assert.assertNull("Wenn kein Spieler übergeben wird gibt es keinen Spawnpunkt.", MapUtil.getPlayerSpawnPoint(map, PlayerType.NONE));
    }

    @Test
    public void createMapFromNumbers() {
        int[][] numbers = {
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, 20, 19, -1, -1,  2,  6, -1, -1, -1},
                {-1, -1, 21, 22, 18, 11,  3,  5,  4,  8, -1, -1},
                {-1, -1,  7, 12, 23, 14, 13,  1, 17,  0, -1, -1},
                {-1, -1, -1,  9, 16, -1, -1, 15, 10, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
        };

        MapTile[][] map = MapUtil.createMapFromNumbers(numbers);

        // Um den Test nicht zu repititiv zu gestalten werden nur drei besonders interessante Reihen betrachtet.
        MapTile[] nullRow = {null, null, null, null, null, null, null, null, null, null, null, null};
        MapTile[] thirdRow = {
                null, null,
                new MapTile(CORAL_PALACE),
                new MapTile(TIDAL_PALACE),
                new MapTile(TEMPLE_OF_THE_SUN),
                new MapTile(PHANTOM_ROCK),
                new MapTile(BREAKERS_BRIDGE),
                new MapTile(TWIGHLIGHT_HORROW),
                new MapTile(CRIMSON_FOREST),
                new MapTile(OBSERVATORY),
                null, null
        };

        Assert.assertArrayEquals("Erste Reihe der Map wurde nicht richtig gefüllt.", nullRow, map[0]);
        Assert.assertArrayEquals("Reihe unter der Map wurde nicht richtig gefüllt.", nullRow, map[5]);
        Assert.assertArrayEquals("Volle Reihe der Map wurde nicht richtig gefüllt.", thirdRow, map[2]);
    }

    @Test
    public void readNumberMapFromString() {
        String extendedMap = "-, -,20,19, -, -, 2, 6\n" +
                             "-,21,22,18,11, 3, 5, 4, 8\n" +
                             "-, 7,12,23,14,13, 1,17, 0\n" +
                             "-, -, 9,16, -, -,15,10\n";

        int[][] numbers = MapUtil.readNumberMapFromString(extendedMap);
        Assert.assertNotNull("Zahlen wurden nicht aus dem String gelesen", numbers);

        int[][] expectedNumbers = {
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, 20, 19, -1, -1,  2,  6, -1, -1},
                {-1, -1, 21, 22, 18, 11,  3,  5,  4,  8, -1},
                {-1, -1,  7, 12, 23, 14, 13,  1, 17,  0, -1},
                {-1, -1, -1,  9, 16, -1, -1, 15, 10, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        };

        for (int y = 0; y < expectedNumbers.length; ++y) {
            Assert.assertArrayEquals("Fehler beim Einlesen einer Kartenzeile", expectedNumbers[y], numbers[y]);
        }
    }

    @Test
    public void readBoolMapFromString() {

    }
}