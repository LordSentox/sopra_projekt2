package de.sopra.javagame.control;

import de.sopra.javagame.model.player.PlayerType;

import java.awt.*;
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
     *
     * @param sourcePlayer  der Spieler, welcher die Karte ausspielt, darf nicht <code>null</code> sein
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     * @param flyFrom       die Start-Position des Helikopters
     * @param flyTo         die Ziel-Position des Helikopters
     * @param players       die Spieler, welche transportiert werden sollen, darf nicht leer sein
     */
    public void playHelicopterCard(PlayerType sourcePlayer, int handCardIndex, Point flyFrom, Point flyTo, List<PlayerType> players) {

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
