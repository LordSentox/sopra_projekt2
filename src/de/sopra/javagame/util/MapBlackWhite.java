package de.sopra.javagame.util;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;

import java.util.Arrays;

/**
 * Beschreibt eine Karte, die nur anzeigt, wo sich Wasser, und wo sich Land befindet.
 *
 * @author Arne Dußin
 */
public class MapBlackWhite extends Map<Boolean> {
    public MapBlackWhite() {
        super();
    }

    public MapBlackWhite(MapBlackWhite from) {
        super(from.raw);
    }

    public MapBlackWhite(MapFull map) {
        super(new Boolean[Map.SIZE_Y + 2][Map.SIZE_X + 2]);

        for (int y = 0; y < Map.SIZE_Y; ++y) {
            for (int x = 0; x < Map.SIZE_X; ++x) {
                set(map.get(x, y) != null, x, y);
            }
        }

    }

    public MapBlackWhite(Boolean[][] raw) {
        super(raw);
    }

    @Override
    public Boolean[][] newEmptyRaw() {
        Boolean[][] atarashii = new Boolean[Map.SIZE_Y + 2][Map.SIZE_X + 2];

        for (int y = 0; y < Map.SIZE_Y + 2; ++y) {
            Arrays.fill(atarashii[y], false);
        }

        return atarashii;
    }

     public static String toString(MapTile[][] tiles) {
         final String[] mapString = {""};
        for (MapTile[] row : tiles) {
            for (MapTile tile : row) {
                if (tile == null) {
                    mapString[0] = mapString[0] + "   ☐   ";
                } else {
                    mapString[0] = mapString[0] + (tile.getState() == MapTileState.DRY ? "  d" : (tile.getState() == MapTileState.FLOODED ? "  f" : "  g")) + tile.getProperties().getIndex() + "  ";
                }
            }
            /*
            Arrays.stream(row).map(t -> {
                if (t == null)
                    mapString[0] = mapString[0] + "  ☐  ";
                else
                    mapString[0] = mapString[0] + (t.getState() == MapTileState.DRY ? "  d" : (t.getState() == MapTileState.FLOODED ? "  f" : "  g")) + t.getProperties().getIndex() + "  ";
                return null;
            }).forEach(t -> mapString[0] = mapString[0] + t);
            */
            mapString[0] = mapString[0] + "\n";

        }
        return mapString[0];
    }
}
