package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.player.PlayerType;

import static de.sopra.javagame.control.ai2.DecisionResult.PLAY_SPECIAL_CARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.*;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
@DoAfter(act = PLAY_SPECIAL_CARD, value = Decision.class)
@PreCondition(allTrue = {PLAYER_HAS_SANDBAGS_CARD, PLAYER_NO_ACTION_LEFT, GAME_LANDING_SITE_IS_FLOODED})
public class SpecialUseSandbagToDrainLandingSite extends Decision {

    @Override
    public Decision decide() {

        /* Wahrscheinlichkeit berechnen, dass Landeplatz versinken wird:
         * befindet sich der Landeplatz im Ablagestapel der Flutkarten, wird also
         * nicht gezogen, wenn keine Flut-steigt-Karten mehr im Artefaktstapel sind
         * wäre zb 0% Wahrscheinlichkeit, zu versinken
         */

        //if (true) {
            //TODO
            //@see control.getFloodCardStackTracker()
            //dort sind Methoden zum erhalten der gemerkten Karten oben auf dem Stapel
            return this;
        //}
        //return null;

//        if (true) {
//            //TODO
//            //@see control.getFloodCardStackTracker()
//            //dort sind Methoden zum erhalten der gemerkten Karten oben auf dem Stapel
//            return this;
        }
        //return null;


    @Override
    public ActionQueue act() {
        return startActionQueue().sandbagCard(control.getTile(PlayerType.PILOT).getLeft());
    }

}
