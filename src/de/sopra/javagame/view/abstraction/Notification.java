package de.sopra.javagame.view.abstraction;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 17.09.2019
 * @since 17.09.2019
 */
public interface Notification {

    String message();

    default boolean hasMessage() {
        return message() != null;
    }

    default boolean isGameWon() {
        return false;
    }

    default boolean isGameLost() {
        return false;
    }

    default boolean isError() {
        return false;
    }


}