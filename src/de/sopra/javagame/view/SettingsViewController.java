package de.sopra.javagame.view;

import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
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

    @FXML
    ImageView mainPane;


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
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }
}
