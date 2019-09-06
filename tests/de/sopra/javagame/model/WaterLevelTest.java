package de.sopra.javagame.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WaterLevelTest {

    private WaterLevel waterLevel;

    @Before
    public void setUp() {
        waterLevel = new WaterLevel();
    }

    @Test
    public void isGameLostTest() {
        Assert.assertFalse("Game lost despite water level not being 9", waterLevel.isGameLost());
        for (int i = 0; i < 10; i++) {
            waterLevel.increment();
        }
        Assert.assertTrue("Game not lost despite water level being 9", waterLevel.isGameLost());
    }

    @Test
    public void getDrawAmountTest() {
        Assert.assertEquals("Incorrect amount of cards drawn", 2, waterLevel.getDrawAmount());
        waterLevel.increment();
        waterLevel.increment();
        Assert.assertEquals("Incorrect amount of cards drawn", 3, waterLevel.getDrawAmount());
        waterLevel.increment();
        waterLevel.increment();
        waterLevel.increment();
        Assert.assertEquals("Incorrect amount of cards drawn", 4, waterLevel.getDrawAmount());
        waterLevel.increment();
        waterLevel.increment();
        Assert.assertEquals("Incorrect amount of cards drawn", 5, waterLevel.getDrawAmount());
        waterLevel.increment();
        waterLevel.increment();
        waterLevel.increment();
        waterLevel.increment();
        waterLevel.increment();
        waterLevel.increment();
        Assert.assertEquals("Incorrect amount of cards drawn", 5, waterLevel.getDrawAmount());
    }

    @Test
    public void copyTest() {
        WaterLevel level = new WaterLevel();
        level.increment();
        level.increment();
        WaterLevel copy = level.copy(); //make copy
        copy.increment(); //change copy
        copy.increment();
        copy.increment();
        //original should not change
        Assert.assertNotEquals(level, copy);
        Assert.assertNotEquals(level.getDrawAmount(), copy.getDrawAmount());
    }

}