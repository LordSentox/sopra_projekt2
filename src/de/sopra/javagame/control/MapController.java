package de.sopra.javagame.control;

import de.sopra.javagame.util.map.Map;
import de.sopra.javagame.util.map.MapBlackWhite;
import de.sopra.javagame.util.map.MapUtil;
import de.sopra.javagame.view.MapEditorViewAUI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Behandelt die Funktionen des Karteneditors, bzw. Funktionen auf einfache Karten, welche noch keine gesetzten Tiles
 * haben.
 */
public class MapController {

    public static final String MAP_FOLDER = "data/maps/";

    /**
     * Die AUI mit der die angezeigten Daten im {@link de.sopra.javagame.view.MapEditorViewController} aktualisiert
     * werden.
     */
    private MapEditorViewAUI mapEditorViewAUI;

    /**
     * Erstellt einen neün {@link MapController}
     */
    //MapController(ControllerChan controllerChan) {}

    public void setMapEditorViewAUI(MapEditorViewAUI mapEditorViewAUI) {
        this.mapEditorViewAUI = mapEditorViewAUI;
    }

    public MapEditorViewAUI getMapEditorViewAUI() {
        return mapEditorViewAUI;
    }

    /**
     * Generiert zufällig eine Karte und lädt den Inhalt in den offenen Karteneditor. Erzeugt immer eine einzige,
     * zusammenhängende Insel.
     */
    public void generateMapToEditor() {
        mapEditorViewAUI.setMap("Coole Insel!", MapUtil.generateRandomIsland());
    }

    /**
     * Lädt die Karte mit dem angegebenen Namen in den offenen Karteneditor. Kann sie nicht geladen werden, z.B. weil
     * es keine Karte mit dem Namen gibt, wird eine Fehlermeldung im Karteneditor ausgegeben.
     *
     * @param name Der Name der Karte, die geladen werden soll.
     */
    public void loadMapToEditor(String name) {
        if (name.trim().isEmpty()) {
            System.err.println("Man muss einen Name angeben!");
            return;
        }
        try {
            File file = new File(MAP_FOLDER, name + ".map");
            if (!file.exists())
                throw new FileNotFoundException(file.getAbsolutePath());
            byte[] bytes = Files.readAllBytes(file.toPath());
            String mapString = new String(bytes, StandardCharsets.UTF_8);

            MapBlackWhite map = MapUtil.readBlackWhiteMapFromString(mapString);

            mapEditorViewAUI.setMap(name, map);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Map konnten nicht eingelesen werden");
        }

    }

    /**
     * Speichert die Karte als Datei auf der Festplatte, sodass sie beim nächsten mal unter gleichem Namen wieder
     * geladen werden kann. Existiert die Datei bereits, überschreibt die Funktion sie. Kann die Datei nicht gespeichert
     * werden, wird eine Fehlermeldung im Karteneditor ausgegeben.
     *
     * @param name  Der Name der Karte, die gespeichert werden soll.
     * @param tiles Die Tiles, die die Karte beschreiben.
     */
    public void saveMap(String name, MapBlackWhite tiles) {
        if (name.trim().isEmpty()) {
            System.err.println("Die Karte muss einen Namen enthalten!");
            getMapEditorViewAUI().showNotification("Die Karte muss einen Namen enthalten!");
            return;
        }
        try {
            File file = new File(MAP_FOLDER, name + ".map");
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            for (int y = 0; y < Map.SIZE_Y; y++) {
                for (int x = 0; x < Map.SIZE_X; x++) {
                    if (tiles.get(x, y)) {
                        out.write("X");
                    } else {
                        out.write("-");
                    }
                    if (x < Map.SIZE_X - 1) {
                        out.write(",");
                    }
                }
                out.newLine();
            }
            out.close();

        } catch (IOException e) {
            System.err.println("Maps konnten nicht gespeichert werden.");
            e.printStackTrace();
        }
    }
}
