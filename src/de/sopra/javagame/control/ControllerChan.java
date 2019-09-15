package de.sopra.javagame.control;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.MapFull;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.InGameViewAUI;
import de.sopra.javagame.view.MapEditorViewAUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.*;
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
     * @param map        Die Karte mit der Inselform
     * @param players    ein Listli, welches die teilnehmenden Spielfiguren enthält
     * @param difficulty die Schwierigkeitsstufe des JavaGames {@link Difficulty}
     */

    public void startNewGame(String mapName, MapBlackWhite map, List<Pair<PlayerType, Boolean>> players, Difficulty difficulty) {
        MapFull fullMap = MapUtil.createAndFillMap(map);
        Pair<JavaGame, Action> pair = JavaGame.newGame(mapName, fullMap, difficulty, players);
        this.javaGame = pair.getLeft();
        this.currentAction = pair.getRight();

        this.inGameViewAUI.refreshAll();
    }

    /**
     * loadGame lädt ein gespeichertes JavaGame aus einer Datei
     *
     * @param loadGameName ist der Name der zu ladenden Spieldatei
     */

    public void loadGame(String loadGameName) {
        this.gameName = loadGameName;
        try (FileInputStream fileInputStream = new FileInputStream(SAVE_GAME_FOLDER + this.gameName + ".save");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            this.javaGame = (JavaGame) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Es gab keine solche Datei.");
        } catch (IOException e) {
            System.out.println("Beim Import ist ein Fehler aufgetreten!");
        } catch (ClassNotFoundException e) {
            System.out.println("Die Klasse wurde nicht gefunden!");
        }
        //schneide ".save" ab
        this.gameName = this.gameName.substring(0, this.gameName.length() - 5);

    }

    /**
     * saveGame speichert das aktuell ausgeführte JavaGame in einer Datei und gibt ihm einen Namen
     *
     * @param gameName ist der Name des Spiels, für das eine Datei angelegt werden soll
     */

    public void saveGame(String gameName) {
        if (gameName.isEmpty()) {
            this.gameName = "current";
        } else {
            this.gameName = gameName;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.getGameName() + ".save");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(javaGame);
            //Wenn das Spiel beendet wurde, speichere es auch in den HighScores
            if (javaGame.getPreviousAction().isGameEnded()) {
                while (javaGame.canUndo()) {
                    javaGame.undoAction();
                }
                try (FileOutputStream fileOutputStreamReplay =
                     new FileOutputStream(HighScoresController.REPLAY_FOLDER + this.gameName + ".replay");
                     ObjectOutputStream objectOutputStreamReplay =
                     new ObjectOutputStream(fileOutputStreamReplay)) {
                            objectOutputStream.writeObject(javaGame);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Es gab keine solche Datei.");
        } catch (IOException e) {
            System.out.println("Beim Export ist ein Fehler aufgetreten.");
        }
    }

    /**
     * replayGame spielt ein beendetes Spiel ab, welches vorher geladen wurde
     */
    public void replayGame(String replayGameName) {
        inGameViewAUI.setIsReplayWindow(true);
        saveGame("");
        loadGame(replayGameName);
        inGameViewAUI.refreshAll();
    }

    /**
     * continueGame setzt ein gespeichertes Spiel nach dem zuletzt ausgeführten Spielzug (vor dem Speichern) fort
     */
    public void continueGame() {
        //Das Spiel mit Namen mapName + ".currentlyPlayed" im Orner SAVE_GAME_FOLDER und alles refreshen
        try (FileInputStream fileInputStream = new FileInputStream("current.save");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            this.javaGame = (JavaGame) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Es gab keine solche Datei.");
        } catch (IOException e) {
            System.out.println("Beim Import ist ein Fehler aufgetreten!");
        } catch (ClassNotFoundException e) {
            System.out.println("Die Klasse wurde nicht gefunden!");
        }
        //lösche geladenes Spiel aus Speicher
        boolean delete = new File(gameName).delete();
        if (!delete) {
            System.out.println("Die Datei wurde nicht gelöscht!");
        }
        //schneide ".currentlyPlayed" ab
        this.gameName = this.gameName.substring(0, this.gameName.length() - 16);
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
