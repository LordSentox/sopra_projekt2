package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.CopyUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Explorer implementiert die Spielfigur "Forscher".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Explorer extends Player {
    
    public Explorer (String name, Point position, Turn turn){
        super(PlayerType.EXPLORER, name, turn);
        this.position = position;
        this.isAI = false;
    } 
    
    public Explorer (String name, Point position, Turn turn, boolean isAI){
        super(PlayerType.EXPLORER, name, turn);
        this.position = position;
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
        if (!specialActive)
            return super.legalMoves(specialActive);
        if (actionsLeft >= 1) {
            List<Point> movement = new ArrayList<>();
            MapTile right = this.turn.getTiles()[position.y][position.x + 1];
            if (right != null && right.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y, position.x + 1));
            }
            MapTile left = this.turn.getTiles()[position.y][position.x - 1];
            if (left != null && left.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y, position.x - 1));
            }
            MapTile up = this.turn.getTiles()[position.y - 1][position.x];
            if (up != null && up.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y - 1, position.x));
            }
            MapTile down = this.turn.getTiles()[position.y + 1][position.x];
            if (down != null && down.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y + 1, position.x));
            }
            MapTile topLeft = this.turn.getTiles()[position.y - 1][position.x - 1];
            if (topLeft != null && topLeft.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y - 1, position.x - 1));
            }
            MapTile topRight = this.turn.getTiles()[position.y - 1][position.x + 1];
            if (topRight != null && topRight.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y - 1, position.x + 1));
            }
            MapTile bottomLeft = this.turn.getTiles()[position.y + 1][position.x - 1];
            if (bottomLeft != null && bottomLeft.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y + 1, position.x - 1));
            }
            MapTile bottomRight = this.turn.getTiles()[position.y + 1][position.x + 1];
            if (bottomRight != null && bottomRight.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y + 1, position.x + 1));
            }
            return movement;
        } else {

            return null;
        }
    }

    /**
     * drainablePositions gibt ein Listli von Koordinaten-Punkten zurück, deren {@link MapTile} trockengelegt werden können.
     * Der Forscher darf Felder auch diagonal trocken legen.
     *
     * @return Listli
     */
    public List drainablePositions() {
        if (actionsLeft >= 1) {
            List<Point> drainable = new ArrayList<>();
            MapTile right = this.turn.getTiles()[position.y][position.x + 1];
            if (right != null && right.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y, position.x + 1));
            }
            MapTile left = this.turn.getTiles()[position.y][position.x - 1];
            if (left != null && left.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y, position.x - 1));
            }
            MapTile up = this.turn.getTiles()[position.y - 1][position.x];
            if (up != null && up.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y - 1, position.x));
            }
            MapTile down = this.turn.getTiles()[position.y + 1][position.x];
            if (down != null && down.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y + 1, position.x));
            }
            MapTile topLeft = this.turn.getTiles()[position.y - 1][position.x - 1];
            if (topLeft != null && topLeft.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y - 1, position.x - 1));
            }
            MapTile topRight = this.turn.getTiles()[position.y - 1][position.x + 1];
            if (topRight != null && topRight.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y - 1, position.x + 1));
            }
            MapTile bottomLeft = this.turn.getTiles()[position.y + 1][position.x - 1];
            if (bottomLeft != null && bottomLeft.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y + 1, position.x - 1));
            }
            MapTile bottomRight = this.turn.getTiles()[position.y + 1][position.x + 1];
            if (bottomRight != null && bottomRight.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y + 1, position.x + 1));
            }
            return drainable;
        } else {

            return null;
        }
    }

    @Override
    public Player copy() {
        Player player = new Explorer(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
