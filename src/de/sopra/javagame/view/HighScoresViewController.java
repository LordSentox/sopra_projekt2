package de.sopra.javagame.view;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import de.sopra.javagame.control.HighScoresController;
import de.sopra.javagame.control.MapController;
import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.Notification;
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

import static de.sopra.javagame.util.DebugUtil.debug;

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
    @FXML
    Label highScoreListViewLabel, chooseMapLabel;

    public void init() throws IOException {
        mainPane.setImage(TextureLoader.getBackground());
        /*
        String[] arr = new String[]{"arch_of_destiny", "atoll_of_judgement", "bone", "bridge_of_horrors", "coral_reef",
                "gull_cove", "island_of_death", "island_of_shadows", "skull_island", "vulcan_island"};
        List<String> standardMaps = Arrays.asList(arr);
        */

        File mapFile = new File(MapController.MAP_FOLDER);
        File[] files = mapFile.listFiles();
        List<String> mapNames = Arrays.stream(files).map(File::getName).collect(Collectors.toList());


        File scoreFile = new File(HighScoresController.SCORE_FOLDER);
        System.out.println(scoreFile.isDirectory());
        File[] myScores = scoreFile.listFiles();
       
        File[] scoreFiles = scoreFile.listFiles(file -> !file.getName().startsWith(".")
                && file.getName().endsWith(".score") && file.getName().length() > 6);
        List<String> scoreNames = Arrays.stream(scoreFiles).map(File::getName).collect(Collectors.toList());

        for (String currentMap : mapNames) {
            String fileName = currentMap.substring(0, currentMap.length() - 4);
            if (!scoreNames.contains(fileName + ".score")) {
                new File(HighScoresController.SCORE_FOLDER + fileName + ".score").createNewFile();
            }
        }

        for (String currentName : scoreNames) {
            mapSelectionComboBox.getItems().addAll(currentName.substring(0, currentName.length() - 6));
            mapSelectionComboBox.getItems().sort(Comparator.naturalOrder());

            debug(currentName + "\n");
        }

        //mapSelectionComboBox.getItems().addAll(standardMaps);
        mapSelectionComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue)
                -> getGameWindow().getControllerChan().getHighScoresController().loadHighScores(newValue));

    }

    public void onResetClicked() {
        String selectedMap = mapSelectionComboBox.getSelectionModel().getSelectedItem();
        getGameWindow().getControllerChan().getHighScoresController().resetHighScores(selectedMap);
    }

    public void onMapChosen() {
        HighScoresController hsController = getGameWindow().getControllerChan().getHighScoresController();
        String selectedMap = mapSelectionComboBox.getSelectionModel().getSelectedItem();
        hsController.loadHighScores(selectedMap);
    }

    public void onShowReplayClicked() {
        HighScore selectedHighScore = highScoreListView.getSelectionModel().getSelectedItem();
        changeState(ViewState.HIGH_SCORES, ViewState.IN_GAME);
        getGameWindow().getControllerChan().loadSaveGame(selectedHighScore.getReplayName(), true);
    }

    public void onCloseClicked() {
        ((MainMenuViewController)getGameWindow().getView(ViewState.MENU)).init();
        changeState(ViewState.HIGH_SCORES, ViewState.MENU);
    }

    public void onMainMenuClicked() {
        ((MainMenuViewController)getGameWindow().getView(ViewState.MENU)).init();
        changeState(ViewState.HIGH_SCORES, ViewState.MENU);
    }

    @Override
    public void showNotification(Notification notification) {
        super.showNotification(notification);
    }

    @Override
    public void refreshList(List<HighScore> scores) {
        scores.sort(null);
        if (scores.isEmpty()) {
            highScoreListView.getItems().clear();
            changeHighScoreLabelVisibility(true);
        } else {
            changeHighScoreLabelVisibility(false);
            scores.forEach(score -> highScoreListView.getItems().add(score));
        }
    }

    private void changeHighScoreLabelVisibility(boolean active) {
        highScoreListViewLabel.setVisible(active);
    }
}
