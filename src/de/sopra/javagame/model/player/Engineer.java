package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;

import java.awt.*;

/**
 * Engineer implementiert die Spielfigur "Ingenieur".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Engineer extends Player {

    private boolean hasExtraDrain;

    /**
     * drain wandelt den State des {@link MapTile} in DRY um. {@link MapTileState}
     * Wenn der Engineer zweimal direkt aufeinander folgend drained, wird ihm nur eine Aktion dafür abgezogen.
     *
     * @param position Koordinate des zu verändernden MapTiles
     * @return false, wenn Fehler eingetroffen, true sonst
     */

    @Override
    public boolean drain(Point position) {
        return false;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
