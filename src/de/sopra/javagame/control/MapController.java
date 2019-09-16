package de.sopra.javagame.control;

import de.sopra.javagame.util.Map;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.view.MapEditorViewAUI;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Behandelt die Funktionen des Karteneditors, bzw. Funktionen auf einfache Karten, welche noch keine gesetzten Tiles
 * haben.
 */
public class MapController {

    public static final String MAP_FOLDER = "data/maps/";

    private final ControllerChan controllerChan;

    /**
     * Die AUI mit der die angezeigten Daten im {@link de.sopra.javagame.view.MapEditorViewController} aktualisiert
     * werden.
     */
    private MapEditorViewAUI mapEditorViewAUI;

    /**
     * Erstellt einen neün {@link MapController}
     */
    MapController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

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
        if (name.equals("")){
            System.err.println("Man muss einen Name angeben!");
        }
        try {
            String mapString = new String(Files.readAllBytes(Paths.get(MAP_FOLDER + name +".map")), StandardCharsets.UTF_8);

            MapBlackWhite map = MapUtil.readBlackWhiteMapFromString(mapString);

            mapEditorViewAUI.setMap(name, map);
            
        } catch (IOException e) {
            System.out.println("Map konnten nicht eingelesen werden");
            e.printStackTrace();
        }

    }

    /**
     * Speichert die Karte als Datei auf der Festplatte, sodass sie beim nächsten mal unter gleichem Namen wieder
     * geladen werden kann. Existiert die Datei bereits, überschreibt die Funktion sie. Kann die Datei nicht gespeichert
     * werden, wird eine Fehlermeldung im Karteneditor ausgegeben.
     *  @param name  Der Name der Karte, die gespeichert werden soll.
     * @param tiles Die Tiles, die die Karte beschreiben.
     */
    public void saveMap(String name, MapBlackWhite tiles) {
        if (name.equals("")) {
            System.out.println("Die Karte muss einen Namen enthalten!");
            getMapEditorViewAUI().showNotification("Die Karte muss einen Namen enthalten!");
        }
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(MAP_FOLDER + name + ".map"), StandardCharsets.UTF_8));
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
            System.out.println("Maps konnten nicht gelöscht werden.");
            e.printStackTrace();
        }
    }
}
