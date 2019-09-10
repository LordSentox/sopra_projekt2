package de.sopra.javagame.model;

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
        wannKommtDieFlut = new FloodCard(blubBlub);
        
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

}