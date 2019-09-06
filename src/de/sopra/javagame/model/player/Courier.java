package de.sopra.javagame.model.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.Turn;

/**
 * Courier implementiert die Spielfigur "Bote".
 *
 * @author Max Bühmann, Melanie Arnds
 */
public class Courier extends Player {

    private PlayerType type;

    private String name;

    private Point position;
    
    private Turn turn;

    private int actionsLeft;

    private boolean isAI;

    private Collection<ArtifactCard> hand;
    
    public Courier (String name, Point position, Turn turn){
        this.type = PlayerType.COURIER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = false;
    } 
    
    public Courier (String name, Point position, Turn turn, boolean isAI){
        this.type = PlayerType.DIVER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<ArtifactCard>();
        this.isAI = isAI;
    }
    
    /**
     * legalReceivers gibt eine Liste aller anderen Spieler zurück.
     * (Der Bote darf allen anderen Spielern etwas übergeben, egal, wo sie sich befinden.)
     *
     * @return das Listli aller Spieler außer dem Boten selbst.
     */

    @Override
    public List<Player> legalReceivers() {
        return null;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
