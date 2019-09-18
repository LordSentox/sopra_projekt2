package de.sopra.javagame.view;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import de.sopra.javagame.util.GameSettings;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.GameWindow;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

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

    private Stage modalCopy;

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

    public static void openModal(GameWindow window) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SettingsViewController.class.getResource("/Settings.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        SettingsViewController settingsViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(SettingsViewController.class.getResource("/application.css").toExternalForm());
        settingsViewController.setGameWindow(window);
        settingsViewController.setScene(mainMenuScene);
        settingsViewController.init();
        Stage stage = new Stage();
        stage.setScene(mainMenuScene);
        stage.initOwner(window.getMainStage());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setAlwaysOnTop(true);
        stage.toFront();
        settingsViewController.modalCopy = stage;
        stage.show();
    }

    public void onCloseClicked() {
        if (modalCopy == null) {
            getGameWindow().getSettings().save();
            ((MainMenuViewController)getGameWindow().getView(ViewState.MENU)).init();
            changeState(ViewState.SETTINGS, ViewState.MENU);
        } else modalCopy.close();
    }

}
