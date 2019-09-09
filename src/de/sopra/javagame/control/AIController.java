package de.sopra.javagame.control;

import java.util.*;

import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
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
    public List<Player> getAllPlayers(){
        return null; //TODO
    }
}