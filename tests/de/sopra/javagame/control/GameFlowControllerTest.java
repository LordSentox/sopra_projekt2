package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.MapTileState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class GameFlowControllerTest {
    private ControllerChan controllerChan;
    private TestDummy.InGameView inGameViewAUI;
    private GameFlowController gameFlowController;
    MapTile dry;

    @Before
    public void setUp() throws Exception {
        controllerChan = TestDummy.getDummyControllerChan();
        inGameViewAUI = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
        gameFlowController = controllerChan.getGameFlowController();
        this.dry = new MapTile(MapTileProperties.CORAL_PALACE);
        this.dry.setState(MapTileState.DRY);
    }

    @Test
    public void testDrawArtifactCards() {
        fail("Not yet implemented");
    }

    @Test
    public void testDrawFloodCards() {
        fail("Not yet implemented");
    }

    @Test
    public void testCheckGameEnded() {
        fail("Not yet implemented");
    }

    @Test
    public void testUndoRedo() {
        this.dry.drain();
        gameFlowController.undo();
        Assert.assertEquals(MapTileState.DRY, this.dry.getState());
        gameFlowController.undo();
        Assert.assertFalse(controllerChan.getJavaGame().canUndo());
        gameFlowController.redo();
        Assert.assertEquals(MapTileState.FLOODED, this.dry.getState());
        Assert.assertFalse(gameFlowController.redo());

    }

}