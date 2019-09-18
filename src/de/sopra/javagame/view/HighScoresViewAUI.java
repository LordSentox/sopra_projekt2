package de.sopra.javagame.view;

import de.sopra.javagame.util.HighScore;

import java.util.List;

/**
 * bietet Methoden zum aktualisieren der Highscores
 *
 * @author Lisa, Hannah
 */
public interface HighScoresViewAUI extends NotificationAUI {

    /**
     * aktualisiert die Anzeige der Highscoe Liste
     *
     * @param scores Liste der neuen scores
     */
    void refreshList(List<HighScore> scores);

}
