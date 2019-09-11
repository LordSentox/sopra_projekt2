package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.*;

import java.util.List;

public class ActivePlayerController {

    private final ControllerChan controllerChan;

    ActivePlayerController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    /**
     * Zeigt alle Bewegungsmöglichkeiten des aktiven Spieler an.
     *
     * @param specialActive
     *            Falls <code>true</code>, werden die
     *            Spezialbewegungsfähigkeiten des jeweiligen Spielers
     *            miteinberechnet. Sonst. weiden nur die
     *            Standardbewegungsmöglichkeiten angezeigt.
     */
    public void showMovements(boolean specialActive) {
        MapTile[][] map = controllerChan.getCurrentTurn().getTiles();
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        List<de.sopra.javagame.util.Point> movements = player.legalMoves(specialActive);
        controllerChan.getInGameViewAUI().refreshMovementOptions(movements);

    }

    /**
     * Zeigt alle Felder an, die vom aktiven Spieler trockengelegt werden
     * können.
     */
    public void showDrainOptions() {
        MapTile[][] map = controllerChan.getCurrentTurn().getTiles();
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        List<Point> drainable = player.drainablePositions();
        controllerChan.getInGameViewAUI().refreshDrainOptions(drainable);
     
    }

    /**
     * Zeigt die Möglichkeiten an, die Spezialfähigkeit des aktiven Spielers
     * anzuwenden. Zum Beispiel werden beim Piloten alle Felder angezeigt, auf
     * die er mit seiner Spezialfähigkeit ziehen kann.
     *
     * @see PlayerType
     */
    public void showSpecialAbility() {
        //TODO: ergänzen die Methode später.
        MapTile[][] map = controllerChan.getCurrentTurn().getTiles();
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        if (player.getType() == PlayerType.PILOT) {
            List<Point> movements = new Pilot(player.getName(), player.getPosition(), currentTurn).legalMoves(true);
            controllerChan.getInGameViewAUI().refreshMovementOptions(movements);
        } else if (player.getType() == PlayerType.COURIER){
            controllerChan.getInGameViewAUI().showNotification("Courier darf die Artefaktkarten an einen beliebigen Mitspieler übergeben!");
        } else if (player.getType() == PlayerType.DIVER){
            List<Point> movements = new Diver(player.getName(), player.getPosition() , currentTurn).legalMoves(true);
            controllerChan.getInGameViewAUI().refreshMovementOptions(movements);
        } else if (player.getType() == PlayerType.EXPLORER){
            List<Point> movements = new Explorer(player.getName(), player.getPosition(), currentTurn).legalMoves(true);
            List<Point> drainables = new Explorer(player.getName(), player.getPosition(), currentTurn).drainablePositions();
            controllerChan.getInGameViewAUI().refreshMovementOptions(movements);
            controllerChan.getInGameViewAUI().refreshDrainOptions(drainables);
        } else if (player.getType() == PlayerType.ENGINEER){
            controllerChan.getInGameViewAUI().showNotification("Engineer darf gleichzeitig zwei InselFeldern trochen legen!");
        } else if (player.getType() == PlayerType.NAVIGATOR) {
            controllerChan.getInGameViewAUI().showNotification("Navigator darf einen anderen Abenteurer um bis zu 2 oder 2 andere Abenteurer um jeweils 1 Inselfeld bewegen!");
        }
        

    }

    /**
     * Bricht das einsetzen einer Spezialfähigkeit ab, das vorher durch
     * {@link #showSpecialAbility()} eingeleitet wurde.
     *
     * @see PlayerType
     * @see #showSpecialAbility
     */
    public void cancelSpecialAbility() {

    }

    /**
     * Zeigt an, ob der aktive Spieler dem gegebenen Spieler eine Karte geben
     * kann
     *
     * @param targetPlayer
     *            Der Spieler, dem eine Karte gegeben werden soll.
     */
    public void showTransferable(PlayerType targetPlayer) {
        MapTile[][] map = controllerChan.getCurrentTurn().getTiles();
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        

    }

    /**
     * Gibt die Karte mit dem gegebenen Index aus der Hand des aktiven Spielers,
     * dem gegebenen anderen Spieler
     *
     * @param handCardIndex
     *            Der Index der Karte, die übergeben werden soll.
     * @param targetPlayer
     *            Der Spieler, dem die Karte gegeben werden soll
     */
    public void transferCard(int handCardIndex, PlayerType targetPlayer) {

    }

    /**
     * Sammelt das Artefakt, das auf dem {@link MapTile} erhältlich ist, falls
     * der Spieler die nötigen Artefaktkarten besitzt
     *
     * @see ArtifactCard
     */
    public void collectArtifact() {

    }

    /**
     * Bewegt den aktiven Spieler auf das von dem {@link Point} angegebenen
     * {@link MapTile}
     *
     * @param destination
     *            Die Position des {@link MapTile}, auf das der Spieler bewegt
     *            werden soll
     * @param useSpecial
     *            Falls true, benutzt der Spieler seine Spezialfähigkeit, um
     *            sich auf das Feld zu bewegen
     */
    public void move(Point destination, boolean useSpecial) {

    }

    /**
     * Bewegt einen anderen Spieler in eine Richtung.
     *
     * @param direction
     *            Die Richtung in die sich der Spieler um ein Feld bewegt werden
     *            soll.
     * @param target
     *            Der Spieler, der Bewegt werden soll.
     * @see Direction
     * @see PlayerType
     */
    public void moveOther(Direction direction, PlayerType target) {

    }

    /**
     * Legt alle Felder trocken an den gegebenen Positionen.
     *
     * @param position
     *            Die Positionen aller Felder die Trockengelegt werden sollen.
     */
    public void drain(Point... position) {

    }

    /**
     * Zeigt einen Spielhinweis an. Danach wird dieses Spiel nicht mehr in die
     * Bestenliste aufgenommen
     *
     * @see HighScore
     */
    public void showTip() {

    }

    /**
     * Beendet den Zug und startet den nächsten Zug.
     */
    public void endTurn() {

    }
}
