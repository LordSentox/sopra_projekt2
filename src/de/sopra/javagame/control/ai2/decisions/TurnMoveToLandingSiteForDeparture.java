package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.Decision;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */

public class TurnMoveToLandingSiteForDeparture extends Decision  {

    @Override
    public Decision decide() {
        Turn turn = control.getActiveTurn();
        EnumSet<ArtifactType> discoveredArtifacts = turn.getDiscoveredArtifacts();
        
        if (discoveredArtifacts.size() != 4){
            return null;
        }
        
        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();
        
        Player player = turn.getActivePlayer();
        Point playerPosition = player.getPosition();
        if (!landingSitePosition.equals(playerPosition)){
            return this;
        }
        
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub
        
    }

}
