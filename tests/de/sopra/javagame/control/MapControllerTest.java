package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.util.map.Map;
import de.sopra.javagame.util.map.MapBlackWhite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MapControllerTest {

    private MapController mapController;
    private TestDummy.MapEditorView mapEditorView;
    private MapBlackWhite map;
    private String name;
    private String mapString;

    @Before
    public void setUp() {
        ControllerChan controllerChan = TestDummy.getDummyControllerChan();
        mapController = controllerChan.getMapController();
        JavaGame javaGame = controllerChan.getJavaGame();
        Action action = controllerChan.getCurrentAction();
        mapEditorView = (TestDummy.MapEditorView) mapController.getMapEditorViewAUI();
        map = new MapBlackWhite();
        name = "hallo";
        mapString = "x,x,x,x,x,x,x,x,x,x\n"
                + "x,x,x,x,x,x,x,x,x,x\n"
                + "x,x,x,x,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-\n"
                + "-,-,-,-,-,-,-,-,-,-\n";
    }


    @Test
    public void testGenerateMapToEditor() {
        Assert.fail();
    }

    @Test
    public void testLoadMapToEditor() throws FileNotFoundException {
        File outFile = new File(MapController.MAP_FOLDER + name + ".map");
        PrintWriter out = new PrintWriter(outFile);
        out.write(mapString);
        out.close();

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 10; x++) {
                map.set(true, x, y);
            }
        }
        for (int x = 0; x < 4; x++) {
            map.set(true, x, 2);
        }

        mapController.loadMapToEditor(name);
        System.out.println(map.toString() + "\n");
        System.out.println(mapEditorView.getMap().toString());
        Assert.assertEquals(map, mapEditorView.getMap());

        outFile.delete();
    }

    @Test(expected = NullPointerException.class)
    public void testSaveMapWithNull() {
        //teste saveMap ohne map
        mapController.saveMap(name, null);
    }

    @Test
    public void testSaveMap() {

        //teste mit korrekter map ohne Namen
        mapController.saveMap("", map);
        Assert.assertTrue("Es hätte eine Meldung aufgerufen werden müssen",
                mapEditorView.getNotifications().contains("Die Karte muss einen Namen enthalten!"));

        //teste mit erwarteter map und name
        for (int y = 0; y < Map.SIZE_Y; y++) {
            for (int x = 0; x < 2; x++) {
                map.set(true, x, y);
            }
        }

        for (int x = 0; x < 4; x++) {
            map.set(true, x, 3);
        }
        mapController.saveMap("lisasNeuerTest.txt", map);
        File savedMap = new File(MapController.MAP_FOLDER, "lisasNeuerTest.txt" + ".map");
        Assert.assertTrue("", savedMap.exists());
        savedMap.delete();
    }

}
