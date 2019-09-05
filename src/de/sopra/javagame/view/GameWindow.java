package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;

import java.util.Collection;

/**
 * Schnittstelle für alle Views und die Controller Schicht
 * @author Hannah, Lisa
 *
 */
public class GameWindow {

    private ControllerChan controllerChan;

    private Collection<AbstractViewController> views;

    /**
     * setzt die übergebene Stage
     * @param state Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
     */
    public void setState(ViewState state) {

    }

}
