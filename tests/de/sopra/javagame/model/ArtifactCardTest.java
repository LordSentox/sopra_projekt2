package de.sopra.javagame.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * <h1>projekt2</h1>
 *
 * @author Roman Korweck, Max Bühmann, Melanie Arnds
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class ArtifactCardTest {

    //keine zu testenden Methoden
    private ArtifactCard cardToTest;

    @Before
    public void setUp() {
        cardToTest = new ArtifactCard(ArtifactCardType.HELICOPTER);
    }

    @Test
    public void testGetType() {
        assertEquals(ArtifactCardType.HELICOPTER, cardToTest.getType());
        cardToTest = new ArtifactCard(ArtifactCardType.AIR);
        assertEquals(ArtifactCardType.AIR, cardToTest.getType());
        cardToTest = new ArtifactCard(ArtifactCardType.EARTH);
        assertEquals(ArtifactCardType.EARTH, cardToTest.getType());
        cardToTest = new ArtifactCard(ArtifactCardType.FIRE);
        assertEquals(ArtifactCardType.FIRE, cardToTest.getType());
        cardToTest = new ArtifactCard(ArtifactCardType.WATER);
        assertEquals(ArtifactCardType.WATER, cardToTest.getType());
        cardToTest = new ArtifactCard(ArtifactCardType.SANDBAGS);
        assertEquals(ArtifactCardType.SANDBAGS, cardToTest.getType());
        cardToTest = new ArtifactCard(ArtifactCardType.WATERS_RISE);
        assertEquals(ArtifactCardType.WATERS_RISE, cardToTest.getType());
    }

    @Test
    public void copyTest() {
        ArtifactCard copy = cardToTest.copy();
        assertFalse(copy == cardToTest);
        assertEquals(copy.getType(), cardToTest.getType());
    }

}