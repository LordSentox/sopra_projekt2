package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;

import java.util.EnumSet;
import java.util.List;

public class DiscardTreasureCardOfWhichareTwoInDiscard implements Decision {
    @Override
    public Decision decide(AIController control) {
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        int count;
        Turn turn = control.getActiveTurn();
        //List discardedStack = turn.getArtifactCardStack().
        for (ArtifactCard activeCard : activeHand) {
            count=0;
            for(ArtifactCard discarded : discardStack) {
                if(discarded.equals(activeCard)){
                    count++;
                }
            }
            if(count>1){
                return this;
            }
        }
        return null;
    }
    
    @Override
    public void act(AIController control) {
        //TODO
    }
}
