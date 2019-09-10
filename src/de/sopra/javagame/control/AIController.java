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

    /**
     * Ob der aktive Spieler Karten abwerfen muss
     *
     * @return <code>true</code> wenn der Spieler Karten abwerfen muss, sonst <code>false</code>
     */
    public boolean isCurrentlyDiscarding() {
        return false; //TODO
    }

    /**
     * Der aktive Zug, an welchem Änderung vorgenommen werden dürfen
     *
     * @return der aktuelle Zug
     */
    public Turn getActiveTurn() {
        return controllerChan.getCurrentTurn();
    }

    /**
     * Der aktive Spieler, für welchen die KI berechnen soll
     *
     * @return der aktive Spieler
     */
    public Player getActivePlayer() {
        return null; //TODO
    }

    /**
     * Berechne MapTile und Startpunkt in dem aktuellen Spiel für einen Spieler
     *
     * @param playerType der Typ des Spielers
     * @return ein Pair aus Punkt und MapTile
     */
    public Pair<Point, MapTile> getTile(PlayerType playerType) {
        return null; //TODO
    }

    /**
     * Berechne beide Tempel Positionen und deren MapTiles
     *
     * @param artifactType der Typ der Tempel
     * @return ein Pair bestehend aus wiederum zwei Pairs,
     * die jeweils die Position und das zugehörige MapTile eines Tempels beinhalten
     */
    public Pair<Pair<Point, MapTile>, Pair<Point, MapTile>> getTile(ArtifactType artifactType) {
        return null; //TODO
    }

    /**
     * Berechne eine Liste aller Tempelpunkte
     *
     * @return eine Liste aller Tempelpunkte (erwartete Länge: 4x2=8)
     */
    public List<Pair<Point, MapTile>> getTemples() {
        return null; //TODO
    }

    /**
     * Berechne das MapTile zu einem Punkt
     *
     * @param point der Punkt
     * @return das MapTile zu einem Punkt
     */
    public MapTile getTile(Point point) {
        return null; //TODO
    }

    /**
     * Eine Liste aller aktiven Spieler
     *
     * @return die Liste der aktiven Spieler im aktuellen Zustand
     */
    public List<Player> getAllPlayers() {
        return null; //TODO
    }

    /**
     * Eine Liste alle drainable positions nachdem ein Schritt auf jede Position von {@link Player#legalMoves(boolean)}
     * vorgenommen wurde. Positionen, die vom Startpunkt aus drainable sind sollen nicht in der Liste enthalten sein
     *
     * @param position   der Startpunkt
     * @param playerType der Spielertyp, welcher die legalMoves berechnen soll
     * @return eine Liste aller drainable positions nach einem Schritt
     */
    public List<Point> getDrainablePositionsOneMoveAway(Point position, PlayerType playerType) {
        return null; //TODO
    }

    /**
     * Prüft ob ein beliebiger aktiver Spieler eine Karte des gegebenen Types in der Hand hält
     *
     * @param artifactCardType der Typ der gesuchten Karte
     * @return <code>true</code> wenn ein beliebiger Spieler mindestens eine solche Karte in der Hand hält
     */
    public boolean anyPlayerHasCard(ArtifactCardType artifactCardType) {
        // TODO Auto-generated method stub
        return false;
    }
}