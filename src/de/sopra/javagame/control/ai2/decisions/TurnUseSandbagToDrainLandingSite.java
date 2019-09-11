package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
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

public class TurnUseSandbagToDrainLandingSite extends Decision {

    @Override
    public Decision decide() {

        if (!control.anyPlayerHasCard(ArtifactCardType.SANDBAGS)) {
            return null;
        }

        if (player().getActionsLeft() != 0) {
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
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
