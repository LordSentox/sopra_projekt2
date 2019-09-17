package de.sopra.javagame.view;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import de.sopra.javagame.util.GameSettings;
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
    public JFXSlider effectVolumeSlider;
    @FXML
    public JFXSlider musicVolumeSlider;
    @FXML
    public JFXCheckBox developerToolsCheckbox;
    @FXML
    ImageView mainPane;


    public void init() {
        effectVolumeSlider.getStylesheets().add(getClass().getResource("/stylesheets/sliders.css").toExternalForm());
        musicVolumeSlider.getStylesheets().add(getClass().getResource("/stylesheets/sliders.css").toExternalForm());

        mainPane.setImage(TextureLoader.getBackground());
        GameSettings settings = getGameWindow().getSettings();

        effectVolumeSlider.valueProperty().set(settings.getEffectsVolume().intValue());
        settings.getEffectsVolume().bind(effectVolumeSlider.valueProperty());

        musicVolumeSlider.valueProperty().set(settings.getMusicVolume().intValue());
        settings.getMusicVolume().bind(musicVolumeSlider.valueProperty());

        developerToolsCheckbox.selectedProperty().set(settings.devToolsEnabled().get());
        settings.devToolsEnabled().bind(developerToolsCheckbox.selectedProperty());
    }

    public void onCloseClicked() {
        getGameWindow().getSettings().save();
        changeState(ViewState.MENU);
    }

    @Override
    public void reset() {
        getGameWindow().resetSettings();
        init();
    }

    @Override
    public void show(Stage stage) {

    }
}
