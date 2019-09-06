package de.sopra.javagame.model;

import java.util.Stack;

/**
 * Repräsentiert ein JavaSpiel im Gesamten bis zu dem letzten Zug, der gemacht wurde. Der momentane Zug kann verändert
 * werden mit undo und redo.
 */
public class JavaGame {

    /**
     * Wenn ein Spiel gestartet wird, ist diese Variable zunächst false. Sollte ein Spieler sich aber im Laufe des Spiels
     * einen Tipp holen oder einen Zug rückgängig machen wird sie auf true gesetzt und das Spiel kann nicht mehr in die
     * Bestenliste eingetragen werden.
     */
    private boolean cheetah;

    /**
     * Der Name der Karte, die gerade gespielt wird.
     */
    private String mapName;

    /**
     * Stack mit den Zügen, die rückgängig gemacht werden können. Der oberste Zug ist der zuletzt gemachte, der
     * abgeschlossen wurde.
     */
    private Stack<Turn> undoTurns;

    /**
     * Der aktülle Zug, der noch nicht vollendet wurde.
     */
    private Turn currentTurn;

    /**
     * Stack mit den Zügen, die gesehen vom Zug, der gerade getätigt wird in der Zukunft liegen. Er ist leer, wenn das
     * Spiel gerade gespielt wird und kein Zug rückgängig gemacht wurde.
     */
    private Stack<Turn> redoTurns;

    /**
     * Der Schwierigkeitsgrad mit dem das Spiel angefangen wurde. Bestimmt den Anfangswasserpegel.
     */
    private Difficulty difficulty;

    /**
     * Beendet den aktüllen Zug und erstellt einen neuen, der zum aktüllen Zug gemacht wird. Gibt es auf dem redo-Stack
     * noch Züge, wird der neue Zug davor eingefügt.
     */
    public void endCurrentTurn() {
    }

    /**
     * Berechnet die Punkte, die es für das Spiel gibt. Es gibt keine Punkte, wenn die cheetah-Variable auf true ist.
     *
     * @return Die berechneten Punkte
     */
    public int calculateScore() {
        return 0;
    }

    public Turn getCurrentTurn() {
        return this.currentTurn;
    }
}