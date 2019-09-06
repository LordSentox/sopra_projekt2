package de.sopra.javagame.model;

/**
 * MapTile beschreibt die einzelnen Kacheln, die das Spielfeld bilden
 *
 * @author Hermann "Roxi" Bühmann, Melanie Arnds
 */

import de.sopra.javagame.model.player.PlayerType;

public class MapTile {

    private String name;

    private PlayerType playerSpawn;

    private MapTileState state;

    private ArtifactType hiddenArtifact;

    /**
     * setzt den state des MapTile von FLOODER auf DRY
     */
    public void drain() {

    }

    /**
     * setzt den state des MapTile von DRY auf FLOODED oder von FLOODED auf GONE
     * @return false wenn Fehler, true, sonst
     */
    public void flood(){
        
    }

    public MapTileState getState() {
        return this.state;
    }

    public void setState(MapTileState state) {
        this.state = state;
    }
}
