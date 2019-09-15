package de.sopra.javagame.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class MapCheckUtilTest {
    @Test
    public void checkMapValidity() throws IOException {
        // Lade die Schwarz-Weißen Karten
        String tooBig = new String(Files.readAllBytes(Paths.get("duke.java")), StandardCharsets.UTF_8);

        // Teste mit unvollständiger map
        mapController.saveMap(name, map);
        Assert.assertTrue("Es hätte eine Meldung aufgerufen werden müssen",
                mapEditorView.getNotifications().contains("Die Karte muss genau 24 Felder enthalten!"));


        // Teste mit zu voller map
        mapEditorView.getNotifications().clear();
        for (int y = 0; y < Map.SIZE_Y; y++) {
            for (int x = 0; x < Map.SIZE_X; x++) {
                map.set(true, x, y);
            }
        }

        mapController.saveMap(name, map);
        Assert.assertTrue("Es hätte eine Meldung aufgerufen werden müssen",
                mapEditorView.getNotifications().contains("Die Karte muss genau 24 Felder enthalten!"));


        // Teste unzusammenhängende map
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



        content = new String(Files.readAllBytes(Paths.get(name + ".map")), StandardCharsets.UTF_8);
        Assert.assertEquals(mapString, content);
    }

    @Test
    public void checkMapValidityExtended() {
        fail("Not yet implemented");
    }
}