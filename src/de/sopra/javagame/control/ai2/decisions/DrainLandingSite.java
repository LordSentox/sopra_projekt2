package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;

import java.awt.Point;
import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

public class DrainLandingSite implements Decision {
    
    @Override
    public Decision decide(AIController control){
        
        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();
        Point landingSitePosition = informationLandingSite.getLeft();
        
        Player activePlayer = control.getActivePlayer();
        List<Point> drainablePositionslist = activePlayer.drainablePositions();
        
        if (drainablePositionslist.contains(landingSitePosition) 
                && landingSite.getState().equals(MapTileState.FLOODED)){
            return this;           
        }     
                 
        return null;
    }
    
    @Override
    public void act(AIController control){
        //TODO
    }
    
}
