package de.sopra.javagame.model;

import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.CardStackUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
    private MapTile[][] tiles;
    private MapTile wirHabenLandGefunden;
    private List<ArtifactCard> hand;

    @Before
    public void setUp() {
        wirHabenLandGefunden = MapTile.fromNumber(20);
        tiles = new MapTile[1][1];
        tiles[0][0] = wirHabenLandGefunden;

        hand = new ArrayList<>();
        floodCardStack = CardStackUtil.createFloodCardStack(tiles);
        artifactCardStack = CardStackUtil.createArtifactCardStack();
    }

    @Test
    public void testDraw() {
        //Test draw floodCard

        assertEquals(wirHabenLandGefunden, floodCardStack.draw(1, true).get(0).getTile());
        assertEquals(wirHabenLandGefunden, floodCardStack.draw(1, true).get(0).getTile());

        //Test draw artifactCard
        hand.addAll(artifactCardStack.draw(28, false));
        assertEquals(28, hand.size());

        hand.addAll(artifactCardStack.draw(1, true));
        assertEquals(28, hand.size());

    }

    @Test
    public void testShuffleDrawStack() {
        //test artifactCardStack shuffle mopped
        CardStack<ArtifactCard> moppedStapel = CardStackUtil.createArtifactCardStack();
        CardStack<ArtifactCard> moppedStapel2 = CardStackUtil.createArtifactCardStack();

        assertEquals("created stacks not equal", moppedStapel, moppedStapel2);

        for (int i = 0; i < 5; i++) {
            //moppedStapel.shuffleDrawStack();
            moppedStapel2.shuffleDrawStack();
            if (!moppedStapel.equals(moppedStapel2)) {
                return;
            }
        }
        fail("Stack not shuffled");
    }

    @Test
    public void copyTest() {
        CardStack<ArtifactCard> copy = this.artifactCardStack.copy();
        List<ArtifactCard> drawOriginal = this.artifactCardStack.draw(1, false);
        List<ArtifactCard> drawCopy = copy.draw(1, false);
        assertNotSame(drawCopy.get(0), drawOriginal.get(0));
        assertEquals(drawCopy.get(0).getType(), drawOriginal.get(0).getType());

        copy.shuffleDrawStack();
        drawCopy = copy.draw(2, false);
        drawOriginal = this.artifactCardStack.draw(2, false);
        if (drawCopy.get(0).equals(drawOriginal.get(0))
                && drawCopy.get(1).equals(drawOriginal.get(1)))
            fail("deck shuffle");
    }

}