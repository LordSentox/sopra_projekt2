package de.sopra.javagame.view;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.customcontrol.*;
import de.sopra.javagame.view.textures.TextureLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

    @FXML MapPane mapPane;

    @FXML
    WaterLevelView waterLevelView;

    @FXML GridPane cardGridPane, handOneCardGridPane, handTwoCardGridPane, handThreeCardGridPane, artifactCardDrawStackGridPane,
    artifactCardDicardGridPane, floodCardDrawStackGridPane, floodCardDiscardGridPane;
    @FXML Button endTurnButton;
    @FXML ImageView mainPane, activePlayerTypeImageView, playerOneTypeImageView, playerTwoTypeImageView, playerThreeTypeImageView,
    fireArtefactImageView, waterArtefactImageView, earthArtefactImageView, airArtefactImageView, turnSpinnerWithoutMarkerImageView, markerForSpinnerImageView;
    private static final int ACTIVE_CARD_SIZE = 150;
    private static final int PASSIVE_CARD_SIZE = 110;
    final int SPINNER_SIZE = 250;
    private static final int ARTIFACT_SIZE = 100;
    private static final ColorAdjust DESATURATION = new ColorAdjust(0, -1, 0, 0);

    public void init() {
        //MapTile[][] tiles = this.getGameWindow().getControllerChan().getCurrentAction().getTiles();
        
//        /* TEMP */
//        MapTile[][] tiles = new MapTile[9][12];
//        IntStream.range(0, 24).forEach(i -> tiles[i/10 +1][i%10 +1] = MapTile.fromNumber(i));
//        IntStream.range(0, 9).forEach(i -> { IntStream.range(0, 12).forEach(j -> System.out.print(tiles[i][j])); System.out.println();});
        /* END TEMP */ 
        mainPane.setImage(TextureLoader.getBackground());
        mainPane.setFitHeight(1200);
        initGridPane();

        activePlayerTypeImageView.setImage(TextureLoader.getPlayerTexture(PlayerType.DIVER));
        activePlayerTypeImageView.setPreserveRatio(true);
        activePlayerTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        activePlayerTypeImageView.setVisible(true);
        
        playerOneTypeImageView.setImage(TextureLoader.getPlayerTexture(PlayerType.PILOT));
        playerOneTypeImageView.setPreserveRatio(true);
        playerOneTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        playerOneTypeImageView.setFitHeight(ACTIVE_CARD_SIZE);
        playerOneTypeImageView.setVisible(true);
        
        playerTwoTypeImageView.setImage(TextureLoader.getPlayerTexture(PlayerType.EXPLORER));
        playerTwoTypeImageView.setPreserveRatio(true);
        playerTwoTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        playerTwoTypeImageView.setFitHeight(ACTIVE_CARD_SIZE);
        playerTwoTypeImageView.setVisible(true);
        
        playerThreeTypeImageView.setImage(TextureLoader.getPlayerTexture(PlayerType.ENGINEER));
        playerThreeTypeImageView.setPreserveRatio(true);
        playerThreeTypeImageView.setFitWidth(ACTIVE_CARD_SIZE);
        playerThreeTypeImageView.setFitHeight(ACTIVE_CARD_SIZE);
        playerThreeTypeImageView.setVisible(true);
        
        turnSpinnerWithoutMarkerImageView.setImage(TextureLoader.getTurnSpinner());
        turnSpinnerWithoutMarkerImageView.setPreserveRatio(true);

        turnSpinnerWithoutMarkerImageView.setFitWidth(SPINNER_SIZE);
        turnSpinnerWithoutMarkerImageView.setFitHeight(SPINNER_SIZE);
        turnSpinnerWithoutMarkerImageView.setVisible(true);
        //Bild um 72 Grad drehen
        turnSpinnerWithoutMarkerImageView.setRotate(72.0);
        turnSpinnerWithoutMarkerImageView.getStyleClass().add("CardView");
        markerForSpinnerImageView.setImage(TextureLoader.getSpinnerMarker());
        
        markerForSpinnerImageView.setPreserveRatio(true);
        markerForSpinnerImageView.setFitWidth(SPINNER_SIZE);
        markerForSpinnerImageView.setFitHeight(SPINNER_SIZE);
        markerForSpinnerImageView.setVisible(true);
        markerForSpinnerImageView.getStyleClass().add("CardView");
        
        
        
        
        fireArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.FIRE));
        fireArtefactImageView.setPreserveRatio(true);
        fireArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        //fireArtefactImageView.setFitHeight(ARTIFACT_SIZE);
        fireArtefactImageView.setVisible(true);
        fireArtefactImageView.getStyleClass().add("Artifact_Fire");
        //fireArtefactImageView.setEffect(DESATURATION);
        
        waterArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.WATER));
        waterArtefactImageView.setPreserveRatio(true);
        waterArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        //waterArtefactImageView.setFitHeight(ARTIFACT_SIZE);
        waterArtefactImageView.setVisible(true);
        waterArtefactImageView.getStyleClass().add("Artifact_Water");
        //waterArtefactImageView.setEffect(DESATURATION);
        
        earthArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.EARTH));
        earthArtefactImageView.setPreserveRatio(true);
        earthArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        //earthArtefactImageView.setFitHeight(ARTIFACT_SIZE);
        earthArtefactImageView.setVisible(true);
        earthArtefactImageView.getStyleClass().add("Artifact_Earth");
        //earthArtefactImageView.setEffect(DESATURATION);
        
        airArtefactImageView.setImage(TextureLoader.getArtifactTexture(ArtifactType.AIR));
        airArtefactImageView.setPreserveRatio(true);
        airArtefactImageView.setFitWidth(ARTIFACT_SIZE);
        //airArtefactImageView.setFitHeight(ARTIFACT_SIZE);
        airArtefactImageView.setVisible(true);
        airArtefactImageView.getStyleClass().add("Artifact_Air");
        //airArtefactImageView.setEffect(DESATURATION);



        /* Cards */
        for(int i = 0; i < 9; i+=2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showFrontImage();
            cardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }




        for(int i = 0; i < 5; i++) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handOneCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 5; i++) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handTwoCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 5; i++) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handThreeCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }

        
        for(int i = 0; i < 10; i+=2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showBackImage();
            artifactCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 10; i+=2) {
            CardView v = new ArtifactCardView(ArtifactCardType.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showFrontImage();
            artifactCardDicardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 10; i+=2) {
            CardView v = new FloodCardView(MapTileProperties.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showBackImage();
            floodCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 10; i+=2) {
            CardView v = new FloodCardView(MapTileProperties.values()[(new Random().nextInt(7))], ACTIVE_CARD_SIZE);
            v.showFrontImage();
            floodCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }

        refreshWaterLevel(4);
    }
    
    private void initGridPane() {
//        IntStream.range(0, 21).forEach(i -> gridPane.getColumnConstraints().add(new ColumnConstraints(i%2==0 ? 5 : 130)));
//        IntStream.range(0, 15).forEach(i -> gridPane.getRowConstraints().add(new RowConstraints(i%2==0 ? 5 : 130)));
//
        IntStream.range(0, 9).forEach(i -> cardGridPane.getColumnConstraints().add(new ColumnConstraints(i%2==0 ? ACTIVE_CARD_SIZE : 5)));

        IntStream.range(0, 5).forEach(i -> handOneCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE/2)));
        IntStream.range(0, 5).forEach(i -> handTwoCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE/2)));
        IntStream.range(0, 5).forEach(i -> handThreeCardGridPane.getColumnConstraints().add(new ColumnConstraints(PASSIVE_CARD_SIZE/2)));

        IntStream.range(0, 28).forEach(i -> artifactCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1)));
        IntStream.range(0, 28).forEach(i -> artifactCardDicardGridPane.getColumnConstraints().add(new ColumnConstraints(1)));
        IntStream.range(0, 24).forEach(i -> floodCardDrawStackGridPane.getColumnConstraints().add(new ColumnConstraints(1)));
        IntStream.range(0, 24).forEach(i -> floodCardDiscardGridPane.getColumnConstraints().add(new ColumnConstraints(1)));

    }


    public void onShowMovementOptionsClicked() {

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

    }
    
    public void onArtifactCardDiscardStackClicked(){
        
    }
    
    public void onFloodCardDiscardStackClicked(){
        
    }
    
    public void onArtifactCardDrawStackClicked(){
        
    }
    
    public void onFloodCardDrawStackClicked(){
        
    }


    @Override
    ViewState getType() {
        return ViewState.IN_GAME;
    }

    @Override
    void reset() {

    }

    @Override
    void show(Stage stage) {

    }

    @Override
    public void refreshMovementOptions(List<Point> points) {

    }

    @Override
    public void refreshDrainOptions(List<Point> points) {

    }

    @Override
    public void showNotification(String notification) {

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
    public void refreshActivePlayer(PlayerType player) {

    }

    @Override
    public void refreshActionsLeft(int actionsLeft) {

    }

    @Override
    public void refreshPlayerName(String name, PlayerType player) {

    }

    @Override
    public void refreshAll() {

    }

    @Override
    public void setIsReplayWindow(boolean replay) {

    }
}
