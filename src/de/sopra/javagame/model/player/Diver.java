package de.sopra.javagame.model.player;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Diver implementiert die Spielfigur "Taucher".
 *
 * @author Max Bühmann, Melanie Arnds
 */
public class Diver extends Player {

    private PlayerType type;

    private String name;

    private Point position;
    
    private Turn turn;

    private int actionsLeft;

    private boolean isAI;

    private Collection<ArtifactCard> hand;
    
    public Diver (String name, Point position, Turn turn){
        this.type = PlayerType.DIVER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = false;
    }    
    
    public Diver (String name, Point position, Turn turn, boolean isAI){
        this.type = PlayerType.DIVER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = isAI;
    }
    
    /**
     * legalMoves übergibt ein Listli von Koordinaten-Punkten, die der Taucher regelkonform erreichen kann.
     *
     * @return ein Listli mit legal erreichbaren {@link MapTile}
     */

    @Override
    public List<Point> legalMoves(boolean specialActive) {
        return null;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
