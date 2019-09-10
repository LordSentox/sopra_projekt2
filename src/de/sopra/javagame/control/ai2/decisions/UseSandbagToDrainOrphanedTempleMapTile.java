package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactCardType;
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
 * @version 10.09.2019
 * @since 10.09.2019
 */

public class UseSandbagToDrainOrphanedTempleMapTile extends Decision{

        @Override
        public Decision decide() {
            
            if (!control.anyPlayerHasCard(ArtifactCardType.SANDBAGS)) {
                return null;
            }
            
            if (player().getActionsLeft() != 0) {
                return null;
            }
            
            List<Pair<Point, MapTile>> templeList = control.getTemples();
        
            for (int i=0; i<8; i++){
                Point orphanedTemplePoint = templeList.get(i).getLeft();
                MapTile orphanedTemple = templeList.get(i).getRight();            
                
                if(orphanedTemple.getState() != MapTileState.FLOODED){
                    continue;
                }
                
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
                
                if (!((northernNeighbour == null || northernNeighbour.getState() == MapTileState.GONE)
                        && (northEasternNeighbour == null || northEasternNeighbour.getState() == MapTileState.GONE)
                        && (easternNeighbour == null || easternNeighbour.getState() == MapTileState.GONE)
                        && (southEasternNeighbour == null || southEasternNeighbour.getState() == MapTileState.GONE)
                        && (southernNeighbour == null || southernNeighbour.getState() == MapTileState.GONE)
                        && (southWesternNeighbour == null || southWesternNeighbour.getState() == MapTileState.GONE)
                        && (westernNeighbour == null || westernNeighbour.getState() == MapTileState.GONE)
                        && (northWesternNeighbour == null || northWesternNeighbour.getState() == MapTileState.GONE))){
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
