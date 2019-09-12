package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static de.sopra.javagame.view.ViewState.SETTINGS;

/**
 * GUI für das Hauptfenster
 *
 * @author Lisa, Hannah
 */
public class MainMenuViewController extends AbstractViewController {
    @FXML
    ImageView mainPane;
    @FXML
    JFXButton settingsButton, closeButton, startGameButton, loadGameButton, mapEditorButton, highscoreButton;

    public void init() {
        mainPane.setImage(TextureLoader.getBackground());
    }

    public void onSettingsClicked() {
        changeState(SETTINGS);
    }

    public void onStartGameClicked() {
        changeState(ViewState.IN_GAME);
    }

    public void onLoadGameClicked() {

    }

    public void onMapEditorClicked() {

    }

    public void onHighscoresClicked() {

    }

    public void onCloseClicked() {
        changeState(ViewState.CLOSE);
    }

    public void onLoadReplayClicked() {

    }

    @Override
    ViewState getType() {
        return ViewState.MENU;
    }

    @Override
    void reset() {

    }

    @Override
    void show(Stage stage) {

    }
}
