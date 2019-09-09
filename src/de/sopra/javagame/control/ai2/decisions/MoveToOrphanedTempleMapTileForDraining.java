package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Direction;
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

public class MoveToOrphanedTempleMapTileForDraining implements Decision {

    @Override
    public Decision decide(AIController control) {
        
        List<Pair<Point, MapTile>> templeList = control.getTemples();
        
        for (int i=0; i<8; i++){
            Point northernNeighbourPoint = Direction.UP.translate(templeList.get(i).getLeft());
            MapTile northernNeighbour = control.getTile(northernNeighbourPoint);
            Point easternNeighbourPoint = Direction.RIGHT.translate(templeList.get(i).getLeft());
            MapTile easternNeighbour = control.getTile(easternNeighbourPoint);
            Point southernNeighbourPoint = Direction.DOWN.translate(templeList.get(i).getLeft());
            MapTile southernNeighbour = control.getTile(southernNeighbourPoint);
            Point westernNeighbourPoint = Direction.LEFT.translate(templeList.get(i).getLeft());
            MapTile westernNeighbour = control.getTile(westernNeighbourPoint);
            
            if (!northernNeighbour.getState().equals(MapTileState.GONE)
                    || !easternNeighbour.getState().equals(MapTileState.GONE)
                    || !southernNeighbour.getState().equals(MapTileState.GONE)
                    || !westernNeighbour.getState().equals(MapTileState.GONE) ){
                continue;
            }
            
            MapTile orphanedTemple = templeList.get(i).getRight();
            Point orphanedTemplePoint = templeList.get(i).getLeft();
            
            if(!orphanedTemple.getState().equals(MapTileState.FLOODED)){
                continue;
            }
            
            Player activePlayer = control.getActivePlayer();
            PlayerType activePlayerType = activePlayer.getType();
            
            if(!activePlayerType.equals(PlayerType.DIVER)
                    && !activePlayerType.equals(PlayerType.EXPLORER)
                    && !activePlayerType.equals(PlayerType.PILOT)){
                continue;
            }
            
            List<Point> inOneMovedrainablePositionslist = control.getDrainablePositionsOneMoveAway(orphanedTemplePoint, activePlayerType);
            
            if(!inOneMovedrainablePositionslist.contains(orphanedTemplePoint)){
                continue;
            }
            
            int leftActions = activePlayer.getActionsLeft();
            
            if(leftActions<2){
                continue;
            }
            return this;
            
            
        }   
        
        return null;
        
    }

    @Override
    public void act(AIController control) {
        // TODO Auto-generated method stub
        
    }

}
