package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.Point;

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

    public Navigator(String name, Point position, Action action) {
        super(PlayerType.NAVIGATOR, name, action);
        this.position = position;
        this.isAI = false;
        this.hasExtraPush = false;
    }

    public Navigator(String name, Point position, Action action, boolean isAI) {
        super(PlayerType.NAVIGATOR, name, action);
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

        // Ist das Feld, auf das der Spieler bewegt werden soll ein Inselfeld?
        Point newPosition = other.getPosition().add(direction);
        MapTile destinationTile = this.action.getMap().get(newPosition);
        if (destinationTile == null || destinationTile.getState() == GONE) {
            return false;
        }

        if (!this.hasExtraPush)
            --this.actionsLeft;
        this.hasExtraPush = !this.hasExtraPush;

        other.getPosition().setLocation(newPosition);
        return true;
    }

    public boolean move(Point destination, boolean costsAction, boolean specialActive) {
        boolean success = super.move(destination, costsAction, specialActive);
        if (success) {
            this.hasExtraPush = false;
        }

        return success;
    }

    public boolean drain(Point position) {
        boolean success = super.drain(position);
        if (success) {
            this.hasExtraPush = false;
        }

        return success;
    }

    @Override
    public Player copy() {
        Navigator player = new Navigator(CopyUtil.copy(this.name), new Point(position), null);
        player.hasExtraPush = this.hasExtraPush;
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
