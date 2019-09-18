package de.sopra.javagame.view.command;

import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.view.abstraction.GameWindow;
import de.spaceparrots.api.command.annotation.Command;
import de.spaceparrots.api.command.annotation.Scope;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 18.09.2019
 * @since 18.09.2019
 */
public class MapManipulation {

    @Command("dryall")
    public void dryout(@Scope GameWindow window) {
        window.getControllerChan().getCurrentAction().getMap().forEach(tile -> tile.setState(MapTileState.DRY));
    }

}