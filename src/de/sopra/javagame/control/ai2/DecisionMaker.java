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
        turnDecisionTower.setControl(control);
        return turnDecisionTower.decide();
    }

    Decision makeDiscardDecision(AIController control) {
        discardDecisionTower.setControl(control);
        return discardDecisionTower.decide();
    }

    Decision makeSpecialCardDecision(AIController control) {
        specialCardDecisionTower.setControl(control);
        return specialCardDecisionTower.decide();
    }

    @Override
    public void makeStep(AIController control) {
        if (control.isCurrentlyDiscarding()) {
            Decision decision = makeDiscardDecision(control);
            decision.act();
        } else {
            Decision turn = makeTurnDecision(control);
            turn.act();
            Decision special = makeSpecialCardDecision(control);
            if (special != null) //Nicht immer müssen Spezialkarten gespielt werden
                special.act();
        }
    }

    @Override
    public String getTip(AIController control) {
        return null; //TODO
    }

}