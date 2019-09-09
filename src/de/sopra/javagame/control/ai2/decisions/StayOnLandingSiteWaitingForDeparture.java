package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;

import java.awt.Point;
import java.util.EnumSet;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

public class StayOnLandingSiteWaitingForDeparture implements Decision {

    @Override
    public Decision decide(AIController control) {
        Turn turn = control.getActiveTurn();
        EnumSet<ArtifactType> discoveredArtifacts = turn.getDiscoveredArtifacts();
        
        Player activePlayer = control.getActivePlayer();
        Point playerPosition = activePlayer.getPosition();
        
        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();
        
        if(discoveredArtifacts.size()==4 && landingSitePosition.equals(playerPosition)){
            return this;
            
        }
        return null;
    }

    @Override
    public void act(AIController control) {
        // TODO Auto-generated method stub
        
    }
    

}
