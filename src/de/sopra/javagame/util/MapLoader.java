package de.sopra.javagame.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MapLoader {
    public MapBlackWhite loadMap(String mapName) {
        List<String> strist;
        try {
            strist = Files.readAllLines(Paths.get(getClass().getResource("/maps/" + mapName + ".map").toURI()));

            return MapUtil.readBlackWhiteMapFromString(String.join("\n", strist));
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
