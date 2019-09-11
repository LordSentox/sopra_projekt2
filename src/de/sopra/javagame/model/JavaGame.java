package de.sopra.javagame.model;

import de.sopra.javagame.control.AIController;
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
     * Erfordert den Aufruf von {@link AIController#connectTrackers()} danach.
     *
     * @param mapName    Der Name der Karte, die geladen wurde
     * @param tiles      Die TileMap, welche als Spielfeld benutzt werden soll
     * @param difficulty Anfangsschwierigkeit, welche den anfänglichen Wasserpegel festlegt.
     * @param players    Die Spieler, die das Spiel spielen
     * @return Der erste Zug, der von Spielern gemacht wird.
     */
    public Turn newGame(String mapName, MapTile[][] tiles, Difficulty difficulty, List<Pair<PlayerType, Boolean>> players)
        throws NullPointerException, IllegalArgumentException {
        // Erstellen des ersten Turns, der auf den undoTurns-Stapel abgelegt wird.
        if (mapName == null) {
            throw new NullPointerException();
        } else {
            if (mapName == "") {
                throw new IllegalArgumentException();
            } else {
                this.mapName = mapName;
            }
        }
        if (difficulty == null) {
            throw new NullPointerException();
        } else {
            this.difficulty = difficulty;
        }
        Turn initialTurn = Turn.createInitialTurn(difficulty, players, tiles);
        if (initialTurn == null) {
            return null;
        }
        
        return endTurn(initialTurn);
    }

    /**
     * Bekommt den zuletzt getätigten Zug und erstellt einen neün, der für den nächsten Zug benutzt werden kann. Gab es
     * noch spätere Züge, werden diese vom Stapel entfernt.
     */
    public Turn endTurn(Turn currentTurn) {
        this.undoTurns.push(currentTurn);
        while (!redoTurns.empty()) {
            redoTurns.pop();
        }

        return currentTurn.copy();
    }

    /**
     * Berechnet die Punkte, die es für das Spiel gibt. Es gibt keine Punkte, wenn die cheetah-Variable auf true ist.
     *
     * @return Die berechneten Punkte
     */
    public int calculateScore() {
        // Keine Punkte für Mogler
        if (getIsCheetah()) {
            return 0;
        }

        double score = (1.0 / (double) this.numRounds()) * 100;

        int extraPoints = 0;
        if (!undoTurns.isEmpty()) {
            // Für jedes gefundene Artefakt gibt es 100 Extrapunkte
            extraPoints += 100 * undoTurns.peek().getDiscoveredArtifacts().size();

            // Extrapunkte, wenn das Spiel gewonnen wurde
            if (undoTurns.peek().isGameEnded() && undoTurns.peek().isGameWon()) {
                extraPoints += 1000;
            }
        }
        score += extraPoints;

        return (int) score;
    }

    /**
     * Zählt die im Spiel vorgekommenen Runden, also wie oft der erste Spieler bereits an der Reihe war, was zwar
     * proportional, aber nicht direkt abhängig von der Anzahl der Turns ist.
     *
     * @return Anzahl der im Spiel gespielten Runden
     */
    public int numRounds() {
        int playerOne = 0;
        int rounds = 0;
        boolean finishedOneRound = false;
        for (Turn currentTurn : undoTurns) {
            if (!finishedOneRound && currentTurn.getActivePlayerIndex() == playerOne) {
                rounds++;
                finishedOneRound = true;
            } else if (currentTurn.getActivePlayerIndex() != playerOne) {
                finishedOneRound = false;
            }
        }

        return rounds;
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

    public Turn getPreviousTurn() {
        return undoTurns.peek();
    }

    public boolean canRedo() {
        return !redoTurns.isEmpty();
    }
    public void markCheetah() {
        this.cheetah = true;
    }
}