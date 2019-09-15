package de.sopra.javagame.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

public class MapCheckUtilTest {
    @Test
    public void checkMapValidity() throws IOException {
        // Teste mit unvollständiger map
        MapBlackWhite map = new MapBlackWhite();
        Assert.assertFalse("Unvolständige Karte wurde nicht erkannt", MapCheckUtil.checkMapValidity(map));

        // Teste mit zu voller map
        for (int y = 0; y < Map.SIZE_Y; y++) {
            for (int x = 0; x < Map.SIZE_X; x++) {
                map.set(true, x, y);
            }
        }

        // Teste unzusammenhängende map
        fail("Nicht implementiert");
    }

    @Test
    public void checkMapValidityExtended() {
        fail("Not yet implemented");
    }
}