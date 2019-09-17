package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS;

import java.util.List;

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
        List<Pair<Point,MapTile>> temples = control.getTemples();
        EnhancedPlayerHand hand = playerHand();
        int minRange = Integer.MAX_VALUE;
        int range;
        if (hand.getAmount(ArtifactCardType.EARTH) >= FOUR_CARDS ) {
            for(Pair<Point,MapTile> temple: temples){
                if(temple.getRight().getProperties().getHidden()==ArtifactType.EARTH && 
                 !(temple.getRight().getState()==MapTileState.GONE)) {
                    range=control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if(range< minRange){
                        minRange=range;
                        moveTowards=control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            }
        }else
        if (hand.getAmount(ArtifactCardType.FIRE) >= FOUR_CARDS ) { 
            for(Pair<Point,MapTile> temple: temples){
                if(temple.getRight().getProperties().getHidden()==ArtifactType.FIRE && 
                 !(temple.getRight().getState()==MapTileState.GONE)) {
                    range=control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if(range< minRange){
                        minRange=range;
                        moveTowards=control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            }
        }else
        if(hand.getAmount(ArtifactCardType.AIR) >= FOUR_CARDS ) {
            for(Pair<Point,MapTile> temple: temples){
                if(temple.getRight().getProperties().getHidden()==ArtifactType.AIR && 
                 !(temple.getRight().getState()==MapTileState.GONE)) {
                    range=control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if(range< minRange){
                        minRange=range;
                        moveTowards=control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            } 
        }else
        if(hand.getAmount(ArtifactCardType.WATER) >= FOUR_CARDS ){
            for(Pair<Point,MapTile> temple: temples){
                if(temple.getRight().getProperties().getHidden()==ArtifactType.WATER && 
                 !(temple.getRight().getState()==MapTileState.GONE)) {
                    range=control.getMinimumActionsNeededToReachTarget(player().getPosition(), temple.getLeft(), player().getType());
                    if(range< minRange){
                        minRange=range;
                        moveTowards=control.getClosestPointInDirectionOf(player().legalMoves(true), temple.getLeft(), player().getType());
                    }
                }
            }
        }

        
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().move(moveTowards);
    }

}
