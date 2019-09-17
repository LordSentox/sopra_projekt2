package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;

import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;

@DoAfter(act = SWIM_TO_SAFETY, value = SafetyLandingSite.class)
public class SafetyTemple extends Decision {

    private Point targetPoint;

    @Override
    public Decision decide() {

        EnumSet<ArtifactType> remainingTypes = EnumSet.complementOf(action().getDiscoveredArtifacts());
        remainingTypes.remove(ArtifactType.NONE); //dont want this

        if (condition(Condition.GAME_ANY_LAST_TEMPLE_IN_DANGER).isTrue()) {
            Point targetTemple = null;
            for (ArtifactType type : remainingTypes) {
                Pair<Pair<Point, MapTile>, Pair<Point, MapTile>> templePair = control.getTile(type);
                Pair<Point, MapTile> firstTemple = templePair.getRight();
                Pair<Point, MapTile> secondTemple = templePair.getLeft();
                if (all(firstTemple.getRight().getState() == MapTileState.FLOODED, secondTemple.getRight().getState() == MapTileState.GONE)) {
                    targetTemple = firstTemple.getLeft();
                }
                if (all(firstTemple.getRight().getState() == MapTileState.GONE, secondTemple.getRight().getState() == MapTileState.FLOODED)) {
                    targetTemple = secondTemple.getLeft();
                }

            }
            targetPoint = control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                    targetTemple, control.getActivePlayer().getType());
            return this;
        } else if (condition(Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS).isTrue()) {
            EnhancedPlayerHand activeHand = playerHand();
            for(ArtifactType type : remainingTypes)
            {
                if (activeHand.getAmount(type) == FOUR_CARDS) {
                    targetPoint = control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                            control.getTile(type).getLeft().getLeft(),
                            control.getActivePlayer().getType());
                    return this;
                }
            }
        }

        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().move(targetPoint);
    }

}
