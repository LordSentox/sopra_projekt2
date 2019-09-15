package de.sopra.javagame.view;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.abstraction.AbstractViewController;
import de.sopra.javagame.view.abstraction.ViewState;
import de.sopra.javagame.view.customcontrol.*;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
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
    final int SPINNER_SIZE = 250;
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

    public void init() {
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
       

        refreshWaterLevel(4);
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

        IntStream.range(0, 28).forEach(item ->  {
            artifactCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1));
            artifactCardDicardGridPane.getColumnConstraints().add(new ColumnConstraints(1));
        });
           
        IntStream.range(0, 24).forEach(item -> {
            floodCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1));
            floodCardDiscardGridPane.getColumnConstraints().add(new ColumnConstraints(1));
        });
    }

    private void initPlayerHands(){
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

    public void onShowMovementOptionsClicked() {
        System.out.println("juch, ich bin der bewegungsindikator");
        getGameWindow().getControllerChan().getActivePlayerController().showMovements(false);
    }

    public void onShowDrainOptionsClicked() {
        System.out.println("juch, ich bin der drainindikator");
        getGameWindow().getControllerChan().getActivePlayerController().showDrainOptions();
    }

    public void onShowSpecialAbilityOptionsClicked() {

        System.out.println("juch, ich bin der specialindikator");
        getGameWindow().getControllerChan().getActivePlayerController().showSpecialAbility();
    }
    //TODO button zum abbrechen einbauen
    public void onSpecialAbilityCancelClicked() {
        getGameWindow().getControllerChan().getActivePlayerController().cancelSpecialAbility();
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

    public void onCollectArtifactClicked() {
        System.out.println("juch, ich bin der artefaktfinderotto");
        getGameWindow().getControllerChan().getActivePlayerController().collectArtifact();
    }

    public void onRedoClicked() {

    }

    public void onUndoClicked() {

    }

    public void onPlayClicked() {

    }

    public void onPauseClicked() {
     
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
        
        //DEBUG
        refreshHand(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType(), Arrays.asList(new ArtifactCard[]{new ArtifactCard(ArtifactCardType.AIR)}));
        mapPane.putPlayer(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getPosition().xPos, getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getPosition().yPos, getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType());
    }

    @Override
    public void refreshMovementOptions(List<Point> points) {
        points.forEach(point -> mapPane.highlightMapTile(point,false));
    }

    @Override
    public void refreshDrainOptions(List<Point> points) {

        points.forEach(point -> mapPane.highlightMapTile(point,false));
    }

    @Override
    public void refreshCardsTransferable(boolean transferable) {

    }

    @Override
    public void refreshWaterLevel(int level) {
        waterLevelView.setProgress(level);
        System.out.println("uwu");
    }

    @Override
    public void refreshHand(PlayerType player, List<ArtifactCard> cards) {
        if(getGameWindow().getControllerChan().getCurrentAction().getActivePlayer().getType() == player) {
            cardGridPane.getChildren().clear();
            int index = 0;
            for (ArtifactCard card : cards) {
                CardView v = new ArtifactCardView(card.getType(), ACTIVE_CARD_SIZE);
                v.showFrontImage();
                cardGridPane.getChildren().add(v);
                GridPane.setConstraints(v, index, 0);
                index += 2;
            }            
        } else {
            Action action = getGameWindow().getControllerChan().getCurrentAction();
            List<Player> players = action.getPlayers();
            GridPane pane = null;
            
            if(players.get((action.getActivePlayerIndex() + 1) % players.size()).getType().equals(player))
                pane = handOneCardGridPane;
            if(players.get((action.getActivePlayerIndex() + 2) % players.size()).getType().equals(player))
                pane = handTwoCardGridPane;
            if(players.get((action.getActivePlayerIndex() + 3) % players.size()).getType().equals(player))
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
        this.refreshActionsLeft(-216);
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
            index +=2;
        }
    }

    @Override
    public void refreshFloodStack(CardStack<FloodCard> stack) {
        this.refreshActionsLeft(-288);
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
            CardView v = new FloodCardView(card.getTile().getProperties(), ACTIVE_CARD_SIZE);
            v.showFrontImage();
            floodCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, index, 0);
            index +=2;
        }
    }

    @Override
    public void refreshPlayerPosition(Point position, PlayerType player) {
        mapPane.putPlayer(position.xPos, position.yPos, player);
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
        if(players.size() == 3){
            playerTwoTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 2) % players.size()).getType()));
        } else if(players.size() == 4){
            playerThreeTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 3) % players.size()).getType()));            
        }
    }

    @Override
    public void refreshActionsLeft(int actionsLeft) {
        switch (actionsLeft) {
        case 1: this.rotateTurnSpinner(-144); break;
        case 2: this.rotateTurnSpinner(-72); break;
        case 3: this.rotateTurnSpinner(0);break;
        default: this.rotateTurnSpinner(0);
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
}
