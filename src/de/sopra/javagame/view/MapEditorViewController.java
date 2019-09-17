package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.MapCheckUtil;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.EditorMapPane;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI für den Mapeditor
 *
 * @author Lisa, Hannah
 */
public class MapEditorViewController extends AbstractViewController implements MapEditorViewAUI {
    
    @FXML
    JFXButton saveButton, loadMapButton, generateButton, closeButton;
    @FXML
    EditorMapPane editorMapPane;
    @FXML
    JFXComboBox<String> comboBoxChooseGivenMap;
    @FXML
    JFXTextField textFieldCreatedMapName;
    @FXML
    ImageView mainPane;

    private String chosenMapName;

    public void init() {
        /* Set Background */
        mainPane.setImage(TextureLoader.getBackground());
        mainPane.setFitHeight(1200);

        editorMapPane.setMapEditorViewController(this);
        chosenMapName = "";
        comboBoxChooseGivenMap.valueProperty().addListener((options , oldValue, newValue) -> {
            chosenMapName = newValue;
        });

        File mapFile = new File ("resources/maps");
        File[] files = mapFile.listFiles();
        List<String> mapNames = Arrays.stream(files).map(File::getName).collect(Collectors.toList());

        for(String currentName : mapNames) {
            comboBoxChooseGivenMap.getItems().addAll(currentName.substring(0, currentName.length()-4));
        }

        textFieldCreatedMapName.textProperty().addListener((options, oldValue, newValue) ->{
            boolean isValid = MapCheckUtil.checkMapValidity(editorMapPane.getBooleanMap());
            setSaveButtonDisabled(isValid);
        });
    }

    public void onSaveClicked() {
        boolean isValid = MapCheckUtil.checkMapValidity(editorMapPane.getBooleanMap());
        if (isValid && !textFieldCreatedMapName.getText().isEmpty()) {
            getGameWindow().getControllerChan().getMapController().saveMap(textFieldCreatedMapName.getText(), editorMapPane.getBooleanMap());
        } else {
            if (textFieldCreatedMapName.getText().isEmpty()) {
                showNotification("Bitte gib der neuen Karte einen Namen!");
            } else {
                showNotification("Die Karte muss aus 24 zusammenhängenden Feldern bestehen!");
            }
        }
    }

    public void onLoadMapClicked() {
        getGameWindow().getControllerChan().getMapController().loadMapToEditor(chosenMapName);
    }

    public void onGenerateClicked() {
        getGameWindow().getControllerChan().getMapController().generateMapToEditor();
    }

    public void onCloseClicked() {
        changeState(ViewState.MENU);
    }

    @Override
    public void reset() {
        editorMapPane.buildMap(null);
        comboBoxChooseGivenMap.getSelectionModel().clearSelection();

    }

    @Override
    public void show(Stage stage) {

    }

    @Override
    public void showNotification(String notification) {

    }

    @Override
    public void setMap(String mapName, MapBlackWhite map) {
        editorMapPane.buildMap(map);
        textFieldCreatedMapName.clear();
    }

    public void setSaveButtonDisabled(boolean disable) {
        saveButton.setDisable(disable && !textFieldCreatedMapName.getText().isEmpty());
    }
}
