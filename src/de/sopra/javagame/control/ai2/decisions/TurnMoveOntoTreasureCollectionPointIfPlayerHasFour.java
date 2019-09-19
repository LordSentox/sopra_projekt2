package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
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
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnGiveTreasureCardsToPlayersWhoNeedThem.class)
@PreCondition(allTrue = PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS)
public class TurnMoveOntoTreasureCollectionPointIfPlayerHasFour extends Decision {
    private Point moveTowards;

    @Override
    public Decision decide() {
        List<Pair<Point, MapTile>> temples = control.getTemples();
        EnhancedPlayerHand hand = playerHand();
        int minRange = Integer.MAX_VALUE;
        int range;

        EnumSet<ArtifactType> artifacts = EnumSet.of(ArtifactType.EARTH, ArtifactType.FIRE, ArtifactType.WATER, ArtifactType.AIR);
        for (ArtifactType artifact : artifacts) {
            if (hand.getAmount(artifact) < FOUR_CARDS) {
                continue;
            }

            for (Pair<Point, MapTile> temple : temples) {
                if (temple.getRight().getProperties().getHidden() == artifact &&
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
