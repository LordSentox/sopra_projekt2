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

    private PlayerType type;

    private String name;

    private Point position;
    
    private Turn turn;

    private int actionsLeft;

    private boolean isAI;

    private Collection<ArtifactCard> hand;

    /**
     * Der Pilot kann seine Spezialfähigkeit nur einmal pro Zug nutzen, also wird sie nach benutzen auf false gesetzt
     */
    private boolean hasSpecialMove;
    
    public Pilot (String name, Point position, Turn turn){
        this.type = PlayerType.PILOT;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = false;
    } 
    
    public Pilot (String name, Point position, Turn turn, boolean isAI){
        this.type = PlayerType.PILOT;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = isAI;
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

}
