package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.CopyUtil;

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
        if (!hasExtraDrain) {
            MapTile mapTile = this.turn.getTiles()[position.y][position.x];
            if (mapTile.getState() == MapTileState.GONE || mapTile.getState() == MapTileState.DRY) {
                return false;
            } else {
                mapTile.drain();
                return super.drain(position);
            }
        } else {
            return super.drain(position);
        }
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
