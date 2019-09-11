package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;

import java.util.Arrays;
import java.util.Random;

/**
 * Enthält Helferfunktionen für Kartenerstellung und Kartenmanipulation
 */
public class MapUtil {
    /**
     * Füllt die einfache Karte, die nur die Inselform enthält mit zufällig ausgewählten MapTiles und gibt die so
     * entstandene, spielbare Karte zurück.
     *
     * @param tiles Die Inselform, die gefüllt werden soll
     * @return Spielbare Karte
     */
    public static MapTile[][] createAndFillMap(boolean[][] tiles) {
        MapTile[][] mapTiles = new MapTile[tiles.length][12];

        Random random = new Random();

        // Damit Karten nicht öfters benutzt werden muss sich gemerkt werden, welche bereits benutzt wurden.
        Boolean[] tilesUsed = new Boolean[24];
        Arrays.fill(tilesUsed, false);
        // Gehe durch alle tiles
        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles[y].length; ++x) {
                // Wenn es sich um eine Insel-Tile handelt muss sie mit einem zufälligen Tile gefüllt werden.
                if (tiles[y][x]) {
                    int nextTile = random.nextInt(24);
                    do {
                        nextTile = (nextTile + 1) % 24;
                    } while (tilesUsed[nextTile]);

                    mapTiles[y][x] = MapTile.fromNumber(nextTile);
                    tilesUsed[nextTile] = true;

                    // Beende die Funktion, wenn alle MapTiles benutzt wurden
                    if (!Arrays.asList(tilesUsed).contains(false)) {
                        return mapTiles;
                    }
                }
            }
        }

        System.err.println("createAndFillMap wurde falsch benutzt. Die Insel besteht aus weniger als 24 Tiles.");
        return mapTiles;
    }

    /**
     * Gibt den Anfangspunkt der Spielerfigur auf der Karte zurück.
     *
     * @param tiles  Die Insel, auf der der Anfangspunkt gesucht wird.
     * @param player Die Figur, dessen Anfangspunkt bestimmt werden soll.
     * @return Der Anfangspunkt oder <code>null</code>, wenn kein Anfangspunkt für die Figur gefunden werden kann.
     */
    public static Point getPlayerSpawnPoint(MapTile[][] tiles, PlayerType player) {
        if (player == PlayerType.NONE) {
            return null;
        }

        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles[y].length; ++x) {
                if (tiles[y][x] != null && tiles[y][x].getProperties().getSpawn() == player) {
                    return new Point(x, y);
                }
            }
        }

        return null;
    }

    /**
     * Erstellt eine Karte aus den Daten wenn sie z.B. aus einer CSV-Datei gelesen wurden, wie sie für das KI-Turnier
     * benutzt wird.
     *
     * @param numbers Die Kartendaten, nummernkodiert. -1 bedeutet eine leere Stelle, also Wasser.
     * @return Die erstellte Karte, oder <code>null</code>, wenn die Nummerndaten nicht interpretiert werden konnten.
     */
    public static MapTile[][] createMapFromNumbers(int[][] numbers) {
        // Gehe durch den numbers-array und fülle die MapTiles mit den den numbers entsprechenden Werten
        MapTile[][] tiles = new MapTile[12][12];
        for (int y = 0; y < numbers.length; ++y) {
            tiles[y] = new MapTile[12];
            for (int x = 0; x < numbers[y].length; ++x) {
                if (numbers[y][x] != -1)
                    tiles[y][x] = MapTile.fromNumber(numbers[y][x]);
            }
        }

        return tiles;
    }


    /**
     * Parst einen String als int[][], der als Karte interpretiert werden kann
     *
     * @param toParse Die Eingabe aus der gelesen werden soll
     * @return Die Nummern, die einzelnen Tiles entsprechen, bzw. -1, wo Wasser ist.
     */
    public static int[][] readNumberMapFromString(String toParse) {
        // FIXME: Um die Map muss noch eine null-Zeile hinzugefügt werden

        // Erstelle aus dem String eine Liste von einzelnen Zeilen und splitte diese dann mit ;, der CSV-Trennung.
        String[] lines = toParse.split("\n");
        String[][] map = new String[lines.length][];
        for (int i = 0; i < lines.length; ++i) {
            String[] split = lines[i].split(",");
            map[i] = split;
        }

        // Erstelle eine leere Map
        int[][] numbers = new int[12][12];
        for (int[] line : numbers) {
            Arrays.fill(line, -1);
        }

        // Fülle die map mit den Werten, die in map stehen. Ist die map größer als die Kartengröße werden Sachen, die
        // rechts und unten überstehen ignoriert.
        for (int y = 0; y < Math.min(numbers.length, map.length); ++y) {
            for (int x = 0; x < Math.min(numbers[y].length, map[y].length); ++x) {
                String sign = map[y][x].trim();
                if (!sign.equals("-")) {
                    numbers[y][x] = Integer.parseUnsignedInt(sign) % 24;
                }
            }
        }

        return numbers;
    }
}
