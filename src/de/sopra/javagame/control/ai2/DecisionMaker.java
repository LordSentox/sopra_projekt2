package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.AIController;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class DecisionMaker implements AIController.AIProcessor {

    private Decision turnDecisionTower;
    private Decision discardDecisionTower;
    private Decision specialCardDecisionTower;

    void buildTurnTower() {

    }

    void buildDiscardTower() {

    }

    void buildSpecialCardTower() {

    }

    Decision makeTurnDecision(AIController control) {
        return turnDecisionTower.decide(control);
    }

    Decision makeDiscardDecision(AIController control) {
        return discardDecisionTower.decide(control);
    }

    Decision makeSpecialCardDecision(AIController control) {
        return specialCardDecisionTower.decide(control);
    }

    @Override
    public void makeStep() {
        //TODO
    }

    @Override
    public String getTip() {
        return null; //TODO
    }

}