package de.sopra.javagame.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import de.sopra.javagame.control.MapController;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.CardStackUtil;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.MapFull;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Triple;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.EditorMapPane;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

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
            playerFourChooseCharakterComboBox, editDifficultyComboBox, chooseMapComboBox, 
            chooseDeveloperMapComboBox ,chooseArtifactCardStackComboBox, chooseFloodCardStackComboBox;
    @FXML
    ToggleButton addPlayerThreeToggleButton, addPlayerFourToggleButton;
    @FXML
    JFXButton addPlayerThreeButton, addPlayerFourButton, openMapEditorButton;
    @FXML
    JFXCheckBox isPlayerOneKiCheckBox, isPlayerTwoKiCheckBox, isPlayerThreeKiCheckBox, isPlayerFourKiCheckBox;
    @FXML
    Label cannotStartGameLabel, developerArtifactLabel, developerFloodLabel;
    

    private Difficulty difficulty;
    private List<Triple<PlayerType, String, Boolean>> playerList = new LinkedList<>();
    
    private ObservableList<String> playerTypesList ;
    private List<String> devFloodStackNames ;
    
    public static final String DEV_MAP_FOLDER = "resources/ai_tournament/maps/";
    public static final String DEV_ARTIFACT_STACK_FOLDER = "resources/ai_tournament/artifact_stack/";
    public static final String DEV_FLOOD_STACK_FOLDER = "resources/ai_tournament/flood_stack/";
    final int DOT_MAP_ENDING_LENGTH = 4;
    final int DOT_CSV_ENDING_LENGTH = 4;

    public void init() {
        mainPane.setImage(TextureLoader.getBackground());

        playerTypesList =
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

        File mapFile = new File(MapController.MAP_FOLDER);
        File[] files = mapFile.listFiles();
        devFloodStackNames = Arrays.stream(files).map(File::getName).collect(Collectors.toList());

        for (String currentName : devFloodStackNames) {
            if (!currentName.startsWith(".")) {
                chooseMapComboBox.getItems().addAll(currentName.substring(0, currentName.length() - DOT_MAP_ENDING_LENGTH));
            }
            chooseMapComboBox.getItems().sort(null);
        }
        chooseMapComboBox.getItems().add("neu generieren");
        chooseMapComboBox.getSelectionModel().select(devFloodStackNames.size());
        
        cannotStartGameLabel.setTextFill(Paint.valueOf("#FF0000"));
        
        
        File devMapFile = new File(DEV_MAP_FOLDER);
        File[] devMapFiles = devMapFile.listFiles();
        devFloodStackNames = Arrays.stream(devMapFiles).map(File::getName).collect(Collectors.toList());

        for (String currentName : devFloodStackNames) {
            if (!currentName.startsWith(".")) {
            chooseDeveloperMapComboBox.getItems().addAll(currentName.substring(0, currentName.length() - DOT_MAP_ENDING_LENGTH ));
            }
            chooseDeveloperMapComboBox.getItems().sort(null);
        } 
        File devArtifactStackFile = new File(DEV_ARTIFACT_STACK_FOLDER);
        File[] devArtifactStackFiles = devArtifactStackFile.listFiles();
        devFloodStackNames = Arrays.stream(devArtifactStackFiles).map(File::getName).collect(Collectors.toList());

        for (String currentName : devFloodStackNames) {
            if (!currentName.startsWith(".")) {
                chooseArtifactCardStackComboBox.getItems().addAll(currentName.substring(0, currentName.length() - DOT_CSV_ENDING_LENGTH));
            }
            chooseArtifactCardStackComboBox.getItems().sort(null);
        } 
        File devFloodStackFile = new File(DEV_FLOOD_STACK_FOLDER);
        File[] devFloodStackFiles = devFloodStackFile.listFiles();
        devFloodStackNames = Arrays.stream(devFloodStackFiles).map(File::getName).collect(Collectors.toList());

        for (String currentName : devFloodStackNames) {
            if (!currentName.startsWith(".")) {
                chooseFloodCardStackComboBox.getItems().addAll(currentName.substring(0, currentName.length() - DOT_CSV_ENDING_LENGTH));
            }
            chooseFloodCardStackComboBox.getItems().sort(null);
        }
        
        makeDeveloperToolsVisible(getGameWindow().getSettings().devToolsEnabled().get());

    }

    private void makeDeveloperToolsVisible(boolean visible) {
        chooseMapComboBox.setVisible(!visible); 
        openMapEditorButton.setVisible(!visible);
        chooseDeveloperMapComboBox.setVisible(visible);
        chooseArtifactCardStackComboBox.setVisible(visible);
        chooseFloodCardStackComboBox.setVisible(visible);
        developerArtifactLabel.setVisible(visible);
        developerFloodLabel.setVisible(visible);
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
        MapFull tournamentMap;
        Triple tournamentTriple;

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
        
        playerOneNameTextField.clear();
        playerTwoNameTextField.clear();
        playerThreeNameTextField.clear();
        playerFourNameTextField.clear();
        
        playerOneChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);
        playerTwoChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);
        playerThreeChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);
        playerFourChooseCharakterComboBox.getSelectionModel().select(playerTypesList.size() - 1);
        editDifficultyComboBox.getSelectionModel().select(0);

        playerThreeNameTextField.setDisable(true);
        playerFourNameTextField.setDisable(true);
        playerThreeChooseCharakterComboBox.setDisable(true);
        playerFourChooseCharakterComboBox.setDisable(true);
        addPlayerThreeButton.setDisable(true);
        addPlayerFourButton.setDisable(true);
        isPlayerOneKiCheckBox.setSelected(false);
        isPlayerTwoKiCheckBox.setSelected(false);
        isPlayerThreeKiCheckBox.setSelected(false);
        isPlayerFourKiCheckBox.setSelected(false);
        isPlayerThreeKiCheckBox.setDisable(true);
        isPlayerFourKiCheckBox.setDisable(true);
        
        addPlayerThreeToggleButton.setSelected(false);
        addPlayerFourToggleButton.setSelected(false);

        chooseMapComboBox.getSelectionModel().select(devFloodStackNames.size());

        if (!getGameWindow().getSettings().devToolsEnabled().get()) {
            
            if (chooseMapComboBox.getValue() == null) {
                cannotStartGameLabel.setText("Es ist keine Map ausgewählt");
                return;
            } else if (chooseMapComboBox.getValue().equals("neu generieren")) {
                currentMap = MapUtil.generateRandomIsland();
                debug("Map: random \n");
            } else {
                String mapString = new String(Files.readAllBytes(Paths.get(MapController.MAP_FOLDER + chooseMapComboBox.getValue() + ".map")), StandardCharsets.UTF_8);
                currentMap = MapUtil.readBlackWhiteMapFromString(mapString);
                debug("Map:" + chooseMapComboBox.getValue() + "\n");
            }
            
            
            this.getGameWindow().getControllerChan().startNewGame("Coole Carte", currentMap, playerList, difficulty);
        } else {
            
            if (chooseDeveloperMapComboBox.getValue() == null) {
                cannotStartGameLabel.setText("Es ist keine Map ausgewählt");
                return;
            } else if (chooseArtifactCardStackComboBox.getValue() == null){
                cannotStartGameLabel.setText("Es ist kein Flutkartenstapel ausgewählt");
                return;
            } else if (chooseFloodCardStackComboBox == null) {
                cannotStartGameLabel.setText("Es ist kein Artefaktkartenstapel ausgewählt");
                return;
            } else {
                String devMapString = new String(Files.readAllBytes(Paths.get(DEV_MAP_FOLDER + chooseDeveloperMapComboBox.getValue() + ".map")), StandardCharsets.UTF_8);
                tournamentMap = MapUtil.readFullMapFromString(devMapString);
                debug("Map:" + chooseMapComboBox.getValue() + "\n");
                
                String artifactStackString = new String(Files.readAllBytes(Paths.get(DEV_ARTIFACT_STACK_FOLDER + chooseArtifactCardStackComboBox.getValue() + ".csv")), StandardCharsets.UTF_8);
                CardStack<ArtifactCard> artifactStack = CardStackUtil.readArtifactCardStackFromString(artifactStackString);
                
                String flooodStackString = new String(Files.readAllBytes(Paths.get(DEV_FLOOD_STACK_FOLDER + chooseFloodCardStackComboBox.getValue() + ".csv")), StandardCharsets.UTF_8);
                CardStack<FloodCard> floodStack = CardStackUtil.readFloodCardStackFromString(flooodStackString);
                
                tournamentTriple = new Triple<>(tournamentMap, artifactStack, floodStack);
            }
            
            
            this.getGameWindow().getControllerChan().startNewGame("Coole Carte", tournamentTriple, playerList, difficulty);
        }

        changeState(ViewState.GAME_PREPARATIONS, ViewState.IN_GAME);
        // this.getGameWindow().getControllerChan().startNewGame("vulcan_island", new MapLoader().loadMap("vulcan_island"), playerList, difficulty);

        getGameWindow().getControllerChan().getInGameViewAUI().refreshWaterLevel(getGameWindow().getControllerChan().getCurrentAction().getWaterLevel().getLevel());
    }
    
    public void onDeveloperCardStacksClicked () {
        
    }
    
    public void onDeveloperMapEditorClicked () {
        
    }


    public void onCloseClicked() {
        ((MainMenuViewController)getGameWindow().getView(ViewState.MENU)).init();
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
