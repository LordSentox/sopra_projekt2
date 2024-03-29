package de.sopra.javagame.util.map;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class MapFull extends Map<MapTile> {
    MapFull() {
        super();
    }

    public MapFull(MapFull from) {
        this(from.raw);
    }

    MapFull(MapTile[][] raw) throws IllegalArgumentException {
//        if (raw == null || raw.length - 2 != SIZE_Y)
//            throw new IllegalArgumentException();
        checkRaw(raw);
        if (Arrays.stream(raw).anyMatch(row -> row.length - 2 != SIZE_X)) throw new IllegalArgumentException();

        this.raw = this.newEmptyRaw();
        for (int y = 0; y < raw.length; y++) {
            for (int x = 0; x < raw[y].length; x++) {
                MapTile tile = raw[y][x];
                this.raw[y][x] = tile == null ? null : tile.copy();
            }
        }
    }
    
    //#PMD
    public void checkRaw(MapTile[][] raw){
      if (raw == null || raw.length - 2 != SIZE_Y)
      throw new IllegalArgumentException();
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

    public Collection<Point> validPoints() {
        Collection<Point> points = new LinkedList<>();
        for (int y = 0; y < SIZE_Y; ++y) {
            for (int x = 0; x < SIZE_X; ++x) {
                if (get(x, y) != null) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
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
