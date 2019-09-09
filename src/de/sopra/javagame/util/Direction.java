package de.sopra.javagame.util;

import java.awt.*;

/**
 * Die Richtungen, in die ein Spieler ohne besondere Bewegungsfähigkeiten bewegt werden kann.
 */
public enum Direction {
    /**
     * Oben
     */
    UP(0, -1),
    /**
     * Links
     */
    LEFT(-1, 0),
    /**
     * Unten
     */
    DOWN(0, 1),
    /**
     * Rechts
     */
    RIGHT(1, 0);

    private int xOffset, yOffset;

    Direction(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public Point translate(Point point) {
        return new Point(point.x + xOffset, point.y + yOffset);
    }

}
