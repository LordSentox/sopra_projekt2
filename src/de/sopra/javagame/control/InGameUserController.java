package de.sopra.javagame.control;

import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;

public class InGameUserController {

    private final ControllerChan controllerChan;

    InGameUserController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    /**
     * Spielt eine Helikopterkarte in der aktuellen Situation im Spiel mit den gegebenen Funktionen.
     * Dabei werden alle Spieler in der Liste von der Start-Position an die gegebene Ziel-Position bewegt
     * und die Karte aus der Hand des sourcePlayer entfernt.
     *  @param sourcePlayer  der Spieler, welcher die Karte ausspielt, darf nicht <code>null</code> sein
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     * @param flightRoute   Der Startpunkt und der Endpunkt, welche die Route beschreibt, die die Spieler fliegen sollen
     * @param players       die Spieler, welche transportiert werden sollen, darf nicht leer sein
     */
    public void playHelicopterCard(PlayerType sourcePlayer, int handCardIndex, Pair<Point, Point> flightRoute, List<PlayerType> players) {
        //Überprüfen, ob das Spiel gewonnen ist --> TODO refresh und weitere Funktionalitäten ergänzen
        MapTile[][] map = controllerChan.getCurrentTurn().getTiles();
        Point heliPoint = MapUtil.getPlayerSpawnPoint(map, PlayerType.PILOT);
        Turn currentTurn = controllerChan.getJavaGame().getPreviousTurn();
        EnumSet<ArtifactType> artifactsFound = currentTurn.getDiscoveredArtifacts();
        boolean allOnHeliPoint = true;
                for (Player currentPlayer : currentTurn.getPlayers()) {
                    if (currentPlayer.getPosition() != heliPoint) {
                        allOnHeliPoint = false;
                }

            if (artifactsFound.size() == 4 && !artifactsFound.contains(ArtifactType.NONE) && allOnHeliPoint) {
                controllerChan.getCurrentTurn().setGameEnded(true);
                controllerChan.getCurrentTurn().setGameWon(true);
            }

        }
    }

    /**
     * Spielt eine Sandsackkarte in der aktuelle Situation im Spiel mit den gegebenen Funktionen.
     * Dabei wird das Zielfeld trockengelegt und die Karte aus der Hand des sourcePlayer entfernt.
     *
     * @param sourcePlayer  der Spieler, welcher die Karte ausspielt, darf nicht <code>null</code> sein
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     * @param destination   die Position auf der Karte, welche trockengelegt werden soll
     */
    public void playSandbagCard(PlayerType sourcePlayer, int handCardIndex, Point destination) {

    }

    /**
     * Wirft eine Karte von der Hand des Spielers auf den Artifaktstapel ab.
     *
     * @param sourcePlayer  die Spieler, welcher die Karte abwerfen soll
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     */
    public void discardCard(PlayerType sourcePlayer, int handCardIndex) {

    }

}
