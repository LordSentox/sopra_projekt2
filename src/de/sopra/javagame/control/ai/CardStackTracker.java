package de.sopra.javagame.control.ai;

import de.sopra.javagame.model.Copyable;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.cardstack.CardStackChange;
import de.sopra.javagame.util.cardstack.CardStackObserver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 10.09.2019
 * @since 10.09.2019
 */
public final class CardStackTracker<T extends Copyable<T>> implements CardStackObserver<T> {

    private Collection<T> drawStack;
    private Collection<T> discardPile;

    private Collection<T> knowTopCards;

    public CardStackTracker() {
        drawStack = new LinkedList<>();
        discardPile = new LinkedList<>();
        knowTopCards = new LinkedList<>();
    }

    private void draw(T item) {
        drawStack.remove(item);
        knowTopCards.remove(item);
    }

    private void back(T item) {
        drawStack.add(item);
        knowTopCards.add(item);
    }

    private void discard(T item) {
        discardPile.add(item);
    }

    public Collection<T> getDiscardPile() {
        return discardPile;
    }

    public Collection<T> getDrawStack() {
        return drawStack;
    }

    public Collection<T> getKnowTopCards() {
        return knowTopCards;
    }

    public int cardsInDeck(Predicate<T> matcher) {
        return (int) drawStack.stream().filter(matcher).count();
    }

    public int cardsInTopOfDeck(Predicate<T> matcher) {
        return (int) knowTopCards.stream().filter(matcher).count();
    }

    public int cardsInDiscardPile(Predicate<T> matcher) {
        return (int) discardPile.stream().filter(matcher).count();
    }

    public void applyChange(CardStackChange<T> change) {
        if (change.clear()) {
            drawStack = new LinkedList<>();
            discardPile = new LinkedList<>();
            knowTopCards = new LinkedList<>();
            return;
        }
        if (change.changedDiscardPile()) {
            if (change.newElement() == null)
                discardPile.remove(change.removedElement());
            else discard(change.newElement());
        } else {
            if (change.newElement() == null)
                draw(change.removedElement());
            else back(change.newElement());
        }
    }

    @Override
    public void update(CardStack<T> cardStack, CardStackChange<T> change) {
        applyChange(change);
    }
}