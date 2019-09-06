package de.sopra.javagame.model;

import de.sopra.javagame.util.CardStackUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Maximus Rockus Buhmanus, Melanie Arnds
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class CardStackTest {
    private CardStack<FloodCard> floodCardStack;
    private CardStack<ArtifactCard> artifactCardStack;
    MapTile[][] tiles;
    MapTile wirHabenLandGefunden;
    List<ArtifactCard> hand;

    @Before
    public void setUp() {
        wirHabenLandGefunden = new MapTile();
        tiles = new MapTile[1][1];
        tiles[1][1] = wirHabenLandGefunden;

        hand = new ArrayList<ArtifactCard>();
        floodCardStack = new CardStackUtil().createFloodCardStack(tiles);
        artifactCardStack = new CardStackUtil().createArtifactCardStack();
    }

    @Test
    public void testDraw() {
        //Test draw floodCard

        assertEquals(wirHabenLandGefunden, floodCardStack.draw(1, true).get(0).getTile());
        assertEquals(wirHabenLandGefunden, floodCardStack.draw(1, true).get(0).getTile());

        //Test draw artifactCard
        hand.addAll(artifactCardStack.draw(1, false));
        assertEquals(1, hand.size());

        hand.addAll(artifactCardStack.draw(1, true));
        assertEquals(1, hand.size());

    }

    @Test
    public void testShuffleDrawStack() {
        //test artifactCardStack shuffle mopped
        CardStack<ArtifactCard> moppedStapel = new CardStackUtil().createArtifactCardStack();
        CardStack<ArtifactCard> moppedStapel2 = new CardStackUtil().createArtifactCardStack();

        int countEquals = 0;

        for (int i = 0; i < 5; i++) {
            //moppedStapel.shuffleDrawStack();
            moppedStapel2.shuffleDrawStack();
            if (moppedStapel.equals(moppedStapel2)) {
                countEquals++;
            }
        }

        assertTrue(countEquals <= 1);
        //test floodCardStack shuffle mopped
        tiles = new MapTile[1][2];
        wirHabenLandGefunden = new MapTile();
        tiles[1][1] = wirHabenLandGefunden;
        MapTile javaIstauchEineInsel = new MapTile();
        tiles[1][2] = javaIstauchEineInsel;
        
        floodCardStack = new CardStackUtil().createFloodCardStack(tiles);
        CardStack<FloodCard> floodMoppedStapel = new CardStackUtil().createFloodCardStack(tiles);
        
        int countEqualsForFloodCard = 0;

        for (int i = 0; i < 5; i++) {
            //moppedStapel.shuffleDrawStack();
            floodCardStack.shuffleDrawStack();
            if (floodMoppedStapel.equals(floodCardStack)) {
                countEqualsForFloodCard++;
            }
        }
        //bei 5 maligem mischen mit 2 karten, sollte höchsten 3 mal die reihenfolge gleich sein
        assertTrue(countEquals <= 3);
        
    }
}