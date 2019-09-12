package de.sopra.javagame.view;

import de.sopra.javagame.util.HighScore;
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
    
    @FXML ImageView mainPane;
    
    public void init(){
        mainPane.setImage(TextureLoader.getBackground());
    }

    public void onResetClicked() {

    }

    public void onMapChosen() {

    }

    public void onShowReplayClicked() {

    }

    public void onCloseClicked() {
        changeState(ViewState.MENU);
    }

    @Override
    public void refreshList(List<HighScore> scores) {

    }

    @Override
    ViewState getType() {
        return ViewState.HIGH_SCORES;
    }

    @Override
    void reset() {

    }

    @Override
    void show(Stage stage) {

    }
}
