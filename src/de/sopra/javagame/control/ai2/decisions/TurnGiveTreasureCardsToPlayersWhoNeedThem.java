package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

import java.util.ArrayList;
import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_ANY_ARTIFACT_CARD;
import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = TURN_ACTION, value = TurnFlyActivePlayerToOrphanedTempleMapForDraining.class)
@PreCondition(allTrue = PLAYER_HAS_ANY_ARTIFACT_CARD)
public class TurnGiveTreasureCardsToPlayersWhoNeedThem extends Decision {
    private PlayerType target;
    private ArtifactCardType given;
    private boolean courier;

    @Override
    public Decision decide() {

        List<ArtifactCardType> allTypes = new ArrayList<ArtifactCardType>();
        allTypes.add(FIRE);
        allTypes.add(EARTH);
        allTypes.add(AIR);
        allTypes.add(WATER);

        List<Player> allPlayers = control.getAllPlayers();
        List<PlayerType> receivers = player().legalReceivers();
        allPlayers.removeIf(player -> !receivers.contains(player.getType()));

        for (Player player : allPlayers) {
            for (ArtifactCardType currentType : allTypes) {                
                EnhancedPlayerHand enhancedHand = hand(player);
                int oldAmount = playerHand().getAmount(currentType);
                int newAmount = enhancedHand.getAmount(currentType);
                
                if (all(oldAmount > ZERO_CARDS, oldAmount < newAmount, newAmount < FOUR_CARDS)) {
                    given = currentType;
                    target = player.getType();
                    courier = !player.getPosition().equals(player().getPosition());
                    return this;
                }
            }
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        if (!courier) {
            return startActionQueue().trade(given, target);

        }
        return startActionQueue().courierTrade(given, target);
    }
}
