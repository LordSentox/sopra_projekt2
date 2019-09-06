package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;

import java.util.List;

/**
 * Explorer implementiert die Spielfigur "Forscher".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Explorer extends Player {
    /**
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der Spieler sich regelkonform hinbewegen darf.
     * Der Forscher darf auch diagonal laufen.
     *
     * @param specialActive gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja, wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */
    @Override
    public List legalMoves(boolean specialActive) {
        return null;
    }

    /**
     * drainablePositions gibt ein Listli von Koordinaten-Punkten zurück, deren {@link MapTile} trockengelegt werden können.
     * Der Forscher darf Felder auch diagonal trocken legen.
     *
     * @return Listli
     */
    public List drainablePositions() {
        return null;
    }

}
