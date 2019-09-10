package de.sopra.javagame.view;

import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.view.customcontrol.CardView;
import de.sopra.javagame.view.customcontrol.TileView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.awt.Point;
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
    
    @FXML GridPane gridPane, cardGridPane, handOneCardGridPane, handTwoCardGridPane, handThreeCardGridPane, artifactCardDrawStackGridPane,
    artifactCardDicardGridPane, floodCardDrawStackGridPane, floodCardDiscardGridPane;
    @FXML Button endTurnButton;
    @FXML ImageView activePlayerTypeImageView, playerOneTypeImageView, playerTwoTypeImageView, playerThreeTypeImageView, fireArtefactImageView, waterArtefactImageView, earthArtefactImageView, airArtefactImageView;
    private static final int ACTIVE_CARD_SIZE = 150;
    private static final int PASSIVE_CARD_SIZE = 110;
    private static final int ARTIFACT_SIZE = 100;
    private static final ColorAdjust DESATURATION = new ColorAdjust(0, -1, 0, 0);
    
    public void init() {
        //MapTile[][] tiles = this.getGameWindow().getControllerChan().getCurrentTurn().getTiles();
        
        /* TEMP */
        MapTile[][] tiles = new MapTile[7][10];
        IntStream.range(0, 24).forEach(i -> tiles[i/7][i%10] = MapTile.fromNumber(i));
        IntStream.range(0, 7).forEach(i -> IntStream.range(0, 10).forEach(j -> System.out.println(tiles[i][j])));
        /* END TEMP */ 
        
        initGridPane();
        
        /* Island */
//        IntStream.range(0, 7).forEach(y -> 
//            IntStream.range(0, 10).forEach(x -> {
//                TileView v = new TileView(tiles[y][x].getNumber());
//                gridPane.getChildren().add(v);
//                GridPane.setConstraints(v, y, x);
//            }));
        
        activePlayerTypeImageView.setImage(new Image(getClass().getResource("/textures/default/diver.png").toExternalForm(), ACTIVE_CARD_SIZE, 0, true, true));
        activePlayerTypeImageView.setVisible(true);
        
        playerOneTypeImageView.setImage(new Image(getClass().getResource("/textures/default/pilot.png").toExternalForm(), PASSIVE_CARD_SIZE, 0, true, true));
        playerOneTypeImageView.setVisible(true);
        
        playerTwoTypeImageView.setImage(new Image(getClass().getResource("/textures/default/explorer.png").toExternalForm(), PASSIVE_CARD_SIZE, 0, true, true));
        playerTwoTypeImageView.setVisible(true);
        
        playerThreeTypeImageView.setImage(new Image(getClass().getResource("/textures/default/engineer.png").toExternalForm(), PASSIVE_CARD_SIZE, 0, true, true));
        playerThreeTypeImageView.setVisible(true);
        
        
        
        fireArtefactImageView.setImage(new Image(getClass().getResource("/textures/default/artefact_02_default.png").toExternalForm(), ARTIFACT_SIZE, 0, true, true));
        fireArtefactImageView.setVisible(true);
        fireArtefactImageView.setEffect(DESATURATION);
        
        waterArtefactImageView.setImage(new Image(getClass().getResource("/textures/default/artefact_01_default.png").toExternalForm(), ARTIFACT_SIZE, 0, true, true));
        waterArtefactImageView.setVisible(true);
        waterArtefactImageView.setEffect(DESATURATION);
        
        earthArtefactImageView.setImage(new Image(getClass().getResource("/textures/default/artefact_04.png").toExternalForm(), ARTIFACT_SIZE, 0, true, true));
        earthArtefactImageView.setVisible(true);
        earthArtefactImageView.setEffect(DESATURATION);
        
        airArtefactImageView.setImage(new Image(getClass().getResource("/textures/default/artefact_03_default.png").toExternalForm(), ARTIFACT_SIZE, 0, true, true));
        airArtefactImageView.setVisible(true);
        airArtefactImageView.setEffect(DESATURATION);

           
        
        /* Cards */
        for(int i = 0; i < 9; i+=2) {
            CardView v = new CardView("default", "artefact_0" + (new Random().nextInt(7)+1) + ".png", "artefact_back.png", ACTIVE_CARD_SIZE);
            v.showFrontImage();
            cardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        
        
        
        
        for(int i = 0; i < 5; i++) {
            CardView v = new CardView("default", "artefact_0" + (new Random().nextInt(7)+1) + ".png", "artefact_back.png", PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handOneCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 5; i++) {
            CardView v = new CardView("default", "artefact_0" + (new Random().nextInt(7)+1) + ".png", "artefact_back.png", PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handTwoCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 5; i++) {
            CardView v = new CardView("default", "artefact_0" + (new Random().nextInt(7)+1) + ".png", "artefact_back.png", PASSIVE_CARD_SIZE);
            v.showFrontImage();
            handThreeCardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        
        
        
        for(int i = 0; i < 10; i+=2) {
            CardView v = new CardView("default", "artefact_0" + (new Random().nextInt(7)+1) + ".png", "artefact_back.png", ACTIVE_CARD_SIZE);
            v.showBackImage();
            artifactCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 10; i+=2) {
            CardView v = new CardView("default", "artefact_0" + (new Random().nextInt(7)+1) + ".png", "artefact_back.png", ACTIVE_CARD_SIZE);
            v.showFrontImage();
            artifactCardDicardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 10; i+=2) {
            CardView v = new CardView("default", "floodcard_0" + (new Random().nextInt(7)+1) + ".png", "floodcard_back.png", ACTIVE_CARD_SIZE);
            v.showBackImage();
            floodCardDrawStackGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        for(int i = 0; i < 10; i+=2) {
            CardView v = new CardView("default", "floodcard_0" + (new Random().nextInt(7)+1) + ".png", "floodcard_back.png", ACTIVE_CARD_SIZE);
            v.showFrontImage();
            floodCardDiscardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        
        
    }
    
    private void initGridPane() {
        IntStream.range(0, 21).forEach(i -> gridPane.getColumnConstraints().add(new ColumnConstraints(i%2==0 ? 5 : 130)));
        IntStream.range(0, 15).forEach(i -> gridPane.getRowConstraints().add(new RowConstraints(i%2==0 ? 5 : 130)));
        
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
