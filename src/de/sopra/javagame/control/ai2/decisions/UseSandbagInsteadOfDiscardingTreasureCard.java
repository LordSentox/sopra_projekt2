package de.sopra.javagame.control.ai2.decisions;

import java.util.List;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class UseSandbagInsteadOfDiscardingTreasureCard extends Decision{
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        boolean hasSand=false;
        for(ArtifactCard sand: activeHand) { 
            if(sand.getType()==ArtifactCardType.SANDBAGS){hasSand=true;}
        }    
        if(!hasSand){
            return null;
        }
        for(ArtifactCard treasure: activeHand) {
            if(treasure.getType()==ArtifactCardType.AIR||
               treasure.getType()==ArtifactCardType.EARTH||
               treasure.getType()==ArtifactCardType.WATER||
               treasure.getType()==ArtifactCardType.FIRE){
                return this;
            }
        }
        return null;
    }    
    @Override
    public void act() {
        //TODO
    }
}
