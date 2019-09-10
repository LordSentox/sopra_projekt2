package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

public class DrainOrphanedTempleMapTiles extends Decision{

    @Override
    public Decision decide() {
        
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
            
            if(!orphanedTemple.getState().equals(MapTileState.FLOODED)){
                continue;
            }
            
            return this;
            
        }   
        
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub
        
    }

}
