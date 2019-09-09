package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.Direction;

import java.awt.*;

import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * Navigator implementiert die gleichnamige Spielfigur.
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Navigator extends Player {

    /**
     * er kann zwei spieler (auch zweimal den gleichen) pro aktion pushen
     */
    private boolean hasExtraPush;
    
    public Navigator (String name, Point position, Turn turn){
        super(PlayerType.NAVIGATOR, name, turn);
        this.position = position;
        this.isAI = false;
        this.hasExtraPush = false;
    } 
    
    public Navigator (String name, Point position, Turn turn, boolean isAI){
        super(PlayerType.NAVIGATOR, name, turn);
        this.position = position;
        this.isAI = isAI;
        this.hasExtraPush = false;
    }
       
    /**
     * Der Navigator kann auch andere Spieler bewegen.
     */
    public boolean canMoveOthers() {
        return true;
    }

    /**
     * Methode um einen Spieler ohne Kosten von seinen Aktionspunkten zu bewegen
     *
     * @param direction (oben, unten, rechts, links) Richtung in die bewegt werden darf
     * @param other     der zu bewegende Spieler
     * @return gibt zurück, ob das Bewegen erfolgreich war
     */
    @Override
    public boolean forcePush(Direction direction, Player other) {
        // Wenn der Navigator nicht mehr genügend Aktionspunkte oder einen extraPush hat,
        // kann der Spieler nicht bewegt werden.
        if (this.actionsLeft == 0 && !this.hasExtraPush) {
            return false;
        }
        --this.actionsLeft;

        int deltaX = 0;
        int deltaY = 0;
        switch (direction) {
            case UP:
                deltaY = -1;
                break;
            case LEFT:
                deltaX = -1;
                break;
            case DOWN:
                deltaY = 1;
                break;
            case RIGHT:
                deltaX = 1;
                break;
        }

        // Ist das Feld, auf das der Spieler bewegt werden soll ein Inselfeld?
        Point newPosition = new Point(other.getPosition());
        newPosition.translate(deltaX, deltaY);
        MapTile destinationTile = this.turn.getTile(newPosition);
        if (destinationTile == null || destinationTile.getState() == GONE) {
            return false;
        } else {
            if (this.hasExtraPush) {
                this.hasExtraPush = false;
            } else {
                --this.actionsLeft;
                this.hasExtraPush = true;
            }

            other.getPosition().translate(deltaX, deltaY);
            return true;
        }
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
