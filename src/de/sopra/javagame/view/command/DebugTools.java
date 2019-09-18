package de.sopra.javagame.view.command;

import de.sopra.javagame.control.ai.SimpleAction;
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
    public void fullscreen(@Scope GameWindow window) {
        window.getMainStage().setFullScreen(!window.getMainStage().isFullScreen());
    }

    @Command("minimize")
    public void minimize(@Scope GameWindow window) {
        window.getMainStage().toBack();
    }

    @Command("get")
    public String get(@Scope GameWindow window, String element) {
        if (element != null)
            window.getControllerChan().getInGameViewAUI().showNotification("command result: " + element);
        return element;
    }

    @Command("tip")
    public void getTip(@Scope GameWindow window) {
        SimpleAction tip = window.getControllerChan().getAiController().getTip();
        window.getControllerChan().getInGameViewAUI().showTip(tip);
    }

    @Command("test")
    public void test(@Scope GameWindow window) {
        //use for testing, dont commit
    }

}
/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2019
 *
 ***********************************************************************************************/