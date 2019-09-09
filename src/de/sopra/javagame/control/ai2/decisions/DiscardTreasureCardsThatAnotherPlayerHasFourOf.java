package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
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
public class DiscardTreasureCardsThatAnotherPlayerHasFourOf implements Decision {

    @Override
    public Decision decide(AIController control) {
        Turn turn = control.getActiveTurn();
        EnumSet<ArtifactType> discoveredArtifacts = turn.getDiscoveredArtifacts();
        Player activePlayer = control.getActivePlayer();
        List<ArtifactCard> activeHand = activePlayer.getHand();
        List<Player> allPlayers = control.getAllPlayers();
        for (ArtifactCard activeCard : activeHand) {
            if(activeCard.getType()==ArtifactCardType.SANDBAGS || activeCard.getType()==ArtifactCardType.HELICOPTER) {break;}
            for(Player player : allPlayers) {
                List<ArtifactCard> hand = player.getHand();
                if(hand.size()<4) {break;}
                int water=0; 
                int fire=0;
                int earth=0;
                int air=0;
                for(ArtifactCard card: hand) { 
                    if(card.getType()==ArtifactCardType.AIR) {
                        air++;
                    } else if(card.getType()==ArtifactCardType.EARTH) {
                        earth++;
                    } else if(card.getType()==ArtifactCardType.FIRE) {
                        fire++;
                    } else if(card.getType()==ArtifactCardType.WATER) {
                        water++;
                    }
                    if(water >3&& activeCard.getType()==ArtifactCardType.WATER) {return this;}
                    if(fire >3&& activeCard.getType()==ArtifactCardType.FIRE) {return this;}
                    if(earth >3&& activeCard.getType()==ArtifactCardType.EARTH) {return this;}
                    if(air >3&& activeCard.getType()==ArtifactCardType.AIR) {return this;}
                }
            }                                            
       }
       return null;
    }

    

    @Override
    public void act(AIController control) {
        //TODO
    }

}