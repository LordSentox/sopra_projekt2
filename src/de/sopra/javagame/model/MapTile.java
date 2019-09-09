package de.sopra.javagame.model;

import de.sopra.javagame.model.player.PlayerType;

import java.util.Objects;

/**
 * MapTile beschreibt die einzelnen Kacheln, die das Spielfeld bilden
 *
 * @author Hermann "Roxi" Bühmann, Melanie Arnds
 */
public class MapTile implements Copyable<MapTile> {
    private MapTileProperties properties;
    private MapTileState state;

    public MapTile(MapTileProperties properties) {
        this.properties = properties;
        this.state = MapTileState.DRY;
    }

    /**
     * Erstellt ein neues MapTile vom übergebenen Wert number.
     *
     * @param number Der Index des gesuchten MapTiles. Muss eine Zahl zwischen 0 und 23 sein
     */
    public static MapTile fromNumber(int number) {
        return new MapTile(MapTileProperties.getByIndex(number));
    }

    /**
     * setzt den state des MapTile von FLOODED auf DRY
     */
    public void drain() {

    }

    /**
     * setzt den state des MapTile von DRY auf FLOODED oder von FLOODED auf GONE
     *
     * @return false wenn Fehler, true, sonst
     */
    public boolean flood() {
        return false;
    }

    public MapTileState getState() {
        return this.state;
    }

    void setState(MapTileState state) {
        this.state = state;
    }

    public boolean hasPlayerSpawn() {
        return properties.getSpawn() != PlayerType.NONE;
    }

    public boolean hasHiddenArtifact() {
        return this.properties.getHidden() != ArtifactType.NONE;
    }

    public int getTileIndex() {
        return this.properties.getIndex();
    }

    public MapTileProperties getProperties() {
        return this.properties;
    }

    @Override
    public MapTile copy() {
        MapTile mapTile = new MapTile(this.properties);
        mapTile.state = this.state;
        return mapTile;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;

        MapTile tile = (MapTile) other;
        return Objects.equals(this.properties, tile.properties) &&
                Objects.equals(this.state, tile.state);
    }
}
