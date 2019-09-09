package de.sopra.javagame.control.ai2;

import de.sopra.javagame.model.Turn;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class DicisionMaker {

    private Decision turnDecisionTower;
    private Decision discardDecisionTower;
    private Decision specialCardDecisionTower;

    void buildTurnTower() {

    }

    void buildDiscardTower() {

    }

    void buildSpecialCardTower() {

    }

    Decision makeTurnDecision(Turn turn) {
        return turnDecisionTower.makeDecision(turn);
    }

    Decision makeDiscardDecision(Turn turn) {
        return discardDecisionTower.makeDecision(turn);
    }

    Decision makeSpecialCardDecision(Turn turn) {
        return specialCardDecisionTower.makeDecision(turn);
    }

}