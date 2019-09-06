package de.sopra.javagame.control;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.HighScore;

import java.awt.*;

public class ActivePlayerController {

    private final ControllerChan controllerChan;

    ActivePlayerController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    /**
     * Zeigt alle Bewegungsmöglichkeiten des aktiven Spieler an.
     *
     * @param specialActive Falls <code>true</code>, werden die Spezialbewegungsfähigkeiten des jeweiligen Spielers miteinberechnet.
     *                      Sonst. weiden nur die Standardbewegungsmöglichkeiten angezeigt.
     */
    public void showMovements(boolean specialActive) {

    }

    /**
     * Zeigt alle Felder an, die vom aktiven Spieler trockengelegt werden können.
     */
    public void showDrainOptions() {

    }

    /**
     * Zeigt die Möglichkeiten an, die Spezialfähigkeit des aktiven Spielers anzuwenden.
     * Zum Beispiel werden beim Piloten alle Felder angezeigt, auf die er mit seiner Spezialfähigkeit ziehen kann.
     *
     * @see PlayerType
     */
    public void showSpecialAbility() {

    }

    /**
     * Bricht das einsetzen einer Spezialfähigkeit ab, das vorher durch {@link #showSpecialAbility()} eingeleitet wurde.
     *
     * @see PlayerType
     * @see #showSpecialAbility
     */
    public void cancelSpecialAbility() {

    }

    /**
     * Zeigt an, ob der aktive Spieler dem gegebenen Spieler eine Karte geben kann
     *
     * @param targetPlayer Der Spieler, dem eine Karte gegeben werden soll.
     */
    public void showTransferable(PlayerType targetPlayer) {

    }

    /**
     * Gibt die Karte mit dem gegebenen Index aus der Hand des aktiven Spielers, dem gegebenen anderen Spieler
     *
     * @param handCardIndex Der Index der Karte, die übergeben werden soll.
     * @param targetPlayer  Der Spieler, dem die Karte gegeben werden soll
     */
    public void transferCard(int handCardIndex, PlayerType targetPlayer) {

    }

    /**
     * Sammelt das Artefakt, das auf dem {@link MapTile} erhältlich ist, falls der Spieler die nötigen Artefaktkarten besitzt
     *
     * @see ArtifactCard
     */
    public void collectArtifact() {

    }

    /**
     * Bewegt den aktiven Spieler auf das von dem {@link Point} angegebenen {@link MapTile}
     *
     * @param destination Die Position des {@link MapTile}, auf das der Spieler bewegt werden soll
     * @param useSpecial  Falls true, benutzt der Spieler seine Spezialfähigkeit, um sich auf das Feld zu bewegen
     */
    public void move(Point destination, boolean useSpecial) {

    }

    /**
     * Bewegt einen anderen Spieler in eine Richtung.
     *
     * @param direction Die Richtung in die sich der Spieler um ein Feld bewegt werden soll.
     * @param target    Der Spieler, der Bewegt werden soll.
     * @see Direction
     * @see PlayerType
     */
    public void moveOther(Direction direction, PlayerType target) {

    }

    /**
     * Legt alle Felder trocken an den gegebenen Positionen.
     *
     * @param position Die Positionen aller Felder die Trockengelegt werden sollen.
     */
    public void drain(Point... position) {

    }

    /**
     * Zeigt einen Spielhinweis an. Danach wird dieses Spiel nicht mehr in die Bestenliste aufgenommen
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
