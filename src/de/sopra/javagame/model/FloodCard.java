package de.sopra.javagame.model;

import java.util.Objects;

/**
 * Flutkarten können einen bestimmten Feldtyp fluten oder versenken, falls das Inselfeld schon überflutet war.
 */
public class FloodCard implements Copyable<FloodCard> {
    /**
     * Das Inselfeld, welches von dieser Karte überflutet wird.
     */
    private MapTile tile;

    public FloodCard(MapTile tile) {
        this.tile = tile;
    }

    /**
     * Ruft Methode {@link MapTile#flood()} auf.
     * Diese setzt das der FloodCard entsprechende MapTile von DRY auf FLOODED oder von FLOODED auf GONE
     *
     * @throws IllegalStateException wenn das Feld bereits versunken war.
     *                               Die Karte hätte dann aus dem Stapel entfernt werden sollen.
     */
    void flood() throws IllegalStateException {
    }

    public MapTile getTile() {
        return tile;
    }

    public void setTile(MapTile tile) {
        this.tile = tile;
    }

    @Override
    public FloodCard copy() {
        return new FloodCard(tile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloodCard floodCard = (FloodCard) o;
        return tile.equals(floodCard.tile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tile);
    }
}
