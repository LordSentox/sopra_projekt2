package de.sopra.javagame.view;

import de.sopra.javagame.control.ControllerChan;

import java.util.Collection;

/**
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
