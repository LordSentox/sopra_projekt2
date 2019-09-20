package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.sopra.javagame.util.DebugUtil;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.GameWindow;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.EditorMapPane;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SaveGameViewController extends AbstractViewController {
    @FXML
    ImageView mainPane;
    @FXML
    JFXListView<String> loadMapListView;
    @FXML
    Label loadMapViewLabel, notificationLabel;
    @FXML
    JFXTextField saveGameTextField;
    @FXML
    JFXButton saveGameButton, backButton;

    private Stage modalCopy;
    private File[] loadFiles;
    private final int NO_SAVE_FILES = 0;

    public void init() throws IOException {
        mainPane.setImage(TextureLoader.getBackground());

        fillListView();
        loadMapViewLabel.setVisible(loadFiles.length == NO_SAVE_FILES);
        notificationLabel.setTextFill(EditorMapPane.RED);
    }


    private void fillListView() {
        loadMapListView.getItems().clear();
        File loadFile = new File(getGameWindow().getControllerChan().SAVE_GAME_FOLDER);
        loadFiles = loadFile.listFiles(file -> !file.getName().startsWith(".")
                && file.getName().endsWith(".save") && file.getName().length() > 5);
        List<String> loadNames = Arrays.stream(loadFiles).map(File::getName).collect(Collectors.toList());

        for (String currentName : loadNames) {
            loadMapListView.getItems().addAll(currentName.substring(0, currentName.length() - 5));
            loadMapListView.getItems().sort(Comparator.naturalOrder());
            DebugUtil.debug("currentName: " + currentName);
        }
    }


    public void onCloseClicked() {
//        changeState(ViewState.LOAD_GAME, ViewState.IN_GAME_SETTINGS);
        if (getGameWindow().getPreviousViewState() == ViewState.SETTINGS) {
        modalCopy.close();
        } else if (getGameWindow().getPreviousViewState() == ViewState.IN_GAME) {
            changeState(ViewState.SAVE_GAME, ViewState.MENU);
        }

    }

    public void onSaveGameClicked() {
        String selectedGame = saveGameTextField.getText();
        if (selectedGame.isEmpty()) {
            showNotificatoin("Das Feld ist nicht ausgefüllt");
            return;
        }
        getGameWindow().getControllerChan().saveGame(selectedGame);
        loadMapViewLabel.setVisible(loadFiles.length == NO_SAVE_FILES);
        fillListView();
        onCloseClicked();
    }

    public static void openModal(GameWindow window) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SettingsViewController.class.getResource("/SaveGame.fxml"));
        AnchorPane mainPane = fxmlLoader.load();
        SaveGameViewController saveGameViewController = fxmlLoader.getController();
        Scene mainMenuScene = new Scene(mainPane);
        mainMenuScene.getStylesheets().add(SettingsViewController.class.getClass().getResource("/application.css").toExternalForm());
        saveGameViewController.setGameWindow(window);
        saveGameViewController.setScene(mainMenuScene);
        saveGameViewController.init();
        Stage stage = new Stage();
        stage.setScene(mainMenuScene);
        stage.initOwner(window.getMainStage());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        saveGameViewController.modalCopy = stage;
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.toFront();
        stage.requestFocus();
    }

    public void showNotificatoin(String notification) {
        notificationLabel.setText(notification);
    }


}
