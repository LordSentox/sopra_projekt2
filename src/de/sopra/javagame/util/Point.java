package de.sopra.javagame.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.sopra.javagame.util.Direction.*;

public class Point implements Serializable {

    private static final long serialVersionUID = 974151156673528867L;
    public int xPos;
    public int yPos;

    public Point() {
        this(0, 0);
    }

    public Point(Point point) {
        this(point.xPos, point.yPos);
    }

    public Point(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Point getLocation() {
        return new Point(this.xPos, this.yPos);
    }

    public void setLocation(Point location) {
        this.setLocation(location.xPos, location.yPos);
    }

    public void setLocation(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void move(int deltaX, int deltaY) {
        this.xPos += deltaX;
        this.yPos += deltaY;
    }

    public void move(Point delta) {
        this.xPos += delta.xPos;
        this.yPos += delta.yPos;
    }

    public Direction getPrimaryDirection(Point target) {
        if (this.equals(target)) return null;
        int xDiff = target.xPos - this.xPos;
        int yDiff = target.yPos - this.yPos;
        //LEFT or RIGHT
        if (Math.abs(xDiff) > Math.abs(yDiff)) {
            if (xDiff > 0)
                return RIGHT;
            else return LEFT;
        } else { //UP or DOWN
            if (yDiff > 0)
                return DOWN;
            else return UP;
        }
    }

    public Point add(int deltaX, int deltaY) {
        return new Point(this.xPos + deltaX, this.yPos + deltaY);
    }

    public Point add(Point toAdd) {
        return new Point(this.xPos + toAdd.xPos, this.yPos + toAdd.yPos);
    }

    public Point add(Direction direction) {
        return direction.translate(this);
    }

    /**
     * Eine Liste mit den Punkten links, rechts, unten und oben vom Punkt
     *
     * @return die neue Liste mit den Nachbarn
     */
    public List<Point> getNeighbours() {
        List<Point> neighbours = new ArrayList<>();
        neighbours.add(new Point(this.xPos, this.yPos - 1));
        neighbours.add(new Point(this.xPos - 1, this.yPos));
        neighbours.add(new Point(this.xPos, this.yPos + 1));
        neighbours.add(new Point(this.xPos + 1, this.yPos));

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
        neighbours = neighbours.stream().filter(point -> point.xPos >= minimum.xPos &&
                point.yPos >= minimum.yPos &&
                point.xPos <= maximum.xPos &&
                point.yPos <= maximum.yPos).collect(Collectors.toList());

        return neighbours;
    }

    public List<Point> getSurrounding() {
        List<Point> surrounding = this.getNeighbours();
        surrounding.add(this.add(UP).add(LEFT));
        surrounding.add(this.add(UP).add(RIGHT));
        surrounding.add(this.add(DOWN).add(LEFT));
        surrounding.add(this.add(DOWN).add(RIGHT));

        return surrounding;
    }
    
    public List<Point> getSurrounding(Point minimum, Point maximum) {
        List<Point> surrounding = this.getSurrounding();
        
        surrounding = surrounding.stream().filter(point -> point.xPos >= minimum.xPos &&
                point.yPos >= minimum.yPos &&
                point.xPos <= maximum.xPos &&
                point.yPos <= maximum.yPos).collect(Collectors.toList());

        return surrounding;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Point)) {
            return super.equals(other);
        } else {
            Point point = (Point) other;
            return this.xPos == point.xPos && this.yPos == point.yPos;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos);
    }

    public String toString() {
        return this.getClass().getName() + "[x=" + this.xPos + ",y=" + this.yPos + "]";
    }
}
