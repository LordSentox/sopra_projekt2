package de.sopra.javagame.model;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * @param <T> ist der Kartentyp. Entweder {@link ArtifactCard} oder {@link FloodCard}
 * @author Max Bühmann, Melanie Arnds
 * <p>
 * Ein CardStack implementiert einen Zieh- sowie einen Ablagestapel eines Kartentyps.
 */
public class CardStack<T> implements Copyable<CardStack<T>> {

    private Stack<T> drawStack;

    private Collection<T> discardPile;

    /**
     * draw nimmt die angegebene Anzahl Karten von oben vom Stack
     *
     * @param amount  Anzahl gewünschter Karten
     * @param discard bezeichnet, ob die gezogenen Karten sofort ausgeführt und abgeworfen werden sollen
     * @return gibt eine Liste vom Kartentyp T zurück
     */
    public List<T> draw(int amount, boolean discard) {
        return null;
    }

    /**
     * shuffleBack mischt den Ablagestapel durch und legt ihn auf den DrawStack, beide Stapel können auch leer sein kann, nicht beide gleichzeitig.
     */
    void shuffleBack() {

    }

    /**
     * mischt den drawStack in eine zufällige Reihenfolge
     */
    void shuffleDrawStack() {

    }

    /**
     * discard fügt eine beliebige Anzahl an Karten dem discardPile hinzu.
     *
     * @param card ist ein varargs mit beliebiger Anzahl an Karten vom Typ T.
     * @return false, wenn fehlgeschlagen, true, sonst.
     */
    boolean discard(T... card) {
        return false;
    }

    @Override
    public CardStack<T> copy() {
        return null; //TODO
    }
}
