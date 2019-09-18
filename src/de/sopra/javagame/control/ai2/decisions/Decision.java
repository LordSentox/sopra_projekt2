package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.DebugUtil;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.Point;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * <h1>Decision</h1>
 * Ist der Kern der entscheidungbasierten KI.
 * Stellt eine Aktion bereit, die mit einer Entscheidung als Bedingung verknüpft wird.
 *
 * @author Julius Korweck
 * @version 12.09.2019
 * @since 09.09.2019
 */
public abstract class Decision {

    final int ZERO_CARDS = 0;
    final int ONE_CARD = 1;
    final int TWO_CARDS = 2;
    final int THREE_CARDS = 3;
    final int FOUR_CARDS = 4;

    protected AIController control;

    private HashMap<Condition, ICondition> conditions = new HashMap<>();

    private PreCondition preCondition;

    protected void debug(String debug) {
        DebugUtil.debug(debug);
    }

    public final static Decision empty() {
        return new Decision() {
            @Override
            public Decision decide() {
                return this;
            }

            @Override
            public ActionQueue act() {
                return null;
            }
        };
    }

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
    public abstract ActionQueue act();

    /**
     * Baut aus zwei entscheidungsabhängigen Aktionen einen Turm.
     * Das Argument wird zur weniger priorisierten Aktion gegenüber der Aktuellen.
     *
     * @param lessImportantDecision die Aktion, welche als weniger wichtig betrachtet wird, als die Aktuelle
     * @return ein neues Decision Objekt, welches keine eigene Aktion enthält,
     * aber mittels {@link #decide()} ein Objekt mit Aktion liefert
     */
    public final Decision next(Decision lessImportantDecision) {
        Decision self = this; //um Zugriff auf decide von this in decide von neuer Decision zu haben
        debug("order: " + self.getClass().getSimpleName() + " -> " + lessImportantDecision.getClass().getSimpleName());
        return new Decision() {
            @Override
            public Decision decide() {
                //Wenn die PreCondition nicht erfüllt, darf die decide Methode nicht verwendet werden
                Decision decision = self.matchPreCondition() ? self.decide() : null;
                debug("current decision: " + decision);
                if (decision == null) {
                    //mögliche getroffene Conditions sollen nicht neu getroffen werden (Effizienz)
                    lessImportantDecision.conditions = self.conditions;
                    //Auch hier: Wenn die PreCondition nicht erfüllt, darf die decide Methode nicht verwendet werden
                    Decision otherDecision = lessImportantDecision.matchPreCondition() ? lessImportantDecision.decide() : null;
                    if (otherDecision == null)
                        debug("less important decision not made: " + lessImportantDecision.getClass().getSimpleName());
                    return otherDecision;
                } else return decision;
            }

            @Override
            public void setControl(AIController control) {
                super.setControl(control);
                self.setControl(control);
                lessImportantDecision.setControl(control);
            }

            @Override
            public ActionQueue act() {
                //empty
                return null;
            }
        };
    }

    protected ActionQueue startActionQueue() {
        return new ActionQueue(control.getActivePlayer().getType());
    }

    //condition NUR prüfen
    protected ICondition getCondition(Condition condition, ICondition defCondition) {
        return conditions.getOrDefault(condition, defCondition);
    }

    //condition prüfen und wenn nicht gesetzt neu setzen
    protected ICondition condition(Condition condition) {
        if (conditions.containsKey(condition))
            return conditions.get(condition);
        else {
            boolean value = condition.isTrue(this);
            conditions.put(condition, Conditions.condition(value));
            return condition(condition);
        }
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

    protected Action action() {
        return control.getCurrentAction();
    }

    protected MapTile tile() {
        return action().getMap().get(player().getPosition());
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

    @SuppressWarnings("unchecked")
    protected <T> boolean checkAll(Predicate<T> checker, T... objects) {
        if (objects == null || objects.length == 0)
            return true;
        for (T object : objects)
            if (object != null && !checker.test(object)) return false;
        return true;
    }

    protected boolean needSpecialToMove(Point startPoint, Point targetPoint) {
        Direction direction = startPoint.getPrimaryDirection(targetPoint);
        return startPoint.add(direction).equals(targetPoint);
    }

    protected int minMovesWithoutSpecial(Point startPoint, Point targetPoint) {
        //Courier um keine Spezialbewegungen zu haben
        return control.getMinimumActionsNeededToReachTarget(startPoint, targetPoint, PlayerType.COURIER);
    }

    protected List<Point> surroundingPoints(Point center, boolean edges) {
        if (edges) {
            return center.getSurrounding();
        } else {
            return center.getNeighbours();
        }
    }

    public void setControl(AIController control) {
        this.control = control;
    }

    public final void setPreCondition(PreCondition preCondition) {
        this.preCondition = preCondition;
    }

    private final boolean matchPreCondition() {
        if (preCondition == null) return true;
        boolean allMatchTrue = Arrays.stream(preCondition.allTrue())
                .allMatch(condition -> condition(condition).isTrue(this));
        boolean allMatchFalse = Arrays.stream(preCondition.allFalse())
                .allMatch(condition -> condition(condition).isFalse(this));
        return allMatchTrue && allMatchFalse;
    }

}