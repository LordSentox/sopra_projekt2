package de.sopra.javagame.view;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import de.sopra.javagame.control.HighScoresController;
import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * GUI für das anzeigen der Highscores
 *
 * @author Hannah, Lisa
 */
public class HighScoresViewController extends AbstractViewController implements HighScoresViewAUI {

    @FXML
    ImageView mainPane;
    @FXML
    JFXComboBox<String> mapSelectionComboBox;
    @FXML
    JFXListView<HighScore> highScoreListView;

    public void init() {
        mainPane.setImage(TextureLoader.getBackground());
        
        String[] arr = new String[]{"arch_of_destiny", "atoll_of_judgement", "bone", "bridge_of_horrors", "coral_reef",
                "gull_cove", "island_of_death", "island_of_shadows", "skull_island", "vulcan_island"};
        List<String> standardMaps = Arrays.asList(arr);
        
        mapSelectionComboBox.getItems().addAll(standardMaps);
        mapSelectionComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) 
                -> getGameWindow().getControllerChan().getHighScoresController().loadHighScores(newValue));
        
    }

    public void onResetClicked() {
        String selectedMap = (String) mapSelectionComboBox.getSelectionModel().getSelectedItem();
        getGameWindow().getControllerChan().getHighScoresController().resetHighScores(selectedMap);
    }

    public void onMapChosen() {
        HighScoresController hsController = getGameWindow().getControllerChan().getHighScoresController();
        String selectedMap = (String) mapSelectionComboBox.getSelectionModel().getSelectedItem();
        hsController.loadHighScores(selectedMap);
    }

    public void onShowReplayClicked() {
        HighScore selectedHighScore = (HighScore) highScoreListView.getSelectionModel().getSelectedItem();
        changeState(ViewState.IN_GAME);
        getGameWindow().getControllerChan().loadSaveGame(selectedHighScore.getReplayName());
    }

    public void onCloseClicked() {
        changeState(ViewState.MENU);
        
        
    }
    
    public void onMainMenuClicked() {
        changeState(ViewState.MENU);
    }
    
    @Override
    public void refreshList(List<HighScore> scores) {
        scores.sort(null);
        scores.forEach(score -> {
            highScoreListView.getItems().add(score);
        });
    }

    @Override
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }
}
