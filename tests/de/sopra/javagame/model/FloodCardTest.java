package de.sopra.javagame.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Max Bühmann, Melanie Arnds
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class FloodCardTest {
	
	private MapTile blubBlub;
	private FloodCard wannKommtDieFlut;
	
	@Before
	public void setup(){
		blubBlub = new MapTile();
		wannKommtDieFlut = new FloodCard();
	}
	@Test
	public void testFlood(){
		blubBlub = new MapTile();
		wannKommtDieFlut = new FloodCard();
		
		wannKommtDieFlut.setTile(blubBlub);
		assertEquals(MapTileState.DRY, wannKommtDieFlut.getTile().getState());
		
		wannKommtDieFlut.flood();
		assertEquals(MapTileState.FLOODED, wannKommtDieFlut.getTile().getState());
		
		wannKommtDieFlut.flood();
		assertEquals(MapTileState.GONE, wannKommtDieFlut.getTile().getState());
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testWrongFlood(){
		
		wannKommtDieFlut = new FloodCard();
		wannKommtDieFlut.flood();
		wannKommtDieFlut.flood();
		
		//Test
		wannKommtDieFlut.flood();
		
	}

}