package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

import java.util.EnumSet;
import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.TURN_ACTION;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_ANY_ARTIFACT_CARD;
import static de.sopra.javagame.model.ArtifactCardType.*;

/**
 * @author Niklas Falke
 * @version 10.09.2019
 * @since 10.09.2019
 */

@DoAfter(act = TURN_ACTION, value = TurnCaptureTreasure.class)
@PreCondition(allTrue = PLAYER_HAS_ANY_ARTIFACT_CARD)
public class TurnGivePlayerTheFourthTreasureCard extends Decision {
    private PlayerType target;
    private ArtifactCardType given;
    private boolean courier;

    @Override
    public Decision decide() {
        List<Player> allPlayers = control.getAllPlayers();
        List<PlayerType> receivers = player().legalReceivers();
        allPlayers.removeIf(player -> !receivers.contains(player.getType()));

        EnumSet<ArtifactCardType> artifacts = EnumSet.of(EARTH, FIRE, WATER, AIR);
        for (Player player : allPlayers) {
            EnhancedPlayerHand otherHand = hand(player);

            for (ArtifactCardType artifact : artifacts) {
               if (all(playerHand().getAmount(artifact) > ZERO_CARDS, otherHand.getAmount(artifact) == THREE_CARDS)) {
                   given = artifact;
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
