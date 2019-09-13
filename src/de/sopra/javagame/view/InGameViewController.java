package de.sopra.javagame.view;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.MapTileState;
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
    private static double turnSpinnerCount;
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
        


        





        /* Cards */
        for (int i = 0; i < 9; i += 2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showFrontImage();
            cardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }


        for (int i = 0; i < 5; i++) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handOneCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);activePlayerTypeImageView.setPreserveRatio(true);
            activePlayerTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
            activePlayerTypeImageView.setVisible(true);
        }
        for (int i = 0; i < 5; i++) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handTwoCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for (int i = 0; i < 5; i++) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handThreeCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }


        for (int i = 0; i < 10; i += 2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showBackImage();
            artifactCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for (int i = 0; i < 10; i += 2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showFrontImage();
            artifactCardDicardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for (int i = 0; i < 10; i += 2) {
            CardView v = new FloodCardView(MapTileProperties.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showBackImage();
            floodCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for (int i = 0; i < 10; i += 2) {
            CardView v = new FloodCardView(MapTileProperties.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showFrontImage();
            floodCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }

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
        IntStream.range(0, 9).forEach(i -> cardGridPane.getColumnConstraints().add(new ColumnConstraints(i % 2 == 0 ? ACTIVE_CARD_SIZE : 5)));

        IntStream.range(0, 5).forEach(i -> handOneCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2)));
        IntStream.range(0, 5).forEach(i -> handTwoCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2)));
        IntStream.range(0, 5).forEach(i -> handThreeCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE / 2)));

        IntStream.range(0, 28).forEach(i -> artifactCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1)));
        IntStream.range(0, 28).forEach(i -> artifactCardDicardGridPane.getColumnConstraints().add(new ColumnConstraints(1)));
        IntStream.range(0, 24).forEach(i -> floodCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1)));
        IntStream.range(0, 24).forEach(i -> floodCardDiscardGridPane.getColumnConstraints().add(new ColumnConstraints(1)));
    }

    private void initPlayerHands(){
        activePlayerTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        playerOneTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        playerTwoTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        playerThreeTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
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

    }

    public void onShowSpecialAbilityOptionsClicked() {

    }

    public void onSpecialAbilityCancelClicked() {

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

    }

    public void onSpecialCardPlayClicked(int cardIndex) {

    }

    public void onDiscardSelectedCardsClicked() {

    }

    public void onCollectArtifactClicked() {

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
        refreshArtifactsFound(EnumSet.of(ArtifactType.EARTH, ArtifactType.WATER));
        refreshActivePlayer();
    }

    @Override
    public void refreshMovementOptions(List<Point> points) {
        points.forEach(point ->((TileView) mapPane.getMapStackPane(point.yPos, point.xPos).getChildren().get(0)).showImage(MapTileState.FLOODED));
    }

    @Override
    public void refreshDrainOptions(List<Point> points) {

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
    public void refreshHand(PlayerType player, List cards) {

    }

    @Override
    public void refreshArtifactsFound(EnumSet<ArtifactType> artifacts) {
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
    public void refreshArtifactStack(CardStack stack) {

    }

    @Override
    public void refreshFloodStack(CardStack stack) {

    }

    @Override
    public void refreshPlayerPosition(Point position, PlayerType player) {

    }

    @Override
    public void refreshMapTile(Point position, MapTile tile) {

    }

    @Override
    public void refreshActivePlayer() {
        Action action = this.getGameWindow().getControllerChan().getCurrentAction();
        List<Player> players = action.getPlayers();
        
        activePlayerTypeImageView.setImage(TextureLoader.getPlayerCardTexture(action.getActivePlayer().getType()));
        playerOneTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 1) % players.size()).getType()));
        playerTwoTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 2) % players.size()).getType()));
        playerThreeTypeImageView.setImage(TextureLoader.getPlayerCardTexture(players.get((action.getActivePlayerIndex() + 3) % players.size()).getType()));
    }

    @Override
    public void refreshActionsLeft(int actionsLeft) {

    }

    @Override
    public void refreshPlayerName(String name, PlayerType player) {

    }

    @Override
    public void setIsReplayWindow(boolean replay) {

    }
}
