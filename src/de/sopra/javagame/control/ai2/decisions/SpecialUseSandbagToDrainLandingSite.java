package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;


/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

@DoAfter(act = DecisionResult.PLAY_SPECIAL_CARD, value = Decision.class)
public class SpecialUseSandbagToDrainLandingSite extends Decision {

    @Override
    public Decision decide() {

        if (!control.anyPlayerHasCard(ArtifactCardType.SANDBAGS)) {
            return null;
        }

        if (!hasValidActions(0)) {
            return null;
        }

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();

        if (landingSite.getState() != MapTileState.FLOODED) {
            return null;
        }

        /* Wahrscheinlichkeit berechnen, dass Landeplatz versinken wird:
         * befindet sich der Landeplatz im Ablagestapel der Flutkarten, wird also
         * nicht gezogen, wenn keine Flut-steigt-Karten mehr im Artefaktstapel sind
         * wäre zb 0% Wahrscheinlichkeit, zu versinken
         */
        if (true) {
            //TODO
            //@see control.getFloodCardStackTracker()
            //dort sind Methoden zum erhalten der gemerkten Karten oben auf dem Stapel
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
