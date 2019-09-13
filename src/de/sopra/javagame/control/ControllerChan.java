package de.sopra.javagame.control;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.InGameViewAUI;
import de.sopra.javagame.view.MapEditorViewAUI;

import java.io.File;
import java.util.List;

/**
 * @author Max Bühmann, Melanie Arnds
 */
public class ControllerChan {
    public static final String SAVE_GAME_FOLDER = "data/save_games/";
    public static final String REPLAY_FOLDER = "data/replays/";
    public static final File SETTINGS_FILE = new File("data/settings");

    private final ActivePlayerController activePlayerController;
    private final GameFlowController gameFlowController;
    private final InGameUserController inGameUserController;
    private final HighScoresController highScoresController;
    private final MapController mapController;
    private final AIController aiController;

    private String gameName;
    private InGameViewAUI inGameViewAUI;

    private JavaGame javaGame;
    private Action currentAction;

    public ControllerChan() {
        this.javaGame = null;
        this.activePlayerController = new ActivePlayerController(this);
        this.gameFlowController = new GameFlowController(this);
        this.inGameUserController = new InGameUserController(this);
        this.highScoresController = new HighScoresController(this);
        this.mapController = new MapController(this);
        this.aiController = new AIController(this); //setAI um die AI festzulegen
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

    public AIController getAiController() {
        return aiController;
    }

    public JavaGame getJavaGame() {
        return javaGame;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * startNewGame erstellt ein neues JavaGame
     *
     * @param tiles      ein boolean Array, das die Form der Insel angibt
     * @param players    ein Listli, welches die teilnehmenden Spielfiguren enthält
     * @param difficulty die Schwierigkeitsstufe des JavaGames {@link Difficulty}
     */
        
    public void startNewGame(String mapName, boolean[][] tiles, List<Pair<PlayerType, Boolean>> players, Difficulty difficulty) {
        MapTile[][] map = MapUtil.createAndFillMap(tiles);
        if (map != null) {
            Pair<JavaGame, Action> pair = JavaGame.newGame(mapName, map, difficulty, players);
            this.javaGame = pair.getLeft();
            this.currentAction = pair.getRight();
        }
        
        this.inGameViewAUI.refreshAll();
    }

    /**
     * loadGame lädt ein gespeichertes JavaGame aus einer Datei
     *
     * @param file ist die zu ladende Spieldatei
     */

    public void loadGame(File file) {

    }

    /**
     * saveGame speichert das aktuell ausgeführte JavaGame in einer Datei und gibt ihm einen Namen
     *
     * @param gameName ist der Name des Spiels, für das eine Datei angelegt werden soll
     */

    public void saveGame(String gameName) {
        this.gameName = gameName;
    }

    /**
     * replayGame spielt ein beendetes Spiel ab, welches vorher geladen wurde
     */
    public void replayGame() {

    }

    /**
     * continueGame setzt ein gespeichertes Spiel nach dem zuletzt ausgeführten Spielzug (vor dem Speichern) fort
     */
    public void continueGame() {

    }

    public void finishAction() {
        this.currentAction = this.javaGame.finishAction(this.currentAction);
    }

    public Action getCurrentAction() {
        return this.currentAction;
    }

    public String getGameName() {
        return gameName;
    }
}
