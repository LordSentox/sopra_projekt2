package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.control.GameFlowController;
import de.sopra.javagame.control.ai.SimpleAction;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.view.abstraction.*;
import de.sopra.javagame.view.customcontrol.*;
import de.sopra.javagame.view.skin.WaterLevelSkin;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.sopra.javagame.util.DebugUtil.debug;
//Todo: Todo fixen
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
    private boolean transferActive = false;
    private Supplier<Player> targetPlayer;
    private int floodCardDiscardPileSize = 24;

    // Wird gesetzt, wenn eine Helikopterkarte gespielt werden soll
    private HelicopterHelper helicopterHelper;

    // Kümmert sich darum, dass Spieler gerettet werden können.
    private RescueHelper rescueHelper;

    //mal dem ganzen current-kram zwischenspeichern
    @FXML
    MapPane mapPane;
    @FXML
    WaterLevelView waterLevelView;
    @FXML
    GridPane cardGridPane, handOneCardGridPane, handTwoCardGridPane, handThreeCardGridPane, artifactCardDrawStackGridPane,
            artifactCardDiscardGridPane, floodCardDrawStackGridPane, floodCardDiscardGridPane;
    @FXML
    Button endTurnButton, floodCardDiscardStackButton, artifactCardDiscardStackButton;
    @FXML
    ImageView mainPane, activePlayerTypeImageView, playerOneTypeImageView, playerTwoTypeImageView, playerThreeTypeImageView,
            fireArtefactImageView, waterArtefactImageView, earthArtefactImageView, airArtefactImageView, turnSpinnerWithoutMarkerImageView, markerForSpinnerImageView;
    @FXML
    Label roundNumber;
    private Timeline timeline;



    public void init() {
        getGameWindow().getSettings().getMusicVolume().addListener((x, oldVal, newVal) -> getGameWindow().dorfPlayer.volumeProperty().set(newVal.doubleValue() / 100.0));

        this.helicopterHelper = null;
        this.rescueHelper = new RescueHelper(this);
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
        IntStream.range(0, 6).forEach(item -> cardGridPane.getColumnConstraints().add(new ColumnConstraints(ACTIVE_CARD_SIZE - 50)));

        IntStream.range(0, 5).forEach(item -> {
            handOneCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2));
            handTwoCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2));
            handThreeCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2));
        });

        IntStream.range(0, 280).forEach(item -> {
            artifactCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1));
            artifactCardDiscardGridPane.getColumnConstraints().add(new ColumnConstraints(1));
        });

        IntStream.range(0, 24).forEach(item -> {
            floodCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1));
        });
        IntStream.range(0, floodCardDiscardPileSize + 24).forEach(item -> {
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
        getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().setActionsLeft(0);
        refreshActionsLeft(0);
        getGameWindow().getControllerChan().getActivePlayerController().endActionPhase();
        setFloodCardStackHighlighted(true);
    }

    public void onRedoClicked() {
        getGameWindow().getControllerChan().getGameFlowController().redo();
    }

    public void onMaybeGetHintClicked() {
        if (getGameWindow().getControllerChan().getJavaGame().getIsCheetah()) {
            getHint();
        } else {
            DialogPack pack = new DialogPack(getGameWindow().getMainStage(),
                    null,
                    "Möchtest du dir wirklich einen Tipp anzeigen lassen?",
                    "Du wirst dann mit diesem Spiel für immer \n"
                            + "aus der Highscore-Liste verbannt!");
            pack.addButton("Tipp zeigen", this::getHint);
            pack.addButton("Abbrechen", () -> {
            });
            pack.setAlertType(AlertType.CONFIRMATION);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
        }
    }

    private void getHint() {
        SimpleAction tip = getGameWindow().getControllerChan().getAiController().getTip();
        getGameWindow().getControllerChan().getInGameViewAUI().showTip(tip);
    }

    public void onMaybeUndoClicked() {
        if (getGameWindow().getControllerChan().getJavaGame().getIsCheetah()) {
            undo();
        } else {
            DialogPack pack = new DialogPack(getGameWindow().getMainStage(),
                    null,
                    "Möchtest du einen Zug rückgängig machen?",
                    "Du wirst dann mit diesem Spiel für immer \n"
                            + "aus der Highscore-Liste verbannt!");
            pack.addButton("Rückgängig machen", this::undo);
            pack.addButton("Abbrechen", () -> {
            });
            pack.setAlertType(AlertType.CONFIRMATION);
            pack.setStageStyle(StageStyle.UNDECORATED);
            pack.open();
        }
    }

    private void undo() {
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
        map.forEach(mapTile -> debug(mapTile.getState().name()));

        //debug for AI tip
//        getGameWindow().getControllerChan().getActivePlayerController().showTip(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer());

    }

    public void onSettingsClicked() {
        getGameWindow().dorfPlayer.play();

        try {
            InGameSettingsViewController.openModal(getGameWindow());
            getGameWindow().pauseBgm();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //changeState(ViewState.IN_GAME, ViewState.IN_GAME_SETTINGS);
    }

    public void onArtifactCardDiscardStackClicked() {
        showNotification(this.getGameWindow().getControllerChan().getCurrentAction().getArtifactCardStack().getDiscardPile().stream().map(card -> card.getType().name()).collect(Collectors.joining("\n")));

    }

    public void onSpecialCardClicked(ArtifactCardType card, int index, PlayerType owner) {
        if (card.equals(ArtifactCardType.HELICOPTER)) {
            // Initialisieren des Helicopter-Helpers
            this.helicopterHelper = new HelicopterHelper(owner, index, this.mapPane);
        } else if (card.equals(ArtifactCardType.SANDBAGS)) {
            List<Point> drainable = new ArrayList<>();
            MapFull map = getGameWindow().getControllerChan().getCurrentAction().getMap();
            map.forEach(mapTile -> {
                if (mapTile.getState().equals(MapTileState.FLOODED)) {
                    drainable.add(map.getPositionForTile(mapTile.getProperties()));
                }
            });
            debug("on card clicked: " + card + " " + index + " " + owner);
            drainable.forEach(point -> mapPane.getMapStackPane(point).setCanSandBagAndCardIndex(true, index));
        }
    }

    public void onFloodCardDiscardStackClicked() {
        showNotification(this.getGameWindow().getControllerChan().getCurrentAction().getFloodCardStack().getDiscardPile().stream().map(card -> card.getTile().getName()).collect(Collectors.joining("\n")));
    }

    public void onArtifactCardDrawStackClicked() {
    }

    public void onFloodCardDrawStackClicked() {
        if ((getGameWindow().getControllerChan().getCurrentAction() != null &&
                getGameWindow().getControllerChan().getCurrentAction().getState() == TurnState.FLOOD)) {
            this.getGameWindow().getControllerChan().getGameFlowController().drawFloodCard();
            if (getGameWindow().getControllerChan().getCurrentAction() != null) {
                refreshHopefullyAll(getGameWindow().getControllerChan().getCurrentAction());
            }
        }
    }

    public void setFloodCardStackHighlighted(boolean highlight) {
        ObservableList<String> styleClass = floodCardDrawStackGridPane.getStyleClass();
        if (!styleClass.contains(HIGHLIGHT) && highlight)
            styleClass.add(HIGHLIGHT);
        else if (!highlight)
            styleClass.removeIf(s -> s.equals(HIGHLIGHT));
        if (getGameWindow().getControllerChan().getCurrentAction() != null) {
        refreshFloodStack(getGameWindow().getControllerChan().getCurrentAction().getFloodCardStack());
        }
    }

    @Override
    public void showNotification(Notification notification) {
        String header = "";
        String confirmationButtonText = "Spielstand speichern";
        String cancelButtonText = "Zurück ins Hauptmenü";
        if (!getGameWindow().getControllerChan().getCurrentAction().isGameEnded()) {
            super.showNotification(notification);
            return;
        }
        if (notification.isGameWon()) {
            header = "Herzlichen Glückwunsch! Ihr habt die Insel besiegt.";
            getGameWindow().stopBgm();
            getGameWindow().dorfPlayer.play();
        } else if (notification.isGameLost()) {
            header = "Ihr habt leider verloren!";
            getGameWindow().stopBgm();
            getGameWindow().ripPlayer.play();
        }
       getGameWindow().getControllerChan().getAiController().setActive(false);
        DialogPack endGameDialogue = new DialogPack(getGameWindow().getMainStage(), "", header, notification.message());
        endGameDialogue.setAlertType(AlertType.CONFIRMATION);
        endGameDialogue.setStageStyle(StageStyle.UNDECORATED);
        endGameDialogue.addButton(confirmationButtonText, () -> {
            openSaveDialogue();
            getGameWindow().dorfPlayer.stop();
        });
        endGameDialogue.addButton(cancelButtonText, () -> {
            endGameBackToMenu();
            getGameWindow().dorfPlayer.stop();
        });
        endGameDialogue.open();
    }

    private void openSaveDialogue()  {
        ((SettingsViewController) getGameWindow().getView(ViewState.SETTINGS)).init();
        ((InGameViewController) getGameWindow().getView(ViewState.IN_GAME)).init();
        changeState(ViewState.IN_GAME, ViewState.SAVE_GAME);
    }

    private void endGameBackToMenu() {
        ((InGameViewController) getGameWindow().getView(ViewState.IN_GAME)).init();
        changeState(ViewState.IN_GAME, ViewState.MENU);
    }

    @Override
    public void refreshSome() {
        refreshArtifactsFound();
        refreshActivePlayer();
        refreshArtifactStack(getGameWindow().getControllerChan().getCurrentAction().getArtifactCardStack());
        refreshFloodStack(getGameWindow().getControllerChan().getCurrentAction().getFloodCardStack());
        //TODO mapPane darf nur noch vollständig refresht werdne, nicht neu gebaut ´!
        mapPane.buildMap(getGameWindow().getControllerChan().getCurrentAction().getMap());
        this.refreshTurnState(getGameWindow().getControllerChan().getCurrentAction().getState());
        List<Player> list = getGameWindow().getControllerChan().getCurrentAction().getPlayers();
        list.forEach(player -> this.refreshPlayerPosition(player.getPosition(), player.getType()));

        //Always fix docs in InGameViewAUI if you change this
//        refreshHand(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType(), Arrays.asList(new ArtifactCard[]{new ArtifactCard(ArtifactCardType.AIR)}));
//        mapPane.movePlayer(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getPosition(), getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType());
    }

    public void resetHighlighting() {
        movePoints = new LinkedList<>();
        drainablePoints = new LinkedList<>();
        setFloodCardStackHighlighted(false);
        mapPane.forEach(MapPaneTile::dehighlightAll);
    }

    @Override
    public void refreshMovementOptions(List<Point> points) {
        movePoints.forEach(point -> mapPane.getMapStackPane(point).setCanMoveTo(false));
        movePoints = new ArrayList<>(points);
        points.forEach(point -> mapPane.getMapStackPane(point).setCanMoveTo(true));
    }

    @Override
    public void refreshDrainOptions(List<Point> points) {
        drainablePoints.forEach(point -> mapPane.getMapStackPane(point).setCanDrain(false));
        drainablePoints = new ArrayList<>(points);
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
        debug("uwu");
    }

    @Override
    public void refreshHand(PlayerType player, List<ArtifactCard> cards) {

        if (getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType() == player) {
            cardGridPane.getChildren().clear();
            int index = 0;
            for (ArtifactCard card : cards) {
                ArtifactCardView v = new ArtifactCardView(card.getType(), ACTIVE_CARD_SIZE, index);
                v.showFrontImage();
                v.setInGameViewController(this);
                v.setOwner(player);
                if (transferActive) v.setTradeable(transferActive);
                cardGridPane.getChildren().add(v);
                GridPane.setConstraints(v, index, 0);
                index++;
            }
        } else {
            Action action = getGameWindow().getControllerChan().getCurrentAction();
            List<Player> players = action.getPlayers();
            GridPane pane = null;

            if (players.get((action.getActivePlayerIndex() + 1) % players.size()).getType().equals(player)) {
                pane = handOneCardGridPane;
            } else if (players.get((action.getActivePlayerIndex() + 2) % players.size()).getType().equals(player)) {
                pane = handTwoCardGridPane;
            } else if (players.get((action.getActivePlayerIndex() + 3) % players.size()).getType().equals(player)) {
                pane = handThreeCardGridPane;
            }


            pane.getChildren().clear();
            int index = 0;
            for (ArtifactCard card : cards) {
                ArtifactCardView v = new ArtifactCardView(card.getType(), PASSIVE_CARD_SIZE, index);
                v.showFrontImage();
                v.setInGameViewController(this);
                v.setOwner(player);
                pane.getChildren().add(v);
                GridPane.setConstraints(v, index++, 0);
            }
        }
    }

    @Override
    public void refreshArtifactsFound() {
        EnumSet<ArtifactType> artifacts = this.getGameWindow().getControllerChan().getCurrentAction().getDiscoveredArtifacts();

        highlightArtifacts(artifacts);
    }

    @Override
    public void refreshArtifactStack(CardStack<ArtifactCard> stack) {
        artifactCardDrawStackGridPane.getChildren().clear();
        for (int i = 0; i < stack.size(); i += 2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE, -1);
            v.showBackImage();
            artifactCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }

        List<ArtifactCard> discardPile = stack.getDiscardPile();
        int index = 0;
        artifactCardDiscardGridPane.getChildren().clear();
        for (ArtifactCard card : discardPile) {
            CardView v = new ArtifactCardView(card.getType(), ACTIVE_CARD_SIZE, -1);
            v.showFrontImage();
            artifactCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, index, 0);
            index++;
            this.artifactCardDiscardStackButton.toFront();
        }
    }

    @Override
    public void refreshFloodStack(CardStack<FloodCard> stack) {
        floodCardDrawStackGridPane.getChildren().clear();
        for (int i = 0; i < stack.size(); i += 2) {
            CardView v = new FloodCardView(MapTileProperties.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showBackImage();
            v.addEventFilter(MouseEvent.MOUSE_CLICKED, (event) -> {onFloodCardDrawStackClicked();});
            floodCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        List<FloodCard> discardPile = stack.getDiscardPile();
        int index = 0;
        floodCardDiscardPileSize = discardPile.size();
        floodCardDiscardGridPane.getChildren().clear();
        for (FloodCard card : discardPile) {
            CardView v = new FloodCardView(card.getTile(), ACTIVE_CARD_SIZE);
            v.showFrontImage();
            floodCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, index, 0);
            index++;
            this.floodCardDiscardStackButton.toFront();
        }
    }

    @Override
    public void refreshPlayerPosition(Point position, PlayerType player) {
        mapPane.movePlayer(position, player);
        refreshTurnState(getGameWindow().getControllerChan().getCurrentAction().getState());
    }

    //TODO neue refresh map einbauen
    public void refreshMap(MapFull map) {
        map.forEach(maptile -> refreshMapTile(map.getPositionForTile(maptile.getProperties()), maptile));
    }

    @Override
    public void refreshMapTile(Point position, MapTile tile) {
        //TODO ersetzen durch echten refresh, nicht nur das neu setzen des states
        mapPane.setMapTile(position, tile);
    }

    @Override
    public void refreshActivePlayer() {
        Action action = this.getGameWindow().getControllerChan().getCurrentAction();
        refreshPlayerCardImages(action);
        refreshTurnState(getGameWindow().getControllerChan().getCurrentAction().getState());

        roundNumber.setText("Runde: " + getGameWindow().getControllerChan().getJavaGame().numTurns());
    }

    @Override
    public void refreshActionsLeft(int actionsLeft) {
        debug("remaining actions: " + actionsLeft);
        switch (actionsLeft) { //3 is covered by default
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
    public void showTip(SimpleAction recommendation) {

        if (recommendation == null) {
            showNotification("Wir sind selber ratlos - Ihr werdet alle sterben (siehe Knorkator)");
            return;
        }

        Action currentAction = getGameWindow().getControllerChan().getCurrentAction();
        Player playerToUse = currentAction.getPlayer(recommendation.getTargetPlayers().iterator().hasNext() ? recommendation.getTargetPlayers().iterator().next() : null);
        Point targetPoint = recommendation.getTargetPoint();
        ArtifactCardType card = recommendation.getCardType();
        String notification = "";
        refreshTurnState(getGameWindow().getControllerChan().getCurrentAction().getState());

        switch (recommendation.getType()) {
            case MOVE:
                mapPane.getMapStackPane(targetPoint).getBase().highlight();
                notification = "Bewege den Spieler " + playerToUse.getType().name() + " auf das angezeigte Feld.";
                break;

            case DRAIN:
                mapPane.getMapStackPane(targetPoint).getBase().highlight();
                notification = "Lege das angezeigte Feld trocken.";
                break;

            case DISCARD_CARD:
                notification = "Wirf eine Karte vom Typ " + card.name() + " ab";
                break;

            case TRADE_CARD:
                notification = "Gib eine Karte vom Typ " + card.name() + " an den Spieler " + playerToUse.getType().name() + " ab.";
                break;

            case SPECIAL_CARD:
                notification = "Spiele die Spezialkarte " + card.name() + ".";
                break;

            case COLLECT_TREASURE:
                notification = "Sammle das Artefakt.";
                break;

            case SPECIAL_ABILITY:
                notification = "Nutze deine Spezialaktion";//TODO: Split special ability
                break;

            case WAIT_AND_DRINK_TEA:
                notification = "Zug abgeben.";
        }
        showNotification(Notifications.info(notification));
    }

    @Override
    public void refreshTurnState(TurnState turnState) {
        resetHighlighting();
        switch (turnState) {
            case PLAYER_ACTION:
                this.refreshActionsLeft(getGameWindow()
                        .getControllerChan()
                        .getCurrentAction()
                        .getActivePlayer()
                        .getActionsLeft());
                this.endTurnButton.setDisable(false);
                break;
            case DRAW_ARTIFACT_CARD:
                this.rotateTurnSpinner(-240);
                this.endTurnButton.setDisable(true);
                break;
            case FLOOD:
                this.rotateTurnSpinner(-300);
                setFloodCardStackHighlighted(true);
                this.endTurnButton.setDisable(true);
                break;
            default:
                this.rotateTurnSpinner(0);
                break;
        }

    }

    @Override
    public void refreshHopefullyAll(Action action) {
        resetTargetPlayer();
        setSpecialActive(false);
        //refreshArtifacts found
        highlightArtifacts(action.getDiscoveredArtifacts());
        //refresh active player
        refreshPlayerCardImages(action);
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

    @Override
    public void refreshPlayersToRescue(Set<PlayerType> players) {
        this.rescueHelper.refreshPlayersToRescue(players);
    }

    public boolean isSpecialActive() {
        return specialActive;
    }

    public void setSpecialActive(boolean specialActive) {
        this.specialActive = specialActive;
        debug("special is active: " + specialActive);
    }

    public void setTransferActive(boolean transferActive) {
        this.transferActive = transferActive;
    }

    private void highlightArtifacts(EnumSet<ArtifactType> artifacts) {
        fireArtefactImageView.setEffect(artifacts.contains(ArtifactType.FIRE) ? null : DESATURATION);
        waterArtefactImageView.setEffect(artifacts.contains(ArtifactType.WATER) ? null : DESATURATION);
        earthArtefactImageView.setEffect(artifacts.contains(ArtifactType.EARTH) ? null : DESATURATION);
        airArtefactImageView.setEffect(artifacts.contains(ArtifactType.AIR) ? null : DESATURATION);
    }

    private void refreshPlayerCardImages(Action action) {
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

    public HelicopterHelper getHelicopterHelper() {
        return this.helicopterHelper;
    }

    public RescueHelper getRescueHelper() {
        return this.rescueHelper;
    }

    public void tryPlayHelicopterCard() {
        if (this.helicopterHelper == null || !this.helicopterHelper.readyToTransport()) {
            return;
        }



        this.getGameWindow().getControllerChan().getInGameUserController().playHelicopterCard(helicopterHelper.getCaster(),
                helicopterHelper.getCardIndex(),
                new Pair<>(helicopterHelper.getStartTile().getPosition(), helicopterHelper.getDestinationTile().getPosition()),
                new ArrayList<>(helicopterHelper.getToTransportConst()));
        getGameWindow().sugoiSugoi();

        // Dehighlight all and reset Helicopter card
        refreshTurnState(getGameWindow().getControllerChan().getCurrentAction().getState());
        this.helicopterHelper = null;
    }

    public MapPane getMapPane() {
        return mapPane;
    }

    private void doAIActionActivePlayer() {
        PlayerType activePlayer = this.getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType();
        this.doAIAction(activePlayer);
    }

    private void doAIAction(PlayerType player) {
        GameFlowController flowController = this.getGameWindow().getControllerChan().getGameFlowController();
        flowController.letAIAct(player);
    }
}
