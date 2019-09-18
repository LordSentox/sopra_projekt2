package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.DialogPack;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

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

        setContinueButtonDisabled(getGameWindow().getControllerChan().getCurrentAction() == null);
    }

    public void onSettingsClicked() {
        changeState(ViewState.MENU, SETTINGS);
    }

    public void onMaybeStartGameClicked() {
        if (continueGameButton.isDisabled()) {
            startGame();
        } else {
            DialogPack pack = new DialogPack(getGameWindow().getMainStage(), 
                    null, 
                    "Möchtest du wirklich ein neues Spiel starten?", 
                    "Dein aktuell laufendes Spiel geht verloren!");  
            pack.addButton("Spiel starten", () -> startGame());
            pack.addButton("Abbrechen", () -> {});
            pack.setAlertType(AlertType.CONFIRMATION);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
        }
    }
    
    private void startGame() {
        changeState(ViewState.MENU, ViewState.GAME_PREPARATIONS);
    }

    public void onLoadGameClicked() {
        changeState(ViewState.MENU, ViewState.LOAD_GAME);

    }
    public void onContinueClicked(){
        setContinueButtonDisabled(getGameWindow().getControllerChan().getCurrentAction() == null);
        changeState(ViewState.MENU, ViewState.IN_GAME);
    }

    private void setContinueButtonDisabled(boolean disabled) {
        continueGameButton.setDisable(disabled);
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
