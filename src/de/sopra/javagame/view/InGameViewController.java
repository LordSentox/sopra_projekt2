package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.Map;
import de.sopra.javagame.util.MapFull;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.DialogPack;
import de.sopra.javagame.view.abstraction.Notification;
import de.sopra.javagame.view.customcontrol.*;
import de.sopra.javagame.view.skin.WaterLevelSkin;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * GUI für den Spielablauf
 *
 * @author Lisa, Hannah
 */
public class InGameViewController extends AbstractViewController implements InGameViewAUI {

    private static final int ACTIVE_CARD_SIZE = 150;
    private static final int PASSIVE_CARD_SIZE = 110;
    private static final int ARTIFACT_SIZE = 100;
    private static final ColorAdjust DESATURATION = new ColorAdjust(0, -1, 0, 0);
    private static final int SPINNER_SIZE = 250;

    private List<Point> drainablePoints = new ArrayList<>();
    private List<Point> movePoints = new ArrayList<>();
    private boolean specialActive = false;
    private Supplier<Player> targetPlayer;

    //mal dem ganzen current-kram zwischenspeichern
    @FXML
    MapPane mapPane;
    @FXML
    WaterLevelView waterLevelView;
    @FXML
    GridPane cardGridPane, handOneCardGridPane, handTwoCardGridPane, handThreeCardGridPane, artifactCardDrawStackGridPane,
            artifactCardDiscardGridPane, floodCardDrawStackGridPane, floodCardDiscardGridPane;
    @FXML
    Button endTurnButton;
    @FXML
    ImageView mainPane, activePlayerTypeImageView, playerOneTypeImageView, playerTwoTypeImageView, playerThreeTypeImageView,
            fireArtefactImageView, waterArtefactImageView, earthArtefactImageView, airArtefactImageView, turnSpinnerWithoutMarkerImageView, markerForSpinnerImageView;
    private Timeline timeline;

    public void init() {
        waterLevelView.setSkin(new WaterLevelSkin(waterLevelView));
        waterLevelView.setProgress(7);

        /* Set Background */
        mainPane.setImage(TextureLoader.getBackground());
        mainPane.setFitHeight(1200);
        mapPane.setIngameViewController(this);

        /* Turn Marker */
        turnSpinnerWithoutMarkerImageView.setImage(TextureLoader.getTurnSpinner());
        turnSpinnerWithoutMarkerImageView.setFitWidth(SPINNER_SIZE);
        turnSpinnerWithoutMarkerImageView.getStyleClass().add("CardView");
        markerForSpinnerImageView.setImage(TextureLoader.getSpinnerMarker());
        markerForSpinnerImageView.setFitWidth(SPINNER_SIZE);
        markerForSpinnerImageView.getStyleClass().add("CardView");

        initGridPane();
        initPlayerHands();
        initArtifactsFound();

        //debug
//        refreshWaterLevel(4);

        //setze Timeline für Replays
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            getGameWindow().getControllerChan().getGameFlowController().redo();
            refreshSome();
        }));

        resetTargetPlayer();

    }

    private void initArtifactsFound() {
        fireArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.FIRE));
        fireArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        fireArtefactImageView.getStyleClass().add("Artifact_Fire");

        waterArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.WATER));
        waterArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        waterArtefactImageView.getStyleClass().add("Artifact_Water");

        earthArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.EARTH));
        earthArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        earthArtefactImageView.getStyleClass().add("Artifact_Earth");

        airArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.AIR));
        airArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        airArtefactImageView.getStyleClass().add("Artifact_Air");
    }

    private void initGridPane() {
        IntStream.range(0, 9).forEach(item -> cardGridPane.getColumnConstraints().add(new ColumnConstraints(item % 2 == 0 ? ACTIVE_CARD_SIZE : 5)));

        IntStream.range(0, 5).forEach(item -> {
            handOneCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2));
            handTwoCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2));
            handThreeCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2));
        });

        IntStream.range(0, 28).forEach(item -> {
            artifactCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1));
            artifactCardDiscardGridPane.getColumnConstraints().add(new ColumnConstraints(1));
        });

        IntStream.range(0, 24).forEach(item -> {
            floodCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1));
            floodCardDiscardGridPane.getColumnConstraints().add(new ColumnConstraints(1));
        });
    }

    private void initPlayerHands() {
        activePlayerTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        playerOneTypeImageView.setFitWidth(PASSIVE_CARD_SIZE);
        playerTwoTypeImageView.setFitWidth(PASSIVE_CARD_SIZE);
        playerThreeTypeImageView.setFitWidth(PASSIVE_CARD_SIZE);
    }

    public void rotateTurnSpinner(double degree) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1));
        rotateTransition.setToAngle(degree);
        rotateTransition.setNode(turnSpinnerWithoutMarkerImageView);
        rotateTransition.play();
    }

    public void onPlayerSelected() {

    }

    public void onTransferCardClicked(int cardIndex) {

    }

    public void onFloodDiscardPileToggled() {

    }

    public void onArtifactDiscardPileToggled() {

    }

    public void onEndTurnClicked() {
        getGameWindow().getControllerChan().getActivePlayerController().endActionPhase();
    }

    public void onSpecialCardPlayClicked(int cardIndex) {

    }

    public void onDiscardSelectedCardsClicked() {

    }

    public void onRedoClicked() {
        getGameWindow().getControllerChan().getGameFlowController().redo();
    }

    public void onUndoClicked() {
        //TODO Fenster öffnen, das Bescheid gibt über Löschen aus HighScoreListe
        getGameWindow().getControllerChan().getGameFlowController().undo();
    }

    public void onPlayClicked() {
        ControllerChan controllerChan = getGameWindow().getControllerChan();
        controllerChan.replayGame("");
        //Alle 10 Sekunden einen Zug Wiederholen bis das Spiel beendet ist
        timeline.setCycleCount(controllerChan.getJavaGame().getRedoStackSize());
        timeline.setOnFinished(event -> setIsReplayWindow(false));
        timeline.play();
    }

    public void onPauseClicked() {
//        timeline.pause();
        //DEBUG

        MapFull map = getGameWindow().getControllerChan().getCurrentAction().getMap();
        map.forEach(mapTile -> System.out.println(mapTile.getState()));
    }

    public void onSettingsClicked() {
        try {
            SettingsViewController.openModal(getGameWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //changeState(ViewState.IN_GAME, ViewState.IN_GAME_SETTINGS);
    }

    public void onArtifactCardDiscardStackClicked() {

    }

    public void onFloodCardDiscardStackClicked() {

    }

    public void onArtifactCardDrawStackClicked() {

    }

    public void onFloodCardDrawStackClicked() {
        if (getGameWindow().getControllerChan().getCurrentAction().getState() == TurnState.FLOOD)
            this.getGameWindow().getControllerChan().getGameFlowController().drawFloodCard();        
    }

    public void setFloodCardStackHighlighted(boolean highlight) {
        ObservableList<String> styleClass = floodCardDrawStackGridPane.getStyleClass();
        if (!styleClass.contains(HIGHLIGHT) && highlight)
            styleClass.add(HIGHLIGHT);
        else if (!highlight)
            styleClass.removeIf(s -> s.equals(HIGHLIGHT));
    }

    @Override
    public void showNotification(Notification notification) {
        if (notification.isError()) {
            DialogPack pack = new DialogPack(getGameWindow().getMainStage(), "", "Es ist ein Fehler aufgetreten: ", notification.message());
            pack.setAlertType(Alert.AlertType.ERROR);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
        } else if (notification.isGameWon()) {
            //TODO
        } else if (notification.isGameLost()) {
            //TODO
        } else if (notification.hasMessage()) {
            DialogPack pack = new DialogPack(getGameWindow().getMainStage(), "", "Das Spiel informiert:", notification.message());
            pack.setAlertType(Alert.AlertType.INFORMATION);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
            System.out.println("info: " + notification.message());
        }
    }

    @Override
    public void refreshSome() {
        refreshArtifactsFound();
        refreshActivePlayer();
        refreshArtifactStack(getGameWindow().getControllerChan().getCurrentAction().getArtifactCardStack());
        refreshFloodStack(getGameWindow().getControllerChan().getCurrentAction().getFloodCardStack());
        //TODO mapPane darf nur noch vollständig refresht werdne, nicht neu gebaut ´!
        //mapPane.buildMap(getGameWindow().getControllerChan().getCurrentAction().getMap());
        this.refreshTurnState(getGameWindow().getControllerChan().getCurrentAction().getState());

        //Always fix docs in InGameViewAUI if you change this
//        refreshHand(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType(), Arrays.asList(new ArtifactCard[]{new ArtifactCard(ArtifactCardType.AIR)}));
//        mapPane.movePlayer(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getPosition(), getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType());
    }

    public void resetHighlighting() {
        movePoints = new LinkedList<>();
        drainablePoints = new LinkedList<>();
        setFloodCardStackHighlighted(false);
        mapPane.forEach(tile -> tile.dehighlightAll());
    }

    @Override
    public void refreshMovementOptions(List<Point> points) {
        movePoints.forEach(point -> mapPane.getMapStackPane(point).setCanMoveTo(false));
        movePoints = points;
        points.forEach(point -> {
            mapPane.getMapStackPane(point).setCanMoveTo(true);
            System.out.println("42 "+mapPane.getMapStackPane(point).canMoveTo());
        });
    }

    @Override
    public void refreshDrainOptions(List<Point> points) {
        drainablePoints.forEach(point -> mapPane.getMapStackPane(point).setCanDrain(false));
        drainablePoints = points;
        points.forEach(point -> mapPane.getMapStackPane(point).setCanDrain(true));
    }

    @Override
    public void refreshCardsTransferable(boolean transferable) {
        //FIXME
//        if (transferable) {
//            List<ArtifactCardView> cardsTohighLight = cardGridPane.getChildren().stream().map(node -> (ArtifactCardView) node)
//                    .filter(cardView -> (cardView.getType().equals(ArtifactCardType.HELICOPTER) || cardView.getType().equals(ArtifactCardType.SANDBAGS) || cardView.getType().equals(ArtifactCardType.WATERS_RISE))).collect(Collectors.toList());
//            cardsTohighLight.forEach(view -> view.getStyleClass().add(HIGHLIGHT));
//        }
    }

    @Override
    public void refreshWaterLevel(int level) {
        waterLevelView.setProgress(level);
        System.out.println("uwu");
    }

    @Override
    public void refreshHand(PlayerType player, List<ArtifactCard> cards) {
        if (getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType() == player) {
            cardGridPane.getChildren().clear();
            int index = 0;
            for (ArtifactCard card : cards) {
                ArtifactCardView v = new ArtifactCardView(card.getType(), ACTIVE_CARD_SIZE);
                v.showFrontImage();
                cardGridPane.getChildren().add(v);
                GridPane.setConstraints(v, index, 0);
                index += 2;
            }
        } else {
            Action action = getGameWindow().getControllerChan().getCurrentAction();
            List<Player> players = action.getPlayers();
            GridPane pane = null;

            if (players.get((action.getActivePlayerIndex() + 1) % players.size()).getType().equals(player))
                pane = handOneCardGridPane;

            else if (players.get((action.getActivePlayerIndex() + 2) % players.size()).getType().equals(player))
                pane = handTwoCardGridPane;

            else if (players.get((action.getActivePlayerIndex() + 3) % players.size()).getType().equals(player))
                pane = handThreeCardGridPane;


            pane.getChildren().clear();
            int index = 0;
            for (ArtifactCard card : cards) {
                CardView v = new ArtifactCardView(card.getType(), PASSIVE_CARD_SIZE);
                v.showFrontImage();
                pane.getChildren().add(v);
                GridPane.setConstraints(v, index++, 0);
            }
        }
    }

    private void onSpecialCardClicked(ArtifactCardView card, int index) {
        if (card.getType().equals(ArtifactCardType.HELICOPTER)) {

        } else if (card.getType().equals(ArtifactCardType.SANDBAGS)) {

        }
    }

    @Override
    public void refreshArtifactsFound() {
        EnumSet<ArtifactType> artifacts = this.getGameWindow().getControllerChan().getCurrentAction().getDiscoveredArtifacts();

        fireArtefactImageView.setEffect(artifacts.contains(ArtifactType.FIRE) ? null : DESATURATION);
        waterArtefactImageView.setEffect(artifacts.contains(ArtifactType.WATER) ? null : DESATURATION);
        earthArtefactImageView.setEffect(artifacts.contains(ArtifactType.EARTH) ? null : DESATURATION);
        airArtefactImageView.setEffect(artifacts.contains(ArtifactType.AIR) ? null : DESATURATION);

        System.out.println(artifacts.contains(ArtifactType.FIRE));
        System.out.println(artifacts.contains(ArtifactType.WATER));
        System.out.println(artifacts.contains(ArtifactType.EARTH));
        System.out.println(artifacts.contains(ArtifactType.AIR));
    }

    @Override
    public void refreshArtifactStack(CardStack<ArtifactCard> stack) {
        artifactCardDrawStackGridPane.getChildren().clear();
        for (int i = 0; i < stack.size(); i += 2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showBackImage();
            artifactCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }

        List<ArtifactCard> discardPile = stack.getDiscardPile();
        int index = 0;
        artifactCardDiscardGridPane.getChildren().clear();
        for (ArtifactCard card : discardPile) {
            CardView v = new ArtifactCardView(card.getType(), ACTIVE_CARD_SIZE);
            v.showFrontImage();
            artifactCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, index, 0);
            index += 2;
        }
    }

    @Override
    public void refreshFloodStack(CardStack<FloodCard> stack) {
        floodCardDrawStackGridPane.getChildren().clear();
        for (int i = 0; i < stack.size(); i += 2) {
            CardView v = new FloodCardView(MapTileProperties.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showBackImage();
            v.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> onFloodCardDrawStackClicked());
            floodCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        List<FloodCard> discardPile = stack.getDiscardPile();
        int index = 0;
        floodCardDiscardGridPane.getChildren().clear();
        for (FloodCard card : discardPile) {
            CardView v = new FloodCardView(card.getTile(), ACTIVE_CARD_SIZE);
            v.showFrontImage();
            floodCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, index, 0);
            index += 2;
        }
    }

    @Override
    public void refreshPlayerPosition(Point position, PlayerType player) {
        mapPane.movePlayer(position, player);
        resetHighlighting();
    }
    
    //TODO neue refresh map einbauen

    @Override
    public void refreshMapTile(Point position, MapTile tile) {
        //TODO ersetzen durch echten refresh, nicht nur das neu setzen des states
        mapPane.setMapTile(position, tile);
    }

    @Override
    public void refreshActivePlayer() {
        Action action = this.getGameWindow().getControllerChan().getCurrentAction();
        List<Player> players = action.getPlayers();

        activePlayerTypeImageView.setImage(TextureLoader.getPlayerCardTexture(action.getActivePlayer().getType()));
        playerOneTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 1) % players.size()).getType()));
        if (players.size() >= 3) {
            playerTwoTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 2) % players.size()).getType()));
        }
        if (players.size() == 4) {
            playerThreeTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 3) % players.size()).getType()));
        }
        resetHighlighting();
    }

    @Override
    public void refreshActionsLeft(int actionsLeft) {
        System.out.println(actionsLeft);
        switch (actionsLeft) {
            case 3:
                this.rotateTurnSpinner(0);
                break;
            case 2:
                this.rotateTurnSpinner(-60);
                break;
            case 1:
                this.rotateTurnSpinner(-120);
                break;
            case 0:
                this.rotateTurnSpinner(-180);
                break;
            default:
                this.rotateTurnSpinner(0);
                break;
        }
    }

    public Player getTargetPlayer() {
        return targetPlayer.get();
    }

    public void resetTargetPlayer() {
        targetPlayer = () -> getGameWindow().getControllerChan().getCurrentAction().getActivePlayer();
    }

    public void setTargetPlayer(PlayerType type) {
        targetPlayer = () -> getGameWindow().getControllerChan().getCurrentAction().getPlayer(type);
    }

    @Override
    public void refreshPlayerName(String name, PlayerType player) {

    }

    @Override
    public void setIsReplayWindow(boolean replay) {

    }

    @Override
    public void showTip(ActionQueue queue) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refreshTurnState(TurnState turnState) {
        resetHighlighting();
        switch (turnState) {
            case PLAYER_ACTION:
                this.rotateTurnSpinner(0);
                break;
            case DRAW_ARTIFACT_CARD:
                this.rotateTurnSpinner(-240);
                break;
            case FLOOD:
                this.rotateTurnSpinner(-300);
                setFloodCardStackHighlighted(true);
                break;
            default:
                this.rotateTurnSpinner(0);
                break;
        }

    }

    @Override
    public void refreshHopefullyAll(Action action) {
        resetHighlighting();
        resetTargetPlayer();
        setSpecialActive(false);
        //refreshArtifacts found
        EnumSet<ArtifactType> artifacts = action.getDiscoveredArtifacts();
        fireArtefactImageView.setEffect(artifacts.contains(ArtifactType.FIRE) ? null : DESATURATION);
        waterArtefactImageView.setEffect(artifacts.contains(ArtifactType.WATER) ? null : DESATURATION);
        earthArtefactImageView.setEffect(artifacts.contains(ArtifactType.EARTH) ? null : DESATURATION);
        airArtefactImageView.setEffect(artifacts.contains(ArtifactType.AIR) ? null : DESATURATION);
        //refresh active player
        List<Player> players = action.getPlayers();
        activePlayerTypeImageView.setImage(TextureLoader.getPlayerCardTexture(action.getActivePlayer().getType()));
        playerOneTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 1) % players.size()).getType()));
        if (players.size() >= 3) {
            playerTwoTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 2) % players.size()).getType()));
        }
        if (players.size() == 4) {
            playerThreeTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 3) % players.size()).getType()));
        }
        //refreshes
        mapPane.buildMap(action.getMap());
        refreshArtifactStack(action.getArtifactCardStack());
        refreshFloodStack(action.getFloodCardStack());
        refreshTurnState(action.getState());
        refreshActionsLeft(action.getActivePlayer().getActionsLeft());
        action.getPlayers().forEach(player ->
        {
            refreshHand(player.getType(), player.getHand());
            refreshPlayerName(player.getName(), player.getType());
            mapPane.movePlayer(player.getPosition(), player.getType());
        });
        refreshWaterLevel(action.getWaterLevel().getLevel());
    }

    public boolean isSpecialActive() {
        return specialActive;
    }

    public void setSpecialActive(boolean specialActive) {
        this.specialActive = specialActive;
    }

}
