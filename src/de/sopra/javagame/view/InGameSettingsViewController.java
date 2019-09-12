package de.sopra.javagame.view;

import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InGameSettingsViewController extends AbstractViewController {
    
    
    @FXML ImageView mainPane;
    
    
    public void init() {
        mainPane.setImage(TextureLoader.getBackground());
    }

    public void onEffectVolumeChanged() {

    }

    public void onMusicVolumeChanged() {

    }

    public void onDeveloperToolsChecked() {

    }

    public void onCloseClicked() {
        changeState(ViewState.IN_GAME);
    }

    @Override
    ViewState getType() {
        return ViewState.IN_GAME_SETTINGS;
    }

    @Override
    void reset() {

    }

    @Override
    void show(Stage stage) {

    }
}
