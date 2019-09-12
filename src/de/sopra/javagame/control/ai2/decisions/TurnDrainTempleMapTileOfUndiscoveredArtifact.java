package de.sopra.javagame.control.ai2.decisions;


import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
@DoAfter(act = DecisionResult.TURN_ACTION, value = TurnMoveToOrphanedTempleMapTileForDraining.class)
public class TurnDrainTempleMapTileOfUndiscoveredArtifact extends Decision {

    @Override
    public Decision decide() {
        List<Pair<Point, MapTile>> templeList = control.getTemples();
        EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

        for (int i = 0; i < 8; i++) {

            if (hasValidActions(0)) {
                return null;
            }

            MapTile orphanedTemple = templeList.get(i).getRight();
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();

            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }
            if (orphanedTemple.getState() != MapTileState.FLOODED) {
                continue;
            }
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }


}
