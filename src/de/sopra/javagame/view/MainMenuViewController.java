package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;

import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * GUI für das Hauptfenster
 *
 * @author Lisa, Hannah
 */
public class MainMenuViewController extends AbstractViewController {
    @FXML ImageView mainPane;
    @FXML JFXButton settingsButton, closeButton, startGameButton, loadGameButton, mapEditorButton, highscoreButton;
    public void init() {
        mainPane.setImage(TextureLoader.getBackground());
        System.out.println("bin im menuC");
    }
    public void onSettingsClicked() {
        
    }

    public void onStartGameClicked() {
        this.getGameWindow().setState(ViewState.IN_GAME);
    }

    public void onLoadGameClicked() {

    }

    public void onMapEditorClicked() {

    }

    public void onHighscoresClicked() {

    }

    public void onCloseClicked() {
     this.getGameWindow().setState(ViewState.CLOSE);   
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
