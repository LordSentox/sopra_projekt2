package de.sopra.javagame.view;

import javafx.stage.Stage;

/**
 * GUI für den Mapeditor
 * @author Lisa, Hannah
 *
 */
public class MapEditorViewController extends AbstractViewController implements MapEditorViewAUI {

    public void onSaveClicked() {

    }

    public void onLoadMapClicked() {

    }

    public void onGenerateClicked() {

    }

    public void onCloseClicked() {

    }


    @Override
    ViewState getType() {
        return ViewState.MAP_EDITOR;
    }

    @Override
    void reset() {

    }

    @Override
    void show(Stage stage) {

    }

    @Override
    public void showNotification(String notification) {

    }

    @Override
    public void setMap(boolean[][] tiles) {

    }
}
