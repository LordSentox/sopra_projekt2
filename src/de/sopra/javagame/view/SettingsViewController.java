package de.sopra.javagame.view;

import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * GUI für die Spieleinstellungen
 *
 * @author Lisa, Hannah
 */
public class SettingsViewController extends AbstractViewController {
    
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
        changeState(ViewState.MENU);
    }

    @Override
    ViewState getType() {
        return ViewState.SETTINGS;
    }

    @Override
    void reset() {

    }

    @Override
    void show(Stage stage) {

    }
}
