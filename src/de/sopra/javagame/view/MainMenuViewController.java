package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;

import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapLoader;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static de.sopra.javagame.view.abstraction.ViewState.HIGH_SCORES;
import static de.sopra.javagame.view.abstraction.ViewState.SETTINGS;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * GUI für das Hauptfenster
 *
 * @author Lisa, Hannah
 */
public class MainMenuViewController extends AbstractViewController {
    @FXML
    ImageView mainPane;
    @FXML
    JFXButton settingsButton, closeButton, startGameButton, loadGameButton, mapEditorButton, highscoreButton;

    public void init() {
        mainPane.setImage(TextureLoader.getBackground());
    }

    public void onSettingsClicked() {
        changeState(SETTINGS);
    }

    public void onStartGameClicked() {
//        changeState(ViewState.GAME_PREPARATIONS);
        
        //TODO: DEBUG
        LinkedList<Pair<PlayerType, Boolean>> list = new LinkedList<>();
        list.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, false));
        list.add(new Pair<PlayerType, Boolean>(PlayerType.EXPLORER, false));
        list.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
        list.add(new Pair<PlayerType, Boolean>(PlayerType.DIVER, true));
        this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), list, Difficulty.ELITE);
        changeState(ViewState.IN_GAME);
    }

    public void onLoadGameClicked() {

    }

    public void onMapEditorClicked() {

    }

    public void onHighscoresClicked() {
        changeState(HIGH_SCORES);
    }

    public void onCloseClicked() {
        changeState(ViewState.CLOSE);
    }

    public void onLoadReplayClicked() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }
}
