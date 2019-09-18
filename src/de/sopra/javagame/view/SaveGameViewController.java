package de.sopra.javagame.view;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.GameWindow;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class SaveGameViewController extends AbstractViewController {
    @FXML
    ImageView mainPane;
    @FXML
    JFXListView<String> loadGameListViewLabel;
    @FXML
    Label loadMapViewLabel;
    @FXML
    JFXTextField saveGameTextField, notificationLabel;

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
        changeState(ViewState.LOAD_GAME, ViewState.IN_GAME_SETTINGS);


    }
    
    public void onSaveGameClicked(){
        String selectedGame = saveGameTextField.getText();
        if(selectedGame.isEmpty()){
            notificationLabel.setText("Das Feld ist nicht ausgefüllt");
            return;
        }
        getGameWindow().getControllerChan().loadSaveGame(selectedGame);
    }
    
    

}
