package de.sopra.javagame.control;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.HighScoresViewController;
import junit.framework.Assert;

public class HighScoresControllerTest {
	
	private ControllerChan controllerChan; 
	private HighScoresController highScoresController;
	private TestDummy.HighScoreView highScoresView;
	private JavaGame javaGame;


	@Before
	public void setUp() {
		controllerChan = TestDummy.getDummyControllerChan();
		highScoresController = controllerChan.getHighScoresController();
		highScoresView = (TestDummy.HighScoreView) highScoresController.getHighScoresViewAUI();
		javaGame = controllerChan.getJavaGame();
	}

	@Test
	public void testLoadHighScores() {

	    
	    HighScore no1 = new HighScore("no1", "newMap", 1000);
				
		highScoresController.loadHighScores("newMap");
		Assert.assertTrue("", highScoresView.getHighScores().contains(no1));
		
	}

	@Test
	public void testResetHighScores() {
		fail("Not yet implemented");
	}

}
