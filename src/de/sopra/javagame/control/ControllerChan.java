package de.sopra.javagame.control;

import com.google.gson.GsonBuilder;
import de.sopra.javagame.control.ai.GameAI;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Triple;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.map.MapBlackWhite;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.util.serialize.typeadapter.ActionTypeAdapter;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.InGameViewAUI;
import de.sopra.javagame.view.MapEditorViewAUI;

import java.io.*;
import java.util.List;

import static de.sopra.javagame.util.DebugUtil.debug;

/**
 * @author Max Bühmann, Melanie Arnds
 */
public class ControllerChan {
    public static final String SAVE_GAME_FOLDER = "data/save_games/";
    public static final String REPLAY_FOLDER = "data/replays/";
    public static final File SETTINGS_FILE = new File("data/settings.json");

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
        this.highScoresController = new HighScoresController();
        this.mapController = new MapController();
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
    
    public void startNewGame(String mapName, MapBlackWhite map, List<Triple<PlayerType, String, Boolean>> players, Difficulty difficulty) {
        Pair<JavaGame, Action> pair = JavaGame.newGame(mapName, map, difficulty, players);
        
        startGame(pair);
    }    
    public void startNewGame(String mapName, Triple<MapFull, CardStack<ArtifactCard>, CardStack<FloodCard>> tournamentTriple, List<Triple<PlayerType, String, Boolean>> players, Difficulty difficulty) {
        Pair<JavaGame, Action> pair = JavaGame.newGame(mapName, tournamentTriple, difficulty, players);
        
        startGame(pair);
    }
    
    public void startGame(Pair<JavaGame, Action> pair) {
        this.currentAction = pair.getRight();
        debug("initial players: " + currentAction.getPlayers().size());
        aiController.connectTrackers();
        aiController.setAI(GameAI.DECISION_BASED_AI);

        this.currentAction = pair.getLeft().finishAction(this.currentAction);

        this.javaGame = pair.getLeft();

        this.inGameViewAUI.refreshSome();
        //6 MapTiles zu beginn fluten
        CardStack<FloodCard> floodCardCardStack = this.getCurrentAction().getFloodCardStack();
        for (int i = 0; i < 6; i++) {
            //TODO: Wait
            //try{ Thread.sleep(1000); }catch(InterruptedException ignored){}

            MapFull map = this.getCurrentAction().getMap();
            FloodCard floodCard = floodCardCardStack.draw(true);
            floodCard.flood(map);

            this.inGameViewAUI.refreshMapTile(map.getPositionForTile(floodCard.getTile()), map.get(floodCard.getTile()));
            this.inGameViewAUI.refreshFloodStack(floodCardCardStack);

        }
        //player auf map packen
        this.currentAction.getPlayers().forEach(player -> {
            this.inGameViewAUI.refreshPlayerPosition(player.getPosition(), player.getType());
        });

        //2 Artefaktkarten ziehen
        this.currentAction.getPlayers().forEach(player -> {

            player.getHand().add(this.currentAction.getArtifactCardStack().drawAndSkip(card -> card.getType() == ArtifactCardType.WATERS_RISE));
            player.getHand().add(this.currentAction.getArtifactCardStack().drawAndSkip(card -> card.getType() == ArtifactCardType.WATERS_RISE));
            this.inGameViewAUI.refreshHand(player.getType(), player.getHand());
        });

        //Pilot braucht auch im ersten Zug seine Spezialfähigkeit
        this.getCurrentAction().getActivePlayer().onTurnStarted();

        //start KI wenns ihr Zug ist
        //TODO

    }

    /**
     * loadGame lädt ein gespeichertes JavaGame aus einer Datei
     *
     * @param loadGameName ist der Name der zu ladenden Spieldatei
     */

    public void loadSaveGame(String loadGameName, boolean isReplay) {
        File file = new File(isReplay ? (REPLAY_FOLDER + loadGameName + ".replay") : (SAVE_GAME_FOLDER + loadGameName + ".save"));

        try (FileReader fileReader = new FileReader(file)) {

            this.javaGame = new GsonBuilder()
                    .registerTypeAdapter(Action.class, new ActionTypeAdapter())
                    .create()
                    .fromJson(fileReader, JavaGame.class);

            // Setze die momentane Aktion auf die nächste des geladenen Spiels
            this.currentAction = this.javaGame.getPreviousAction().copy();

            // Spiel wurde erfolgreich geladen, der Name des momentanen Spiels kann gesetzt werden und
            // das GUI kann informiert werden
            this.gameName = loadGameName;
            this.inGameViewAUI.refreshSome();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Es gab keine solche Datei.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Beim Import ist ein Fehler aufgetreten!");
        }
    }

    public void rewindToStart() {
        while (this.javaGame.canUndo()) {
            this.javaGame.undoAction();
        }
    }

    /**
     * saveGame speichert das aktuell ausgeführte JavaGame in einer Datei und gibt ihm einen Namen
     *
     * @param gameName ist der Name des Spiels, für das eine Datei angelegt werden soll
     */

    public void saveGame(String gameName) {
        boolean gameFinished = javaGame.getPreviousAction().isGameEnded();
        // FIXME: Funktioniert das wirklich so? Der Name müsste ja erst gesetzt worden sein. Vielleicht lieber auf null
        // überprüfen?
        if (gameName.isEmpty()) {
            this.gameName = "current";
        } else {
            this.gameName = gameName;
        }

        File saveFile;
        if (gameFinished) {
            saveFile = new File(ControllerChan.REPLAY_FOLDER + this.getGameName() + ".replay");
        } else {
            saveFile = new File( ControllerChan.SAVE_GAME_FOLDER + this.getGameName() + ".save");
        }

        try (FileWriter fileWriter = new FileWriter(saveFile, false)) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Action.class, new ActionTypeAdapter())
                    .create()
                    .toJson(javaGame, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * replayGame spielt ein beendetes Spiel ab, welches vorher geladen wurde
     */
    public void replayGame(String replayGameName) {
        inGameViewAUI.setIsReplayWindow(true);
        saveGame("");
        loadSaveGame(replayGameName, true);
        rewindToStart();
        inGameViewAUI.refreshSome();
    }

    /**
     * continueGame setzt ein gespeichertes Spiel nach dem zuletzt ausgeführten Spielzug (vor dem Speichern) fort
     */
    public void continueGame() {
        //Das Spiel mit Namen mapName + ".save" im Data-Ordner laden und alles refreshen
        try (FileReader fileReader = new FileReader(ControllerChan.SAVE_GAME_FOLDER + "current.save")) {
            this.javaGame = new GsonBuilder()
                    .registerTypeAdapter(Action.class, new ActionTypeAdapter())
                    .create()
                    .fromJson(fileReader, JavaGame.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Es gab keine solche Datei.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Beim Import ist ein Fehler aufgetreten!");
        }

        this.gameName = "current";
    }

    public Action finishAction() {
        this.currentAction = this.javaGame.finishAction(this.currentAction);
        return this.currentAction;
    }

    void setAction(Action action) {
        this.currentAction = action;
    }

    public Action getCurrentAction() {
        return this.currentAction;
    }

    public String getGameName() {
        return gameName;
    }
}
