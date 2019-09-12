package de.sopra.javagame.view.command;

import de.sopra.javagame.view.GameWindow;
import de.spaceparrots.api.command.core.CommandProcessor;
import de.spaceparrots.api.command.interfaces.CommandResult;
import de.spaceparrots.api.command.interfaces.Definitions;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 11.09.2019
 * @since 11.09.2019
 */
public final class Commands {

    private static Commands HANDLER;

    public static CommandResult processCommand(GameWindow window, String command) {
        synchronized (window) {
            if (HANDLER == null) {
                HANDLER = new Commands();
                HANDLER.init();
            }
        }
        return HANDLER.processor.simpleInputHandler().apply(command);
    }

    private CommandProcessor processor;

    private Commands() {
    }

    private void init() {
        processor = new CommandProcessor(Definitions.standard());
        loadCommands();
        prepareTypes();
    }

    private void loadCommands() {

    }

    private void prepareTypes() {

    }

}