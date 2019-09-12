package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.control.ai2.DecisionResult;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.player.Player;

import java.util.EnumSet;
import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
@DoAfter(act=DecisionResult.DISCARD,value=Decision.class)
public class DiscardTreasureCardsOfCollectedTreasures extends Decision {

    @Override
    public Decision decide() {
        Action action = control.getCurrentAction();
        EnumSet<ArtifactType> discoveredArtifacts = action.getDiscoveredArtifacts();
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> hand = activePlayer.getHand();
        for (ArtifactCard card : hand) {
            if (discoveredArtifacts.contains(card.getType().toArtifactType())) {
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