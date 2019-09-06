package de.sopra.javagame.model;

/**
 * MapTile beschreibt die einzelnen Kacheln, die das Spielfeld bilden
 *
 * @author Hermann "Roxi" Bühmann, Melanie Arnds
 */

import de.sopra.javagame.model.player.PlayerType;

public class MapTile implements Copyable<MapTile> {

    private String name;

    private PlayerType playerSpawn;

    private MapTileState state;

    private ArtifactType hiddenArtifact;

    public MapTile(String name, PlayerType playerSpawn, ArtifactType hiddenArtifact) {
        this.name = name;
        this.playerSpawn = playerSpawn;
        this.state = MapTileState.DRY;
        this.hiddenArtifact = hiddenArtifact;
    }

    /**
     * setzt den state des MapTile von FLOODER auf DRY
     */
    void drain() {

    }

    /**
     * setzt den state des MapTile von DRY auf FLOODED oder von FLOODED auf GONE
     *
     * @return false wenn Fehler, true, sonst
     */
    boolean flood() {
        return false;
    }

    MapTileState getState() {
        return this.state;
    }

    void setState(MapTileState state) {
        this.state = state;
    }

    @Override
    public MapTile copy() {
        MapTile mapTile = new MapTile(new String(name.toCharArray()), playerSpawn, hiddenArtifact);
        mapTile.state = this.state;
        return mapTile;
    }
}
