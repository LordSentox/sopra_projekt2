package de.sopra.javagame.control;

import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.InGameViewAUI;
import de.sopra.javagame.view.MapEditorViewAUI;

import java.io.File;
import java.util.List;

public class ControllerChan {
    
    public static final String SAVE_GAME_FOLDER = "data/save_games/";
    
    public static final String REPLAY_FOLDER = "data/replays/";
    
    public static final File SETTINGS_FILE = new File("data/settings");

    private final ActivePlayerController activePlayerController;

    private final GameFlowController gameFlowController;

    private final InGameUserController inGameUserController;

    private final HighScoresController highScoresController;

    private final MapController mapController;

    private InGameViewAUI inGameViewAUI;

    private JavaGame javaGame;

    public ControllerChan() {
        this.javaGame = null;
        this.activePlayerController = new ActivePlayerController(this);
        this.gameFlowController = new GameFlowController(this);
        this.inGameUserController = new InGameUserController(this);
        this.highScoresController = new HighScoresController(this);
        this.mapController = new MapController(this);
    }

    public void setMapEditorViewAUI(MapEditorViewAUI mapEditorViewAUI) {
        this.mapController.setMapEditorViewAUI(mapEditorViewAUI);
    }

    public void setHighScoresViewAUI(HighScoresViewAUI highScoresViewAUI) {
        this.highScoresController.setHighScoresViewAUI(highScoresViewAUI);
    }

    public void setInGameViewAUI(InGameViewAUI inGameViewAUI) {
        this.inGameViewAUI = inGameViewAUI;
    }

    public InGameViewAUI getInGameViewAUI() {
        return inGameViewAUI;
    }

    public ActivePlayerController getActivePlayerController() {
        return activePlayerController;
    }

    public GameFlowController getGameFlowController() {
        return gameFlowController;
    }

    public HighScoresController getHighScoresController() {
        return highScoresController;
    }

    public InGameUserController getInGameUserController() {
        return inGameUserController;
    }

    public MapController getMapController() {
        return mapController;
    }

    public JavaGame getJavaGame() {
        return javaGame;
    }

    //----------------------------------------------------------------------------------------------------

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
