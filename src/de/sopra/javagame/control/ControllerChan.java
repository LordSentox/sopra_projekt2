package de.sopra.javagame.control;

import de.sopra.javagame.model.model.Difficulty;
import de.sopra.javagame.model.model.JavaGame;
import de.sopra.javagame.model.view.InGameViewAUI;

import java.io.File;
import java.util.List;

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
