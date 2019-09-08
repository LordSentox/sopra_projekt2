package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;

import java.awt.*;
import java.util.List;

/**
 * Explorer implementiert die Spielfigur "Forscher".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Explorer extends Player {
    
    public Explorer (String name, Point position, Turn turn){
        super(PlayerType.EXPLORER, name, turn);
        this.position = position;
        this.isAI = false;
    } 
    
    public Explorer (String name, Point position, Turn turn, boolean isAI){
        super(PlayerType.EXPLORER, name, turn);
        this.position = position;
        this.isAI = isAI;
    }
       
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

    @Override
    public Player copy() {
        return null; //TODO
    }
}
