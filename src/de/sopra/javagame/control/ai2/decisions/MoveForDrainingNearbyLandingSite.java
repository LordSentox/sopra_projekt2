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

public class MoveForDrainingNearbyLandingSite implements Decision {

    /**
     * Prüfe: ist der Spieler einen Schritt entfernt, um den Landeplatz trocken legen zu können
     * kann der Spieler innerhalb seines Zuges trockenlegen
     */
    
    @Override
    public Decision decide(AIController control) {
        Player activePlayer = control.getActivePlayer();
        int leftActions = activePlayer.getActionsLeft();
        
        if(leftActions<=1){
            return null;
        }
        
        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();
        Point landingSitePosition = informationLandingSite.getLeft();
        
        if(landingSite.getState().equals(MapTileState.FLOODED)){
                
            Point playerPosition = activePlayer.getPosition();
            PlayerType playerType = activePlayer.getType();
            List<Point> drainablePositionslist = control.getDrainablePositionsOneMoveAway(playerPosition, playerType);
                    
            if(drainablePositionslist.contains(landingSitePosition)){
                return this;
            }
        }
        
        return null;
    }

    @Override
    public void act(AIController control) {
        // TODO Auto-generated method stub
        
    }

}
