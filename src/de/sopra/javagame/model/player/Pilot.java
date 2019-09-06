package de.sopra.javagame.model.player;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Pilot implementiert die gleichnamige Spielfigur.
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Pilot extends Player {

    /**
     * Der Pilot kann seine Spezialfähigkeit nur einmal pro Zug nutzen, also wird sie nach benutzen auf false gesetzt
     */
    private boolean hasSpecialMove;
    
    public Pilot (String name, Point position, Turn turn){
        super(PlayerType.PILOT, name, turn);
        this.position = position;
        this.isAI = false;
        this.hasSpecialMove = false;
    } 
    
    public Pilot (String name, Point position, Turn turn, boolean isAI){
        super(PlayerType.PILOT, name, turn);
        this.position = position;
        this.isAI = isAI;
        this.hasSpecialMove = false;
    }
        
    /**
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der Spieler sich regelkonform hinbewegen darf.
     * Wenn specialActive, dann werden alle {@link MapTile} der Liste hinzugefügt, die nicht GONE sind
     *
     * @param specialActive gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja, wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */
    @Override
    public List<Point> legalMoves(boolean specialActive) {
        return null;
    }

    /**
     * move bewegt den Spieler zur angegebenen destination, zieht dabei eine Aktion ab, wenn costsAction true ist
     *
     * @param destination Zielkoordinaten
     * @param costsAction wenn false, wird keine Action abgezogen, wenn true, wird eine abgezogen
     * @return false, wenn es einen Fehler gab, true, sonst
     */
    @Override
    public boolean move(Point destination, boolean costsAction) {
        return false;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
