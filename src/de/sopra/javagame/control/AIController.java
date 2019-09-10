package de.sopra.javagame.control;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.AIActionTip;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class AIController {

    public interface AIProcessor {

        /**
         * Fordert die KI auf, mit dem aktuellen Spieler einen automatischen Zug durchzuführen.
         * Dieser Zug kann mehrere Aktionen verbrauchen und das Ausspielen von Spezialkarten beinhalten.
         * MakeStep kann ebenfalls zum Abwerfen von Karten verwendet werden.
         */
        void makeStep(AIController control);

        /**
         * Fordert einen Tipp von der KI an.
         * Der Tipp wird in Befehlsform definiert durch {@link AIActionTip} dargestellt.
         *
         * @return ein Tipp in Befehlsform
         */
        String getTip(AIController control);

    }

    private ControllerChan controllerChan;

    public AIController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }


    public boolean isCurrentlyDiscarding() {
        return false; //TODO
    }

    public Turn getActiveTurn() {
        return controllerChan.getCurrentTurn();
    }

    public Player getActivePlayer() {
        return null; //TODO
    }

    public Pair<Point, MapTile> getTile(PlayerType playerType) {
        return null; //TODO
    }

    public Pair<Pair<Point, MapTile>, Pair<Point, MapTile>> getTile(ArtifactType artifactType) {
        return null; //TODO
    }

    public List<Pair<Point, MapTile>> getTemples() {
        return null; //TODO
    }

    public MapTile getTile(Point point) {
        return null; //TODO
    }

    public List<Player> getAllPlayers() {
        return null; //TODO
    }

    public List<Point> getDrainablePositionsOneMoveAway(Point position, PlayerType playerType) {
        return null; //TODO
    }

    public boolean anyPlayerHasCard(ArtifactCardType artifactCardType) {
        // TODO Auto-generated method stub
        return false;
    }
}