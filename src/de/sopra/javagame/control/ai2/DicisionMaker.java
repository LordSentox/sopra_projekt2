package de.sopra.javagame.control.ai2;

import de.sopra.javagame.model.JavaGame;

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

    Decision makeTurnDecision(JavaGame javaGame) {
        return turnDecisionTower.decide(javaGame);
    }

    Decision makeDiscardDecision(JavaGame javaGame) {
        return discardDecisionTower.decide(javaGame);
    }

    Decision makeSpecialCardDecision(JavaGame javaGame) {
        return specialCardDecisionTower.decide(javaGame);
    }

}