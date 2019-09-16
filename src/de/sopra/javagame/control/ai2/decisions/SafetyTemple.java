package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.DecisionResult.SWIM_TO_SAFETY;

@DoAfter(act = SWIM_TO_SAFETY, value = SafetyLandingSite.class)
public class SafetyTemple extends Decision {
    
    private Point targetPoint;
    @Override
    public Decision decide() {
        //Es wird nicht ueberprueft, ob das Artefakt aus dem Tempel bereits geborgen ist
        if (condition(Condition.GAME_ANY_LAST_TEMPLE_IN_DANGER).isTrue()) {
            Point targetTemple=null;
            //Pair<Pair<Point, MapTile>,Pair<Point, MapTile>> templePair =control.getTile(artifactType);
            for(Pair<Point, MapTile> temple : control.getTemples()){
                if(temple.getRight().getState()==MapTileState.FLOODED) {
                    for(Pair<Point, MapTile> templeBro : control.getTemples()){
                        if(!temple.getLeft().equals(templeBro.getLeft()) && 
                           temple.getRight().getProperties().getHidden()==templeBro.getRight().getProperties().getHidden()&&
                           templeBro.getRight().getState()==MapTileState.GONE){
                            targetTemple=temple.getLeft();
                        }
                    }
                }
                if(targetTemple==null){
                    targetTemple= control.getTemples().get(0).getLeft();
                }
            }    
            targetPoint = control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                    targetTemple,
                    control.getActivePlayer().getType());
            return this;
        }    
        if (condition(Condition.PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS).isTrue()) {
            EnhancedPlayerHand activeHand = playerHand();
            
            if(activeHand.getAmount(ArtifactType.WATER)==4) {
                targetPoint=control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                        control.getTile(ArtifactType.WATER).getLeft().getLeft(),
                        control.getActivePlayer().getType());
                        return this;
            }else if(activeHand.getAmount(ArtifactType.EARTH)==4) {
                targetPoint= control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                        control.getTile(ArtifactType.EARTH).getLeft().getLeft(),
                        control.getActivePlayer().getType());
                        return this;
            }else if(activeHand.getAmount(ArtifactType.FIRE)==4) {
                targetPoint= control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                        control.getTile(ArtifactType.FIRE).getLeft().getLeft(),
                        control.getActivePlayer().getType());
                        return this;
            }else if(activeHand.getAmount(ArtifactType.AIR)==4) {
                targetPoint= control.getClosestPointInDirectionOf(control.getActivePlayer().legalMoves(true),
                        control.getTile(ArtifactType.AIR).getLeft().getLeft(),
                        control.getActivePlayer().getType());
                        return this;    
            } 
        }
        
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().move(targetPoint);
    }

}
