package de.sopra.javagame.control;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.util.HighScore;

public class HighScoresControllerTest {
	
	private ControllerChan controllerChan; 
	private HighScoresController highScoresController;
	private JavaGame javaGame;


	@Before
	public void setUp() {
		controllerChan = TestDummy.getDummyControllerChan();
		highScoresController = controllerChan.getHighScoresController();
		javaGame = controllerChan.getJavaGame();
	}

	@Test
	public void testLoadHighScores() {
		ArrayList<HighScore> list = new ArrayList<HighScore>();
		JavaGame game = new JavaGame();
	}

	@Test
	public void testResetHighScores() {
		fail("Not yet implemented");
	}

}
