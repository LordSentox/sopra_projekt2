package de.sopra.javagame.control;


import de.sopra.javagame.view.HighScoresViewAUI;

public class HighScoresController {

    private final ControllerChan controllerChan;

    private HighScoresViewAUI highScoresViewAUI;

    public HighScoresController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    public void setHighScoresViewAUI(HighScoresViewAUI highScoresViewAUI) {
        this.highScoresViewAUI = highScoresViewAUI;
    }

    public void loadHighScores(String mapName) {

    }

    public void resetHighScores(String mapName) {

    }

}
