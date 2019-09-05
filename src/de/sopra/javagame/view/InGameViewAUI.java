package de.sopra.javagame.view;

import de.sopra.javagame.model.CardStack;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;

import java.awt.*;
import java.util.List;

public interface InGameViewAUI {

    public abstract void refreshMovementOptions(List<Point> points);

    public abstract void refreshDrainOptions(List<Point> points);

    public abstract void showNotification(String notification);

    public abstract void refreshCardsTransferable(boolean transferable);

    public abstract void refreshWaterLevel(int level);

    public abstract void refreshHand(PlayerType player, List cards);

    public abstract void refreshArtifactsFound(boolean artifacts);

    public abstract void refreshArtifactStack(CardStack stack);

    public abstract void refreshFloodStack(CardStack stack);

    public abstract void refreshPlayerPosition(Point position, PlayerType player);

    public abstract void refreshMapTile(Point position, MapTile tile);

    public abstract void refreshActivePlayer(PlayerType player);

    public abstract void refreshActionsLeft(int actionsLeft);

    public abstract void refreshPlayerName(String name, PlayerType player);

    public abstract void refreshAll();

    public abstract void setIsReplayWindow(boolean replay);

}
