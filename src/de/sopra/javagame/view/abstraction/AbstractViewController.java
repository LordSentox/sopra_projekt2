package de.sopra.javagame.view.abstraction;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * bietet Schnittstellenmethoden die alle ViewController benötigen
 *
 * @author Lisa, Hannah
 */
public abstract class AbstractViewController {

    private GameWindow gameWindow;
    private Scene scene;

    protected GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * neu initailisieren des aktuellen Controllers
     */
    public abstract void reset();

    /**
     * zeigen des aktuellen controllers als neue scene auf der gegebenen stage
     *
     * @param stage Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    public abstract void show(Stage stage);

    /**
     * Wechsel zum übergebenen ViewState
     *
     * @param next Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    protected void changeState(ViewState next) {
        gameWindow.setState(next);
    }

}
