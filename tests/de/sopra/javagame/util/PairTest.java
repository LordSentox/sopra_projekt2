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
public class PairTest {

    @Test
    public void coverage() {
        Pair<String, String> pair = new Pair<>("left", "right");
        assertEquals(pair.getLeft(), "left");
        assertEquals(pair.getRight(), "right");
        pair.setLeft("links");
        pair.setRight("rechts");
        assertEquals(pair.getLeft(), "links");
        assertEquals(pair.getRight(), "rechts");
    }

}