package de.sopra.javagame.util;

import de.sopra.javagame.model.Copyable;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 10.09.2019
 * @since 10.09.2019
 */
public interface CardStackObserver<T extends Copyable<T>> {

    /**
     * Wird durch das übergebene Oberservable aufgerufen, wenn eine Änderung geschieht
     *
     * @param cardStack der Auslöser des Events, der CardStack
     * @param change    die Änderung die vorgenommen wurde
     */
    void update(CardStack<T> cardStack, CardStackChange<T> change);

}