package de.sopra.javagame.control.ai1;

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

    double getPriority(Action action) {
        Optional<Priority> priority = priorities.stream()
                .reduce((pr1, pr2) -> action1 -> pr1.getPriority(action1) + pr2.getPriority(action1));
        return priority.map(value -> value.getPriority(action)).orElse(-1.0);
    }

}