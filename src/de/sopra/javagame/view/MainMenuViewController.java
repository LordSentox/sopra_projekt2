package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import static de.sopra.javagame.view.abstraction.ViewState.*;

import java.io.IOException;

/**
 * GUI für das Hauptfenster
 *
 * @author Lisa, Hannah
 */
public class MainMenuViewController extends AbstractViewController {
    @FXML
    ImageView mainPane;
    @FXML
    JFXButton settingsButton, closeButton, startGameButton, loadGameButton, continueGameButton, mapEditorButton, highscoreButton;

    public void init() {
        mainPane.setImage(TextureLoader.getBackground());
        //TODO: Continue Button disablen, wenn noch kein Spiel am laufen ist
//        if(getGameWindow().getControllerChan().getCurrentAction() != null){
//            continueGameButton.setDisable(false);
//        }
//        if(getGameWindow().getControllerChan().getCurrentAction() == null){
//            continueGameButton.setDisable(true);
//        }
    }

    public void onSettingsClicked() {
        changeState(ViewState.MENU, SETTINGS);
    }

    public void onStartGameClicked() {
        changeState(ViewState.MENU, ViewState.GAME_PREPARATIONS);
    }

    public void onLoadGameClicked() {
        changeState(ViewState.MENU, ViewState.LOAD_GAME);

    }
    public void onContinueClicked(){
        if(getGameWindow().getControllerChan().getCurrentAction() == null){  
            return;
        }
        changeState(ViewState.MENU, ViewState.IN_GAME);
    }

    public void onMapEditorClicked() {

        changeState(ViewState.MENU, MAP_EDITOR);
    }

    public void onHighscoresClicked() {
        changeState(ViewState.MENU, HIGH_SCORES);
    }

    public void onCloseClicked() {
        changeState(ViewState.MENU, ViewState.CLOSE);
    }

    public void onLoadReplayClicked() {

    }

}
