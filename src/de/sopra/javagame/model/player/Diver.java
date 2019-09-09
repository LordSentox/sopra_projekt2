package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static de.sopra.javagame.model.MapTileState.DRY;

/**
 * Diver implementiert die Spielfigur "Taucher".
 *
 * @author Max Bühmann, Melanie Arnds
 */
public class Diver extends Player {
    
    public Diver (String name, Point position, Turn turn){
        super(PlayerType.DIVER, name, turn);
        this.position = position;
        this.isAI = false;
    } 
    
    public Diver (String name, Point position, Turn turn, boolean isAI){
        super(PlayerType.DIVER, name, turn);
        this.position = position;
        this.isAI = isAI;
    }
    
    /**
     * legalMoves übergibt ein Listli von Koordinaten-Punkten, die der Taucher regelkonform erreichen kann.
     *
     * @return ein Listli mit legal erreichbaren {@link MapTile}
     */
    @Override
    public List<Point> legalMoves(boolean specialActive) {
        if (!specialActive) {
            return super.legalMoves(false);
        }

        boolean[][] reachable = new boolean[12][12];
        // Initialisieren der Wasserwege, sofern es welche gibt
        MapTile up = this.turn.getTile(this.position.x, this.position.y - 1);
        MapTile left = this.turn.getTile(this.position.x - 1, this.position.y);
        MapTile down = this.turn.getTile(this.position.x, this.position.y + 1);
        MapTile right = this.turn.getTile(this.position.x + 1, this.position.y);
        if (up != null && up.getState() != DRY) {
            reachable[this.position.y - 1][this.position.x] = true;
        }
        if (left != null && left.getState() != DRY) {
            reachable[this.position.y][this.position.x - 1] = true;
        }
        if (down != null && down.getState() != DRY) {
            reachable[this.position.y + 1][this.position.x] = true;
        }
        if (right != null && right.getState() != DRY) {
            reachable[this.position.y][this.position.x + 1] = true;
        }

        // Suche dynamisch heraus, welche Positionen der Taucher alles erreichen kann. Dies beinhaltet allerdings
        // insbesondere alle Positionen auf untergegangenen Inselteilen, auf denen er nicht enden darf.
        boolean somethingChanged;
        do {
            somethingChanged = false;
            for (int y = 0; y < reachable.length; ++y) {
                for (int x = 0; x < reachable[y].length; ++x) {
                    if (reachable[y][x] && this.turn.getTile(x, y) != null && this.turn.getTile(x, y).getState() != DRY) {
                        if (!reachable[y - 1][x]) {
                            reachable[y - 1][x] = true;
                            somethingChanged = true;
                        }
                        if (!reachable[y][x - 1]) {
                            reachable[y][x - 1] = true;
                            somethingChanged = true;
                        }
                        if (!reachable[y + 1][x]) {
                            reachable[y + 1][x] = true;
                            somethingChanged = true;
                        }
                        if (!reachable[y][x + 1]) {
                            reachable[y][x + 1] = true;
                            somethingChanged = true;
                        }
                    }
                }
            }
        } while (somethingChanged);

        // Gebe alle Positionen zurück, welche der Taucher erreichen kann, aber filtere alle heraus, welche überflutet
        // sind, oder kein Inselfeld sind.
        List<Point> legalMoves = new ArrayList<>();
        for (int y = 0; y < reachable.length; ++y) {
            for (int x = 0; x < reachable[y].length; ++x) {
                if (reachable[y][x] && this.turn.getTile(x, y) != null && this.turn.getTile(x, y).getState() == DRY) {
                    legalMoves.add(new Point(x, y));
                }
            }
        }

        return legalMoves;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
