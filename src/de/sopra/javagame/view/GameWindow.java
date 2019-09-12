package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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


    private Map<ViewState, AbstractViewController> views;

    private ViewState currentViewState;

    public GameWindow(Stage stage) {
        this.controllerChan = new ControllerChan();
        this.views = new HashMap<>();
        this.mainStage = stage;
    }

    public void init() throws IOException {
        initMainMenu();
//        initGamePreparations();
//        initHighScore();
        initInGame();
//        initMapEditor();
        initSettings();
        
        mainStage.setResizable(false);
        mainStage.initStyle(StageStyle.UNDECORATED);
        this.setState(ViewState.MENU);
        mainStage.show();
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
    //TODO
    private void initHighScore() throws IOException {
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
    }
    //TODO
    private void initMapEditor() throws IOException {
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
    //TODO
    private void initGamePreparations() throws IOException {
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
 
        
        
        
        
        
        
        
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GameWindow.fxml"));
//        AnchorPane mainPane = fxmlLoader.load();
//        InGameViewController inGameViewController = fxmlLoader.getController();
//
//
//        Scene mainScene = new Scene(mainPane);
//        mainScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
//        fullScreenStage.setScene(mainScene);
//        fullScreenStage.setResizable(false);
//        fullScreenStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//        fullScreenStage.setFullScreen(true);
//        fullScreenStage.show();
//
//        fullScreenStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            if (event.isAltDown() && event.getCode() == KeyCode.C) {
//                TextInputDialog dialog = new TextInputDialog("");
//                dialog.setContentText("Command:");
//                dialog.setTitle(null);
//                dialog.setHeaderText(null);
//                dialog.initModality(Modality.WINDOW_MODAL);
//                dialog.initOwner(fullScreenStage);
//                dialog.initStyle(StageStyle.UTILITY);
//                Optional<String> result = dialog.showAndWait();
//                System.out.println(result.get());
//                //TODO replace sout with Commands call
//            }
//        });
//
//        inGameViewController.init();

    /**
     * Wechselt die aktuelle {@link ViewState} zur Übergebenen
     *
     * @param state Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    public void setState(ViewState state) {
        if (currentViewState == state)
            return;
        
        if(state == ViewState.CLOSE){
            mainStage.close();
            return;
        }
        
        mainStage.setScene(views.get(state).getScene());
        mainStage.setFullScreen(state.isFullscreen());
    }

    public ControllerChan getControllerChan() {
        return this.controllerChan;
    }
}
