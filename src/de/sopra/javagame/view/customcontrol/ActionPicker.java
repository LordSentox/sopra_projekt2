package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.InGameViewController;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import jfxtras.scene.menu.CirclePopupMenu;


public class ActionPicker extends CirclePopupMenu {

    MapPane mapPane;
    InGameViewController inGameViewController;
    PlayerType clickedOnType;

    public ActionPicker(Node node, MouseButton mouseButton, MapPane mapPane, PlayerType type) {
        super(node, mouseButton);
        
        this.mapPane = mapPane;
        this.clickedOnType = type;
        PlayerType activePlayerType = mapPane.getInGameViewController().getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType();
        if(activePlayerType.equals(type)){
            this.initActive();
            
        } else if(!activePlayerType.equals(type) && activePlayerType.equals(PlayerType.NAVIGATOR)) {
            this.initPassivAndNav();
        } else {
            this.initPassive();
        }
    }

    private void initPassive() {
        this.getItems().add(createGiveCardButton());
        
    }

    private void initPassivAndNav() {
        this.getItems().add(createGiveCardButton());
        this.getItems().add(createMoveButton());
    }

    private void initActive() {

        this.getItems().add(createFindArtifactButton());
        this.getItems().add(createSpecialButton());
        this.getItems().add(createDrainButton());
        this.getItems().add(createMoveButton());

    }

    private CustomMenuItem createMoveButton() {
        Button moveButton = new Button("move");
        EventHandler<ActionEvent> moveHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Hello Move");
                mapPane.getInGameViewController().onShowMovementOptionsClicked();
            }
        };
        moveButton.setOnAction(moveHandler);
        CustomMenuItem moveButtonMenuItem = new CustomMenuItem(new Button("move"));
        moveButtonMenuItem.setGraphic(new ImageView(TextureLoader.getMove()));
        moveButtonMenuItem.setOnAction(moveHandler);

        return moveButtonMenuItem;
    }

    private CustomMenuItem createDrainButton() {
        Button drainButton = new Button("move");
        EventHandler<ActionEvent> drainHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Hello Drain");
                mapPane.getInGameViewController().onShowDrainOptionsClicked();
                
            }
        };
        drainButton.setOnAction(drainHandler);
        CustomMenuItem drainButtonMenuItem = new CustomMenuItem(new Button("drain"));
        drainButtonMenuItem.setGraphic(new ImageView(TextureLoader.getDrain()));
        drainButtonMenuItem.setOnAction(drainHandler);

        return drainButtonMenuItem;
    }

    private CustomMenuItem createSpecialButton() {
        Button specialButton = new Button("move");
        EventHandler<ActionEvent> specialHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Hello Special");
                mapPane.getInGameViewController().setSpecialActive(true);
                mapPane.getInGameViewController().onShowSpecialAbilityOptionsClicked();
            }
        };
        specialButton.setOnAction(specialHandler);
        CustomMenuItem specialButtonMenuItem = new CustomMenuItem(new Button("move"));
        specialButtonMenuItem.setGraphic(new ImageView(TextureLoader.getSpecial()));
        specialButtonMenuItem.setOnAction(specialHandler);

        return specialButtonMenuItem;
    }

    private CustomMenuItem createGiveCardButton() {
        Button giveCardButton = new Button("move");
        EventHandler<ActionEvent> giveCardHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Hello Transfer");
                mapPane.getInGameViewController().getGameWindow().getControllerChan().getActivePlayerController().showTransferable(clickedOnType);
            }
        };
        giveCardButton.setOnAction(giveCardHandler);
        CustomMenuItem giveCardButtonMenuItem = new CustomMenuItem(new Button("move"));
        giveCardButtonMenuItem.setGraphic(new ImageView(TextureLoader.getGiveCard()));
        giveCardButtonMenuItem.setOnAction(giveCardHandler);

        return giveCardButtonMenuItem;
    }

    private CustomMenuItem createFindArtifactButton() {
        Button findArtifactButton = new Button("move");
        EventHandler<ActionEvent> findArtifactHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Hello Artifact");
                mapPane.getInGameViewController().onCollectArtifactClicked();
            }
        };
        findArtifactButton.setOnAction(findArtifactHandler);
        CustomMenuItem findArtifactButtonMenuItem = new CustomMenuItem(new Button("move"));
        findArtifactButtonMenuItem.setGraphic(new ImageView(TextureLoader.getFindArtifact()));
        findArtifactButtonMenuItem.setOnAction(findArtifactHandler);

        return findArtifactButtonMenuItem;
    }
   


}
