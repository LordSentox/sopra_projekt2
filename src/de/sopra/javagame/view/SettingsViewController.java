package de.sopra.javagame.view;

import javafx.stage.Stage;

/**
 * GUI für die Spieleinstellungen
 *
 * @author Lisa, Hannah
 */
public class SettingsViewController extends AbstractViewController {
    public void init() {
        
    }

    public void onEffectVolumeChanged() {

    }

    public void onMusicVolumeChanged() {

    }

    public void onDeveloperToolsChecked() {

    }

    public void onCloseClicked() {
        this.getGameWindow().setState(ViewState.MENU);
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
