package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.player.PlayerType;

public class MapFull extends Map<MapTile> {
    MapFull() {
        super();
    }

    public MapFull(MapFull from) {
        super(from.raw);
    }

    MapFull(MapTile[][] raw) throws IllegalArgumentException {
        super(raw);
    }

    /**
     * Gibt den Anfangspunkt der Spielerfigur auf der Karte zurück.
     *
     * @param player Die Figur, dessen Anfangspunkt bestimmt werden soll.
     * @return Der Anfangspunkt oder <code>null</code>, wenn kein Anfangspunkt für die Figur gefunden werden kann.
     */
    public Point getPlayerSpawnPoint(PlayerType player) {
        if (player == PlayerType.NONE) {
            return null;
        }

        for (int y = 0; y < SIZE_Y; ++y) {
            for (int x = 0; x < SIZE_X; ++x) {
                if (get(x, y) != null && get(x, y).getProperties().getSpawn() == player) {
                    return new Point(x, y);
                }
            }
        }

        return null;
    }

    public MapTile get(MapTileProperties tileProperties) {
        return this.get(this.getPositionForTile(tileProperties));
    }

    public Point getPositionForTile(MapTileProperties tileProperties) {
        for (int y = 0; y < SIZE_Y; ++y) {
            for (int x = 0; x < SIZE_X; ++x) {
                if (get(x, y) != null && get(x, y).getProperties() == tileProperties) {
                    return new Point(x, y);
                }
            }
        }

        return null;
    }

    @Override
    protected MapTile[][] newEmptyRaw() {
        return new MapTile[Map.SIZE_Y + 2][Map.SIZE_X + 2];
    }
}
