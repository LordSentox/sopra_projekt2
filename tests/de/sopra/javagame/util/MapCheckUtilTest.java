package de.sopra.javagame.util;

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
    }

    @Test
    public void checkMapValidityExtended() {
        fail("Not yet implemented");
    }
}