package de.sopra.javagame.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Point {
    public int x;
    public int y;

    public Point() {
        this(0, 0);
    }

    public Point(Point point) {
        this(point.x, point.y);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point getLocation() {
        return new Point(this.x, this.y);
    }

    public void setLocation(Point location) {
        this.setLocation(location.x, location.y);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public void move(Point delta) {
        this.x += delta.x;
        this.y += delta.y;
    }

    public Point add(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point add(Point toAdd) {
        return new Point(this.x + toAdd.x, this.y + toAdd.y);
    }

    /**
     * Eine Liste mit den Punkten links, rechts, unten und oben vom Punkt
     *
     * @return die neue Liste mit den Nachbarn
     */
    public List<Point> getNeighbours() {
        List<Point> neighbours = new ArrayList<>();
        neighbours.add(new Point(this.x, this.y - 1));
        neighbours.add(new Point(this.x - 1, this.y));
        neighbours.add(new Point(this.x, this.y + 1));
        neighbours.add(new Point(this.x + 1, this.y));

        return neighbours;
    }


    /**
     * Eine Liste mit den Punkten links, rechts, unten und oben vom Punkt, wobei die übergebenen Grenzen eingehalten
     * werden müssen. Es werden nur Nachbarn zurückgegeben, welche auch innerhalb der Grenzen liegen.
     *
     * @param minimum Inklusives Minimum, der x-Wert und y-Wert des Punktes dürfen nicht unterschritten werden.
     * @param maximum Inklusives Maximum, der x-Wert und y-Wert des Punktes dürfen nicht überschritten werden.
     * @return die neue Liste mit den Nachbarn
     */
    public List<Point> getNeighbours(Point minimum, Point maximum) {
        // Zunächst alle hinzufügen
        List<Point> neighbours = this.getNeighbours();

        // Entferne Punkte, welche nicht innerhalb der gegebenen Grenzen sind
        neighbours = neighbours.stream().filter(point -> point.x >= minimum.x &&
                point.y >= minimum.y &&
                point.x <= maximum.x &&
                point.y <= maximum.y).collect(Collectors.toList());

        return neighbours;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Point)) {
            return super.equals(other);
        } else {
            Point point = (Point) other;
            return this.x == point.x && this.y == point.y;
        }
    }

    public String toString() {
        return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
    }
}
