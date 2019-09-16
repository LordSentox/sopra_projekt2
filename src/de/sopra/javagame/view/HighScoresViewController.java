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
    JFXComboBox<String> mapSelectionBox;
    @FXML
    JFXListView<HighScore> highScoreListView;

    public void init() {
        mainPane.setImage(TextureLoader.getBackground());
    }

    public void onResetClicked() {
        String selectedMap = (String) mapSelectionBox.getSelectionModel().getSelectedItem();
        getGameWindow().getControllerChan().getHighScoresController().resetHighScores(selectedMap);
    }

    public void onMapChosen() {
        HighScoresController hsController = getGameWindow().getControllerChan().getHighScoresController();
        String selectedMap = (String) mapSelectionBox.getSelectionModel().getSelectedItem();
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

    @Override
    public void refreshList(List<HighScore> scores) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }
}
