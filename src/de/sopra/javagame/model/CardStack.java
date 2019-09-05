package de.sopra.javagame.model;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * <FloodCard>
 */
public class CardStack<T> {

    private Stack drawStack;

    private Collection discardPile;

    List draw(int amount, boolean discard) {
        return null;
    }

    void shuffleBack() {

    }

    boolean discard(T... card) {
        return false;
    }

}
