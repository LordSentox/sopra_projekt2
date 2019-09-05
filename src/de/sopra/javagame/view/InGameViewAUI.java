package de.sopra.javagame.view;

import de.sopra.javagame.model.CardStack;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;

import java.awt.*;
import java.util.List;

public interface InGameViewAUI {

    void refreshMovementOptions(List<Point> points);

    void refreshDrainOptions(List<Point> points);

    void showNotification(String notification);

    void refreshCardsTransferable(boolean transferable);

    void refreshWaterLevel(int level);

    void refreshHand(PlayerType player, List cards);

    void refreshArtifactsFound(boolean artifacts);

    void refreshArtifactStack(CardStack stack);

    void refreshFloodStack(CardStack stack);

    void refreshPlayerPosition(Point position, PlayerType player);

    void refreshMapTile(Point position, MapTile tile);

    void refreshActivePlayer(PlayerType player);

    void refreshActionsLeft(int actionsLeft);

    void refreshPlayerName(String name, PlayerType player);

    void refreshAll();

    void setIsReplayWindow(boolean replay);

}
