package de.sopra.javagame.view.command;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.view.abstraction.GameWindow;
import de.spaceparrots.api.command.core.CommandProcessor;
import de.spaceparrots.api.command.interfaces.CommandResult;
import de.spaceparrots.api.command.interfaces.Definitions;
import de.spaceparrots.api.command.util.PropertyResolver;
import de.spaceparrots.api.command.util.TypeResolver;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 11.09.2019
 * @since 11.09.2019
 */
public final class Commands {

    private static Commands handler;
    private static GameWindow window;
    private CommandProcessor processor;

    private Commands() {
    }

    public static CommandResult processCommand(GameWindow gameWindow, String command) {
        synchronized (gameWindow) {
            if (handler == null) {
                handler = new Commands();
                handler.init();
                window = gameWindow;
            }
        }
        return handler.processor.simpleInputHandler().apply(command);
    }

    private void init() {
        processor = new CommandProcessor(Definitions.standard());
        loadCommands();
        prepareTypes();
    }

    private void loadCommands() {

    }

    private void prepareTypes() {
        processor.registerType(Action.class, "turn")
                .registerType(Difficulty.class, "difficulty",
                        TypeResolver.create(String.class, string -> Difficulty.valueOf(string.toUpperCase()), Difficulty::name))
                .registerType(JavaGame.class, "javagame",
                        TypeResolver.simple(window.getControllerChan()::getJavaGame),
                        PropertyResolver.create("mapname", String.class, JavaGame::getMapName),
                        PropertyResolver.create("cheetah", boolean.class, JavaGame::getIsCheetah),
                        PropertyResolver.create("score", int.class, JavaGame::calculateScore),
                        PropertyResolver.create("rounds", int.class, JavaGame::numTurns),
                        PropertyResolver.create("canRedo", boolean.class, JavaGame::canRedo),
                        PropertyResolver.create("canUndo", boolean.class, JavaGame::canUndo),
                        PropertyResolver.create("difficulty", Difficulty.class, JavaGame::getDifficulty))
        ;
    }

}