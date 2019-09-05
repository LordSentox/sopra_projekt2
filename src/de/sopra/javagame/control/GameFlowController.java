package de.sopra.javagame.control;

/**
 * Kontrolliert den Spielablauf, auf den kein Spieler einen direkten Einfluss hat.
 */
public class GameFlowController {
    private ControllerChan controllerChan;

    /**
     * Zieht zwei Artefaktkarten vom Artefaktkartenstapel und gibt sie dem aktiven Spieler auf die Hand. Die Karten
     * werden atomar gezogen. Behandelt "Die Flut steigt"-Karten, falls sie gezogen werden.
     */
    public void drawArtifactCards() {

    }

    /**
     * Zieht die Menge an Flutkarten, die laut {@link de.sopra.javagame.model.WaterLevel} gezogen werden müssen und
     * überflutet bzw. versenkt die entsprechenden Felder.
     */
    public void drawFloodCards() {

    }

    /**
     * Überprüft, ob das Spiel vorbei ist und gibt sofern dies der Fall ist eine Rückmeldung an das
     * {@link de.sopra.javagame.view.InGameViewAUI}.
     */
    public void checkGameEnded() {

    }

    /**
     * Wenn mindestens eine Aktion gemacht wurde, wird eine Aktion zurückgenommen. Eine Aktion heißt in diesem Fall
     * genau ein {@link de.sopra.javagame.model.Turn}. Wurde keine gemacht passiert nichts.
     */
    public void undo() {

    }

    /**
     * Wenn mindestens ein {@link #undo()} aufgerufen wurde, wird die zuletzt rückgängig gemachte Aktion wiederholt.
     * Ist das Spiel bereits auf der letzten gemachten Aktion passiert nichts.
     *
     * @return true, falls eine Aktion wiederholt wurde, false, falls keine wiederholt werden konnte.
     */
    public boolean redo() {
        return false;
    }
}
