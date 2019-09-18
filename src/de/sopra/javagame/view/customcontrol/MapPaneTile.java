package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.InGameViewController;
import de.sopra.javagame.view.customcontrol.ActionPicker.ActionButton;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MapPaneTile extends StackPane implements EventHandler<MouseEvent> {
    private final int PLAYERSIZE = 65;

    private InGameViewController control;
    private final Point position;
    private TileView base;
    private List<PlayerType> playersOnTile = new LinkedList<>();
    private GridPane fourPlayerPane = new GridPane();
    private boolean canDrain;
    private boolean canMoveTo;
    private boolean canSandBag = false;
    private int cardIndex;
    private ActionPicker contextPicker;

    public MapPaneTile(InGameViewController control, TileView base, Point position) {
        this.control = control;
        this.position = position;
        this.getChildren().add(base);
        this.getChildren().add(fourPlayerPane);
        this.base = base;
        this.initGridPane();
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, this);
        this.contextPicker = new ActionPicker(this, MouseButton.NONE);
        this.contextPicker.setMapPaneTile(this);
    }

    public MapPaneTile(ImageView base) {
        this.position = null;
        this.getChildren().add(base);
        this.base = null;
    }

    public void putPlayer(PlayerType type) {
        playersOnTile.add(type);
        this.refreshGridPane();
    }

    public void unputPlayer(PlayerType type) {
        playersOnTile.remove(type);
        this.refreshGridPane();
    }

    public void refreshGridPane() {
        fourPlayerPane.getChildren().clear();
        AtomicInteger x = new AtomicInteger(0), y = new AtomicInteger(0);
        playersOnTile.forEach(type -> {
            PlayerImageView view = new PlayerImageView(this, type, TextureLoader.getPlayerIconTexture(type));
            view.setPreserveRatio(true);
            view.setFitHeight(PLAYERSIZE);
            fourPlayerPane.getChildren().add(view);
            GridPane.setConstraints(view, x.get(), y.get());
            view.addEventFilter(MouseEvent.MOUSE_CLICKED, view);
            if (!x.compareAndSet(0, 1))
                if (!y.compareAndSet(0, 1))
                    if (!x.compareAndSet(1, 0))
                        if (!y.compareAndSet(1, 0)) ;
        });
    }

    public void initGridPane() {
        IntStream.range(0, 2).forEach(i -> fourPlayerPane.getColumnConstraints().add(new ColumnConstraints(PLAYERSIZE)));
        IntStream.range(0, 2).forEach(i -> fourPlayerPane.getRowConstraints().add(new RowConstraints(PLAYERSIZE)));
    }

    public TileView getBase() {
        return base;
    }

    public List<PlayerType> getPlayers() {
        return playersOnTile;
    }

    public void setCanMoveTo(boolean canMoveTo) {
        this.canMoveTo = canMoveTo;
        updateHighlight();
    }

    public void setCanDrain(boolean canDrain) {
        this.canDrain = canDrain;
        updateHighlight();
    }

    public boolean canDrain() {
        return canDrain;
    }
    public void setCanSandBagAndCardIndex(boolean canDrain, int index) {
        this.canSandBag = canDrain;
        this.cardIndex = index;
        updateHighlight();
    }

    public boolean canSandbag() {
        return canSandBag;
    }

    public boolean canMoveTo() {
        return canMoveTo;
    }

    public void dehighlightAll() {
        this.canDrain = false;
        this.canMoveTo = false;
        this.canSandBag = false;
        playersOnTile.forEach(type -> {
            PlayerImageView view = new PlayerImageView(this, type, TextureLoader.getPlayerIconTexture(type));
            view.dehighlight();
        });
        updateHighlight();
    }

    private void updateHighlight() {
        if (this.base == null) return;
        if (isHighlighted())
            this.base.highlight();
        else this.base.dehighlight();
    }

    public boolean isHighlighted() {
        return this.canDrain || this.canMoveTo || this.canSandBag;
    }

    public Point getPosition() {
        return position;
    }

    public InGameViewController getControl() {
        return control;
    }

    public boolean canCollectTreasure(PlayerType playerType) {
        Player player = control.getGameWindow().getControllerChan().getCurrentAction().getPlayer(playerType);
        if (playersOnTile.contains(playerType)) {
            return EnhancedPlayerHand.ofPlayer(player).getAmount(base.getType().getHidden()) >= 4;
        }
        return false;
    }

    public void highlightPlayerForPlayerType(PlayerType type) {
        if (playersOnTile.contains(type)) {
            PlayerImageView view = new PlayerImageView(this, type, TextureLoader.getPlayerIconTexture(type));
            view.highlight();
        }
    }

    @Override
    public void handle(MouseEvent event) {
        PlayerType activePlayerType = control.getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType();

        List<ActionButton> buttons = new LinkedList<>();
        if (canMoveTo)
            buttons.add(ActionButton.MOVE);
        if (canDrain && control.getTargetPlayer().getType() == activePlayerType)
            buttons.add(ActionButton.DRAIN);
        if(canSandBag){
            contextPicker.setCardIndex(cardIndex);
            buttons.add(ActionButton.SANDBAG);
        }
        if (buttons.size() > 0) {
            contextPicker.setDelegatingPlayer(activePlayerType);
            contextPicker.setMovingPlayer(control.getTargetPlayer().getType());
            contextPicker.init(buttons.toArray(new ActionButton[0]));
            contextPicker.show(event);
        }

    }

    public void setState(MapTileState state) {
        base.showImage(state);
    }

}
