package de.sopra.javagame.view.abstraction;

import de.sopra.javagame.util.DebugUtil;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

/**
 * bietet Schnittstellenmethoden die alle ViewController benötigen
 *
 * @author Lisa, Hannah
 */
public abstract class AbstractViewController {

    public static final String HIGHLIGHT = "highlighting";

    private GameWindow gameWindow;
    private Scene scene;

    public GameWindow getGameWindow() {
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
     * Wechsel zum übergebenen ViewState
     *
     * @param next Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    protected void changeState(ViewState previous, ViewState next) {
        gameWindow.setState(previous, next);
    }

    /**
     * gibt dem Spieler eine Mitteilung in dem dafür vorgesehenen Fenster
     *
     * @param notification Mitteilung an den Spieler
     */
    protected void showNotification(Notification notification) {
        if (notification.isError()) {
            DialogPack pack = new DialogPack(getGameWindow().getMainStage(), "", "Es ist ein Fehler aufgetreten: ", notification.message());
            pack.setAlertType(Alert.AlertType.ERROR);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
        } else if (notification.hasMessage()) {
            DialogPack pack = new DialogPack(getGameWindow().getMainStage(), "", "Das Spiel informiert:", notification.message());
            pack.setAlertType(Alert.AlertType.INFORMATION);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
            DebugUtil.debug("info notification: " + notification.message());
        }
    }

}
