package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.player.Player;

import java.util.List;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class TurnMoveOntoTreasureCollectionPointIfPlayerHasFour extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        int water = 0;
        int fire = 0;
        int earth = 0;
        int air = 0;
        for (ArtifactCard card : activeHand) {
            if (card.getType() == ArtifactCardType.AIR) {
                air++;
            } else if (card.getType() == ArtifactCardType.EARTH) {
                earth++;
            } else if (card.getType() == ArtifactCardType.FIRE) {
                fire++;
            } else if (card.getType() == ArtifactCardType.WATER) {
                water++;
            }
        }
        ArtifactType type = control.getTile(activePlayer.getPosition()).getProperties().getHidden();
        if (air == 4 && !(type == ArtifactType.AIR)) {
            return this;
        } else if (earth == 4 && !(type == ArtifactType.EARTH)) {
            return this;
        } else if (fire == 4 && !(type == ArtifactType.FIRE)) {
            return this;
        } else if (water == 4 && !(type == ArtifactType.WATER)) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
