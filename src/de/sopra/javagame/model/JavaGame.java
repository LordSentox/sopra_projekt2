package de.sopra.javagame.model;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Triple;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.cardstack.CardStackUtil;
import de.sopra.javagame.util.map.MapBlackWhite;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.util.map.MapUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

/**
 * Repräsentiert ein JavaSpiel im Gesamten bis zu dem letzten Zug, der gemacht wurde. Der momentane Zug kann verändert
 * werden mit undo und redo.
 */
public class JavaGame implements Serializable {

    private static final long serialVersionUID = -5194350096601415362L;
    public static final int MAP_SIZE_X = 10;
    public static final int MAP_SIZE_Y = 7;


    //region Attribute
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
    private final Stack<Action> undoActions;

    /**
     * Stack mit den Zügen, die gesehen vom Zug, der gerade getätigt wird in der Zukunft liegen. Er ist leer, wenn das
     * Spiel gerade gespielt wird und kein Zug rückgängig gemacht wurde.
     */
    private final Stack<Action> redoActions;

    /**
     * Der Schwierigkeitsgrad mit dem das Spiel angefangen wurde. Bestimmt den Anfangswasserpegel.
     */
    private Difficulty difficulty;
    //endregion

    private JavaGame() {
        this.cheetah = false;
        this.redoActions = new Stack<>();
        this.undoActions = new Stack<>();
    }

    /**
     * Startet ein neues JavaGame und gibt den ersten Zug zurück, der bearbeitet werden kann.
     * Erfordert den Aufruf von {@link AIController#connectTrackers()} danach.
     *
     * @param mapName    Der Name der Karte, die geladen wurde
     * @param map        Die TileMap, welche als Spielfeld benutzt werden soll
     * @param difficulty Anfangsschwierigkeit, welche den anfänglichen Wasserpegel festlegt.
     * @param players    Die Spieler, die das Spiel spielen
     * @return Der erste Zug, der von Spielern gemacht wird.
     */
    public static Pair<JavaGame, Action> newGame(String mapName, MapBlackWhite map, Difficulty difficulty, List<Triple<PlayerType, String, Boolean>> players)
            throws NullPointerException, IllegalArgumentException {

        MapFull fullMap = MapUtil.createAndFillMap(map);
        
        CardStack<FloodCard> floodCardStack = CardStackUtil.createFloodCardStack(fullMap.raw());
        CardStack<ArtifactCard> artifactCardStack = CardStackUtil.createArtifactCardStack();       
        
        floodCardStack.shuffleDrawStack();
        artifactCardStack.shuffleDrawStack();     
        
        return newGame(mapName, new Triple<> (fullMap, artifactCardStack, floodCardStack), difficulty, players);
        
    }
    
    public static Pair<JavaGame, Action> newGame(String mapName, Triple<MapFull, CardStack<ArtifactCard>, CardStack<FloodCard>> tournamentTriple, Difficulty difficulty, List<Triple<PlayerType, String, Boolean>> players)
            throws NullPointerException, IllegalArgumentException {
        JavaGame game = new JavaGame();

        // Erstellen des ersten Turns, der auf den undoActions-Stapel abgelegt wird.
        if (mapName == null) {
            throw new NullPointerException();
        } else if (mapName.equals("")) {
            throw new IllegalArgumentException();
        } else {
            game.mapName = mapName;
        }

        if (difficulty == null) {
            throw new NullPointerException();
        } else {
            game.difficulty = difficulty;
        }

        Action initialAction = Action.createInitialAction(difficulty, players, tournamentTriple);

        return new Pair<>(game, initialAction);
    }
    

    /**
     * Bekommt die zuletzt getätigte Aktion und erstellt eine neüe, die für den nächsten Zug benutzt werden kann. Gab es
     * noch spätere Aktionen, werden diese vom Stapel entfernt.
     */
    public Action finishAction(Action currentAction) {
        this.undoActions.push(currentAction);
        while (!redoActions.empty()) {
            redoActions.pop();
        }
        if (currentAction.isGameEnded() || currentAction.isGameWon() || currentAction.getWaterLevel().isGameLost()) {
            return null;
        }

        return currentAction.copy();
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
        double score;
        if (undoActions.peek().isGameWon()) {
            score = (1.0 / (double) this.numTurns()) * 10000;

        } else {
            score = this.numTurns() * 100;
        }
        int extraPoints = 0;
        if (!undoActions.isEmpty()) {
            // Für jedes gefundene Artefakt gibt es 100 Extrapunkte
            extraPoints += 10000 / this.numTurns() * undoActions.peek().getDiscoveredArtifacts().size();

            // Extrapunkte, wenn das Spiel gewonnen wurde
            if (undoActions.peek().isGameEnded() && undoActions.peek().isGameWon()) {
                extraPoints += 100000;
            }
        }
        score *= (getDifficulty().getInitialWaterLevel() + 1);
        score += extraPoints;
        return (int) score;
    }

    /**
     * Zählt die im Spiel vorgekommenen Züge.
     *
     * @return Anzahl der im Spiel gespielten Züge
     */
    public int numTurns() {
        int currentPlayer = 0;
        int turns = 1;
        for (Action currentAction : undoActions) {
            if (currentAction.getActivePlayerIndex() != currentPlayer) {
                turns++;
                currentPlayer = currentAction.getActivePlayerIndex();
            }
        }

        return turns;
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

    public Action getPreviousAction() {
        return undoActions.peek();
    }

    public int getUndoStackSize() {
        return this.undoActions.size();
    }

    public int getRedoStackSize() {
        return this.redoActions.size();
    }

    public boolean canRedo() {
        return !redoActions.isEmpty();
    }

    public boolean canUndo() {
        final int INITIAL_TURN_SIZE = 1;
        return undoActions.size() != INITIAL_TURN_SIZE;
    }

    public void markCheetah() {
        this.cheetah = true;
    }

    public void undoAction() {
        if (canUndo()) {
            Action undoneAction = this.undoActions.pop();
            this.redoActions.push(undoneAction);
        }
    }

    public void redoAction() {
        if (canRedo()) {
            Action redoneAction = this.redoActions.pop();
            this.undoActions.push(redoneAction);
        }
    }
}