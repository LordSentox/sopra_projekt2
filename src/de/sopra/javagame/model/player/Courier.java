package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Turn;

import java.awt.*;
import java.util.List;

/**
 * Courier implementiert die Spielfigur "Bote".
 *
 * @author Max Bühmann, Melanie Arnds
 */
public class Courier extends Player {
    
    public Courier (String name, Point position, Turn turn){
        super(PlayerType.COURIER, name, turn);
        this.position = position;
        this.isAI = false;
    } 
    
    public Courier (String name, Point position, Turn turn, boolean isAI){
        super(PlayerType.COURIER, name, turn);
        this.position = position;
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
