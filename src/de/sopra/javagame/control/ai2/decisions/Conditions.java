package de.sopra.javagame.control.ai2.decisions;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 12.09.2019
 * @since 12.09.2019
 */
public class Conditions {

    public static ICondition condition(boolean value) {
        return decision -> value;
    }

    public static ICondition stayFalse() {
        return condition(false);
    }

    public static ICondition stayTrue() {
        return condition(true);
    }

}