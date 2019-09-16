package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;

import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * GUI für den Mapeditor
 *
 * @author Lisa, Hannah
 */
public class MapEditorViewController extends AbstractViewController implements MapEditorViewAUI {
    
    @FXML JFXButton saveButton, loadMapButton, generateButton, closeButton;

    public void onSaveClicked() {

    }

    public void onLoadMapClicked() {

    }

    public void onGenerateClicked() {

    }

    public void onCloseClicked() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }

    @Override
    public void showNotification(String notification) {

    }

    @Override
    public void setMap(String mapName, MapBlackWhite map) {

    }
}
