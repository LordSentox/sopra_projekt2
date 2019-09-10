package de.sopra.javagame.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals("UP", Direction.UP.name());
        assertEquals("DOWN", Direction.DOWN.name());
        assertEquals("LEFT", Direction.LEFT.name());
        assertEquals("RIGHT", Direction.RIGHT.name());
    }

    @Test
    public void translationTest() {
        Point translationUp = Direction.UP.translate(new Point(0, 0));
        assertEquals(new Point(0, -1), translationUp);
        Point translationDown = Direction.DOWN.translate(new Point(0, 0));
        assertEquals(new Point(0, 1), translationDown);
        Point translationLeft = Direction.LEFT.translate(new Point(0, 0));
        assertEquals(new Point(-1, 0), translationLeft);
        Point translationRight = Direction.RIGHT.translate(new Point(0, 0));
        assertEquals(new Point(1, 0), translationRight);
    }

}