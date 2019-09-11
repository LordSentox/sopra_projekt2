package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class TurnMoveToCollectTreasureWithThreeCards extends Decision {
    @Override
    public Decision decide() {
        if((playerHand().getAmount(ArtifactCardType.EARTH)>2 && tile().getProperties().getHidden() == ArtifactType.EARTH)||
           (playerHand().getAmount(ArtifactCardType.FIRE)>2 && tile().getProperties().getHidden() == ArtifactType.FIRE)||
           (playerHand().getAmount(ArtifactCardType.AIR)>2 && tile().getProperties().getHidden() == ArtifactType.AIR)||
           (playerHand().getAmount(ArtifactCardType.WATER)>2 && tile().getProperties().getHidden() == ArtifactType.WATER)){
            return this;
        }
        return null;
    }
    
    @Override
    public void act() {
        //TODO
    } 
}
