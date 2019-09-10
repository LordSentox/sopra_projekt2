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
public class FlyToTreasurePickupSiteToKeepFourTreasureCards extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        boolean hasHeli=false;
        for(ArtifactCard heli: activeHand) { 
            if(heli.getType()==ArtifactCardType.HELICOPTER){hasHeli=true;}
        }    
        if(!hasHeli){
            return null;
        }
        int water=0; 
        int fire=0;
        int earth=0;
        int air=0;
        for(ArtifactCard card: activeHand) { 
            if(card.getType()==ArtifactCardType.AIR) {
                air++;
            } else if(card.getType()==ArtifactCardType.EARTH) {
                earth++;
            } else if(card.getType()==ArtifactCardType.FIRE) {
                fire++;
            } else if(card.getType()==ArtifactCardType.WATER) {
                water++;
            }
        }
        if(air==4||fire==4||earth==4||water==4){
            return this;
        }    
        return null;
    }
    @Override
    public void act() {
        //TODO
    }
}
