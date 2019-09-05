package model.view;

import model.model.CardStack;
import model.model.MapTile;
import model.player.PlayerType;

import java.util.List;

public class InGameViewController extends AbstractViewController implements InGameViewAUI {

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


	/**
	 * @see model.view.InGameViewAUI#refreshMovementOptions(java.util.List)
	 * 
	 *  
	 */
	public void refreshMovementOptions(List points) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshDrainOptions(java.util.List)
	 * 
	 *  
	 */
	public void refreshDrainOptions(List points) {

	}


	/**
	 * @see model.view.InGameViewAUI#showNotification(java.lang.String)
	 * 
	 *  
	 */
	public void showNotification(String notification) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshCardsTransferable(boolean)
	 * 
	 *  
	 */
	public void refreshCardsTransferable(boolean transferable) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshWaterLevel(int)
	 * 
	 *  
	 */
	public void refreshWaterLevel(int level) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshHand(PlayerType, java.util.List)
	 * 
	 *  
	 */
	public void refreshHand(PlayerType player, List cards) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshArtifactsFound(boolean)
	 * 
	 *  
	 */
	public void refreshArtifactsFound(boolean artifacts) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshArtifactStack(model.model.CardStack)
	 * 
	 *  
	 */
	public void refreshArtifactStack(CardStack stack) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshFloodStack(model.model.CardStack)
	 * 
	 *  
	 */
	public void refreshFloodStack(CardStack stack) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshPlayerPosition(Point, PlayerType)
	 * 
	 *  
	 */
	public void refreshPlayerPosition(Point position, PlayerType player) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshMapTile(Point, model.model.MapTile)
	 */
	public void refreshMapTile(Point position, MapTile tile) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshActivePlayer(PlayerType)
	 * 
	 *  
	 */
	public void refreshActivePlayer(PlayerType player) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshActionsLeft(int)
	 * 
	 *  
	 */
	public void refreshActionsLeft(int actionsLeft) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshPlayerName(java.lang.String, PlayerType)
	 * 
	 *  
	 */
	public void refreshPlayerName(String name, PlayerType player) {

	}


	/**
	 * @see model.view.InGameViewAUI#refreshAll()
	 * 
	 *  
	 */
	public void refreshAll() {

	}


	/**
	 * @see model.view.InGameViewAUI#setIsReplayWindow(boolean)
	 * 
	 *  
	 */
	public void setIsReplayWindow(boolean replay) {

	}

}
