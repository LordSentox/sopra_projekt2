package de.sopra.javagame.model.player;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Explorer implementiert die Spielfigur "Forscher".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Explorer extends Player {

    private PlayerType type;

    private String name;

    private Point position;
    
    private Turn turn;

    private int actionsLeft;

    private boolean isAI;

    private Collection<ArtifactCard> hand;
    
    public Explorer (String name, Point position, Turn turn){
        this.type = PlayerType.EXPLORER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = false;
    } 
    
    public Explorer (String name, Point position, Turn turn, boolean isAI){
        this.type = PlayerType.EXPLORER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
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
