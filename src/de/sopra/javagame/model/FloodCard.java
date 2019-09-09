package de.sopra.javagame.model;

import static de.sopra.javagame.model.MapTileState.GONE;

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
        // Überprüfe, ob das Feld bereits entfernt wurde. Diese Karte hätte dann nicht gespielt
        // werden können dürfen.
        if (this.tile.getState() == GONE) {
            throw new IllegalStateException();
        }
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
}
