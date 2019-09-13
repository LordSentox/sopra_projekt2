package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.ai2.decisions.Condition;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 12.09.2019
 * @since 12.09.2019
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface PreCondition {

    Condition[] allTrue() default {};

    Condition[] allFalse() default {};

}