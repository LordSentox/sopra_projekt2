package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;

import static de.sopra.javagame.control.ai2.decisions.Condition.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 12.09.2019
 * @since 12.09.2019
 */
@PreCondition(allTrue = {PLAYER_HAS_HELICOPTER_CARD, GAME_HAS_ALL_ARTIFACTS},
        allFalse = PLAYER_REACHES_LANDINGSITE_WITH_LESS_THAN_SEVEN_ACTIONS)
public class SpecialFlyPlayersToLandingSiteForDeparture extends Decision {

    @Override
    public Decision decide() {

        if (control.getTotalAmountOfCardsOnHands(ArtifactCardType.HELICOPTER) > 1) {
            return this;
        }
        /* TODO wahrscheinlichkeit berechnen: einzige Helikopterkarte gespielt
         * bis neue Helikopterkarte gezogen wird ist Wasserlevel tödlich
         * dann spieler lieber laufen lassen und heli sparen
         */


        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
