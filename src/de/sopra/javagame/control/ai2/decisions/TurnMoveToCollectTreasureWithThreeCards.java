package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_ANY_ARTIFACT_CARD;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnDrainTile.class)
@PreCondition(allTrue = PLAYER_HAS_ANY_ARTIFACT_CARD)
public class TurnMoveToCollectTreasureWithThreeCards extends Decision {
    private Point moveTowards;

    @Override
    public Decision decide() {
        List<Pair<Point, MapTile>> temples = control.getTemples();
        EnhancedPlayerHand hand = playerHand();
        int minRange = Integer.MAX_VALUE;
        int range;
        if (hand.getAmount(ArtifactCardType.EARTH) > TWO_CARDS) {
            for (Pair<Point, MapTile> temple : temples) {
                if (temple.getRight().getProperties().getHidden() == ArtifactType.EARTH &&
                        !(temple.getRight().getState() == MapTileState.GONE)) {
                    range = control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if (range < minRange) {
                        minRange = range;
                        moveTowards = control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            }
        } else if (hand.getAmount(ArtifactCardType.FIRE) > TWO_CARDS) {
            for (Pair<Point, MapTile> temple : temples) {
                if (temple.getRight().getProperties().getHidden() == ArtifactType.FIRE &&
                        !(temple.getRight().getState() == MapTileState.GONE)) {
                    range = control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if (range < minRange) {
                        minRange = range;
                        moveTowards = control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            }
        } else if (hand.getAmount(ArtifactCardType.AIR) > TWO_CARDS) {
            for (Pair<Point, MapTile> temple : temples) {
                if (temple.getRight().getProperties().getHidden() == ArtifactType.AIR &&
                        !(temple.getRight().getState() == MapTileState.GONE)) {
                    range = control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if (range < minRange) {
                        minRange = range;
                        moveTowards = control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            }
        } else if (hand.getAmount(ArtifactCardType.WATER) > TWO_CARDS) {
            for (Pair<Point, MapTile> temple : temples) {
                if (temple.getRight().getProperties().getHidden() == ArtifactType.WATER &&
                        !(temple.getRight().getState() == MapTileState.GONE)) {
                    range = control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if (range < minRange) {
                        minRange = range;
                        moveTowards = control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            }
        }


        return null;
    }

    @Override
    public ActionQueue act() {
        if (player().getType() == PlayerType.PILOT && needSpecialToMove(player().getPosition(), moveTowards)) {
            return startActionQueue().pilotFlyTo(moveTowards);
        } else if (player().getType() == PlayerType.DIVER && needSpecialToMove(player().getPosition(), moveTowards)) {
            return startActionQueue().diverDiveTo(moveTowards);
        } else {
            return startActionQueue().move(moveTowards);
        }
    }

}
