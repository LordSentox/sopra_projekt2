package model.control;

import model.model.JavaGame;
import model.view.InGameViewAUI;
import java.util.List;
import model.model.Difficulty;

public class ControllerChan {

	private JavaGame javaGame;

	private ActivePlayerController activePlayerController;

	private GameFlowController gameFlowController;

	private InGameUserController inGameUserController;

	private HighScoresController highScoresController;

	private InGameViewAUI inGameViewAUI;

	private MapController mapController;

	public void startNewGame(boolean[][] tiles, List players, Difficulty difficulty) {

	}

	public void loadGame(File file) {

	}

	public void saveGame(File file) {

	}

	public void replayGame() {

	}

	public void continueGame() {

	}

}
