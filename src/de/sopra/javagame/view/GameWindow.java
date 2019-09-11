package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Schnittstelle für alle Views und die Controller Schicht
 *
 * @author Hannah, Lisa
 */
public class GameWindow {

    private ControllerChan controllerChan;

    private Stage fullScreenStage;
    

    private List<AbstractViewController> views;

    private int currentView;

    public GameWindow(Stage stage) {
        this.controllerChan = new ControllerChan();
        this.views = new LinkedList<>();
        currentView = 0;
        this.fullScreenStage = stage;
    }

    public void init() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GameWindow.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        InGameViewController inGameViewController = fxmlLoader.getController();


        Scene mainScene = new Scene(mainPane);
        mainScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        fullScreenStage.setScene(mainScene);
        fullScreenStage.setResizable(false);
        fullScreenStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        fullScreenStage.setFullScreen(true);
        fullScreenStage.show();
        
        inGameViewController.init();

    }

    /**
     * Wechselt die aktuelle {@link ViewState} zur Übergebenen
     *
     * @param state Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    public void setState(ViewState state) {
        //bei gleicher state wird nicht gewechselt
        if (views.get(currentView).getType() == state)
            return;
        //state zu controller zuordnen und wählen
        for (int i = 0; i < views.size(); i++) {
            if (views.get(i).getType() == state) {
                currentView = i;
                views.get(i).show(fullScreenStage);
                return;
            }
        }
    }

    public ControllerChan getControllerChan() {
        return this.controllerChan;
    }
}
