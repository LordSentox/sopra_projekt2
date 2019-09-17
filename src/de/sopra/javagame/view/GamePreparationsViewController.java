package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Triple;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
    @FXML Label cannotStartGameLabel;
    
    private Difficulty difficulty;
    private List<Triple<PlayerType,String, Boolean>> playerList = new LinkedList<>();
    
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
        isPlayerThreeKiCheckBox.setDisable(true);
        isPlayerFourKiCheckBox.setDisable(true);
        
    }

    public void onMapEditorClicked() {

    }
    
    public void onAddPlayerThreeClicked(){
        playerThreeNameTextField.setDisable(!addPlayerThreeToggleButton.isSelected());
        playerThreeChooseCharakterComboBox.setDisable(!addPlayerThreeToggleButton.isSelected());
        addPlayerThreeButton.setDisable(!addPlayerThreeToggleButton.isSelected());
        isPlayerThreeKiCheckBox.setDisable(!addPlayerThreeToggleButton.isSelected());
    }
    public void onAddPlayerFourClicked(){
        playerFourNameTextField.setDisable(!addPlayerFourToggleButton.isSelected());
        playerFourChooseCharakterComboBox.setDisable(!addPlayerFourToggleButton.isSelected());
        addPlayerFourButton.setDisable(!addPlayerFourToggleButton.isSelected());
        isPlayerFourKiCheckBox.setDisable(!addPlayerFourToggleButton.isSelected());
}

    public void onStartGameClicked() {
        playerList.clear();
        
        //Spielertypen hinzufügen
        addPlayerType(playerOneChooseCharakterComboBox.getValue(),
                !isPlayerOneKiCheckBox.isDisabled(),
                playerOneNameTextField.getText());
        addPlayerType(playerTwoChooseCharakterComboBox.getValue(), !isPlayerTwoKiCheckBox.isDisabled(), playerTwoNameTextField.getText());
        if(addPlayerThreeToggleButton.isSelected()){
            addPlayerType(playerThreeChooseCharakterComboBox.getValue(), !isPlayerThreeKiCheckBox.isDisabled(), playerThreeNameTextField.getText());
        }
        if(addPlayerFourToggleButton.isSelected()){
            addPlayerType(playerFourChooseCharakterComboBox.getValue(), !isPlayerFourKiCheckBox.isDisabled(), playerFourNameTextField.getText());
        }
        setDifficulty();
        
        //PLayerList muss mind. zwei Spieler enthalten
        //TODO Button disablen wenn die Bedingungen nicht erfüllt sind
        
        if(!playerList.stream().map(Triple::getFirst).filter(playerType -> !playerType.equals(PlayerType.NONE)).allMatch(new HashSet<PlayerType>()::add)){
            cannotStartGameLabel.setText("Mindestens zwei Spieler haben den gleichen Typ");
            return;
        }        
        if(playerList.isEmpty() || difficulty == null || isTextFieldEmpty() ){
            cannotStartGameLabel.setText("Es sind nicht alle Felder ausgefüllt");            
            return;
        }
        changeState(ViewState.IN_GAME);
        // this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), playerList, difficulty);
        this.getGameWindow().getControllerChan().startNewGame("Coole Carte", MapUtil.generateRandomIsland(), playerList, difficulty);

        getGameWindow().getControllerChan().getInGameViewAUI().refreshWaterLevel(0);
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
    
    
    public void addPlayerType(String type, boolean isAi, String name){
        if(type == null){
            type = "";
        }
        
        switch (type) {
            case "Taucher":
                playerList.add(new Triple<>(PlayerType.DIVER, name, isAi));
                break;
            case "Navigator":
                playerList.add(new Triple<>(PlayerType.NAVIGATOR, name, isAi));
                break;
            case "Pilot":
                playerList.add(new Triple<>(PlayerType.PILOT, name, isAi));
                break;
            case "Entdecker":
                playerList.add(new Triple<>(PlayerType.EXPLORER, name, isAi));
                break;
            case "Bote":
                playerList.add(new Triple<>(PlayerType.COURIER, name, isAi));
                break;
            case "Ingenieur":
                playerList.add(new Triple<>(PlayerType.ENGINEER, name, isAi));
                break;
            default:
                playerList.add(new Triple<>(PlayerType.NONE, name, isAi));
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
        }
    }
    
    public boolean isTextFieldEmpty(){
        if(addPlayerThreeToggleButton.isSelected() && addPlayerFourToggleButton.isSelected()){
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty() || playerThreeNameTextField.getText().isEmpty() || playerFourNameTextField.getText().isEmpty();
        }
        else if(addPlayerThreeToggleButton.isSelected()){
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty() || playerThreeNameTextField.getText().isEmpty();
        }
        else if(addPlayerFourToggleButton.isSelected()){
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty() || playerFourNameTextField.getText().isEmpty();
        }
        else{
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty();
        }
    }
}
