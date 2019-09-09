package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Turn;

import java.awt.*;

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

    public Navigator(String name, Point position, Turn turn) {
        super(PlayerType.NAVIGATOR, name, turn);
        this.position = position;
        this.isAI = false;
        this.hasExtraPush = false;
    }

    public Navigator(String name, Point position, Turn turn, boolean isAI) {
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

    @Override
    public Player copy() {
        return null; //TODO
    }
}
