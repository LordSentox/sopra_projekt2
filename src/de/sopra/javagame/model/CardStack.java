package de.sopra.javagame.model;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * @see FloodCard
 * @see ArtifactCard
 */
public class CardStack<T> {

    private Stack<T> drawStack;

    private Collection<T> discardPile;

    List<T> draw(int amount, boolean discard) {
        return null;
    }

    void shuffleBack() {

    }

    void shuffleDrawStack() {

    }

    boolean discard(T... card) {
        return false;
    }

}
