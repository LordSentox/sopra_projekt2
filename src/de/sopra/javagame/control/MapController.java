package de.sopra.javagame.control;

import de.sopra.javagame.view.MapEditorViewAUI;

public class MapController {

    private final ControllerChan controllerChan;

    private MapEditorViewAUI mapEditorViewAUI;

    public MapController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    public void setMapEditorViewAUI(MapEditorViewAUI mapEditorViewAUI) {
        this.mapEditorViewAUI = mapEditorViewAUI;
    }

    public void generateMapToEditor() {

    }

    public void loadMapToEditor(String name) {

    }

    public void saveMap(String name, boolean[][] tiles) {

    }

}
