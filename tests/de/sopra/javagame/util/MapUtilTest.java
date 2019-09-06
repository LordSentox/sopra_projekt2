package de.sopra.javagame.util;

import org.junit.Assert;
import org.junit.Test;

public class MapUtilTest {

    @Test
    public void createAndFillMap() {
    }

    @Test
    public void getPlayerSpawnPoint() {
    }

    @Test
    public void createMapFromNumbers() {
    }

    @Test
    public void readNumberMapFromString() {
        String extendedMap = "-; -; -; -; -; -; -; -; -; -; -; -\n" +
                             "-; -; -;20;19; -; -; 2; 6; -; -; -\n" +
                             "-; -;21;22;18;11; 3; 5; 4; 8; -; -\n" +
                             "-; -; 7;12;23;14;13; 1;17; 0; -; -\n" +
                             "-; -; -; 9;16; -; -;15;10; -; -; -\n" +
                             "-; -; -; -; -; -; -; -; -; -; -; -\n";

        int[][] numbers = MapUtil.readNumberMapFromString(extendedMap);
        Assert.assertNotNull("Zahlen wurden nicht aus dem String gelesen", numbers);

        int[][] expectedNumbers = {
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, 20, 19, -1, -1,  2,  6, -1, -1, -1},
                {-1, -1, 21, 22, 18, 11,  3,  5,  4,  8, -1, -1},
                {-1, -1,  7, 12, 23, 14, 13,  1, 17,  0, -1, -1},
                {-1, -1, -1,  9, 16, -1, -1, 15, 10, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}};

        for (int y = 0; y < expectedNumbers.length; ++y) {
            Assert.assertArrayEquals("Fehler beim Einlesen einer Kartenzeile", expectedNumbers[y], numbers[y]);
        }
    }
}