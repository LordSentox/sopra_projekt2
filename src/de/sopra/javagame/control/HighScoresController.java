package de.sopra.javagame.control;

import de.sopra.javagame.view.HighScoresViewAUI;

/**
 * Lädt, löscht und speichert die Bestenliste im {@link de.sopra.javagame.view.HighScoresViewController}. Hat keinen
 * Einfluss auf das {@link de.sopra.javagame.model.JavaGame}. Jede Karte hat eine eigene Bestenliste. Die Punkte können
 * nicht miteinander verglichen werden.
 */
public class HighScoresController {

    private final ControllerChan controllerChan;

    /**
     * Die  AUI mit der die angezeigten Daten im {@link de.sopra.javagame.view.HighScoresViewController} aktualisiert
     * werden.
     */
    private HighScoresViewAUI highScoresViewAUI;

    /**
     * Erstellt einen neün {@link HighScoresController}
     */
    HighScoresController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    public void setHighScoresViewAUI(HighScoresViewAUI highScoresViewAUI) {
        this.highScoresViewAUI = highScoresViewAUI;
    }

    public HighScoresViewAUI getHighScoresViewAUI() {
        return highScoresViewAUI;
    }
    
    /**
     * Lädt die aktülle Bestenliste aus einer Bestenlistendatei.
     *
     * @param mapName Der Name der Karte, für den die Datei geladen werden soll.
     */
    public void loadHighScores(String mapName) {

    }

    /**
     * Löscht die komplette Bestenliste.
     *
     * @param mapName Der Name der Karte, dessen Datei zurückgesetzt werden soll.
     */
    public void resetHighScores(String mapName) {

    }

}
