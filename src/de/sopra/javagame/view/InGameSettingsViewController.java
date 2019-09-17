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

public class InGameSettingsViewController extends AbstractViewController {


    public JFXSlider effectVolumeSlider;
    public JFXSlider musicVolumeSlider;
    public JFXCheckBox developerToolsCheckbox;
    @FXML
    ImageView mainPane;

    public void init() {
        effectVolumeSlider.getStylesheets().add(getClass().getResource("/stylesheets/sliders.css").toExternalForm());
        musicVolumeSlider.getStylesheets().add(getClass().getResource("/stylesheets/sliders.css").toExternalForm());

        mainPane.setImage(TextureLoader.getBackground());
        GameSettings settings = getGameWindow().getSettings();

        effectVolumeSlider.valueProperty().set(settings.getEffectsVolume().intValue());
        settings.getEffectsVolume().unbind();
        settings.getEffectsVolume().bind(effectVolumeSlider.valueProperty());

        musicVolumeSlider.valueProperty().set(settings.getMusicVolume().intValue());
        settings.getMusicVolume().unbind();
        settings.getMusicVolume().bind(musicVolumeSlider.valueProperty());

        developerToolsCheckbox.selectedProperty().set(settings.devToolsEnabled().get());
        settings.devToolsEnabled().unbind();
        settings.devToolsEnabled().bind(developerToolsCheckbox.selectedProperty());
    }


    public void onCloseClicked() {
        changeState(ViewState.IN_GAME);
    }

    public void onSaveClicked() {
        getGameWindow().getSettings().save();
    }

    public void onBackToMenuClicked()
    {
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
