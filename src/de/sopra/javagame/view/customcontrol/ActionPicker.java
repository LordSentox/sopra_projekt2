package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.control.ActivePlayerController;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.view.InGameViewController;
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

import static de.sopra.javagame.util.DebugUtil.debug;


public class ActionPicker extends CirclePopupMenu {

    private MapPaneTile mapPaneTile;
    private PlayerType movingPlayer;
    private PlayerType delegatingPlayer;
    private ArtifactCardType cardType;
    private int cardIndex;
    private InGameViewController controller;

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

    public void setArtifactCardType(ArtifactCardType type) {
        this.cardType = type;
    }

    public void setCardIndex(int index) {
        this.cardIndex = index;
    }

    public void setInGameViewController(InGameViewController igvc) {
        this.controller = igvc;
    }

    public enum ActionButton implements Function<ActionPicker, CustomMenuItem> {

        MOVE { //Bewegung direkt oder per spezial nach Position

            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> moveHandler = e -> {
                    // Wenn der aktive Spieler der Navigater ist, überprüfe, ob ein anderer Spieler bewegt werden soll
                    if (picker.delegatingPlayer == PlayerType.NAVIGATOR && picker.mapPaneTile.getControl().isSpecialActive()) {
                        Player targetPlayer = picker.mapPaneTile.getControl().getTargetPlayer();
                        if (targetPlayer != null) {
                            Direction direction = targetPlayer.getPosition().getPrimaryDirection(picker.mapPaneTile.getPosition());
                            picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getActivePlayerController().moveOther(direction, targetPlayer.getType());
                        }

                        return;
                    }

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
                    picker.mapPaneTile.getControl().resetHighlighting();
                    debug("view - delegate move to: " + picker.mapPaneTile.getPosition().toString()
                            + " -> with special: " + picker.mapPaneTile.getControl().isSpecialActive());
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
                EventHandler<ActionEvent> drainHandler = e -> {
                    picker.mapPaneTile.getControl().getGameWindow().getControllerChan()
                            .getActivePlayerController().drain(picker.mapPaneTile.getPosition());
                    picker.mapPaneTile.getControl().resetHighlighting();
                    debug("view delegate drain: " + picker.mapPaneTile.getPosition().toString());
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
                ControllerChan controllerChan = picker.mapPaneTile.getControl().getGameWindow().getControllerChan();
                ActivePlayerController control = controllerChan.getActivePlayerController();
                EventHandler<ActionEvent> specialHandler = e -> {
                    //player clicked on himself
                    if (picker.delegatingPlayer == picker.movingPlayer) {
                        // show damit Nachrichten oder ähnliches gezeigt werden
                        control.showSpecialAbility();
                        picker.mapPaneTile.getControl().setSpecialActive(true);
                    }
                    //Navigator hat auf einen anderen Spieler geklickt
                    else if (picker.delegatingPlayer == PlayerType.NAVIGATOR) {
                        picker.mapPaneTile.getControl().setSpecialActive(true);
                        picker.mapPaneTile.getControl().resetHighlighting();
                        picker.mapPaneTile.getControl().setTargetPlayer(picker.movingPlayer);
                        controllerChan.getInGameUserController().showMovements(picker.movingPlayer, false);
                        debug("navigator clicked on different player, showing movements for: " + picker.movingPlayer.name());
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
                EventHandler<ActionEvent> giveCardHandler = e -> {
//                        TODO picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getActivePlayerController().
                    picker.mapPaneTile.getControl().setTransferActive(true);
                    picker.mapPaneTile.getControl().setTargetPlayer(picker.movingPlayer);
                    picker.mapPaneTile.getControl().refreshHand(picker.delegatingPlayer, picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getHand());

                };
                CustomMenuItem giveCardButtonMenuItem = new CustomMenuItem(new Button("give"));
                giveCardButtonMenuItem.setGraphic(new ImageView(TextureLoader.getGiveCard()));
                giveCardButtonMenuItem.setOnAction(giveCardHandler);

                return giveCardButtonMenuItem;
            }
        },
        COLLECT_ARTIFACT {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> findArtifactHandler = e -> picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getActivePlayerController().collectArtifact();
                CustomMenuItem findArtifactButtonMenuItem = new CustomMenuItem(new Button("collect_artifact"));
                findArtifactButtonMenuItem.setGraphic(new ImageView(TextureLoader.getFindArtifact()));
                findArtifactButtonMenuItem.setOnAction(findArtifactHandler);

                return findArtifactButtonMenuItem;
            }
        },
        DISCARD {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> discardHandler = e -> {
                    //TODO wie genau discarden wir?
                    picker.controller.getGameWindow().getControllerChan().getInGameUserController().discardCard(picker.delegatingPlayer, picker.cardIndex);
                    picker.controller.resetTargetPlayer();
                };
                CustomMenuItem discardButtonMenuItem = new CustomMenuItem(new Button("discard"));
                discardButtonMenuItem.setGraphic(new ImageView(TextureLoader.getDrain()));
                discardButtonMenuItem.setOnAction(discardHandler);

                return discardButtonMenuItem;
            }
        },
        PLAY_CARD {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> playCardHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        picker.controller.onSpecialCardClicked(picker.cardType, picker.cardIndex, picker.delegatingPlayer);

                    }
                };
                CustomMenuItem playcardButtonMenuItem = new CustomMenuItem(new Button("play_card"));
                playcardButtonMenuItem.setGraphic(new ImageView(TextureLoader.getSpecial()));
                playcardButtonMenuItem.setOnAction(playCardHandler);

                return playcardButtonMenuItem;

            }
        },
        SANDBAG {
            @Override
            public CustomMenuItem apply(ActionPicker picker) {
                EventHandler<ActionEvent> sandBagHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        //SANDBAG
                        debug("on card clicked: " + "keine card mehr? " + picker.cardIndex + " " + picker.movingPlayer + " " + picker.mapPaneTile.getPosition());
                        picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getInGameUserController().playSandbagCard(picker.movingPlayer, picker.cardIndex,
                                picker.mapPaneTile.getPosition());
                        picker.mapPaneTile.getControl().resetTargetPlayer();
                        picker.mapPaneTile.getControl().refreshTurnState(picker.mapPaneTile.getControl().getGameWindow().getControllerChan().getCurrentAction().getState());
                    }
                };
                CustomMenuItem sandBagButtonMenuItem = new CustomMenuItem(new Button("sandbag"));
                sandBagButtonMenuItem.setGraphic(new ImageView(TextureLoader.getDrain()));
                sandBagButtonMenuItem.setOnAction(sandBagHandler);

                return sandBagButtonMenuItem;
            }
        };
    }
}


