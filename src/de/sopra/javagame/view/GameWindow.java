package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

/**
 * Schnittstelle für alle Views und die Controller Schicht
 *
 * @author Hannah, Lisa
 */
public class GameWindow {

    private ControllerChan controllerChan;

    private Stage fullscreenStage;

    private List<AbstractViewController> views;

    private int currentView;

    public GameWindow(Stage stage) {
        this.controllerChan = new ControllerChan();
        this.views = new LinkedList<>();
        currentView = 0;
        this.fullscreenStage = stage;
    }

    public void init() {
        //TODO init stage
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
                views.get(i).show(fullscreenStage);
                return;
            }
        }
    }

}
