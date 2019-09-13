package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.CardStackTracker;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.WaterLevel;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import static de.sopra.javagame.control.ai2.decisions.Condition.*;

import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 12.09.2019
 * @since 12.09.2019
 */
@PreCondition(allTrue = {PLAYER_HAS_HELICOPTER_CARD, GAME_HAS_ALL_ARTIFACTS},
        allFalse = PLAYER_REACHES_LANDINGSITE_WITH_LESS_THAN_SEVEN_ACTIONS)
public class SpecialFlyPlayersToLandingSiteForDeparture extends Decision {

    @Override
    public Decision decide() {

//        if (control.getTotalAmountOfCardsOnHands(ArtifactCardType.HELICOPTER) > ONE_CARD) {
//            return this;
//        }
        //prüfe, ob Spieler nicht mehr zu Landeplatz gelangen können, dann fliege sie aus
        Point landingSitePosition = control.getTile(PlayerType.PILOT).getLeft();
        List<Player> allPlayers = control.getAllPlayers();
        for(Player player : allPlayers){
            if (control.getMinimumActionsNeededToReachTarget
                    (player.getPosition(), landingSitePosition, player.getType())== Integer.MAX_VALUE){
                return this;
            }
        }
        if(control.getCurrentAction().getWaterLevel().getLevel() != WaterLevel.MAX_WATER_LEVEL){
            return this;
        }
        
        
        
        /* TODO wahrscheinlichkeit berechnen: einzige Helikopterkarte gespielt
         * bis neue Helikopterkarte gezogen wird ist Wasserlevel tödlich
         * dann spieler lieber laufen lassen und heli sparen
         */
        CardStackTracker<FloodCard> floodCards = control.getFloodCardStackTracker();
        CardStackTracker<ArtifactCard> artifactCards = control.getArtifactCardStackTracker();
        int helisInDiscardPile = artifactCards.cardsInDiscardPile(card -> card.getType() == ArtifactCardType.HELICOPTER);


        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue(); //TODO
    }

}
