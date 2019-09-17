package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static de.sopra.javagame.view.abstraction.ViewState.*;

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
        changeState(ViewState.MENU, SETTINGS);
    }

    public void onStartGameClicked() {
        changeState(ViewState.MENU, ViewState.GAME_PREPARATIONS);
    }

    public void onLoadGameClicked() {
        
    }

    public void onMapEditorClicked() {
        changeState(ViewState.MENU, MAP_EDITOR);
    }

    public void onHighscoresClicked() {
        changeState(ViewState.MENU, HIGH_SCORES);
    }

    public void onCloseClicked() {
        changeState(ViewState.MENU, ViewState.CLOSE);
    }

    public void onLoadReplayClicked() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }
}
