package de.sopra.javagame.view.abstraction;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.util.GameSettings;
import de.sopra.javagame.view.*;
import de.sopra.javagame.view.command.Commands;
import de.spaceparrots.api.command.interfaces.CommandResult;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
public class GameWindow {

    private ControllerChan controllerChan;

    private Stage mainStage;

    private GameSettings settings;

    private Map<ViewState, AbstractViewController> views;

    private ViewState currentViewState;

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
        initHighScore();
        initInGame();
        initMapEditor();
        initInGameSettings();
        initSettings();

        //DO NOT REMOVE THIS - Julius
        initStageStuff();

        mainStage.setResizable(false);
        mainStage.initStyle(StageStyle.UNDECORATED);
        this.setState(ViewState.MENU);
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
                dialog.initStyle(StageStyle.UTILITY);
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    CommandResult commandResult = Commands.processCommand(this, result.get());
                    if (!commandResult.wasSuccessful())
                        System.out.println(commandResult.getResultMessage());
                    else System.out.println("result: " + commandResult.get());
                }
            }
        });
    }


    /**
     * Wechselt die aktuelle {@link ViewState} zur Übergebenen
     *
     * @param state Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    void setState(ViewState state) {
        if (currentViewState == state)
            return;

        if (state == ViewState.CLOSE) {
            mainStage.close();
            return;
        }

        mainStage.setScene(views.get(state).getScene());
        mainStage.setFullScreen(state.isFullscreen());
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Spieler soll den Fullscreen nicht beenden können
        mainStage.centerOnScreen();
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
}
