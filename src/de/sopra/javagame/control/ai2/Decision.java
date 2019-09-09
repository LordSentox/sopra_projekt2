package de.sopra.javagame.control.ai2;

import de.sopra.javagame.model.Turn;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public interface Decision {

    Decision makeDecision(Turn currentTurn);

    void doAction();

    default Decision onTop(Decision moreImportantDecision) {
        return new Decision() {
            @Override
            public Decision makeDecision(Turn currentTurn) {
                Decision decision = moreImportantDecision.makeDecision(currentTurn);
                if (decision == null) {
                    return makeDecision(currentTurn);
                } else return decision;
            }

            @Override
            public void doAction() {
                //empty
            }
        };
    }

}