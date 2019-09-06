package de.sopra.javagame.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class DirectionTest {

    @Test
    public void coverage() {
        Assert.assertEquals("UP", Direction.UP.name());
        Assert.assertEquals("DOWN", Direction.DOWN.name());
        Assert.assertEquals("LEFT", Direction.LEFT.name());
        Assert.assertEquals("RIGHT", Direction.RIGHT.name());
    }

}