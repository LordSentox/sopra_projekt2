package de.sopra.javagame.view.customcontrol;

import java.awt.event.ActionListener;

import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.InGameViewController;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jfxtras.labs.scene.control.radialmenu.RadialMenu;
import jfxtras.labs.scene.control.radialmenu.RadialMenuItem;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.menu.CirclePopupMenu;

public class ActionPicker extends CirclePopupMenu {
    
    MapPane mapPane;
    InGameViewController inGameViewController;
    public ActionPicker(Node node, MouseButton mouseButton, MapPane mapPane){
        super(node, mouseButton);
        this.mapPane = mapPane;
        this.init();
        
    }
    
    private void init(){
        
        Button moveButton = new Button("move");
        EventHandler<ActionEvent> moveHandler = new EventHandler<ActionEvent>() { 
            @Override 
            public void handle(ActionEvent e) { 
               System.out.println("Hello World");
               mapPane.getInGameViewController().onShowMovementOptionsClicked();
            } 
         };   
        moveButton.setOnAction(moveHandler);
        CustomMenuItem moveButtonMenuItem = new CustomMenuItem(new Button("move"));
        moveButtonMenuItem.setGraphic(new ImageView(TextureLoader.getSea1()));
        moveButtonMenuItem.setOnAction(moveHandler);
        
        MenuItem drainButton = new MenuItem();
        drainButton.setText("Drain");
        drainButton.setGraphic(new ImageView(TextureLoader.getGone()));
        
        MenuItem specialButton = new MenuItem();
        specialButton.setText("Special");
        specialButton.setGraphic(new ImageView(TextureLoader.getGone()));
        
        MenuItem findButton = new MenuItem();
        findButton.setText("Find");
        findButton.setGraphic(new ImageView(TextureLoader.getGone()));
        
        MenuItem giveButton = new MenuItem();
        giveButton.setText("give");
        giveButton.setGraphic(new ImageView(TextureLoader.getGone()));
        
        this.getItems().add(giveButton);
        this.getItems().add(findButton);
        this.getItems().add(specialButton);
        this.getItems().add(drainButton);
        this.getItems().add(moveButtonMenuItem);
        
    }

}
