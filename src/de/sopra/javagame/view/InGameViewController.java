package de.sopra.javagame.view;

import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.view.customcontrol.CardView;
import de.sopra.javagame.view.customcontrol.TileView;
import javafx.fxml.FXML;
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
    
    @FXML GridPane gridPane, cardGridPane;
    
    public void init() {
        //MapTile[][] tiles = this.getGameWindow().getControllerChan().getCurrentTurn().getTiles();
        
        /* TEMP */
        MapTile[][] tiles = new MapTile[7][10];
        IntStream.range(0, 24).forEach(i -> tiles[i/7][i%10] = MapTile.fromNumber(i));
        IntStream.range(0, 7).forEach(i -> IntStream.range(0, 10).forEach(j -> System.out.println(tiles[i][j])));
        /* END TEMP */ 
        
        initGridPane();
        
        /* Island */
        IntStream.range(0, 7).forEach(y -> 
            IntStream.range(0, 10).forEach(x -> {
                TileView v = new TileView(tiles[y][x].getNumber());
                gridPane.getChildren().add(v);
                GridPane.setConstraints(v, y, x);
            }));
        
        

           
        
        /* Cards */
        for(int i = 0; i < 9; i+=2) {
            CardView v = new CardView("default", "artefact_0" + (new Random().nextInt(7)+1) + ".png", "artefact_back.png");
            v.showFrontImage();
            cardGridPane.getChildren().add(v);
            GridPane.setConstraints(v, i, 0);
        }
        
        
    }
    
    private void initGridPane() {
        IntStream.range(0, 21).forEach(i -> gridPane.getColumnConstraints().add(new ColumnConstraints(i%2==0 ? 5 : 130)));
        IntStream.range(0, 15).forEach(i -> gridPane.getRowConstraints().add(new RowConstraints(i%2==0 ? 5 : 130)));
        IntStream.range(0, 9).forEach(i -> cardGridPane.getColumnConstraints().add(new ColumnConstraints(i%2==0 ? 150 : 5)));
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
