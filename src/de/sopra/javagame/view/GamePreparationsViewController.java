package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import de.sopra.javagame.control.MapController;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapBlackWhite;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.util.DebugUtil.debug;
import static de.sopra.javagame.view.abstraction.ViewState.MAP_EDITOR;

/**
 * GUI für die Spielvorbereitung
 *
 * @author Lisa, Hannah
 */
public class GamePreparationsViewController extends AbstractViewController {

    @FXML
    ImageView mainPane;
    @FXML
    TextField playerOneNameTextField, playerTwoNameTextField, playerThreeNameTextField, playerFourNameTextField;
    @FXML
    JFXComboBox<String> playerOneChooseCharakterComboBox, playerTwoChooseCharakterComboBox, playerThreeChooseCharakterComboBox,
            playerFourChooseCharakterComboBox, editDifficultyComboBox, chooseMapComboBox;
    @FXML
    ToggleButton addPlayerThreeToggleButton, addPlayerFourToggleButton;
    @FXML
    JFXButton addPlayerThreeButton, addPlayerFourButton;
    @FXML
    JFXCheckBox isPlayerOneKiCheckBox, isPlayerTwoKiCheckBox, isPlayerThreeKiCheckBox, isPlayerFourKiCheckBox;
    @FXML
    Label cannotStartGameLabel;

    private Difficulty difficulty;
    private List<Triple<PlayerType, String, Boolean>> playerList = new LinkedList<>();

    public void init() {
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
        playerOneChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);
        playerTwoChooseCharakterComboBox.getItems().addAll(playerTypesList);
        playerTwoChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);
        playerThreeChooseCharakterComboBox.getItems().addAll(playerTypesList);
        playerThreeChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);
        playerFourChooseCharakterComboBox.getItems().addAll(playerTypesList);
        playerFourChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);

        editDifficultyComboBox.getItems().addAll("Novize", "Normal", "Elite", "Legende");
        editDifficultyComboBox.getSelectionModel().select(0);

        playerThreeNameTextField.setDisable(true);
        playerFourNameTextField.setDisable(true);
        playerThreeChooseCharakterComboBox.setDisable(true);
        playerFourChooseCharakterComboBox.setDisable(true);
        addPlayerThreeButton.setDisable(true);
        addPlayerFourButton.setDisable(true);
        isPlayerThreeKiCheckBox.setDisable(true);
        isPlayerFourKiCheckBox.setDisable(true);
        
        File mapFile = new File (MapController.MAP_FOLDER);
        File[] files = mapFile.listFiles();
        List<String> mapNames = Arrays.stream(files).map(File::getName).collect(Collectors.toList());

        for(String currentName : mapNames) {
            chooseMapComboBox.getItems().addAll(currentName.substring(0, currentName.length()-4));
            chooseMapComboBox.getItems().sort(null);
        }
        chooseMapComboBox.getItems().add("neu generieren");
        chooseMapComboBox.getSelectionModel().select(mapNames.size());

    }

    public void onMapEditorClicked() {
        changeState(ViewState.GAME_PREPARATIONS, MAP_EDITOR);
    }

    public void onAddPlayerThreeClicked() {
        playerThreeNameTextField.setDisable(!addPlayerThreeToggleButton.isSelected());
        playerThreeChooseCharakterComboBox.setDisable(!addPlayerThreeToggleButton.isSelected());
        addPlayerThreeButton.setDisable(!addPlayerThreeToggleButton.isSelected());
        isPlayerThreeKiCheckBox.setDisable(!addPlayerThreeToggleButton.isSelected());
    }

    public void onAddPlayerFourClicked() {
        playerFourNameTextField.setDisable(!addPlayerFourToggleButton.isSelected());
        playerFourChooseCharakterComboBox.setDisable(!addPlayerFourToggleButton.isSelected());
        addPlayerFourButton.setDisable(!addPlayerFourToggleButton.isSelected());
        isPlayerFourKiCheckBox.setDisable(!addPlayerFourToggleButton.isSelected());
    }
    //Exception wegen Map einlesen
    public void onStartGameClicked() throws IOException {
        playerList.clear();
        MapBlackWhite currentMap;
        
        //Spielertypen hinzufügen
        addPlayerType(playerOneChooseCharakterComboBox.getValue(),
                !isPlayerOneKiCheckBox.isDisabled(),
                playerOneNameTextField.getText());
        addPlayerType(playerTwoChooseCharakterComboBox.getValue(), !isPlayerTwoKiCheckBox.isDisabled(), playerTwoNameTextField.getText());
        if (addPlayerThreeToggleButton.isSelected()) {
            addPlayerType(playerThreeChooseCharakterComboBox.getValue(), !isPlayerThreeKiCheckBox.isDisabled(), playerThreeNameTextField.getText());
        }
        if (addPlayerFourToggleButton.isSelected()) {
            addPlayerType(playerFourChooseCharakterComboBox.getValue(), !isPlayerFourKiCheckBox.isDisabled(), playerFourNameTextField.getText());
        }
        setDifficulty();

        //PLayerList muss mind. zwei Spieler enthalten
        //TODO Button disablen wenn die Bedingungen nicht erfüllt sind

        if (!playerList.stream().map(Triple::getFirst).filter(playerType -> !playerType.equals(PlayerType.NONE)).allMatch(new HashSet<PlayerType>()::add)) {
            cannotStartGameLabel.setText("Mindestens zwei Spieler haben den gleichen Typ");
            return;
        }
        if (playerList.isEmpty() || difficulty == null || isTextFieldEmpty()) {
            cannotStartGameLabel.setText("Es sind nicht alle Namensfelder ausgefüllt");
            return;
        }
        
        if(chooseMapComboBox.getValue() == null ){
            cannotStartGameLabel.setText("Es ist keine Map ausgewählt");
            return;
        }
        else if(chooseMapComboBox.getValue().equals("neu generieren")){
            currentMap = MapUtil.generateRandomIsland();
            debug("Map: random \n");
        }
        else{
            String mapString = new String(Files.readAllBytes(Paths.get(MapController.MAP_FOLDER + chooseMapComboBox.getValue() +".map")), StandardCharsets.UTF_8);
            currentMap = MapUtil.readBlackWhiteMapFromString(mapString);
            debug("Map:" + chooseMapComboBox.getValue() + "\n");
        }
        
        
        changeState(ViewState.GAME_PREPARATIONS, ViewState.IN_GAME);
        // this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), playerList, difficulty);
        this.getGameWindow().getControllerChan().startNewGame("Coole Carte", currentMap, playerList, difficulty);

        getGameWindow().getControllerChan().getInGameViewAUI().refreshWaterLevel(getGameWindow().getControllerChan().getCurrentAction().getWaterLevel().getLevel());
    }


    public void onCloseClicked() {
        changeState(ViewState.GAME_PREPARATIONS, ViewState.MENU);
    }

    public void addPlayerType(String type, boolean isAi, String name) {
        if (type == null) {
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

    public void setDifficulty() {
        String diff = editDifficultyComboBox.getValue();
        if (diff == null) {
            diff = "";
        }

        switch (diff) {
            case "Novize":
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

    public boolean isTextFieldEmpty() {
        if (addPlayerThreeToggleButton.isSelected() && addPlayerFourToggleButton.isSelected()) {
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty() || playerThreeNameTextField.getText().isEmpty() || playerFourNameTextField.getText().isEmpty();
        } else if (addPlayerThreeToggleButton.isSelected()) {
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty() || playerThreeNameTextField.getText().isEmpty();
        } else if (addPlayerFourToggleButton.isSelected()) {
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty() || playerFourNameTextField.getText().isEmpty();
        } else {
            return playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty();
        }
    }
    

    
}
