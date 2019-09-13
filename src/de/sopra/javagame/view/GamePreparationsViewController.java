package de.sopra.javagame.view;

import java.util.LinkedList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapLoader;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.MapPane;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * GUI für die Spielvorbereitung
 *
 * @author Lisa, Hannah
 */
public class GamePreparationsViewController extends AbstractViewController {
    
    @FXML ImageView mainPane;
    @FXML TextField playerOneNameTextField, playerTwoNameTextField, playerThreeNameTextField, playerFourNameTextField;
    @FXML JFXComboBox<String> playerOneChooseCharakterComboBox, playerTwoChooseCharakterComboBox, playerThreeChooseCharakterComboBox,
    playerFourChooseCharakterComboBox, editDifficultyComboBox;
    @FXML ToggleButton addPlayerThreeToggleButton, addPlayerFourToggleButton;
    @FXML JFXButton addPlayerThreeButton,addPlayerFourButton;
    
    public void init(){
        mainPane.setImage(TextureLoader.getBackground());
        
        ObservableList<String> playerTypesList = 
                FXCollections.observableArrayList(
                    "Taucher",
                    "Navigator",
                    "Pilot",
                    "Entdecker",
                    "Bote",
                    "Ingenieur",
                    "zufällig"
                );
        playerOneChooseCharakterComboBox.getItems().addAll(playerTypesList);
        playerTwoChooseCharakterComboBox.getItems().addAll(playerTypesList);
        playerThreeChooseCharakterComboBox.getItems().addAll(playerTypesList);
        playerFourChooseCharakterComboBox.getItems().addAll(playerTypesList);
        
        editDifficultyComboBox.getItems().addAll( "Einfach", "Mittel", "Schwer", "Legende");
        
        playerThreeNameTextField.setDisable(true);
        playerFourNameTextField.setDisable(true);
        playerThreeChooseCharakterComboBox.setDisable(true);
        playerFourChooseCharakterComboBox.setDisable(true);
        addPlayerThreeButton.setDisable(true);
        addPlayerFourButton.setDisable(true);
        
//        addPlayerThreeToggleButton. 
        
    }

    public void onMapEditorClicked() {

    }

    public void onStartGameClicked() {
        changeState(ViewState.IN_GAME);
        LinkedList<Pair<PlayerType, Boolean>> list = new LinkedList<>();
        list.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, false));
        list.add(new Pair<PlayerType, Boolean>(PlayerType.EXPLORER, false));
        list.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
        list.add(new Pair<PlayerType, Boolean>(PlayerType.DIVER, true));
        this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), list, Difficulty.ELITE);
        
    }

    public void onCloseClicked() {
        changeState(ViewState.MENU);
    }

    @Override
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }
}
