package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.ai2.decisions.Decision;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 11.09.2019
 * @since 11.09.2019
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface DoAfter {

    Class<? extends Decision> value();

    DecisionResult act();

}