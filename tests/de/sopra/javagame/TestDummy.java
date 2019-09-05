package de.sopra.javagame;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.CardStack;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.InGameViewAUI;
import de.sopra.javagame.view.MapEditorViewAUI;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 05.09.2019
 * @since 05.09.2019
 */
public class TestDummy {

    public static ControllerChan getDummyControllerChan() {
        ControllerChan controllerChan = new ControllerChan();
        controllerChan.setInGameViewAUI(getInGameViewAUI());
        controllerChan.setMapEditorViewAUI(getMapEditorViewAUI());
        controllerChan.setHighScoresViewAUI(getHighScoresViewAUI());
        return controllerChan;
    }

    private static InGameViewAUI getInGameViewAUI() {
        return new InGameView();
    }

    private static MapEditorViewAUI getMapEditorViewAUI() {
        return new MapEditorView();
    }

    private static HighScoresViewAUI getHighScoresViewAUI() {
        return new HighScoreView();
    }

    public static class HighScoreView implements HighScoresViewAUI {
        private List<HighScore> highScores;

        @Override
        public void refreshList(List<HighScore> scores) {
            this.highScores = scores;
        }

        /**
         * Gibt die zuletzt refreshte Liste zurück
         *
         * @return <code>null</code> wenn die Liste nie refreshed wurde
         */
        public List<HighScore> getHighScores() {
            return highScores;
        }
    }

    public static class MapEditorView implements MapEditorViewAUI {

        private List<String> notifications = new LinkedList<>();
        private boolean[][] tiles;

        @Override
        public void showNotification(String notification) {
            notifications.add(notification);
        }

        @Override
        public void setMap(boolean[][] tiles) {
            this.tiles = tiles;
        }

        /**
         * Gibt die Liste vergangener Notifications zurück.
         * Die Neueste ist die Letzte in der Liste
         *
         * @return ist leer, wenn keine Notifications erfolgt sind
         */
        public List<String> getNotifications() {
            return notifications;
        }

        /**
         * Gibt die aktuell gesetzten Tiles zurück
         *
         * @return <code>null</code> wenn die tiles nie gesetzt wurden
         */
        public boolean[][] getTiles() {
            return tiles;
        }
    }

    public static class InGameView implements InGameViewAUI {

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
        public void refreshHand(PlayerType player, List<ArtifactCard> cards) {

        }

        @Override
        public void refreshArtifactsFound(boolean[] artifacts) {

        }

        @Override
        public void refreshArtifactStack(CardStack<ArtifactCard> stack) {

        }

        @Override
        public void refreshFloodStack(CardStack<FloodCard> stack) {

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

}