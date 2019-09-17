package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTileProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MapCheckUtil {
    /**
     * Überprüft, ob die Karte, die übergeben wird eine gültige ist, oder ob sie nicht allen Anforderungen entspricht
     *
     * @param map Eine extended Map, in die bereits alle Tiles eingefügt sein sollten
     * @return true, wenn die Karte allen Ansprüchen genügt, false, wenn mindestens eine Bedingung nicht erfüllt ist
     */
    public static boolean checkMapValidity(MapFull map) {
        // Wenn die Map null ist, oder eine Zeile nicht initialisiert wurde, muss die Karte verworfen werden
        if (map == null || Arrays.stream(map.raw()).anyMatch(Objects::isNull)) {
            return false;
        }

        // Array, um sicherzustellen, dass jedes MapTile genau einmal benutzt wird
        Set<MapTileProperties> tilesUsed = new HashSet<>();
        for (int y = 0; y < Map.SIZE_Y; ++y) {
            for (int x = 0; x < Map.SIZE_X; ++x) {
                // Stelle sicher, dass höchstens ein Feld eine bestimmte MapTile hält
                if (tilesUsed.contains(map.get(x, y).getProperties())) {
                    return false;
                }

                tilesUsed.add(map.get(x, y).getProperties());
            }
        }

        // Stelle sicher, dass mindestens ein Feld eine bestimmte MapTile hält
        if (tilesUsed.size() == MapTileProperties.values().length) {
            return false;
        }

        // Generiere aus der gegebenen Karte und überprüfe die restlichen Merkmale, die eine Karte haben muß
        return checkMapValidity(new MapBlackWhite(map));
    }

    /**
     * Überprüft, ob die Karte, die übergeben wird eine gültige ist, oder ob sie nicht allen Anforderungen entspricht
     *
     * @param map Eine "schwarz-weiße", also ungefüllte Map, in die noch die Tiles eingefügt werden müssen
     * @return true, wenn die Karte allen Ansprüchen genügt, false, wenn mindestens eine Bedingung nicht erfüllt ist
     */
    public static boolean checkMapValidity(MapBlackWhite map) {
        // Wenn die Map null ist, oder eine Zeile nicht initialisiert wurde, muss die Karte verworfen werden
        if (map == null || Arrays.stream(map.raw()).anyMatch(Objects::isNull)) {
            return false;
        }

        // Überprüfe, ob die Map die richtige Anzahl an tiles hat
        int numIslandTiles = (int) map.stream().filter(element -> element).count();
        //TODO remove if test still pass
//        for (int y = 0; y < Map.SIZE_Y; ++y) {
//            for (int x = 0; x < Map.SIZE_X; ++x) {
//                if (map.get(x, y))
//                    ++numIslandTiles;
//            }
//        }

        if (numIslandTiles != MapTileProperties.values().length)
            return false;

        return checkIsSingleIsland(map);
    }

    // Helferfunktionen ------------------------------------------------------------------------------------------------

    private static boolean checkIsSingleIsland(MapBlackWhite map) {
        // Lege zu füllenden Array an, bei dem nur ein MapTile von tiles auf true gesetzt wird.
        // Eines muss zwingend als Vorbedingung existieren
        MapBlackWhite reachedMap = createMapSingleTrueCopied(map);

        // Versuche vom startingTileSet alle anderen Tiles zu erreichen. Der Array wird dynamisch gefüllt.
        // Sobald in einer Iteration keine Änderung mehr gemacht werden, wird abgebrochen.
        tryWalkingEntireIsland(map, reachedMap);

        // In beiden Arrays muss nun das gleiche enthalten sein, wenn alle Punkte auf der Insel erreicht werden konnten
        return map.equals(reachedMap);
    }

    private static MapBlackWhite createMapSingleTrueCopied(MapBlackWhite map) {
        MapBlackWhite singleCopy = new MapBlackWhite();
        for (int y = 0; y < Map.SIZE_Y; ++y) {
            for (int x = 0; x < Map.SIZE_X; ++x) {
                if (map.get(x, y)) {
                    singleCopy.set(true, x, y);
                    return singleCopy;
                }
            }
        }

        return singleCopy;
    }

    private static void tryWalkingEntireIsland(MapBlackWhite map, MapBlackWhite reachedMap) {
        boolean somethingChanged;
        do {
            somethingChanged = false;
            for (int y = 0; y < Map.SIZE_Y; ++y) {
                for (int x = 0; x < Map.SIZE_X; ++x) {
                    // Wenn das Tile erreicht wurde, schaue in alle vier Richtungen, ob noch ein Tile erreicht
                    // werden kann. Ist dieses auch ein Insel-Tile kann es dann auch erreicht werden.
                    if (reachedMap.get(x, y)) {
                        Point reachedPoint = new Point(x, y);
                        for (Point neighbour : reachedPoint.getNeighbours(new Point(0, 0), new Point(Map.SIZE_X - 1, Map.SIZE_Y - 1))) {
                            if (!reachedMap.get(neighbour) && map.get(neighbour)) {
                                reachedMap.set(true, neighbour);
                                somethingChanged = true;
                            }
                        }
                    }
                }
            }
        } while (somethingChanged);
    }

}