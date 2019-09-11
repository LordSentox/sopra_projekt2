package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.Player;

import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class DiscardUseSandbagToKeepFourTreasureCards extends Decision {
    @Override
    public Decision decide() {
        if (control.anyTile(MapTileState.FLOODED) != null) {
            Player activePlayer = control.getActivePlayer();
            List<ArtifactCard> activeHand = activePlayer.getHand();
            boolean hasSand = false;
            for (ArtifactCard sand : activeHand) {
                if (sand.getType() == ArtifactCardType.SANDBAGS) {
                    hasSand = true;
                }
            }
            if (!hasSand) {
                return null;
            }
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
            if (air == 4 || fire == 4 || earth == 4 || water == 4) {
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
