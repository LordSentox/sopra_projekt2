package de.sopra.javagame.util;

import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import org.junit.Test;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 06.09.2019
 * @since 06.09.2019
 */
public class CardStackUtilTest {

    @Test
    public void createFloodCardStackTest() {
        MapTile[][] tiles = new MapTile[1][];
        MapTile tile = new MapTile("tile", PlayerType.NONE, ArtifactType.NONE);
        tiles[0] = new MapTile[]{tile};
        CardStack<FloodCard> floodCardStack = CardStackUtil.createFloodCardStack(tiles);
        //TODO
    }

    @Test
    public void createArtifactCardStackTest() {
        //TODO
    }

}