package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactType;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
public class TurnMoveOntoTreasureCollectionPointIfPlayerHasFour extends Decision {
    @Override
    public Decision decide() {
        ArtifactType type = tile().getProperties().getHidden();
        int amountOfType = playerHand().getAmount(type);

        if (amountOfType == 4) {
            return this;
        }
        return null;
    }

    @Override
    public void act() {
        //TODO
    }
}
