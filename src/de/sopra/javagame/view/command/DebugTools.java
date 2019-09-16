package de.sopra.javagame.view.command;

import de.sopra.javagame.view.abstraction.GameWindow;
import de.spaceparrots.api.command.annotation.Command;
import de.spaceparrots.api.command.annotation.Scope;
import de.spaceparrots.api.command.annotation.Senior;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 16.09.2019
 * @since 16.09.2019
 */
@Senior
public class DebugTools {

    @Command("fullscreen")
    public void minimize(@Scope GameWindow window) {
        window.getMainStage().setFullScreen(!window.getMainStage().isFullScreen());
    }

}
/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2019
 *
 ***********************************************************************************************/