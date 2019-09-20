package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.util.Point;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.model.player.PlayerType.COURIER;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 11.09.2019
 * @since 11.09.2019
 */
@PreCondition(allFalse = Condition.PLAYER_NO_ACTION_LEFT)
@DoAfter(act = TURN_ACTION, value = TurnMoveToDrainTile.class)
public class TurnMoveTowardsMiddle extends Decision {

    private Point middle;

    @Override
    public Decision decide() {
        Collection<Point> points = action().getMap().validPoints();
        List<Path> paths = new LinkedList<>();
        //erstelle alle Pfade, werden nicht doppelt erstellt
        points.forEach(start ->
        {
            for (Point target : points) {
                if (target.equals(start)) continue; //kein Weg auf sich selbst
                Path path = new Path(start, target);
                if (!paths.contains(path))
                    paths.add(path);
            }
        });
        //suche den Punkt, dessen weitester Weg zu einem anderen Punkt, der kürzeste im Vergleich zu allen anderen ist
        int min = 100;
        setMin(points, min, paths);
        //wenn der Spieler da schon steht, passts
        Point playerPosition = player().getPosition();
        if (any(playerPosition.equals(middle), middle == null))
            return null;
        //Punkt auf einen erreichbaren Punkt setzen
        if (!player().legalMoves(true).contains(middle)) //try to translate into directional movement
            middle = playerPosition.getPrimaryDirection(middle).translate(playerPosition);
        if (!player().legalMoves(false).contains(middle)) //should still be reachable
            return null;
        return this;
    }

    //cause PMD
    public void setMin(Collection<Point> points, int min, List<Path> paths) {
        for (Point point : points) {
            //maximale Strecke zu einem anderen Punkt
            int max = paths.stream()
                    .filter(path -> path.hasPoint(point))
                    .map(Path::getMinActions)
                    .reduce(Integer::max).get();
            if (max < min) {
                min = max;
                middle = point;
            }
        }
    }

    @Override
    public ActionQueue act() {
        boolean needSpecial = needSpecialToMove(player().getPosition(), middle);
        if (needSpecial) {
            switch (player().getType()) {
                case DIVER:
                    return startActionQueue().diverDiveTo(middle);
                case EXPLORER:
                    return startActionQueue().explorerDiagonal(middle);
                case PILOT:
                    return startActionQueue().pilotFlyTo(middle);
            }
        }
        return startActionQueue().move(middle);
    }

    private class Path {

        private Point first, second;

        private Integer minActions;

        public Path(Point left, Point right) {
            this.first = left;
            this.second = right;
        }

        public Point getFirst() {
            return first;
        }

        public Point getSecond() {
            return second;
        }

        public boolean hasPoint(Point point) {
            return first.equals(point) || second.equals(point);
        }

        public int getMinActions() {
            return minActions == null ? minActions = control.getMinimumActionsNeededToReachTarget(first, second, COURIER) : minActions;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Path) {
                Path other = (Path) obj;
                if (other.hasPoint(this.first) && other.hasPoint(this.second))
                    return true;
            }
            return super.equals(obj);
        }
    }

}
