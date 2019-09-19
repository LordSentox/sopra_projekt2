package de.sopra.javagame.model;

import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.util.map.MapUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Max Bühmann, Mel Mel
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class FloodCardTest {

    private MapFull blubBlub;
    private FloodCard wannKommtDieFlut;

    @Before
    public void setup() throws IOException {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        this.blubBlub = MapUtil.readFullMapFromString(testMapString);
        wannKommtDieFlut = new FloodCard(blubBlub.get(MapTileProperties.MISTY_MARSH).getProperties());
    }

    @Test
    public void testFlood() {        
        assertEquals(MapTileState.DRY, blubBlub.get(wannKommtDieFlut.getTile()).getState());

        wannKommtDieFlut.flood(blubBlub);
        assertEquals(MapTileState.FLOODED, blubBlub.get(wannKommtDieFlut.getTile()).getState());

        wannKommtDieFlut.flood(blubBlub);
        assertEquals(MapTileState.GONE, blubBlub.get(wannKommtDieFlut.getTile()).getState());

    }

    @Test(expected = IllegalStateException.class)
    public void testWrongFlood() {
        wannKommtDieFlut.flood(blubBlub);
        wannKommtDieFlut.flood(blubBlub);

        //Test
        wannKommtDieFlut.flood(blubBlub);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testEquals() {
        FloodCard newWannKommtDieFlut = new FloodCard(MapTile.fromNumber(4).getProperties());
        FloodCard sameNumberWannKommtDieFlut = new FloodCard(MapTile.fromNumber(5).getProperties());
        
        boolean isEqual = wannKommtDieFlut.equals(wannKommtDieFlut);
        Assert.assertTrue("Equals sollte bei ein und derselben Karte true ausgeben", isEqual);
        
        isEqual = wannKommtDieFlut.equals(newWannKommtDieFlut);
        Assert.assertFalse("Equals sollte nicht bei verschiedenen Karten mit verschiedenen Nummern true ausgeben", isEqual);
        
        isEqual = wannKommtDieFlut.equals(sameNumberWannKommtDieFlut);
        Assert.assertTrue("Equals sollte bei einer Karte und einer anderen mit gleicher Nummer true ausgeben", isEqual);
        
        isEqual = newWannKommtDieFlut.equals(wannKommtDieFlut);
        Assert.assertFalse("Equals sollte nicht bei verschiedenen Karten mit verschiedenen Nummern true ausgeben", isEqual);
        
        isEqual = wannKommtDieFlut.equals(null);
        Assert.assertFalse("Equals sollte bei Vergleich mit null false sein", isEqual);
        
    }
}