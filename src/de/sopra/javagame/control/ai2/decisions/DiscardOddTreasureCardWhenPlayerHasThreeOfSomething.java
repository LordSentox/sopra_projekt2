package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.player.Player;

import java.util.List;

import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class DiscardOddTreasureCardWhenPlayerHasThreeOfSomething extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        int water = playerHand().getAmount(WATER);
        int fire = playerHand().getAmount(FIRE);
        int earth = playerHand().getAmount(EARTH);
        int air = playerHand().getAmount(AIR);
        if (any(air == 3, earth == 3, fire == 3, water == 3)) {
            if (any(air <= 2, earth <= 2, fire <= 2, water <= 2)) {
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