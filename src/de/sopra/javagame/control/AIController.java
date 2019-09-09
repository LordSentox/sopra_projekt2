package de.sopra.javagame.control;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;

import java.awt.*;
import java.util.List;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class AIController {

    private ControllerChan controllerChan;

    public AIController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    public Turn getActiveTurn() {
        return controllerChan.getCurrentTurn();
    }

    public Player getActivePlayer() {
        return null; //TODO
    }

    public Pair<Point, MapTile> getTile(PlayerType playerType) {
        return null; //TODO
    }

    public Pair<Pair<Point, MapTile>, Pair<Point, MapTile>> getTile(ArtifactType artifactType) {
        return null; //TODO
    }
    
    public List<Pair<Point, MapTile>> getTemples(){
        return null; //TODO
    }

    public MapTile getTile(Point point) {
        return null; //TODO
    }

    public List<Player> getAllPlayers() {
        return null; //TODO
    }
    
    public List<Point> getDrainablePositionsOneMoveAway(Point position, PlayerType playerType){
        return null; //TODO
    }

    public boolean anyPlayerHasCard(ArtifactCardType artifactCardType) {
        // TODO Auto-generated method stub
        return false;
    }
}