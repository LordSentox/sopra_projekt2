package de.sopra.javagame.view;

import javafx.stage.Stage;

/**
 * bietet Schnittstellenmethoden die alle ViewController benötigen
 *
 * @author Lisa, Hannah
 */
public abstract class AbstractViewController {

    private GameWindow gameWindow;

    void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    GameWindow getGameWindow() {
        return gameWindow;
    }

    /**
     * definiert den Typen des aktuellen Fensters über {@link ViewState}
     *
     * @return Typ des Fensters
     */
    abstract ViewState getType();

    /**
     * neu initailisieren des aktuellen Controllers
     */
    abstract void reset();

    /**
     * zeigen des aktuellen controllers als neue scene auf der gegebenen stage
     *
     * @param stage Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    abstract void show(Stage stage);

    /**
     * Wechsel zum übergebenen ViewState
     *
     * @param next Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    void changeState(ViewState next) {

    }

}
