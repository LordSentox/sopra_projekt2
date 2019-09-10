package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.Turn;
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
public class DiscardTreasureCardsOfCollectedTreasures implements Decision {

    @Override
    public Decision decide(AIController control) {
        Turn turn = control.getActiveTurn();
        EnumSet<ArtifactType> discoveredArtifacts = turn.getDiscoveredArtifacts();
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
    public void act(AIController control) {
        //TODO
    }

}