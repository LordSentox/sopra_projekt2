package de.sopra.javagame.model;

import java.io.Serializable;
import java.util.Objects;

import de.sopra.javagame.util.MapFull;

import static de.sopra.javagame.model.MapTileState.*;

/**
 * Flutkarten können einen bestimmten Feldtyp fluten oder versenken, falls das
 * Inselfeld schon überflutet war.
 */
public class FloodCard implements Copyable<FloodCard>, Serializable {

    private static final long serialVersionUID = -3623493566759576687L;
    /**
     * Das Inselfeld, welches von dieser Karte überflutet wird.
     */
    private MapTileProperties tile;

    public FloodCard(MapTileProperties tile) {
        this.tile = tile;
    }

    /**
     * Ruft Methode {@link MapTile#flood()} auf. Diese setzt das der FloodCard
     * entsprechende MapTile von DRY auf FLOODED oder von FLOODED auf GONE
     *
     * @throws IllegalStateException
     *             wenn das Feld bereits versunken war. Die Karte hätte dann aus
     *             dem Stapel entfernt werden sollen.
     */
    public void flood(MapFull map) throws IllegalStateException {
        // Überprüfe, ob das Feld bereits entfernt wurde. Diese Karte hätte dann
        // nicht gespielt
        // werden können dürfen.        
        MapTile tile = map.get(map.getPositionForTile(this.tile));
        if (tile == null || tile.getState() == GONE) {
            throw new IllegalStateException();
        } else if (tile.getState() == DRY) {
            tile.setState(FLOODED);
        } else {
            tile.setState(GONE);
        }
    }

    public MapTileProperties getTile() {
        return tile;
    }

    public void setTile(MapTileProperties tile) {
        this.tile = tile;
    }

    @Override
    public FloodCard copy() {
        return new FloodCard(tile);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        FloodCard floodCard = (FloodCard) other;
        return floodCard.tile.equals(this.tile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tile);
    }
}
