package de.sopra.javagame.view;

import com.jfoenix.controls.JFXComboBox;

import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.MapPane;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * GUI für die Spielvorbereitung
 *
 * @author Lisa, Hannah
 */
public class GamePreparationsViewController extends AbstractViewController {
    
    @FXML
    ImageView mainPane;
    TextField playerOneNameTextField, chooseCharakterPlayerTwoLabel, playerThreeNameTextField, playerFourNameTextField;
    JFXComboBox playerOneChooseCharakterComboBox, playerTwoChooseCharakterComboBox, playerThreeChooseCharakterComboBox, playerFourChooseCharakterComboBox;

    
    public void init(){
        mainPane.setImage(TextureLoader.getBackground());
        
    }

    public void onMapEditorClicked() {

    }

    public void onStartGameClicked() {
        changeState(ViewState.IN_GAME);
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
