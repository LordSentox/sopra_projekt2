package de.sopra.javagame.view;

import de.sopra.javagame.control.HighScoresController;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.GameWindow;
import de.sopra.javagame.view.abstraction.ViewState;
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

import com.jfoenix.controls.JFXListView;

/**
 * GUI für das anzeigen der Highscores
 *
 * @author Hannah, Lisa
 */
public class LoadGameViewController extends AbstractViewController {

    @FXML
    ImageView mainPane;
    @FXML
    JFXListView<String> loadGameListViewLabel;
    @FXML
    Label loadMapViewLabel;

    public void init() throws IOException {
        mainPane.setImage(TextureLoader.getBackground());


        File loadFile = new File(getGameWindow().getControllerChan().SAVE_GAME_FOLDER);
        File[] loadFiles = loadFile.listFiles(file -> !file.getName().startsWith(".")
                && file.getName().endsWith(".save") && file.getName().length() > 5);
        List<String> loadNames = Arrays.stream(loadFiles).map(File::getName).collect(Collectors.toList());

        for (String currentName : loadNames) {
            loadGameListViewLabel.getItems().addAll(currentName.substring(0, currentName.length() - 5));
            loadGameListViewLabel.getItems().sort(Comparator.naturalOrder());
            System.out.println(currentName + "\n");
        }
        final int NO_SAVE_FILES = 0;
        loadMapViewLabel.setDisable(loadFiles.length == NO_SAVE_FILES);

    }


    public void onCloseClicked() {
        changeState(ViewState.LOAD_GAME, ViewState.MENU);


    }
    
    public void onLoadGameClicked(){
        String selectedGame = loadGameListViewLabel.getSelectionModel().getSelectedItem();
        getGameWindow().getControllerChan().loadSaveGame(selectedGame);
    }


}
