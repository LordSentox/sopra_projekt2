package de.sopra.javagame.view.command;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.view.abstraction.GameWindow;
import de.spaceparrots.api.command.annotation.Command;
import de.spaceparrots.api.command.annotation.Scope;
import de.spaceparrots.api.command.annotation.Senior;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 18.09.2019
 * @since 18.09.2019
 */
@Senior
public class MapManipulation {

    @Command("dryall")
    public void dryout(@Scope GameWindow window) {
        window.getControllerChan().getCurrentAction().getMap().forEach(tile -> tile.setState(MapTileState.DRY));
        window.getControllerChan().getInGameViewAUI().refreshHopefullyAll(window.getControllerChan().getCurrentAction());
    }

    @Command("floodall")
    public void floodAll(@Scope GameWindow window) {
        window.getControllerChan().getCurrentAction().getMap().stream().forEach(MapTile::flood);
        window.getControllerChan().getInGameViewAUI().refreshHopefullyAll(window.getControllerChan().getCurrentAction());
    }

    @Command("setstate")
    public void setState(@Scope GameWindow window, Integer x, Integer y, MapTileState state) {
        window.getControllerChan().getCurrentAction().getMap().get(x, y).setState(state);
        window.getControllerChan().getInGameViewAUI().refreshHopefullyAll(window.getControllerChan().getCurrentAction());
    }

}