package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;

import java.util.List;
/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class DiscardTreasureCardOfWhichareTwoInDiscard extends Decision {
    @Override
    public Decision decide() {
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        int count;
        Turn turn = control.getActiveTurn();
        //List discardedStack = turn.getArtifactCardStack().
        for (ArtifactCard activeCard : activeHand) {
            count = 0;
            for (ArtifactCard discarded : discardStack) {
                if (discarded.equals(activeCard)) {
                    count++;
                }
            }
            if (count > 1) {
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
