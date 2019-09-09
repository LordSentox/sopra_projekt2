package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.Turn;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MapControllerTest {

    private ControllerChan controllerChan;
    private MapController mapController;
    private JavaGame javaGame;
    private Turn turn;
    private TestDummy.MapEditorView mapEditorView;
    private boolean[][] map;
    private String name;
    private String mapString;

    @Before
    public void setUp() {
        controllerChan = TestDummy.getDummyControllerChan();
        mapController = controllerChan.getMapController();
        javaGame = controllerChan.getJavaGame();
        turn = controllerChan.getCurrentTurn();
        mapEditorView = (TestDummy.MapEditorView) mapController.getMapEditorViewAUI();
        map = new boolean[12][12];
        name = "hallo";
        mapString = "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;1;1;1;1;1;1;1;1;1;1;-\n"
                + "-;1;1;1;1;1;1;1;1;1;1;-\n"
                + "-;1;1;1;1;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n"
                + "-;-;-;-;-;-;-;-;-;-;-;-\n";
    }


    @Test
    public void testGenerateMapToEditor() {
        //TODO generate Methode implementieren
        //TODO danach test anpassen
        Assert.fail();


    }

    @Test
    public void testLoadMapToEditor() throws FileNotFoundException {
        File outFile = new File(MapController.MAP_FOLDER + name + ".txt");
        PrintWriter out = new PrintWriter(outFile);
        out.println(mapString);

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 3; j++) {
                map[i][j] = true;
            }
        }
        for (int i = 1; i < 5; i += 2) {
            map[i][3] = true;
        }

        mapController.loadMapToEditor(name);
        Assert.assertEquals(map, mapEditorView.getTiles());
        
        outFile.delete();
    }

    @Test(expected = NullPointerException.class)
    public void testSaveMapWithNull() {

        //teste saveMap ohne map
        mapController.saveMap(name, null);
    }

    @Test
    public void testSaveMap() throws IOException {

        //teste saveMap mit unvollständiger Map
        mapController.saveMap(name, map);
        Assert.assertTrue("Es hätte eine Meldung aufgerufen werden müssen",
                mapEditorView.getNotifications().contains("Die Karte muss genau 24 Felder enthalten!"));


        //teste saveMap mit zu voller map
        mapEditorView.getNotifications().clear();
        for (int i = 1; i < 11; i++) {
            for (int j = 0; j < 12; j++) {
                map[i][j] = true;
            }
        }
        mapController.saveMap(name, map);
        Assert.assertTrue("Es hätte eine Meldung aufgerufen werden müssen",
                mapEditorView.getNotifications().contains("Die Karte muss genau 24 Felder enthalten!"));


        //teste mit unzusammenhängender map
        mapEditorView.getNotifications().clear();
        map = new boolean[12][12];
        for (int i = 1; i < 11; i += 2) {
            for (int j = 1; j < 6; j++) {
                map[i][j] = true;
            }
        }
        map[1][1] = false;
        mapController.saveMap(name, map);
        Assert.assertTrue("Es hätte eine Meldung aufgerufen werden müssen",
                mapEditorView.getNotifications().contains("Die Karte muss zusammenhängend sein!"));


        //teste mit erwarteter map und name
        map = new boolean[12][12];
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 3; j++) {
                map[i][j] = true;
            }
        }

        for (int i = 1; i < 5; i += 2) {
            map[i][3] = true;
        }
        mapController.saveMap(name, map);

        String content = new String(Files.readAllBytes(Paths.get(name + ".java")), StandardCharsets.UTF_8);
        Assert.assertEquals(mapString, content);

        //teste mit korrekter map ohne Namen
        mapController.saveMap("", map);
        Assert.assertTrue("Es hätte eine Meldung aufgerufen werden müssen",
                mapEditorView.getNotifications().contains("Die Karte muss einen Namen enthalten!"));

    }

}
