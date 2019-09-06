package de.sopra.javagame.model.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.Turn;

/**
 * Navigator implementiert die gleichnamige Spielfigur.
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Navigator extends Player {

    private PlayerType type;

    private String name;

    private Point position;
    
    private Turn turn;

    private int actionsLeft;

    private boolean isAI;

    private Collection<ArtifactCard> hand;
    
    /**
     * er kann zwei spieler (auch zweimal den gleichen) pro aktion pushen
     */
    private boolean hasExtraPush;
    
    public Navigator (String name, Point position, Turn turn){
        this.type = PlayerType.NAVIGATOR;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = false;
        this.hasExtraPush = false;
    } 
    
    public Navigator (String name, Point position, Turn turn, boolean isAI){
        this.type = PlayerType.NAVIGATOR;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = isAI;
        this.hasExtraPush = false;
    }

    /**
     * Der Navigator kann auch andere Spieler bewegen.
     */
    public boolean canMoveOthers() {
        return true;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
