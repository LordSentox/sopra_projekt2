package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MapCheckUtil {
    /**
     * Überprüft, ob die Karte, die übergeben wird eine gültige ist, oder ob sie nicht allen Anforderungen entspricht
     *
     * @param tiles Eine extended Map, in die bereits alle Tiles eingefügt sein sollten
     * @return true, wenn die Karte allen Ansprüchen genügt, false, wenn mindestens eine Bedingung nicht erfüllt ist
     */
    public static boolean checkMapValidity(MapTile[][] tiles) {
        // Wenn die Map null ist, oder eine Zeile nicht initialisiert wurde, muss die Karte verworfen werden
        if (tiles == null || Arrays.stream(tiles).anyMatch(Objects::isNull)) {
            return false;
        }

        // Array, um sicherzustellen, dass jedes MapTile genau einmal benutzt wird
        Set<MapTileProperties> tilesUsed = new HashSet<>();
        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles[y].length; ++x) {
                // Stelle sicher, dass höchstens ein Feld eine bestimmte MapTile hält
                if (tilesUsed.contains(tiles[y][x].getProperties())) {
                    return false;
                }

                tilesUsed.add(tiles[y][x].getProperties());
            }
        }

        // Stelle sicher, dass mindestens ein Feld eine bestimmte MapTile hält
        if (tilesUsed.size() == MapTileProperties.values().length) {
            return false;
        }

        // Generiere eine "Schwarz-Weiß"-Karte, und überprüfe, ob diese zusammenhängend ist.
        boolean[][] blackWhiteTiles = new boolean[tiles.length][];
        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles[y].length; ++x) {
                blackWhiteTiles[y][x] = tiles[y][x] != null;
            }
        }

        return checkMapValidity(blackWhiteTiles);
    }

    /**
     * Überprüft, ob die Karte, die übergeben wird eine gültige ist, oder ob sie nicht allen Anforderungen entspricht
     *
     * @param tiles Eine "schwarz-weiße", also ungefüllte Map, in die noch die Tiles eingefügt werden müssen
     * @return true, wenn die Karte allen Ansprüchen genügt, false, wenn mindestens eine Bedingung nicht erfüllt ist
     */
    public static boolean checkMapValidity(boolean[][] tiles) {
        // Wenn die Map null ist, oder eine Zeile nicht initialisiert wurde, muss die Karte verworfen werden
        if (tiles == null || Arrays.stream(tiles).anyMatch(Objects::isNull)) {
            return false;
        }

        // Überprüfe, ob die Map die richtige Anzahl an tiles hat
        int numIslandTiles = 0;
        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles[x].length; ++x) {
                if (tiles[y][x])
                    ++numIslandTiles;
            }
        }

        if (numIslandTiles != MapTileProperties.values().length)
            return false;

        return checkHasWaterEdge(tiles) && checkIsSingleIsland(tiles);
    }

    // Helferfunktionen ------------------------------------------------------------------------------------------------

    private static boolean checkHasWaterEdge(boolean[][] tiles) {
        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles[y].length; ++x) {
                // Die erste und letzte Zeile von tiles darf nur Wasser enthalten
                if (y == 0 && tiles[y][x]) return false;
                else if (y == tiles.length - 1 && tiles[y][x]) return false;
                    // Das erste und das letzte Element muss Wasser sein
                else if (tiles[y][0] || tiles[y][tiles[y].length - 1]) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean checkIsSingleIsland(boolean[][] tiles) {
        // Lege zu füllenden Array an, bei dem nur ein MapTile von tiles auf true gesetzt wird.
        // Eines muss zwingend als Vorbedingung existieren
        boolean[][] reachedTiles = createArraySingleTrueCopied(tiles);

        // Versuche vom startingTileSet alle anderen Tiles zu erreichen. Der Array wird dynamisch gefüllt.
        // Sobald in einer Iteration keine Änderung mehr gemacht werden, wird abgebrochen.
        tryWalkingEntireIsland(tiles, reachedTiles);

        // In beiden Arrays muss nun das gleiche enthalten sein, wenn alle Punkte auf der Insel erreicht werden konnten
        return checkMapsEqual(tiles, reachedTiles);
    }

    private static boolean checkMapsEqual(boolean[][] expected, boolean[][] actual) {
        for (int y = 0; y < expected.length; ++y) {
            for (int x = 0; x < expected[y].length; ++x) {
                if (actual[y][x] != expected[y][x]) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean[][] createArraySingleTrueCopied(boolean[][] tiles) {
        boolean startingTileSet = false;
        boolean[][] reachedTiles = new boolean[tiles.length][];
        for (int y = 0; y < tiles.length; ++y) {
            for (int x = 0; x < tiles[y].length; ++x) {
                if (tiles[y][x] && !startingTileSet) {
                    reachedTiles[y][x] = true;
                    startingTileSet = true;
                } else {
                    reachedTiles[y][x] = false;
                }
            }
        }
        return reachedTiles;
    }

    private static void tryWalkingEntireIsland(boolean[][] islandTiles, boolean[][] reachedTiles) {
        boolean somethingChanged;
        do {
            somethingChanged = false;
            for (int y = 0; y < reachedTiles.length; ++y) {
                for (int x = 0; x < reachedTiles[y].length; ++x) {
                    // Wenn das Tile erreicht wurde, schaue in alle vier Richtungen, ob noch ein Tile erreicht
                    // werden kann. Ist dieses auch ein Insel-Tile kann es dann auch erreicht werden.
                    if (reachedTiles[y][x]) {
                        Point reachedPoint = new Point(x, y);
                        for (Point neighbour : reachedPoint.getNeighbours()) {
                            if (!reachedTiles[neighbour.yPos][neighbour.xPos] && islandTiles[neighbour.yPos][neighbour.xPos]) {
                                reachedTiles[neighbour.yPos][neighbour.xPos] = true;
                                somethingChanged = true;
                            }
                        }
                    }
                }
            }
        } while (somethingChanged);
    }

}