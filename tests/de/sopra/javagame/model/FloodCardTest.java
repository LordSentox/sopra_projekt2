package de.sopra.javagame.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Max Bühmann, Mel Mel
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class FloodCardTest {

    private MapTile blubBlub;
    private FloodCard wannKommtDieFlut;

    @Before
    public void setup() {
        blubBlub = MapTile.fromNumber(5);
        wannKommtDieFlut = new FloodCard(blubBlub);
    }

    @Test
    public void testFlood() {        
        wannKommtDieFlut.setTile(blubBlub);
        assertEquals(MapTileState.DRY, wannKommtDieFlut.getTile().getState());

        wannKommtDieFlut.flood();
        assertEquals(MapTileState.FLOODED, wannKommtDieFlut.getTile().getState());

        wannKommtDieFlut.flood();
        assertEquals(MapTileState.GONE, wannKommtDieFlut.getTile().getState());

    }

    @Test(expected = IllegalStateException.class)
    public void testWrongFlood() {

        wannKommtDieFlut = new FloodCard(blubBlub);
        wannKommtDieFlut.flood();
        wannKommtDieFlut.flood();

        //Test
        wannKommtDieFlut.flood();
    }

    @Test
    public void testEquals() {
        FloodCard newWannKommtDieFlut = new FloodCard(MapTile.fromNumber(4));
        FloodCard sameNumberWannKommtDieFlut = new FloodCard(MapTile.fromNumber(5));
        
        boolean isEqual = wannKommtDieFlut.equals(wannKommtDieFlut);
        Assert.assertTrue("", isEqual);
        
        isEqual = wannKommtDieFlut.equals(newWannKommtDieFlut);
        Assert.assertFalse("", isEqual);
        
        isEqual = wannKommtDieFlut.equals(sameNumberWannKommtDieFlut);
        Assert.assertTrue("", isEqual);
        
        isEqual = newWannKommtDieFlut.equals(wannKommtDieFlut);
        Assert.assertFalse("", isEqual);
        
        isEqual = wannKommtDieFlut.equals(null);
        Assert.assertFalse("", isEqual);
        
    }
}