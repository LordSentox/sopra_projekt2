package de.sopra.javagame.control.ai2.decisions;


import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_NO_ACTION_LEFT;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnMoveToOrphanedTempleMapTileForDraining.class)
@PreCondition(allFalse = PLAYER_NO_ACTION_LEFT)
public class TurnDrainTempleMapTileOfUndiscoveredArtifact extends Decision {
    private Point drainPoint;

    @Override
    public Decision decide() {
        List<Pair<Point, MapTile>> templeList = control.getTemples();
        EnumSet<ArtifactType> discoveredArtifacts = action().getDiscoveredArtifacts();

        for (int i = 0; i < 8; i++) {

            MapTile orphanedTemple = templeList.get(i).getRight();
            ArtifactType templeType = orphanedTemple.getProperties().getHidden();
            //prüft, ob Artefakt des betroffenen Tempels bereits geborgen wurde, dann Tempelrettung irrelevant
            if (discoveredArtifacts.contains(templeType)) {
                continue;
            }
            //prüft, ob Tempel überhaupt geflutet ist
            if (orphanedTemple.getState() != MapTileState.FLOODED) {
                continue;
            }
            Point orphanedTemplePosition = templeList.get(i).getLeft();
            List<Point> drainablePositions = player().drainablePositions();
            //prüfe, ob der aktive Spieler den betroffenen Tempel trockenlegen kann
            if (!drainablePositions.contains(orphanedTemplePosition)) {
                return null;
            }
            drainPoint = orphanedTemplePosition.getLocation();
            return this;
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        if (!(player().getType() == PlayerType.ENGINEER)) {
            return startActionQueue().drain(drainPoint);
        }
        return startActionQueue().engineersDrain(drainPoint);
    }

}
