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
     * Überflutet das in {@link #tile} gespeicherte Feld.
     *
     * @throws IllegalStateException wenn das Feld bereits versunken war. Die Karte hätte dann aus dem Stapel entfernt werden sollen.
     */
    void flood() throws IllegalStateException {
    }
}
