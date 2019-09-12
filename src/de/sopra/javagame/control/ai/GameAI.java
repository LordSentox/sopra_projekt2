package de.sopra.javagame.control.ai;

import de.sopra.javagame.control.ai1.Prioritizer;
import de.sopra.javagame.control.ai2.DecisionMaker;

import java.util.function.Supplier;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 11.09.2019
 * @since 11.09.2019
 */
public enum GameAI implements Supplier<AIProcessor> {

    DECISION_BASED_AI {
        @Override
        public AIProcessor get() {
            return new DecisionMaker();
        }
    },
    PRIORITY_BASED_AI {
        @Override
        public AIProcessor get() {
            return new Prioritizer();
        }
    }

}