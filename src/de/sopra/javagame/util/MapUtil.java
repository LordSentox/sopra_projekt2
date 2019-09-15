package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Enthält Helferfunktionen für Kartenerstellung und Kartenmanipulation
 */
public class MapUtil {
    public static MapBlackWhite generateRandomIsland() {
        Random random = new Random();
        MapBlackWhite map = new MapBlackWhite();

        // Die Karte wird von der Mitte aus aufgebaut
        Point startPoint = new Point(Map.SIZE_X / 2, Map.SIZE_Y / 2);
        map.set(true, startPoint);
        // Die Optionen, wo das nächste Tile generiert werden kann. Aus ihnen wird in jedem Schritt ein
        // zufälliges ausgewählt, wo ein Inselfeld hinkommt.
        List<Point> nextTileOptions = startPoint.getNeighbours(new Point(0, 0), new Point(Map.SIZE_X - 1, Map.SIZE_Y - 1));

        int tilesGenerated = 0;
        while (tilesGenerated < MapTileProperties.values().length) {
            // Wähle das nächste Tile zufällig aus
            int next = random.nextInt(nextTileOptions.size());
            Point nextPos = nextTileOptions.get(next);
            map.set(true, nextPos);

            // Füge die Punkte, die mit diesem zusätlichene Punkt jetzt auch noch erreicht werden können in die
            // bestehende Liste ein.
            List<Point> neighbours = nextPos.getNeighbours(new Point(0, 0), new Point(Map.SIZE_X - 1, Map.SIZE_Y - 1));
            for (Point neighbour : neighbours) {
                if (!map.get(neighbour) && !nextTileOptions.contains(neighbour)) {
                    nextTileOptions.add(neighbour);
                }
            }

            nextTileOptions.remove(next);
            ++tilesGenerated;
        }

        return map;
    }

    public static boolean[][] generateRandomIsland(int seed) {
        return null;
    }

    /**
     * Füllt die einfache Karte, die nur die Inselform enthält mit zufällig ausgewählten MapTiles und gibt die so
     * entstandene, spielbare Karte zurück.
     *
     * @param blackWhiteMap Die Inselform, die gefüllt werden soll
     * @return Spielbare Karte
     */
    public static MapFull createAndFillMap(MapBlackWhite blackWhiteMap) {
        MapFull full = new MapFull();

        Random random = new Random();

        // Damit Karten nicht öfters benutzt werden muss sich gemerkt werden, welche bereits benutzt wurden.
        Boolean[] tilesUsed = new Boolean[MapTileProperties.values().length];
        Arrays.fill(tilesUsed, false);
        // Gehe durch alle tiles
        for (int y = 0; y < Map.SIZE_Y; ++y) {
            for (int x = 0; x < Map.SIZE_X; ++x) {
                // Wenn es sich um eine Insel-Tile handelt muss sie mit einem zufälligen Tile gefüllt werden.
                if (blackWhiteMap.get(x, y)) {
                    int nextTile = random.nextInt(MapTileProperties.values().length);
                    do {
                        nextTile = (nextTile + 1) % MapTileProperties.values().length;
                    } while (tilesUsed[nextTile]);

                    full.set(MapTile.fromNumber(nextTile), x, y);
                    tilesUsed[nextTile] = true;

                    // Beende die Funktion, wenn alle MapTiles benutzt wurden
                    if (!Arrays.asList(tilesUsed).contains(false)) {
                        return full;
                    }
                }
            }
        }

        System.err.println("createAndFillMap wurde falsch benutzt. Die Insel besteht aus weniger als 24 Tiles.");
        return full;
    }

    /**
     * Parst einen String als voll gefüllte Karte
     *
     * @param toParse Die Eingabe aus der gelesen werden soll
     * @return Die gefüllte Karte
     */
    public static MapFull readFullMapFromString(String toParse) {
        String[][] map = splitCSVString(toParse);

        // Erstelle eine leere Karte mit Platz für einen leeren Rahmen
        MapFull full = new MapFull();

        // Fülle die Karte mit den aus dem String ausgelesenen Werten
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                if (x >= Map.SIZE_X || y >= Map.SIZE_Y) {
                    System.err.println("Map konnte nicht eingelesen werden, da sie zu groß ist.");
                    return null;
                }

                String sign = map[y][x].trim();
                if (!sign.equals("-")) {
                    final int number = Integer.parseUnsignedInt(sign) % MapTileProperties.values().length;
                    full.set(MapTile.fromNumber(number), x, y);
                }
            }
        }

        return full;
    }

    public static MapBlackWhite readBlackWhiteMapFromString(String toParse) {
        String[][] map = splitCSVString(toParse);

        // Erstelle eine leere Karte
        MapBlackWhite blackWhite = new MapBlackWhite();

        // Fülle die Karte mit den aus dem String ausgelesenen Werten
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                if (x >= Map.SIZE_X || y >= Map.SIZE_Y) {
                    System.err.println("Map konnte nicht eingelesen werden, da sie zu groß ist.");
                    return null;
                }

                String sign = map[y][x].trim();
                if (sign.equalsIgnoreCase("x")) {
                    blackWhite.set(true, x, y);
                }
                else if (sign.equals("-")) {
                    blackWhite.set(false, y, x);
                } else {
                    System.err.println("Karte ist korrumpiert und konnte nicht geladen werden");
                    return null;
                }
            }
        }

        return blackWhite;
    }

    // Helferfunktionen ------------------------------------------------------------------------------------------------

    private static String[][] splitCSVString(String toParse) {
        // Erstelle aus dem String eine Liste von einzelnen Zeilen und splitte diese dann mit ",", der CSV-Trennung.
        String[] lines = toParse.split("\n");
        String[][] items = new String[lines.length][];
        for (int i = 0; i < lines.length; ++i) {
            String[] split = lines[i].split(",");
            items[i] = split;
        }

        return items;
    }
}
