package de.sopra.javagame.model.player;

import de.sopra.javagame.model.*;

import java.awt.*;
import java.util.Collection;
import java.util.List;

/**
 * @author Max Bühmann, Melanie Arnds
 * Player beschreibt die Basisfunktionen, die jede Spielfigur ausführen kann.
 */
public class Player implements Copyable<Player> {

    private String name;

    private Point position;

    private int actionsLeft;

    private boolean isAI;

    private PlayerType type;

    private Turn turn;

    private Collection<ArtifactCard> hand;

    /**
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der Spieler sich regelkonform hinbewegen darf
     *
     * @param specialActive gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja, wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */
    List<Point> legalMoves(boolean specialActive) {
        return null;
    }

    /**
     * move bewegt den Spieler zur angegebenen destination, zieht dabei eine Aktion ab, wenn costsAction true ist
     *
     * @param destination Zielkoordinaten
     * @param costsAction wenn false, wird keine Action abgezogen, wenn true, wird eine abgezogen
     * @return false, wenn es einen Fehler gab, true, sonst
     */
    boolean move(Point destination, boolean costsAction) {
        return false;
    }

    /**
     * canMoveOthers gibt an, ob der Spieler andere bewegen kann. Spezialfähigkeit des {@link Navigator}
     *
     * @return false, wenn Spieler andere nicht bewegen kann, true, sonst.
     */
    boolean canMoveOthers() {
        return false;
    }

    /**
     * drainablePositions gibt ein Listli von Koordinaten-Punkten zurück, deren {@link MapTile} trockengelegt werden können.
     *
     * @return Listli
     */

    List<Point> drainablePositions() {
        return null;
    }

    /**
     * drain wandelt den State des {@link MapTile} in DRY um. {@link MapTileState}
     *
     * @param position Koordinate des zu verändernden MapTiles
     * @return false, wenn Fehler eingetroffen, true sonst
     */
    boolean drain(Point position) {
        return false;
    }

    /**
     * collectArtifact prüft, ob und auf welchem Typ eines {@link MapTile} der Spieler steht.
     * Es wird geprüft, ob der Spieler mindestens vier zum Typ passende Karten auf der Hand hat.
     * Wenn ja, werden vier passende Karten abgeworfen.
     *
     * @return den betroffenen ArtefaktTypen, wenn ein Artefakt collected wurde, none, sonst
     */

    ArtifactType collectArtifact() {
        return null;
    }

    /**
     * legalReceivers legt ein Listli von Player an, denen Handkarten regelkonform übergeben werden dürfen.
     *
     * @return das erstellte Listli, wenn Player exisitieren, denen Handkarten übergeben werden dürfen. Null, sonst.
     */
    List<Player> legalReceivers() {
        return null;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
