package de.sopra.javagame.control.ai2.condition;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 12.09.2019
 * @since 12.09.2019
 */
public interface ICondition {

    boolean isTrue();

    default boolean isFalse() {
        return !isTrue();
    }

}