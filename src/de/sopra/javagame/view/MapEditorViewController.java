package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.sopra.javagame.control.HighScoresController;
import de.sopra.javagame.control.MapController;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.MapCheckUtil;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.Notification;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.EditorMapPane;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
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
    @FXML
    Label showUsedTiles, labelShowMessages;

    private String chosenMapName;

    public void init() {
        /* Set Background */
        mainPane.setImage(TextureLoader.getBackground());
        mainPane.setFitHeight(1200);

        showUsedTiles.setFont(new Font(60));
        showUsedTiles.setTextFill(EditorMapPane.WHITE);

        labelShowMessages.setFont(new Font(30));
        labelShowMessages.setTextFill(EditorMapPane.WHITE);
        labelShowMessages.setFont(new Font(20));

        showUsedTiles.setTextAlignment(TextAlignment.RIGHT);

        editorMapPane.setMapEditorViewController(this);
        editorMapPane.setTileCountLabel(showUsedTiles);
        chosenMapName = "";
        comboBoxChooseGivenMap.valueProperty().addListener((options, oldValue, newValue) -> chosenMapName = newValue);

        fillComboBox();

        textFieldCreatedMapName.textProperty().addListener((options, oldValue, newValue) -> {
            boolean isValid = MapCheckUtil.checkMapValidity(editorMapPane.getBooleanMap());
            setSaveButtonDisabled(!isValid);
            if (textFieldCreatedMapName.getText().isEmpty()) {
                showNotification("Bitte gib deiner Karte \neinen Namen");
            } else {
                showNotification("");
            }

        });

        setSaveButtonDisabled(true);
    }

    @Override
    public void showNotification(Notification notification) {
        super.showNotification(notification);
    }

    private void fillComboBox() {
        File mapFile = new File(MapController.MAP_FOLDER);
        File[] files = mapFile.listFiles();
        List<String> mapNames = Arrays.stream(files).map(File::getName).collect(Collectors.toList());

        for (String currentName : mapNames) {
            comboBoxChooseGivenMap.getItems().addAll(currentName.substring(0, currentName.length() - 4));
            comboBoxChooseGivenMap.getItems().sort(null);
        }
    }

    public void onSaveClicked() throws IOException {
        boolean isValid = MapCheckUtil.checkMapValidity(editorMapPane.getBooleanMap());
        if (isValid && !textFieldCreatedMapName.getText().isEmpty()) {
            getGameWindow().getControllerChan().getMapController().saveMap(textFieldCreatedMapName.getText(), editorMapPane.getBooleanMap());
            new File(HighScoresController.SCORE_FOLDER + textFieldCreatedMapName.getText() + ".score").createNewFile();
            fillComboBox();
            comboBoxChooseGivenMap.getSelectionModel().clearSelection();
            showNotification("Die Karte " + textFieldCreatedMapName.getText() + "\nwurde erfolgreich gespeichert!");
        } else {
            if (textFieldCreatedMapName.getText().isEmpty()) {
                showNotification("Bitte gib deiner Karte \neinen Namen!");
            } else {
                showNotification("Die Karte muss aus 24 zusammenhängenden Feldern bestehen!");
            }
        }
    }

    public void onLoadMapClicked() {
        getGameWindow().getControllerChan().getMapController().loadMapToEditor(chosenMapName);
        showNotification("Bitte gib deiner Karte \neinen Namen!");
    }

    public void onGenerateClicked() {
        getGameWindow().getControllerChan().getMapController().generateMapToEditor();
        comboBoxChooseGivenMap.getSelectionModel().clearSelection();
        showNotification("Bitte gib deiner Karte \neinen Namen!");
    }

    public void onCloseClicked() {
        changeState(ViewState.MAP_EDITOR, getGameWindow().getPreviousViewState());
    }

    @Override
    public void showNotification(String notification) {
        labelShowMessages.setText(notification);
    }

    @Override
    public void setMap(String mapName, MapBlackWhite map) {
        editorMapPane.buildMap(map);
        textFieldCreatedMapName.clear();
    }

    public void setSaveButtonDisabled(boolean disable) {
        saveButton.setDisable(disable || textFieldCreatedMapName.getText().isEmpty());
    }
}
