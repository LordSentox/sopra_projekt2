package de.sopra.javagame.util;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.Copyable;
import de.sopra.javagame.model.FloodCard;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @param <T> ist der Kartentyp. Entweder {@link ArtifactCard} oder {@link FloodCard}
 * @author Max Bühmann, Melanie Arnds
 * <p>
 * Ein CardStack implementiert einen Zieh- sowie einen Ablagestapel eines Kartentyps.
 */
public class CardStack<T extends Copyable<T>> extends CardStackObservable<T> implements Copyable<CardStack<T>>, Serializable {

    private static final long serialVersionUID = 8572616465854559809L;

    private Stack<T> drawStack;

    private List<T> discardPile;

    public CardStack() {
        this(Collections.emptyList());
    }

    public CardStack(Collection<T> cards) {
        drawStack = new Stack<>();
        drawStack.addAll(cards);
        discardPile = new ArrayList<>();
    }
//FIXME Tests überarbeiten und draw nur noch ohne amount benutzen!

    /**
     * draw nimmt die angegebene Anzahl Karten von oben vom Stack
     *
     * @param amount  Anzahl gewünschter Karten
     * @param discard bezeichnet, ob die gezogenen Karten sofort ausgeführt und abgeworfen werden sollen
     * @return gibt eine Liste vom Kartentyp T zurück
     */
    public List<T> draw(int amount, boolean discard) {
        List<T> drawn = new ArrayList<>();
        if (drawStack.isEmpty() && discardPile.isEmpty()) return drawn;
        for (int i = 0; i < amount; i++) {
            if (drawStack.isEmpty()) {
                shuffleBack();
            }
            T pop = drawStack.pop();
            drawn.add(pop);
            notifyRemove(this, pop, false);
        }
        if (discard) {
            discard(drawn);
        }
        return drawn;
    }

    /**
     * draw nimmt die angegebene Anzahl Karten von oben vom Stack
     *
     * @param amount Anzahl gewünschter Karten
     * @return gibt eine Liste vom Kartentyp T zurück
     */
    public List<T> draw(int amount) {
        return draw(amount, false);
    }

    public T draw(boolean discard) {
        return draw(1, discard).get(0);
    }

    public T drawAndSkip(Predicate<T> skip) {
        T drawn = null;
        do {
            if (drawn != null)
                discard(drawn);
            drawn = draw(false);
        }
        while (skip.test(drawn));
        shuffleBack();
        shuffleDrawStack();
        return drawn;
    }
    
    /**
     * @return die Anzahl an Karten im Ziehstapel
     */
    public int size() {
        return drawStack.size();
    }

    /**
     * @return die Anzahl an abgeworfenen Karten
     */
    public int discarded() {
        return discardPile.size();
    }

    /**
     * @return den aktuellen Discard Pile
     */
    public List<T> getDiscardPile() {
        return discardPile;
    }

    /**
     * shuffleBack mischt den Ablagestapel durch und legt ihn auf den DrawStack, beide Stapel können auch leer sein kann, nicht beide gleichzeitig.
     */
    public void shuffleBack() {
        Collections.shuffle(discardPile);
        for (T element : discardPile) {
            drawStack.push(element);
            notifyAdd(this, element, false);
        }
        List<T> oldPile = discardPile;
        discardPile = new LinkedList<>();
        for (T element : oldPile) {
            notifyRemove(this, element, true);
        }
    }

    /**
     * mischt den drawStack in eine zufällige Reihenfolge
     */
    public void shuffleDrawStack() {
        Collections.shuffle(drawStack);
    }

    /**
     * discard fügt eine beliebige Anzahl an Karten dem discardPile hinzu.
     *
     * @param card ist ein varargs mit beliebiger Anzahl an Karten vom Typ T.
     */
    @SuppressWarnings("unchecked")
    public void discard(T... card) {
        discard(Arrays.asList(card));
    }

    /**
     * discard fügt eine beliebige Anzahl an Karten dem discardPile hinzu.
     *
     * @param cards ist eine Collection mit beliebiger Anzahl an Karten vom Typ T.
     */
    public void discard(Collection<T> cards) {
        for (T element : cards) {
            discardPile.add(element);
            notifyAdd(this, element, true);
        }
    }

    @Override
    public CardStack<T> copy() {
        CardStack<T> stack = new CardStack<>();
        stack.discardPile = CopyUtil.copyAsList(this.discardPile);
        stack.drawStack = CopyUtil.copy(this.drawStack, Collectors.toCollection(Stack::new));
        stack.setObserver(this.getObserver());
        return stack;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        CardStack<?> stack = (CardStack<?>) other;
        return drawStack.equals(stack.drawStack) &&
                discardPile.containsAll(stack.discardPile) &&
                stack.discardPile.containsAll(discardPile);
    }

    @Override
    public int hashCode() {
        return drawStack.hashCode() + 3 * new HashSet<>(discardPile).hashCode();
    }
}
