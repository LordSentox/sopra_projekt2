package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Direction;

import java.awt.*;

/**
 * Engineer implementiert die Spielfigur "Ingenieur".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Engineer extends Player {

    private boolean hasExtraDrain;

    public Engineer(String name, Point position, Turn turn) {
        super(PlayerType.ENGINEER, name, turn);
        this.position = position;
        this.isAI = false;
        this.hasExtraDrain = false;
    }

    public Engineer(String name, Point position, Turn turn, boolean isAI) {
        super(PlayerType.ENGINEER, name, turn);
        this.position = position;
        this.isAI = isAI;
        this.hasExtraDrain = false;
    }

    /**
     * drain wandelt den State des {@link MapTile} in DRY um. {@link MapTileState}
     * Wenn der Engineer zweimal direkt aufeinander folgend drained, wird ihm nur eine Aktion dafür abgezogen.
     *
     * @param position Koordinate des zu verändernden MapTiles
     * @return false, wenn Fehler eingetroffen, true sonst
     */

    @Override
    public boolean drain(Point position) {
        if (this.hasExtraDrain) {
            this.actionsLeft++;
        }
        
        boolean drained = super.drain(position);
        if (!drained && this.hasExtraDrain) {
            this.actionsLeft--;
        }
        else if (drained && this.hasExtraDrain) {
            this.hasExtraDrain = false;
        }
        else if (drained && !this.hasExtraDrain) {
            this.hasExtraDrain = true;
        }
        
        return drained;
    }

    @Override
    public Player copy() {
        Player player = new Engineer(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
