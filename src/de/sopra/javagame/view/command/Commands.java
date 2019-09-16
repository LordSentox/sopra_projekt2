package de.sopra.javagame.view.command;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;
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
                window = gameWindow;
                handler.init();
            }
        }
        String commandMarker = Definitions.standard().getCommandMarker();
        if (!command.startsWith(commandMarker))
            command = commandMarker + command;
        return handler.processor.simpleInputHandler().apply(command);
    }

    private void init() {
        processor = new CommandProcessor(Definitions.standard());
        processor.setDefaultImplicit(i -> "");
        loadCommands();
        prepareTypes();
        processor.enable();
    }

    private void loadCommands() {
        processor.register(DemonstrateOrdinaryRandomImplosionStrategies.class, DemonstrateOrdinaryRandomImplosionStrategies::new);
        processor.register(DebugTools.class, DebugTools::new);
    }

    @SuppressWarnings("unchecked")
    private void prepareTypes() {
        processor.registerType(Action.class, "turn",
                PropertyResolver.create("activeplayer", Player.class, Action::getActivePlayer))
                .registerType(ArtifactCardType.class, "artifactcardtype",
                        TypeResolver.create(String.class, string -> ArtifactCardType.valueOf(string.toUpperCase()), ArtifactCardType::name))
                .registerType(ArtifactType.class, "artifacttype",
                        TypeResolver.create(String.class, string -> ArtifactType.valueOf(string.toUpperCase()), ArtifactType::name))
                .registerType(MapTileState.class, "maptilestate",
                        TypeResolver.create(String.class, string -> MapTileState.valueOf(string.toUpperCase()), MapTileState::name))
                .registerType(MapTileProperties.class, "maptileproperties",
                        TypeResolver.create(String.class, string -> MapTileProperties.valueOf(string.toUpperCase()), MapTileProperties::name))
                .registerType(PlayerType.class, "playertype",
                        TypeResolver.create(String.class, string -> PlayerType.valueOf(string.toUpperCase()), PlayerType::name))
                .registerType(Difficulty.class, "difficulty",
                        TypeResolver.create(String.class, string -> Difficulty.valueOf(string.toUpperCase()), Difficulty::name))
                .registerType(GameWindow.class, "gameWindow",
                        TypeResolver.create(String.class, string -> window, window -> ""));

        processor.registerType(MapTile.class, "maptile",
                PropertyResolver.create("state", MapTileState.class, MapTile::getState))
                .registerType(Point.class, "point")
                .registerType(ArtifactCard.class, "artifactcard",
                        PropertyResolver.create("type", ArtifactCardType.class, ArtifactCard::getType))
                .registerType(FloodCard.class, "floodcard",
                        PropertyResolver.create("tile", MapTile.class, FloodCard::getTile))
                .registerType(Player.class, "player",
                        TypeResolver.create(PlayerType.class, this::toPlayer, Player::getType))
                .registerType(JavaGame.class, "javagame",
                        TypeResolver.simple(window.getControllerChan()::getJavaGame),
                        PropertyResolver.create("mapname", String.class, JavaGame::getMapName),
                        PropertyResolver.create("cheetah", boolean.class, JavaGame::getIsCheetah),
                        PropertyResolver.create("score", int.class, JavaGame::calculateScore),
                        PropertyResolver.create("rounds", int.class, JavaGame::numTurns),
                        PropertyResolver.create("canRedo", boolean.class, JavaGame::canRedo),
                        PropertyResolver.create("canUndo", boolean.class, JavaGame::canUndo),
                        PropertyResolver.create("difficulty", Difficulty.class, JavaGame::getDifficulty),
                        PropertyResolver.create("previousaction", Action.class, JavaGame::getPreviousAction))
        ;
    }

    private Player toPlayer(PlayerType type) {
        return window.getControllerChan().getCurrentAction().getPlayer(type);
    }

}