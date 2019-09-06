package de.sopra.javagame.model;

/**
 * Flutkarten können einen bestimmten Feldtyp fluten oder versenken, falls das Inselfeld schon überflutet war.
 */
public class FloodCard {
    /**
     * Das Inselfeld, welches von dieser Karte überflutet wird.
     */
    private MapTile tile;

    /**
     * Ruft Methode {@link FloodCard.tile.flood} auf.
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
}
