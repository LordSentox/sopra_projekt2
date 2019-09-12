package de.sopra.javagame.control;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.util.MapCheckUtil;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.view.MapEditorViewAUI;

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
        boolean[][] feldsUsed = new boolean[12][12];
        for (int i = 0; i < feldsUsed[i].length; i++){ 
        Arrays.fill(feldsUsed[i], false);
        }
        Random random = new Random();
        boolean unqualified =true;
        while (unqualified) {           
            for (int i = 0; i<24; i++){
                int x = random.nextInt(12);
                int y = random.nextInt(12);
                if (feldsUsed[x][y]){
                    i--;
                }else {
                    feldsUsed[x][y] = true;
                }
            }
            
            unqualified = !MapCheckUtil.checkMapValidity(feldsUsed);
        }
        mapEditorViewAUI.setMap("Coole Insel!", feldsUsed);
    }

    /**
     * Lädt die Karte mit dem angegebenen Namen in den offenen Karteneditor. Kann sie nicht geladen werden, z.B. weil
     * es keine Karte mit dem Namen gibt, wird eine Fehlermeldung im Karteneditor ausgegeben.
     *
     * @param name Der Name der Karte, die geladen werden soll.
     */
    public void loadMapToEditor(String name) {
        try {
            String scoresToString = new String(Files.readAllBytes(Paths.get(MAP_FOLDER + name +".map")), StandardCharsets.UTF_8);
             // Erstelle aus dem String eine Liste von einzelnen Zeilen und splitte diese dann mit ;, der CSV-Trennung.
            String[] maps = scoresToString.split("\n");
            String[][] mapcsv = new String[maps.length][];
            for (int i = 0; i < maps.length; ++i) {
                String[] split = maps[i].split(",");
                mapcsv[i] = split;
            }
            
            boolean[][] mapTile = new boolean[12][12];
            
            for (String[] row : mapcsv) {
                if (row.length != 12) {
                    System.err.println("Map für " + name + " konnten nicht gelesen werden. Eine Zeile ist korrumpiert.");
                    return;
                }    
            }
            for (int y = 0; y < 12; y++){
                for (int x = 0; x < 12; x++){
                    if (mapcsv[y][x] == "X"){
                        mapTile[y][x] = false;
                    }else {
                        mapTile[y][x] = true;
                    }
                }
            }
            
            mapEditorViewAUI.setMap(name, mapTile);
            
        } catch (IOException e) {
            System.err.println("Map konnten nicht eingelesen werden");
            e.printStackTrace();
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
    public void saveMap(String name, boolean[][] tiles) {
        try {
            BufferedWriter out =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(MAP_FOLDER + name +".map"),  StandardCharsets.UTF_8));
            String[][] mapTiles = new String[tiles.length][];
            for (int y = 0; y < 12; y++){
                for (int x = 0; x < 12; x++){
                    if (tiles[y][x] == false){
                        mapTiles[y][x] = "X";
                    }else {
                        mapTiles[y][x] = "-";
                        
                    }
                    if (x != 11){
                        out.write(",");
                    }
                }
                out.newLine();
            }
            out.close(); 
            
        } catch (IOException e) {
            System.err.println("Maps konnten nicht gelöscht werden.");
            e.printStackTrace();
        }
        
        
        

    }
}
