package de.sopra.javagame.view;

import java.util.LinkedList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
    @FXML JFXCheckBox isPlayerOneKiCheckBox, isPlayerTwoKiCheckBox, isPlayerThreeKiCheckBox, isPlayerFourKiCheckBox;
    
    private Difficulty difficulty;
    
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
        
        editDifficultyComboBox.getItems().addAll( "Novice", "Normal", "Elite", "Legende");
        
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
    
    public void onAddPlayerThreeClicked(){
            playerThreeNameTextField.setDisable(!addPlayerThreeButton.isDisabled());
            playerThreeChooseCharakterComboBox.setDisable(!addPlayerThreeButton.isDisabled());
            addPlayerThreeButton.setDisable(!addPlayerThreeButton.isDisabled());
            isPlayerThreeKiCheckBox.setDisable(!addPlayerThreeButton.isDisabled());
    }
    public void onAddPlayerFourClicked(){
        playerFourNameTextField.setDisable(!addPlayerFourButton.isDisabled());
        playerFourChooseCharakterComboBox.setDisable(!addPlayerFourButton.isDisabled());
        addPlayerFourButton.setDisable(!addPlayerFourButton.isDisabled());
        isPlayerFourKiCheckBox.setDisable(!addPlayerFourButton.isDisabled());
}

    public void onStartGameClicked() {
        //TEMP
//        changeState(ViewState.IN_GAME);
//        LinkedList<Pair<PlayerType, Boolean>> list = new LinkedList<>();
//        list.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, false));
//        list.add(new Pair<PlayerType, Boolean>(PlayerType.EXPLORER, false));
//        list.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, true));
//        list.add(new Pair<PlayerType, Boolean>(PlayerType.DIVER, true));
//        this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), list, Difficulty.ELITE);
        //end TEMP
        
        
        LinkedList<Pair<PlayerType, Boolean>> playerList = new LinkedList<>();
        System.out.println(playerList);
        //Spielertypen hinzufügen
        System.out.println(playerOneChooseCharakterComboBox.getValue());
        addPLayerType(playerOneChooseCharakterComboBox.getValue(),
                playerList,
                !isPlayerOneKiCheckBox.isDisabled());
        addPLayerType(playerTwoChooseCharakterComboBox.getValue(), playerList, !isPlayerTwoKiCheckBox.isDisabled());
        if(!addPlayerThreeButton.isDisabled()){
            addPLayerType(playerThreeChooseCharakterComboBox.getValue(), playerList, !isPlayerThreeKiCheckBox.isDisabled());
        }
        if(!addPlayerFourButton.isDisabled()){
            addPLayerType(playerFourChooseCharakterComboBox.getValue(), playerList, !isPlayerFourKiCheckBox.isDisabled());
        }
        setDifficulty();
        
        //PLayerList muss mind. zwei Spieler enthalten
        //TODO Button disablen wenn die Bedingungen nicht erfüllt sind
        if(!playerList.isEmpty() && difficulty != null){
            changeState(ViewState.IN_GAME);
            this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), playerList, difficulty);
        }
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
    
    
    public void addPLayerType(String type,  LinkedList<Pair<PlayerType, Boolean>> playerList, boolean isAi){
        if(type == null){
            type = "";
        }
        //TODO zufällig soll zufällig sein, und jeder playertype darf nur einmal benutzt werden
        switch (type) {
        case "Taucher":
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.DIVER, isAi));
            System.out.println("i bims ein Taucher");
            break;
        case "Navigator":
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.NAVIGATOR, isAi));
            break;
        case "Pilot":
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.PILOT, isAi));
            break;
        case "Entdecker":
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.EXPLORER, isAi));
            break;
        case "Bote":
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.COURIER, isAi));
            break;
        case "Ingenieur":
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, isAi));
            break;
        case "zufällig":
            //TODO zufällige Verteilung
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.DIVER, isAi));
            break;
        default:
            playerList.add(new Pair<PlayerType, Boolean>(PlayerType.ENGINEER, isAi));
            break;
        }
    }
    
    public void setDifficulty(){
        String diff = editDifficultyComboBox.getValue();
        if(diff == null){
            diff = "";
        }
        switch (diff) {
        case "Novice":
            difficulty = Difficulty.NOVICE;
            break;
        case "Normal":
            difficulty = Difficulty.NORMAL;
            break;
        case "Elite":
            difficulty = Difficulty.ELITE;
            break;
        case "Legende":
            difficulty = Difficulty.LEGENDARY;
            break;
        default:
            difficulty = Difficulty.LEGENDARY;
        }
    }
}
