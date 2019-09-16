package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

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
    @Override
    public Decision decide() {

        int water = playerHand().getAmount(WATER);
        int fire = playerHand().getAmount(FIRE);
        int earth = playerHand().getAmount(EARTH);
        int air = playerHand().getAmount(AIR);

        List<Player> allPlayers = control.getAllPlayers();
        List<PlayerType> receivers = player().legalReceivers();
        allPlayers.removeIf(player -> !receivers.contains(player.getType()));

        for (Player player : allPlayers) {
            EnhancedPlayerHand hand = hand(player);
            int water2 = hand.getAmount(WATER);
            int fire2 = hand.getAmount(FIRE);
            int earth2 = hand.getAmount(EARTH);
            int air2 = hand.getAmount(AIR);       
            if(all(air > ZERO_CARDS, air2 == THREE_CARDS)) {
                given=AIR;
                target= player.getType();
                return this;
            }    
            if(all(earth > ZERO_CARDS, earth2 == THREE_CARDS)) {
                given=EARTH;
                target= player.getType();
                return this;
            }    
            if(all(fire > ZERO_CARDS, fire2 == THREE_CARDS)){
                given=FIRE;
                target= player.getType();
                return this;    
            }
            if(all(water > ZERO_CARDS, water2 == THREE_CARDS)) {
                given=WATER;
                target= player.getType();
                return this;    
            }    
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().trade(given, target); //TODO
    }

}
