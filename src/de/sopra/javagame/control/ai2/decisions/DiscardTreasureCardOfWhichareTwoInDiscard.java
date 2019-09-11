package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke, Julius Korweck
 * @version 11.09.2019
 * @since 10.09.2019
 */
public class DiscardTreasureCardOfWhichareTwoInDiscard extends Decision {
    @Override
    public Decision decide() {
        Collection<ArtifactCard> discardStack = control.getArtifactCardStackTracker().getDiscardPile();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        playerHand().getCards().forEach(activeCard ->
        {
            int count = 0;
            for (ArtifactCard discarded : discardStack) {
                if (discarded.getType() == activeCard.getType()) {
                    count++;
                }
            }
            if (count > ONE_CARD) {
                atomicBoolean.set(true);
            }
        });
        return atomicBoolean.get() ? this : null;
    }

    @Override
    public void act() {
        //TODO
    }
}