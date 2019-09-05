package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;

import java.awt.*;

/**
 * Enthält Helferfunktionen für Kartenerstellung und Kartenmanipulation
 */
public class MapUtil {

    /**
     * Füllt die einfache Karte, die nur die Inselform enthält mit zufällig ausgewählten MapTiles und gibt die so
     * entstandene, spielbare Karte zurück.
     * @param tiles Die Inselform, die gefüllt werden soll
     * @return Spielbare Karte
     */
    public static MapTile[][] createAndFillMap(boolean[][] tiles) {
        return null;
    }

    /**
     * Gibt den Anfangspunkt der Spielerfigur auf der Karte zurück.
     *
     * @param tiles Die Insel, auf der der Anfangspunkt gesucht wird.
     * @param player Die Figur, dessen Anfangspunkt bestimmt werden soll.
     * @return Der Anfangspunkt oder <code>null</code>, wenn kein Anfangspunkt für die Figur gefunden werden kann.
     */
    public static Point getPlayerSpawnPoint(MapTile[][] tiles, PlayerType player) {
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
        return null;
    }
}
