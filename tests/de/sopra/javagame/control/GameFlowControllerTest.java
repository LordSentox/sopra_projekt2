package de.sopra.javagame.control;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.WaterLevel;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.view.GameWindow;
import org.junit.Assert;

public class GameFlowControllerTest {
    private ControllerChan controllerChan;
    private GameWindow inGameViewAUI;
    private GameFlowController gameFlowController;
    MapTile dry;

    @Before
    public void setUp() throws Exception {
        controllerChan = TestDummy.getDummyControllerChan();
        inGameViewAUI = (GameWindow) controllerChan.getInGameViewAUI();
        gameFlowController = controllerChan.getGameFlowController();
        this.dry = new MapTile();
        this.dry.setState(MapTileState.DRY);
        

    }

    @Test
    public void testDrawArtifactCards() {
        

    }

    @Test
    public void testDrawFloodCards() {
        
    }

    @Test
    public void testCheckGameEnded() {
        Assert.assertTrue();
    }

    @Test
    public void testUndoRedo() {
        this.dry.drain();
        gameFlowController.undo();
        Assert.assertEquals(MapTileState.DRY, this.dry.getState());
        Assert.assertFalse(gameFlowController.undo());
        gameFlowController.redo();
        Assert.assertEquals(MapTileState.FLOODED, this.dry.getState());
        Assert.assertFalse(gameFlowController.redo());

    }

}
