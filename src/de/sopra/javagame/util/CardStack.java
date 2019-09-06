package de.sopra.javagame.util;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.Copyable;
import de.sopra.javagame.model.FloodCard;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <T> ist der Kartentyp. Entweder {@link ArtifactCard} oder {@link FloodCard}
 * @author Max Bühmann, Melanie Arnds
 * <p>
 * Ein CardStack implementiert einen Zieh- sowie einen Ablagestapel eines Kartentyps.
 */
public class CardStack<T extends Copyable<T>> implements Copyable<CardStack<T>> {

    private Stack<T> drawStack;

    private List<T> discardPile;

    public CardStack() {
        this(Collections.emptyList());
    }

    public CardStack(Collection<T> cards) {
        drawStack = new Stack<>();
        drawStack.addAll(cards);
        discardPile = Collections.emptyList();
    }

    /**
     * draw nimmt die angegebene Anzahl Karten von oben vom Stack
     *
     * @param amount  Anzahl gewünschter Karten
     * @param discard bezeichnet, ob die gezogenen Karten sofort ausgeführt und abgeworfen werden sollen
     * @return gibt eine Liste vom Kartentyp T zurück
     */
    public List<T> draw(int amount, boolean discard) {
        List<T> drawn = new LinkedList<>();
        while (amount > 0) {
            if (drawStack.isEmpty()) //wenn der stack leer ist, Ablagestapel zurückmischen
                shuffleBack();
            if (drawStack.isEmpty()) //wenn der stack immernoch leer ist, aufhören zu ziehen
                drawn.add(drawStack.pop());
        }
        if (discard) {
            for (T item : drawn)
                discard(item);
        }
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
     * shuffleBack mischt den Ablagestapel durch und legt ihn auf den DrawStack, beide Stapel können auch leer sein kann, nicht beide gleichzeitig.
     */
    public void shuffleBack() {
        Collections.shuffle(discardPile);
        for (T element : discardPile) {
            drawStack.push(element);
        }
        discardPile = new LinkedList<>();
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
    public void discard(T... card) {
        discardPile.addAll(Arrays.asList(card));
    }

    @Override
    public CardStack<T> copy() {
        CardStack<T> stack = new CardStack<>();
        stack.discardPile = CopyUtil.copyAsList(this.discardPile);
        stack.drawStack = CopyUtil.copy(this.drawStack, Collectors.toCollection(Stack::new));
        return stack;
    }
}
