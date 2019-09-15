package de.sopra.javagame.control;

import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.view.HighScoresViewAUI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lädt, löscht und speichert die Bestenliste im {@link de.sopra.javagame.view.HighScoresViewController}. Hat keinen
 * Einfluss auf das {@link de.sopra.javagame.model.JavaGame}. Jede Karte hat eine eigene Bestenliste. Die Punkte können
 * nicht miteinander verglichen werden.
 */
public class HighScoresController {

    public static final String SCORE_FOLDER = "data/high_scores/scores/";

    public static final String REPLAY_FOLDER = "data/high_scores/replays/";

    private final ControllerChan controllerChan;
    
    private List<HighScore> highScores;

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
        try {
			String scoresToString = new String(Files.readAllBytes(Paths.get(SCORE_FOLDER + mapName +".score")), StandardCharsets.UTF_8);
			 // Erstelle aus dem String eine Liste von einzelnen Zeilen und splitte diese dann mit ;, der CSV-Trennung.
	        String[] scores = scoresToString.split("\n");
	        String[][] scorecsv = new String[scores.length][];
	        for (int i = 0; i < scores.length; ++i) {
	            String[] split = scores[i].split(",");
	            scorecsv[i] = split;
	        }
	        
	        List<HighScore> highScores = new ArrayList<>();
	        for (String[] row : scorecsv) {
	            if (row.length != 3) {
	                System.err.println("Highscores für " + mapName + " konnten nicht gelesen werden. Eine Zeile ist korrumpiert.");
	                return;
	            }
	            
	            HighScore highscore = new HighScore(row[0], mapName, Integer.parseInt(row[1]), row[2]);
	            highScores.add(highscore);
	        }
	        
	        Collections.sort(highScores);
	        this.highScores = highScores;
	        highScoresViewAUI.refreshList(highScores);
			
		} catch (IOException e) {
		    System.err.println("Highscores konnten nicht eingelesen werden");
			e.printStackTrace();
		}

    }

    public void saveHighScore(HighScore scoreToSave) {

	}

    /**
     * Löscht die komplette Bestenliste.
     *
     * @param mapName Der Name der Karte, dessen Datei zurückgesetzt werden soll.
     */
    public void resetHighScores(String mapName) {
        try {
            FileOutputStream out = new FileOutputStream(SCORE_FOLDER + mapName +".score");
			out.write(new byte[0]);
			out.close();
			
			if (this.highScores != null)
                this.highScores.clear();
			
			this.highScoresViewAUI.refreshList(this.highScores);
		} catch (IOException e) {
		    System.err.println("HighScores konnten nicht gelöscht werden.");
			e.printStackTrace();
		}
        

    }

}
