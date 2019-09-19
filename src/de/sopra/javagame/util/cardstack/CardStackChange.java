package de.sopra.javagame.util.cardstack;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 10.09.2019
 * @since 10.09.2019
 */
public interface CardStackChange<T> {

    default T newElement() {
        return null;
    }

    default T removedElement() {
        return null;
    }

    boolean changedDiscardPile();

    default boolean clear() {
        return false;
    }

}