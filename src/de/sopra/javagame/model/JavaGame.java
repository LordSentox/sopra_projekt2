package de.sopra.javagame.model;

import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;

import java.util.List;
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
     * Stack mit den Zügen, die gesehen vom Zug, der gerade getätigt wird in der Zukunft liegen. Er ist leer, wenn das
     * Spiel gerade gespielt wird und kein Zug rückgängig gemacht wurde.
     */
    private Stack<Turn> redoTurns;

    /**
     * Der Schwierigkeitsgrad mit dem das Spiel angefangen wurde. Bestimmt den Anfangswasserpegel.
     */
    private Difficulty difficulty;

    JavaGame() {
        this.cheetah = false;
        this.redoTurns = new Stack<>();
        this.undoTurns = new Stack<>();
    }

    /**
     * Startet ein neues JavaGame und gibt den ersten Zug zurück, der bearbeitet werden kann.
     *
     * @param mapName Der Name der Karte, die geladen wurde
     * @param tiles Die TileMap, welche als Spielfeld benutzt werden soll
     * @param difficulty Anfangsschwierigkeit, welche den anfänglichen Wasserpegel festlegt.
     * @param players Die Spieler, die das Spiel spielen
     * @return Der erste Zug, der von Spielern gemacht wird.
     */
    public Turn newGame(String mapName, MapTile[][] tiles, Difficulty difficulty, List<Pair<PlayerType, Boolean>> players) {
        // Erstellen des ersten Turns, der auf den undoTurns-Stapel abgelegt wird.
        this.mapName = mapName;
        this.difficulty = difficulty;
        Turn initialTurn = Turn.createInitialTurn(difficulty, players, tiles);

        return endTurn(initialTurn);
    }

    /**
     * Bekommt den zuletzt getätigten Zug und erstellt einen neün, der für den nächsten Zug benutzt werden kann. Gab es
     * noch spätere Züge, werden diese vom Stapel entfernt.
     */
    public Turn endTurn(Turn currentTurn) {
        this.undoTurns.push(currentTurn);
        while (!redoTurns.empty()) { redoTurns.pop(); }

        return currentTurn.copy();
    }

    /**
     * Berechnet die Punkte, die es für das Spiel gibt. Es gibt keine Punkte, wenn die cheetah-Variable auf true ist.
     *
     * @return Die berechneten Punkte
     */
    public int calculateScore() {
        return 0;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getMapName() {
        return mapName;
    }

    public boolean getIsCheetah() {
        return this.cheetah;
    }
}