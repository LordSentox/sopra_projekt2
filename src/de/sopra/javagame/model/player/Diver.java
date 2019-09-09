package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.CopyUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        return null;
    }

    @Override
    public Player copy() {
        Player player = new Diver(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
