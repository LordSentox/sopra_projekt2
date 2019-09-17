package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.*;
import de.sopra.javagame.view.skin.WaterLevelSkin;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;
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
    private boolean specialActive =  false;
    //mal dem ganzen current-kram zwischenspeichern
    @FXML
    MapPane mapPane;
    @FXML
    WaterLevelView waterLevelView;
    @FXML
    GridPane cardGridPane, handOneCardGridPane, handTwoCardGridPane, handThreeCardGridPane, artifactCardDrawStackGridPane,
            artifactCardDicardGridPane, floodCardDrawStackGridPane, floodCardDiscardGridPane;
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
        refreshWaterLevel(4);

        //setze Timeline für Replays
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            getGameWindow().getControllerChan().getGameFlowController().redo();
            refreshAll();
        }));
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
            artifactCardDicardGridPane.getColumnConstraints().add(new ColumnConstraints(1));
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
        getGameWindow().getControllerChan().getActivePlayerController().endTurn();
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
        timeline.pause();
    }

    public void onSettingsClicked() {
        changeState(ViewState.IN_GAME_SETTINGS);
    }

    public void onArtifactCardDiscardStackClicked() {

    }

    public void onFloodCardDiscardStackClicked() {

    }

    public void onArtifactCardDrawStackClicked() {

    }

    public void onFloodCardDrawStackClicked() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void show(Stage stage) {

    }

    @Override
    public void showNotification(String notification) {

    }

    @Override
    public void refreshAll() {
        refreshArtifactsFound();
        refreshActivePlayer();
        refreshArtifactStack(getGameWindow().getControllerChan().getCurrentAction().getArtifactCardStack());
        refreshFloodStack(getGameWindow().getControllerChan().getCurrentAction().getFloodCardStack());
        mapPane.buildMap(getGameWindow().getControllerChan().getCurrentAction().getMap());

        this.rotateTurnSpinner(72.0);
        this.refreshTurnState(getGameWindow().getControllerChan().getCurrentAction().getState());
        refreshHand(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType(), Arrays.asList(new ArtifactCard[]{new ArtifactCard(ArtifactCardType.AIR)}));
        mapPane.movePlayer(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getPosition(), getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType());
    }

    @Override
    public void refreshMovementOptions(List<Point> points) {
        movePoints.forEach(point -> mapPane.getMapStackPane(point).setCanMoveTo(false));
        movePoints = points;
        points.forEach(point -> mapPane.getMapStackPane(point).setCanMoveTo(true));
    }

    @Override
    public void refreshDrainOptions(List<Point> points) {
        drainablePoints.forEach(point -> mapPane.getMapStackPane(point).setCanDrain(false));
        drainablePoints = points;
        points.forEach(point -> mapPane.getMapStackPane(point).setCanDrain(true));
    }

    @Override
    public void refreshCardsTransferable(boolean transferable) {
        if(transferable) {
             List<ArtifactCardView> cardsTohighLight = cardGridPane.getChildren().stream().map(node -> (ArtifactCardView) node)
                .filter(cardView -> (cardView.getType().equals(ArtifactCardType.HELICOPTER) || cardView.getType().equals(ArtifactCardType.SANDBAGS) ||cardView.getType().equals(ArtifactCardType.WATERS_RISE))).collect(Collectors.toList());
             cardsTohighLight.forEach(view -> view.getStyleClass().add("highlightmapTile"));
        }
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
                CardView v = new ArtifactCardView(card.getType(), ACTIVE_CARD_SIZE);
                if(card.getType().equals(ArtifactCardType.HELICOPTER) || card.getType().equals(ArtifactCardType.SANDBAGS))
                {
                    final int newIndex = index;
                    v.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> onSpecialCardClicked((ArtifactCardView)v, newIndex));
                }
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
            if (players.get((action.getActivePlayerIndex() + 2) % players.size()).getType().equals(player))
                pane = handTwoCardGridPane;
            if (players.get((action.getActivePlayerIndex() + 3) % players.size()).getType().equals(player))
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
        // TODO Auto-generated method stub
        if(card.getType().equals(ArtifactCardType.HELICOPTER)){
            
        } else if(card.getType().equals(ArtifactCardType.SANDBAGS)){
            
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
        artifactCardDicardGridPane.getChildren().clear();
        for (ArtifactCard card : discardPile) {
            CardView v = new ArtifactCardView(card.getType(), ACTIVE_CARD_SIZE);
            v.showFrontImage();
            artifactCardDicardGridPane.getChildren().add(v);
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
        movePoints.forEach(point -> mapPane.getMapStackPane(position).dehighlight());
        movePoints = new ArrayList<>();
        drainablePoints.forEach(point -> mapPane.getMapStackPane(position).dehighlight());
        drainablePoints = new ArrayList<>();
        mapPane.movePlayer(position, player);
    }

    @Override
    public void refreshMapTile(Point position, MapTile tile) {
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
    }

    @Override
    public void refreshActionsLeft(int actionsLeft) {
        switch (actionsLeft) {
            case 1:
                this.rotateTurnSpinner(-144);
                break;
            case 2:
                this.rotateTurnSpinner(-72);
                break;
            case 3:
                this.rotateTurnSpinner(0);
                break;
            default:
                this.rotateTurnSpinner(0);
                break;
        }
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
        switch (turnState) {
        case PLAYER_ACTION:
            this.rotateTurnSpinner(0);
            break;
        case DRAW_ARTIFACT_CARD:
            this.rotateTurnSpinner(-216);
            break;
        case FLOOD:
            this.rotateTurnSpinner(-288);
            this.getGameWindow().getControllerChan().getGameFlowController().drawFloodCards();
            break;
        default:
            this.rotateTurnSpinner(0);
            break;
    }
        
    }

    public boolean isSpecialActive() {
        return specialActive;
    }

    public void setSpecialActive(boolean specialActive) {
        this.specialActive = specialActive;
    }

}
