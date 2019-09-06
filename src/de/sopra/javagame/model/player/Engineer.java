package de.sopra.javagame.model.player;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Engineer implementiert die Spielfigur "Ingenieur".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Engineer extends Player {

    private PlayerType type;

    private String name;

    private Point position;
    
    private Turn turn;

    private int actionsLeft;

    private boolean isAI;

    private Collection<ArtifactCard> hand;
    
    private boolean hasExtraDrain;
    
    public Engineer (String name, Point position, Turn turn){
        this.type = PlayerType.ENGINEER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.isAI = false;
        this.hand = new ArrayList<ArtifactCard>();
        this.hasExtraDrain = false;
    } 
    
    public Engineer (String name, Point position, Turn turn, boolean isAI){
        this.type = PlayerType.ENGINEER;
        this.name = name;
        this.position = position;
        this.turn = turn;
        this.actionsLeft = 0;
        this.isAI = isAI;
        this.hand = new ArrayList<ArtifactCard>();
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
        return false;
    }

    @Override
    public Player copy() {
        return null; //TODO
    }
}
