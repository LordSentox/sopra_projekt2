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

public class InGameSettingsViewController extends AbstractViewController {


    public JFXSlider effectVolumeSlider;
    public JFXSlider musicVolumeSlider;
    public JFXCheckBox developerToolsCheckbox;
    @FXML
    ImageView mainPane;
    private Stage modalCopy;

    @SuppressWarnings("Duplicates")
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

    @SuppressWarnings("Duplicates")
    public static void openModal(GameWindow window) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SettingsViewController.class.getResource("/InGameSettings.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        InGameSettingsViewController iInGameSettingsViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(SettingsViewController.class.getResource("/application.css").toExternalForm());
        iInGameSettingsViewController.setGameWindow(window);
        iInGameSettingsViewController.setScene(mainMenuScene);
        iInGameSettingsViewController.init();
        Stage stage = new Stage();
        stage.setScene(mainMenuScene);
        stage.initOwner(window.getMainStage());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        iInGameSettingsViewController.modalCopy = stage;
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.toFront();
        stage.requestFocus();
    }


    public void onCloseClicked() {
//        if(modalCopy == null) {
//            getGameWindow().getSettings().save();
//            changeState(ViewState.IN_GAME_SETTINGS, ViewState.MENU);
//        }
//        else 
            modalCopy.close();
    }

    public void onSaveClicked() throws IOException {
        //eigentlich soll hier das Spiel gespeichert werden und nicht die Einstellungen
        getGameWindow().getSettings().save();
        modalCopy.close();
        SaveGameViewController.openModal(getGameWindow());
//        changeState(ViewState.IN_GAME_SETTINGS, ViewState.SAVE_GAME);
    }

    public void onBackToMenuClicked() {
        getGameWindow().getSettings().save();
        modalCopy.close();
        changeState(ViewState.IN_GAME_SETTINGS, ViewState.MENU);
    }
}
