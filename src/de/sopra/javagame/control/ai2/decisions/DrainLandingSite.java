package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;

import java.awt.Point;
import java.util.EnumSet;
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
        Turn turn = control.getActiveTurn();
        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();
        Point landingSitePosition = informationLandingSite.getLeft();
        
        Player activePlayer = control.getActivePlayer();
        Point playerPosition = activePlayer.getPosition();
        
         
        if(landingSite.getState().equals(MapTileState.FLOODED) && playerPosition.equals(landingSitePosition)){
             return this;
        }
        return null;
    }
    
    @Override
    public void act(AIController control){
        //TODO
    }
    
}
