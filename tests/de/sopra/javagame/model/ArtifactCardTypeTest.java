package de.sopra.javagame.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * <h1>projekt2</h1>
 *
 * @author Roman Korweck, Max Bühmann, Melanie Arnds
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class ArtifactCardTypeTest {
	
	@Test
	public void testToArtifectType(){
		assertEquals(ArtifactType.AIR, ArtifactCardType.AIR.toArtifactType());
		assertEquals(ArtifactType.EARTH, ArtifactCardType.EARTH.toArtifactType());
		assertEquals(ArtifactType.FIRE, ArtifactCardType.FIRE.toArtifactType());
		assertEquals(ArtifactType.WATER, ArtifactCardType.WATER.toArtifactType());
		
		assertEquals(ArtifactType.NONE, ArtifactCardType.HELICOPTER.toArtifactType());
		assertEquals(ArtifactType.NONE, ArtifactCardType.SANDBAGS.toArtifactType());
		assertEquals(ArtifactType.NONE, ArtifactCardType.WATERS_RISE.toArtifactType());
	}
	
	
	

}