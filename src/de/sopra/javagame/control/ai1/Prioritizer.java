package de.sopra.javagame.control.ai1;

import de.sopra.javagame.control.AIController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Mel, Niklas
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class Prioritizer {

    private List<Priority> priorities;

    public Prioritizer() {
        this.priorities = new LinkedList<>();
    }

    void buildPriorities() {

    }

    double getPriority(Action action, AIController control) {
        Optional<Priority> priority = priorities.stream()
                .reduce((pr1, pr2) -> (act,con) -> pr1.getPriority(act,con) + pr2.getPriority(act,con));
        return priority.map(value -> value.getPriority(action,control)).orElse(-1.0);
    }

}