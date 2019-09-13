package de.sopra.javagame;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.HighScoresViewAUI;
import de.sopra.javagame.view.InGameViewAUI;
import de.sopra.javagame.view.MapEditorViewAUI;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
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

    public static void injectJavaGame(ControllerChan controllerChan, JavaGame game) throws Exception {
        Field field = ControllerChan.class.getDeclaredField("javaGame");
        field.setAccessible(true);
        field.set(controllerChan, game);
        field.setAccessible(false);
    }

    public static void injectCurrentAction(ControllerChan controllerChan, Action action) throws Exception {
        Field field = ControllerChan.class.getDeclaredField("currentAction");
        field.setAccessible(true);
        field.set(controllerChan, action);
        field.setAccessible(false);
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
        public void setMap(String mapName, boolean[][] tiles) {
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

        private List<Point> movementPoints;
        private List<Point> drainPoints;
        private List<String> notifications = new LinkedList<>();
        private Boolean transferable;
        private Integer waterlevel;
        private HashMap<PlayerType, List<ArtifactCard>> playerHands = new HashMap<>();
        private EnumSet<ArtifactType> artifactsFound = EnumSet.noneOf(ArtifactType.class);
        private CardStack<ArtifactCard> cardStackArtifact;
        private CardStack<FloodCard> cardStackFlood;
        private HashMap<PlayerType, Point> playerPositions = new HashMap<>();
        private HashMap<Point, MapTile> mapTiles = new HashMap<>();
        private PlayerType activePlayer;
        private Integer actionsLeft;
        private HashMap<PlayerType, String> playerNames = new HashMap<>();
        private int refreshedAll = 0;
        private boolean isReplay = false;
        private ActionQueue latestTip;

        @Override
        public void refreshMovementOptions(List<Point> points) {
            this.movementPoints = points;
        }

        @Override
        public void refreshDrainOptions(List<Point> points) {
            this.drainPoints = points;
        }

        @Override
        public void showNotification(String notification) {
            notifications.add(notification);
        }

        @Override
        public void refreshCardsTransferable(boolean transferable) {
            this.transferable = transferable;
        }

        @Override
        public void refreshWaterLevel(int level) {
            this.waterlevel = level;
        }

        @Override
        public void refreshHand(PlayerType player, List<ArtifactCard> cards) {
            playerHands.put(player, cards);
        }

        @Override
        public void refreshArtifactsFound() {
            this.artifactsFound = null;//FIXME
        }

        @Override
        public void refreshArtifactStack(CardStack<ArtifactCard> stack) {
            this.cardStackArtifact = stack;
        }

        @Override
        public void refreshFloodStack(CardStack<FloodCard> stack) {
            this.cardStackFlood = stack;
        }

        @Override
        public void refreshPlayerPosition(Point position, PlayerType player) {
            playerPositions.put(player, position);
        }

        @Override
        public void refreshMapTile(Point position, MapTile tile) {
            mapTiles.put(position, tile);
        }

        @Override
        public void refreshActivePlayer() {
            this.activePlayer = PlayerType.COURIER; //FIXME
        }

        @Override
        public void refreshActionsLeft(int actionsLeft) {
            this.actionsLeft = actionsLeft;
        }

        @Override
        public void refreshPlayerName(String name, PlayerType player) {
            playerNames.put(player, name);
        }

        @Override
        public void refreshAll() {
            refreshedAll++;
        }

        @Override
        public void setIsReplayWindow(boolean replay) {
            this.isReplay = replay;
        }

        @Override
        public void showTip(ActionQueue queue) {
            this.latestTip = queue;
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
         * @return <code>null</code> wenn niemals gesetzt
         * @see #refreshCardsTransferable(boolean)
         */
        public Boolean getTransferable() {
            return transferable;
        }

        /**
         * @return <code>null</code> wenn niemals gesetzt
         * @see #refreshArtifactsFound(EnumSet)
         */
        public EnumSet<ArtifactType> getArtifactsFound() {
            return artifactsFound;
        }

        /**
         * Der aktuelle Artifaktkartenstapel
         *
         * @return <code>null</code> wenn niemals gesetzt
         */
        public CardStack<ArtifactCard> getCardStackArtifact() {
            return cardStackArtifact;
        }

        /**
         * Die aktuelle Spielerhand  eines gegebenen Spielers
         *
         * @return <code>null</code> wenn niemals gesetzt
         * @see #refreshHand(PlayerType, List)
         */
        public List<ArtifactCard> getPlayerHand(PlayerType playerType) {
            return playerHands.get(playerType);
        }

        /**
         * Der aktuelle Flutkartenstapel
         *
         * @return <code>null</code> wenn niemals gesetzt
         */
        public CardStack<FloodCard> getCardStackFlood() {
            return cardStackFlood;
        }

        /**
         * Die Spielerpositionen
         *
         * @return leer wenn niemals gesetzt
         * @see #refreshPlayerPosition(Point, PlayerType)
         */
        public HashMap<PlayerType, Point> getPlayerPositions() {
            return playerPositions;
        }

        /**
         * Die Name des Spieler
         *
         * @return null wenn niemals gesetzt
         * @see #refreshPlayerName(String, PlayerType)
         */
        public String getPlayerName(PlayerType type) {
            return playerNames.get(type);
        }

        /**
         * Alle refreshten MapTile sind hier mit ihren Points verzeichnet.
         * Zwei Refreshes auf dem gleichen Punkt überschreiben einander
         *
         * @return leer wenn niemals gesetzt
         * @see #refreshMapTile(Point, MapTile)
         */
        public HashMap<Point, MapTile> getRefreshedMapTiles() {
            return mapTiles;
        }

        /**
         * Der Zähler für die verbleibenden Aktionen
         *
         * @return <code>null</code> wenn niemals gesetzt
         */
        public Integer getActionsLeft() {
            return actionsLeft;
        }

        /**
         * Das aktuelle Wasserlevel
         *
         * @return <code>null</code> wenn niemals gesetzt
         */
        public Integer getWaterlevel() {
            return waterlevel;
        }

        /**
         * Die Anzahl an Aufrufen von refreshAll seit Erstellung
         *
         * @return <code>0</code> wenn niemals verwendet
         */
        public int getRefreshedAll() {
            return refreshedAll;
        }

        /**
         * Die zuletzt gezeigten Punkte, die ein Spieler trocknen kann
         *
         * @return <code>null</code> wenn niemals gesetzt
         * @see #refreshDrainOptions(List)
         */
        public List<Point> getDrainPoints() {
            return drainPoints;
        }

        /**
         * Die zuletzt gezeigten Punkte, zu denen sich ein Spieler bewegen kann
         *
         * @return <code>null</code> wenn niemals gesetzt
         * @see #refreshMovementOptions(List)
         */
        public List<Point> getMovementPoints() {
            return movementPoints;
        }

        /**
         * Der aktive Spieler (am Zug)
         *
         * @return <code>null</code> wenn niemals gesetzt
         */
        public PlayerType getActivePlayer() {
            return activePlayer;
        }

        /**
         * Ob die View auf Replay-Modus gestellt wurde
         *
         * @return <code>false</code> standardmäßig
         */
        public boolean isReplay() {
            return isReplay;
        }

        /**
         * Der letzte gezeigte Tipp in der GUI
         *
         * @return <code>null</code> falls nie einer gezeigt wurde
         */
        public ActionQueue getLatestTip() {
            return latestTip;
        }
    }

}