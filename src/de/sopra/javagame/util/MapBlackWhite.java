package de.sopra.javagame.util;

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

    public String toString () {
        StringBuilder mapString = new StringBuilder();
        for (Boolean[] row : raw) {
            for (Boolean tile : row) {
                if (!tile) {
                    mapString.append("   ☐   ");
                } else {
                    mapString.append("   L  ");
                }
            }
            mapString.append("\n");
        }
        return mapString.toString();
    }
}
