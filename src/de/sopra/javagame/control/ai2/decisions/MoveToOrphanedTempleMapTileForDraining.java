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
            Point orphanedTemplePoint = templeList.get(i).getLeft();
            
            Point northernNeighbourPoint = Direction.UP.translate(orphanedTemplePoint);
            MapTile northernNeighbour = control.getTile(northernNeighbourPoint);
            
            Point northEasternNeighbourPoint = Direction.UP.translate(Direction.RIGHT.translate(orphanedTemplePoint));
            MapTile northEasternNeighbour = control.getTile(northEasternNeighbourPoint);
            
            Point easternNeighbourPoint = Direction.RIGHT.translate(orphanedTemplePoint);
            MapTile easternNeighbour = control.getTile(easternNeighbourPoint);
            
            Point southEasternNeighbourPoint = Direction.DOWN.translate(Direction.RIGHT.translate(orphanedTemplePoint));
            MapTile southEasternNeighbour = control.getTile(southEasternNeighbourPoint);
            
            Point southernNeighbourPoint = Direction.DOWN.translate(orphanedTemplePoint);
            MapTile southernNeighbour = control.getTile(southernNeighbourPoint);
            
            Point southWesternNeighbourPoint = Direction.DOWN.translate(Direction.LEFT.translate(orphanedTemplePoint));
            MapTile southWesternNeighbour = control.getTile(southWesternNeighbourPoint);
            
            Point westernNeighbourPoint = Direction.LEFT.translate(orphanedTemplePoint);
            MapTile westernNeighbour = control.getTile(westernNeighbourPoint);
            
            Point northWesternNeighbourPoint = Direction.UP.translate(Direction.LEFT.translate(orphanedTemplePoint));
            MapTile northWesternNeighbour = control.getTile(northWesternNeighbourPoint);
            
            boolean isOrphaned = false;
            
            if (northernNeighbour.getState().equals(MapTileState.GONE)
                    && northEasternNeighbour.getState().equals(MapTileState.GONE)
                    && easternNeighbour.getState().equals(MapTileState.GONE)
                    && southEasternNeighbour.getState().equals(MapTileState.GONE)
                    && southernNeighbour.getState().equals(MapTileState.GONE)
                    && southWesternNeighbour.getState().equals(MapTileState.GONE)
                    && westernNeighbour.getState().equals(MapTileState.GONE)){
                isOrphaned = true;
            }
            else {
                return null;
            }
            
            MapTile orphanedTemple = templeList.get(i).getRight();
            
            
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
            
            if(activePlayerType.equals(PlayerType.EXPLORER)){
                
                if(!northEasternNeighbour.getState().equals(MapTileState.GONE)) continue;
                
                
                if(!southEasternNeighbour.getState().equals(MapTileState.GONE)) continue;
                
                
                if(!southWesternNeighbour.getState().equals(MapTileState.GONE)) continue;
                
                
                if(!northWesternNeighbour.getState().equals(MapTileState.GONE)) continue;
                
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
