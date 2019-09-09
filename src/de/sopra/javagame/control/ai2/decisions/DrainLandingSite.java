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

public class DrainLandingSite implements Decision {
    
    @Override
    public Decision decide(AIController control){
        Turn turn = control.getActiveTurn();
        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        MapTile landingSite = informationLandingSite.getRight();
         
        if(landingSite.getState().equals(MapTileState.FLOODED)){
             return this;
        }
        return null;
    }
    
    @Override
    public Decision act(AIController control){
        //TODO
    }
    
}
