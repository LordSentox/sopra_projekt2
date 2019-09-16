package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        
        List<Pair<Pair<PlayerType,String>, Boolean>> playerList = new LinkedList<>();
        System.out.println(playerList);
        //Spielertypen hinzufügen
        System.out.println(playerOneChooseCharakterComboBox.getValue());
        addPlayerType(playerOneChooseCharakterComboBox.getValue(),
                playerList,
                !isPlayerOneKiCheckBox.isDisabled(),
                playerOneNameTextField.getText());
        addPlayerType(playerTwoChooseCharakterComboBox.getValue(), playerList, !isPlayerTwoKiCheckBox.isDisabled(), playerTwoNameTextField.getText());
        if(addPlayerThreeToggleButton.isSelected()){
            addPlayerType(playerThreeChooseCharakterComboBox.getValue(), playerList, !isPlayerThreeKiCheckBox.isDisabled(), playerThreeNameTextField.getText());
        }
        if(addPlayerFourToggleButton.isSelected()){
            addPlayerType(playerFourChooseCharakterComboBox.getValue(), playerList, !isPlayerFourKiCheckBox.isDisabled(), playerFourNameTextField.getText());
        }
        setDifficulty();
        
        //PLayerList muss mind. zwei Spieler enthalten
        //TODO Button disablen wenn die Bedingungen nicht erfüllt sind
        if(!playerList.stream().map(Pair::getLeft).filter(playerType -> !playerType.equals(PlayerType.NONE)).allMatch(new HashSet<PlayerType>()::add)){
            cannotStartGameLabel.setText("Mindestens zwei Spieler haben den gleichen Typ");
            return;
        }        
        if(playerList.isEmpty() || difficulty == null){
            cannotStartGameLabel.setText("es sind nicht alle Felder ausgefüllt");            
            return;
        }
        changeState(ViewState.IN_GAME);
        // this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), playerList, difficulty);
        this.getGameWindow().getControllerChan().startNewGame("Coole Carte", MapUtil.generateRandomIsland(), playerList, difficulty);
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
    
    
    public void addPlayerType(String type,  List<Pair<Pair<PlayerType,String>, Boolean>> playerList, boolean isAi, String name){
        if(type == null){
            type = "";
        }
        //TODO zufällig soll zufällig sein
        switch (type) {
        case "Taucher":
            playerList.add(new Pair<>(new Pair<>(PlayerType.DIVER, name), isAi));
            System.out.println("i bims ein Taucher");
            break;
        case "Navigator":
            playerList.add(new Triple<>(PlayerType.NAVIGATOR, name, isAi));
            break;
        case "Pilot":
            playerList.add(new Pair<>(new Pair<>(PlayerType.PILOT, name), isAi));
            break;
        case "Entdecker":
            playerList.add(new Pair<>(new Pair<>(PlayerType.EXPLORER, name), isAi));
            break;
        case "Bote":
            playerList.add(new Pair<>(new Pair<>(PlayerType.COURIER, name), isAi));
            break;
        case "Ingenieur":
            playerList.add(new Pair<>(new Pair<>(PlayerType.ENGINEER, name), isAi));
            break;
        default:
            playerList.add(new Pair<>(new Pair<>(PlayerType.NONE, name), isAi));
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
//            difficulty = Difficulty.LEGENDARY;
        }
    }
}
