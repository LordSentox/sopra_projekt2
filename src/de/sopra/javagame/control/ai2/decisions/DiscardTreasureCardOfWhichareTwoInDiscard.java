package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.sopra.javagame.control.ai2.DecisionResult.DISCARD;

/**
 * <h1>projekt2</h1>
 *
 * @author Niklas Falke, Julius Korweck
 * @version 11.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = DISCARD, value = DiscardTreasureCardsThatAnotherPlayerHasFourOf.class)
public class DiscardTreasureCardOfWhichareTwoInDiscard extends Decision {
    private ArtifactCardType discarding;     
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
                discarding= activeCard.getType();
            }
        });
        return atomicBoolean.get() ? this : null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().discard(discarding);
    }
}
