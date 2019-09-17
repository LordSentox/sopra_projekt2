package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.control.ActivePlayerController;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import jfxtras.scene.menu.CirclePopupMenu;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ActionPicker extends CirclePopupMenu {

    private MapPaneTile mapPaneTile;
    private PlayerType movingPlayer;
    private PlayerType delegatingPlayer;

    public ActionPicker(Node node, MouseButton mouseButton) {
        super(node, mouseButton);
    }

    public void init(ActionButton... buttons) {
        this.getItems().clear();
        this.getItems().addAll(Arrays.stream(buttons).map(item -> item.apply(this)).collect(Collectors.toList()));
        if (this.getItems().size() < 3)
            setAnimationDuration(new Duration(1));
    }

    public void setDelegatingPlayer(PlayerType delegatingPlayer) {
        this.delegatingPlayer = delegatingPlayer;
    }

    public void setMovingPlayer(PlayerType movingPlayer) {
        this.movingPlayer = movingPlayer;
    }

    public void setMapPaneTile(MapPaneTile mapPaneTile) {
        this.mapPaneTile = mapPaneTile;
    }

    public enum ActionButton implements Function<ActionPicker, CustomMenuItem> {

        MOVE { //Bewegung direkt oder per spezial nach Position

            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> moveHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        //TODO
                        picker.mapPaneTile.getControl()
                        .getGameWindow()
                        .getControllerChan()
                        .getActivePlayerController()
                        .move(picker
                                .mapPaneTile
                                .getPosition(), picker
                                .mapPaneTile
                                .getControl()
                                .isSpecialActive());
                    }
                };
                CustomMenuItem moveButtonMenuItem = new CustomMenuItem(new Button("move"));
                moveButtonMenuItem.setGraphic(new ImageView(TextureLoader.getMove()));
                moveButtonMenuItem.setOnAction(moveHandler);

                return moveButtonMenuItem;
            }
        },
        DRAIN {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> drainHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getActivePlayerController().drain(picker.mapPaneTile.getPosition());
                    }
                };
                CustomMenuItem drainButtonMenuItem = new CustomMenuItem(new Button("drain"));
                drainButtonMenuItem.setGraphic(new ImageView(TextureLoader.getDrain()));
                drainButtonMenuItem.setOnAction(drainHandler);

                return drainButtonMenuItem;
            }
        },
        SPECIAL {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> specialHandler = new EventHandler<ActionEvent>() {
                    ActivePlayerController control = picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getActivePlayerController();

                    @Override
                    public void handle(ActionEvent e) {
                        //player clicked on himself
                        if (picker.delegatingPlayer == picker.movingPlayer) {
                            control.showSpecialAbility();
                            picker.mapPaneTile.getControl().setSpecialActive(true);
                        }
                    }
                };
                CustomMenuItem specialButtonMenuItem = new CustomMenuItem(new Button("move"));
                specialButtonMenuItem.setGraphic(new ImageView(TextureLoader.getSpecial()));
                specialButtonMenuItem.setOnAction(specialHandler);

                return specialButtonMenuItem;
            }
        },
        GIVE_CARD {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> giveCardHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
//                        TODO picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getActivePlayerController().
                    }
                };
                CustomMenuItem giveCardButtonMenuItem = new CustomMenuItem(new Button("move"));
                giveCardButtonMenuItem.setGraphic(new ImageView(TextureLoader.getGiveCard()));
                giveCardButtonMenuItem.setOnAction(giveCardHandler);

                return giveCardButtonMenuItem;
            }
        },
        COLLECT_ARTIFACT {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> findArtifactHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getActivePlayerController().collectArtifact();
                    }
                };
                CustomMenuItem findArtifactButtonMenuItem = new CustomMenuItem(new Button("move"));
                findArtifactButtonMenuItem.setGraphic(new ImageView(TextureLoader.getFindArtifact()));
                findArtifactButtonMenuItem.setOnAction(findArtifactHandler);

                return findArtifactButtonMenuItem;
            }
        };

    }


}
