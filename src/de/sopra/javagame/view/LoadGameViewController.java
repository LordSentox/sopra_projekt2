package de.sopra.javagame.view;

import com.jfoenix.controls.JFXListView;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI für das anzeigen der Highscores
 *
 * @author Hannah, Lisa
 */
public class LoadGameViewController extends AbstractViewController {

    @FXML
    ImageView mainPane;
    @FXML
    JFXListView<String> loadMapListView;
    @FXML
    Label loadMapViewLabel;

    @SuppressWarnings("Duplicates")
    public void init() throws IOException {
        mainPane.setImage(TextureLoader.getBackground());


        File loadFile = new File(ControllerChan.SAVE_GAME_FOLDER);
        File[] loadFiles = loadFile.listFiles(file -> !file.getName().startsWith(".")
                && file.getName().endsWith(".save") && file.getName().length() > 5);
        List<String> loadNames = Arrays.stream(loadFiles).map(File::getName).collect(Collectors.toList());

        for (String currentName : loadNames) {
            loadMapListView.getItems().addAll(currentName.substring(0, currentName.length() - 5));
            loadMapListView.getItems().sort(Comparator.naturalOrder());
            System.out.println(currentName + "\n");
        }
        final int NO_SAVE_FILES = 0;
        loadMapViewLabel.setVisible(loadFiles.length == NO_SAVE_FILES);

    }


    public void onCloseClicked() {
        ((MainMenuViewController)getGameWindow().getView(ViewState.MENU)).init();
        changeState(ViewState.LOAD_GAME, ViewState.MENU);


    }
    
    public void onLoadGameClicked(){
        String selectedGame = loadMapListView.getSelectionModel().getSelectedItem();
        getGameWindow().getControllerChan().loadSaveGame(selectedGame, false);
        changeState(ViewState.LOAD_GAME, ViewState.IN_GAME);
        getGameWindow().getControllerChan().getInGameViewAUI().refreshHopefullyAll(getGameWindow().getControllerChan().getJavaGame().getPreviousAction());
    }


}
