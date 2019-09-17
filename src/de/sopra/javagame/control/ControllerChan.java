package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.*;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.InGameViewAUI;
import de.sopra.javagame.view.MapEditorViewAUI;

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

    public void startNewGame(String mapName, MapBlackWhite map, List<Triple<PlayerType, String, Boolean>> players, Difficulty difficulty) {
        MapFull fullMap = MapUtil.createAndFillMap(map);
        startNewGame(mapName, fullMap, players, difficulty);
    }
    
    public void startNewGame (String mapName, MapFull fullMap, List<Triple<PlayerType, String, Boolean>> players, Difficulty difficulty) {
        Pair<JavaGame, Action> pair = JavaGame.newGame(mapName, fullMap, difficulty, players);
        this.javaGame = pair.getLeft();
        this.currentAction = pair.getRight();

        this.inGameViewAUI.refreshSome();
        //6 MapTiles zu beginn fluten
        CardStack<FloodCard> floodCardCardStack = this.getCurrentAction().getFloodCardStack();
        for(int i = 0; i < 6; i++) {
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
        
    }

    /**
     * loadGame lädt ein gespeichertes JavaGame aus einer Datei
     *
     * @param loadGameName ist der Name der zu ladenden Spieldatei
     */

    public void loadSaveGame(String loadGameName) {
        try (FileInputStream fileInputStream = new FileInputStream(SAVE_GAME_FOLDER + loadGameName + ".save");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            this.javaGame = (JavaGame) objectInputStream.readObject();

            // Falls das Spiel nicht bis zum letzten Zug vorgespult wurde, spule es vor.
            while (this.javaGame.canRedo())
                this.javaGame.redoAction();

            // Setze die momentane Aktion auf die nächste des geladenen Spiels
            this.currentAction = this.javaGame.getPreviousAction().copy();

            // Spiel wurde erfolgreich geladen, der Name des momentanen Spiels kann gesetzt werden und
            // das GUI kann informiert werden
            this.gameName = loadGameName;
            this.inGameViewAUI.refreshSome();
        } catch (FileNotFoundException e) {
            System.out.println("Es gab keine solche Datei.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Beim Import ist ein Fehler aufgetreten!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Die Klasse wurde nicht gefunden!");
            e.printStackTrace();
        }
    }

    public void loadReplay(String replayName){
        while(this.javaGame.canUndo()) {
            this.javaGame.undoAction();
        }
        this.gameName = replayName;
    }

    /**
     * saveGame speichert das aktuell ausgeführte JavaGame in einer Datei und gibt ihm einen Namen
     *
     * @param gameName ist der Name des Spiels, für das eine Datei angelegt werden soll
     */

    public void saveGame(String gameName) {
        // FIXME: Funktioniert das wirklich so? Der Name müsste ja erst gesetzt worden sein. Vielleicht lieber auf null
        // überprüfen?
        if (gameName.isEmpty()) {
            this.gameName = "current";
        } else {
            this.gameName = gameName;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.getGameName() + ".save");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(javaGame);

            //Wenn das Spiel beendet wurde, speichere es auch in den HighScores
            // TODO: Sollte das Spiel wirklich hier gespeichert werden und nicht, wenn es beendet wird? Außerdem gibt es
            // verschiedene Ordner für replays und für gespeicherte Spiele. Wenn es beendet wurde sollte es nur bei den
            // replays, nicht bei den Spielen, die man weiterspielen kann landen.
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
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Beim Export ist ein Fehler aufgetreten.");
            e.printStackTrace();
        }
    }

    /**
     * replayGame spielt ein beendetes Spiel ab, welches vorher geladen wurde
     */
    public void replayGame(String replayGameName) {
        inGameViewAUI.setIsReplayWindow(true);
        saveGame("");
        // TODO: Bei der Erstellung des Strukturmodelles wurde noch keine Unterscheidung zwischen Replays und
        // weiterspielbaren Spielen gemacht. Deshalb muss die loadGame/saveGame-Methode noch angepasst werden.
        loadSaveGame(replayGameName);
        inGameViewAUI.refreshSome();
    }

    /**
     * continueGame setzt ein gespeichertes Spiel nach dem zuletzt ausgeführten Spielzug (vor dem Speichern) fort
     */
    public void continueGame() {
        //Das Spiel mit Namen mapName + ".save" im Data-Ordner laden und alles refreshen
        try (FileInputStream fileInputStream = new FileInputStream("data/current.save");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            this.javaGame = (JavaGame) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Es gab keine solche Datei.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Beim Import ist ein Fehler aufgetreten!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Die Klasse wurde nicht gefunden!");
            e.printStackTrace();
        }

        // FIXME: Datei muss wahrscheinlich nicht gelöscht werden, da sie überschrieben werden kann und der letzte Save
        // gerne behalten werden kann, falls das Spiel abstürzt etc.
        //lösche geladenes Spiel aus Speicher
        boolean delete = new File(gameName).delete();
        if (!delete) {
            System.out.println("Die Datei wurde nicht gelöscht!");
        }
        //schneide ".save" ab
        // FIXME: gameName wurde gar nicht gesetzt, nach Länge abschneiden ist nicht sicher. Lieber mittels Pattern oder gleich umgehen
        this.gameName = this.gameName.substring(0, this.gameName.length() - 5);
    }

    public Action finishAction() {
        this.currentAction = this.javaGame.finishAction(this.currentAction);
        return this.currentAction;
    }
    void setAction(Action action){
        this.currentAction = action;
    }

    public Action getCurrentAction() {
        return this.currentAction;
    }

    public String getGameName() {
        return gameName;
    }
}
