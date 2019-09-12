package de.sopra.javagame.control.ai2.decisions;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 12.09.2019
 * @since 12.09.2019
 */
public interface ICondition {

    boolean isTrue(Decision decision);

    default boolean isTrue() {
        return isTrue(null);
    }

    default boolean isFalse(Decision decision) {
        return !isTrue(decision);
    }

    default boolean isFalse() {
        return isFalse(null);
    }

}