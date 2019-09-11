package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.Point;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static de.sopra.javagame.util.Direction.*;

/**
 * <h1>Decision</h1>
 * Ist der Kern der entscheidungbasierten KI.
 * Stellt eine Aktion bereit, die mit einer Entscheidung als Bedingung verknüpft wird.
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public abstract class Decision {

    protected final int ZERO_CARDS = 0;
    protected final int ONE_CARD = 1;
    protected final int TWO_CARDS = 2;
    protected final int THREE_CARDS = 3;
    protected final int FOUR_CARDS = 4;

    protected AIController control;

    /**
     * Entscheidet, ob die mit diesem Objekt verbundene Aktion ausgeführt werden soll, oder nicht.
     *
     * @return sich selbst, wenn die Entscheidung positiv ausfiel, andernfalls <code>null</code>
     */
    public abstract Decision decide();

    /**
     * Führt die Aktion aus.
     * Soll nur nach getroffener Entscheidung durch {@link #decide()} geschehen.
     */
    public abstract void act();

    /**
     * Baut aus zwei entscheidungsabhängigen Aktionen einen Turm.
     * Das Argument wird zur weniger priorisierten Aktion gegenüber der Aktuellen.
     *
     * @param lessImportantDecision die Aktion, welche als weniger wichtig betrachtet wird, als die Aktuelle
     * @return ein neues Decision Objekt, welches keine eigene Aktion enthält,
     * aber mittels {@link #decide()} ein Objekt mit Aktion liefert
     */
    final Decision next(Decision lessImportantDecision) {
        Decision self = this;
        return new Decision() {
            @Override
            public Decision decide() {
                Decision decision = self.decide();
                if (decision == null) {
                    return lessImportantDecision.decide();
                } else return decision;
            }

            @Override
            public void act() {
                //empty
            }
        };
    }

    protected Point translate(Point point, Direction... directions) {
        if (directions == null || directions.length == 0) return point;
        for (Direction direction : directions)
            point = direction.translate(point);
        return point;
    }

    protected Player player() {
        return control.getActivePlayer();
    }

    protected Turn turn() {
        return control.getActiveTurn();
    }

    protected MapTile tile() {
        return turn().getTile(player().getPosition());
    }

    protected boolean hasValidActions(Integer... validActions) {
        return Arrays.asList(validActions).contains(player().getActionsLeft());
    }

    protected EnhancedPlayerHand playerHand() {
        return EnhancedPlayerHand.ofPlayer(player());
    }

    protected EnhancedPlayerHand hand(Player player) {
        return EnhancedPlayerHand.ofPlayer(player);
    }

    protected boolean all(Boolean... bools) {
        return Arrays.stream(bools).allMatch(Boolean::booleanValue);
    }

    protected boolean none(Boolean... bools) {
        return Arrays.stream(bools).noneMatch(Boolean::booleanValue);
    }

    protected boolean any(Boolean... bools) {
        return Arrays.stream(bools).anyMatch(Boolean::booleanValue);
    }

    protected <T> boolean checkAll(Predicate<T> checker, Collection<T> objects) {
        if (objects == null || objects.size() == 0)
            return true;
        for (T object : objects)
            if (object != null && !checker.test(object)) return false;
        return true;
    }

    protected <T> boolean checkAll(Predicate<T> checker, T... objects) {
        if (objects == null || objects.length == 0)
            return true;
        for (T object : objects)
            if (object != null && !checker.test(object)) return false;
        return true;
    }

    protected List<Point> surroundingPoints(Point center, boolean edges) {
        List<Point> points = new LinkedList<>();
        Point northernNeighbourPoint = translate(center, UP);
        Point southernNeighbourPoint = translate(center, DOWN);
        points.add(northernNeighbourPoint);
        points.add(southernNeighbourPoint);
        points.add(translate(center, LEFT));
        points.add(translate(center, RIGHT));
        if (edges) {
            points.add(translate(northernNeighbourPoint, RIGHT));
            points.add(translate(southernNeighbourPoint, RIGHT));
            points.add(translate(southernNeighbourPoint, LEFT));
            points.add(translate(northernNeighbourPoint, LEFT));
        }
        return points;
    }

    public final void setControl(AIController control) {
        this.control = control;
    }
}