package de.sopra.javagame.view.abstraction;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.util.DebugUtil;
import de.sopra.javagame.util.GameSettings;
import de.sopra.javagame.view.*;
import de.sopra.javagame.view.command.Commands;
import de.spaceparrots.api.command.interfaces.CommandResult;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Schnittstelle für alle Views und die Controller Schicht
 *
 * @author Hannah, Lisa
 */
public class GameWindow implements NotificationAUI {

    private ControllerChan controllerChan;

    private Stage mainStage;

    private GameSettings settings;

    private Map<ViewState, AbstractViewController> views;

    private ViewState previousViewState;
    private ViewState currentViewState;
    private boolean developerSettings;

    public GameWindow(Stage stage) {
        this.settings = GameSettings.load();
        this.controllerChan = new ControllerChan();
        this.views = new EnumMap<>(ViewState.class);
        this.mainStage = stage;
        stage.centerOnScreen();
    }

    public void init() throws IOException {
        initMainMenu();
        initGamePreparations();
        initLoadGame();
        initSaveGame();
        initHighScore();
        initInGame();
        initMapEditor();
        initInGameSettings();
        initSettings();

        //DO NOT REMOVE THIS - Julius
        initStageStuff();

        mainStage.setResizable(false);
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setAlwaysOnTop(false);
        this.setState(ViewState.CLOSE, ViewState.MENU);
        mainStage.show();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void initMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        MainMenuViewController mainMenuViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        mainMenuViewController.setGameWindow(this);
        mainMenuViewController.setScene(mainMenuScene);
        mainMenuViewController.init();
        views.put(ViewState.MENU, mainMenuViewController);
    }

    private void initGamePreparations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GamePreparations.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        GamePreparationsViewController gamePreparationsViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        gamePreparationsViewController.setGameWindow(this);
        gamePreparationsViewController.setScene(mainMenuScene);
        gamePreparationsViewController.init();
        views.put(ViewState.GAME_PREPARATIONS, gamePreparationsViewController);
    }

    //TODO
    private void initHighScore() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Highscores.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        HighScoresViewController highScoresViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        highScoresViewController.setGameWindow(this);
        highScoresViewController.setScene(mainMenuScene);
        highScoresViewController.init();
        views.put(ViewState.HIGH_SCORES, highScoresViewController);
        controllerChan.setHighScoresViewAUI(highScoresViewController);
    }

    private void initLoadGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoadGame.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        LoadGameViewController loadGameViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        loadGameViewController.setGameWindow(this);
        loadGameViewController.setScene(mainMenuScene);
        loadGameViewController.init();
        views.put(ViewState.LOAD_GAME, loadGameViewController);
    }

    private void initSaveGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SaveGame.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        SaveGameViewController saveGameViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        saveGameViewController.setGameWindow(this);
        saveGameViewController.setScene(mainMenuScene);
        saveGameViewController.init();
        views.put(ViewState.SAVE_GAME, saveGameViewController);
    }

    private void initInGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GameWindow.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        InGameViewController inGameViewController = fxmlLoader.getController();
        Scene inGameScene = new Scene(mainPane);
        inGameScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        inGameViewController.setGameWindow(this);
        inGameViewController.setScene(inGameScene);
        inGameViewController.init();
        views.put(ViewState.IN_GAME, inGameViewController);
        controllerChan.setInGameViewAUI(inGameViewController);
    }


    private void initMapEditor() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MapEditor.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        MapEditorViewController mapEditorViewController = fxmlLoader.getController();
        Scene mapEditorScene = new Scene(mainPane);
        mapEditorScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        mapEditorViewController.setGameWindow(this);
        mapEditorViewController.setScene(mapEditorScene);
        mapEditorViewController.init();
        views.put(ViewState.MAP_EDITOR, mapEditorViewController);
        controllerChan.setMapEditorViewAUI(mapEditorViewController);
    }

    private void initSettings() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Settings.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        SettingsViewController settingsViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        settingsViewController.setGameWindow(this);
        settingsViewController.setScene(mainMenuScene);
        settingsViewController.init();
        views.put(ViewState.SETTINGS, settingsViewController);
    }

    private void initInGameSettings() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InGameSettings.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        InGameSettingsViewController inGameSettingsViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        inGameSettingsViewController.setGameWindow(this);
        inGameSettingsViewController.setScene(mainMenuScene);
        inGameSettingsViewController.init();
        views.put(ViewState.IN_GAME_SETTINGS, inGameSettingsViewController);
    }

    //DO NOT REMOVE THIS - Julius
    private void initStageStuff() {
        mainStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.getCode() == KeyCode.C) {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setContentText("Command:");
                dialog.setTitle(null);
                dialog.setHeaderText(null);
                dialog.initModality(Modality.WINDOW_MODAL);
                dialog.initOwner(mainStage);
                dialog.initStyle(StageStyle.UNDECORATED);
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    CommandResult commandResult = Commands.processCommand(this, result.get());
                    if (!commandResult.wasSuccessful()) {
                        DebugUtil.debug(commandResult.getResultMessage());
                        if (commandResult.get() instanceof Exception)
                            ((Exception) commandResult.get()).printStackTrace();
                        else System.err.println("error result: " + commandResult.get());
                    } else DebugUtil.debug("result: " + commandResult.get());
                }
            }
        });
    }

    public AbstractViewController getView(ViewState state) {
        return views.get(state);
    }

    /**
     * Wechselt die aktuelle {@link ViewState} zur Übergebenen
     *
     * @param state Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    void setState(ViewState previous, ViewState state) {
        if (currentViewState == state || previous == state)
            return;

        if (state == ViewState.CLOSE) {
            mainStage.close();
            return;
        }

        mainStage.setScene(views.get(state).getScene());
        mainStage.setFullScreen(state.isFullscreen());
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Spieler soll den Fullscreen nicht beenden können
        mainStage.centerOnScreen();

        this.previousViewState = previous;
        this.currentViewState = state;
    }

    public ControllerChan getControllerChan() {
        return this.controllerChan;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void resetSettings() {
        settings = new GameSettings();
    }

    public ViewState getPreviousViewState() {
        return previousViewState;
    }

    @Override
    public void showNotification(Notification notification) {
        if (notification.isError()) {
            DialogPack pack = new DialogPack(this.getMainStage(), "", "Es ist ein Fehler aufgetreten: ", notification.message());
            pack.setAlertType(Alert.AlertType.ERROR);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
        } else if (notification.hasMessage()) {
            DialogPack pack = new DialogPack(this.getMainStage(), "", "Das Spiel informiert:", notification.message());
            pack.setAlertType(Alert.AlertType.INFORMATION);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
            DebugUtil.debug("info notification: " + notification.message());
        }
    }

    public void setDeveloperSettingsActive(boolean setActive) {
        //this.settings.setDevTools(setActive);
        this.developerSettings = setActive;
    }
    
    public boolean getDeveloperSettings() {
        return this.developerSettings;
    }
}
