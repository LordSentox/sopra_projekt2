package de.sopra.javagame.control.ai;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai1.ActionType.*;
import static de.sopra.javagame.model.ArtifactCardType.HELICOPTER;
import static de.sopra.javagame.model.ArtifactCardType.SANDBAGS;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 13.09.2019
 * @since 13.09.2019
 */
public final class ActionQueue {

    private final Queue<SimpleAction> actions = new LinkedList<>();
    private final PlayerType currentPlayer;

    public ActionQueue(PlayerType player) {
        currentPlayer = player;
    }

    public void nextActions(ActionQueue stack) {
        if (stack != null && stack.actions() > 0)
            stack.actionIterator().forEachRemaining(this::nextAction);
    }

    private void nextAction(SimpleAction action) {
        actions.offer(action);
    }

    public int actions() {
        return actions.size();
    }

    public PlayerType getPlayer() {
        return currentPlayer;
    }

    public Iterator<SimpleAction> actionIterator() {
        return new Iterator<SimpleAction>() {
            final Queue<SimpleAction> copy = CopyUtil.copy(actions, Collectors.toCollection(LinkedList::new));

            @Override
            public boolean hasNext() {
                return !copy.isEmpty();
            }

            @Override
            public SimpleAction next() {
                return copy.poll();
            }
        };
    }

    public ActionQueue waitAndDrinkTea() {
        nextAction(new SimpleAction(WAIT_AND_DRINK_TEA, null));
        return this;
    }

    public ActionQueue move(Point targetPoint) {
        nextAction(new SimpleAction(MOVE, targetPoint));
        return this;
    }

    public ActionQueue drain(Point targetPoint) {
        nextAction(new SimpleAction(DRAIN, targetPoint));
        return this;
    }

    public ActionQueue discard(ArtifactCardType type) {
        nextAction(new SimpleAction(DISCARD_CARD, EnumSet.noneOf(PlayerType.class), type));
        return this;
    }

    public ActionQueue trade(ArtifactCardType type, PlayerType target) {
        nextAction(new SimpleAction(TRADE_CARD, EnumSet.of(target), type));
        return this;
    }

    public ActionQueue sandbagCard(Point targetPoint) {
        nextAction(new SimpleAction(null, targetPoint, EnumSet.noneOf(PlayerType.class), SANDBAGS));
        return this;
    }

    public ActionQueue helicopterCard(Point startPoint, Point targetPoint, EnumSet<PlayerType> playersToTransport) {
        nextAction(new SimpleAction(startPoint, targetPoint, playersToTransport, HELICOPTER));
        return this;
    }

    public ActionQueue collectTreasure() {
        nextAction(new SimpleAction(COLLECT_TREASURE));
        return this;
    }

    public ActionQueue finishTheGame(Point landingSide, EnumSet<PlayerType> allPlayers) {
        return helicopterCard(landingSide, landingSide, allPlayers);
    }

    public ActionQueue diverDiveTo(Point targetPoint) {
        nextAction(new SimpleAction(null, targetPoint, EnumSet.noneOf(PlayerType.class)));
        return this;
    }

    public ActionQueue courierTrade(ArtifactCardType type, PlayerType target) {
        nextAction(new SimpleAction(SPECIAL_ABILITY, EnumSet.of(target), type));
        return this;
    }

    public ActionQueue pilotFlyTo(Point targetPoint) {
        nextAction(new SimpleAction(null, targetPoint, EnumSet.noneOf(PlayerType.class)));
        return this;
    }

    public ActionQueue explorerDiagonal(Point targetPoint) {
        nextAction(new SimpleAction(null, targetPoint, EnumSet.noneOf(PlayerType.class)));
        return this;
    }

    public ActionQueue navigatorMoveOther(PlayerType target, Point startPoint, Direction direction) {
        nextAction(new SimpleAction(startPoint, direction.translate(startPoint), EnumSet.of(target)));
        return this;
    }

    public ActionQueue engineersDrain(Point targetPoint) {
        nextAction(new SimpleAction(null, targetPoint, EnumSet.noneOf(PlayerType.class)));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder action = new StringBuilder();
        actionIterator().forEachRemaining(element -> action.append(element.toString() + ","));
        return action.toString();
    }
}