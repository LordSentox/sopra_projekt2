package de.sopra.javagame.control;

import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.view.HighScoresViewAUI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        List<HighScore> highScores = loadScoresNoRefresh(mapName);

        if (highScores != null) {
            this.highScores = highScores;
            highScoresViewAUI.refreshList(highScores);
        }
    }

    public void saveHighScore(HighScore scoreToSave) {

        if (scoreToSave.getMapName().isEmpty())
            System.err.println("Warning: Mapname is empty! saving highscore will still continue.");

        // Lade die alten High-Score-Daten, damit der neue score ihnen hinzugefügt werden kann
        List<HighScore> highScores = loadScoresNoRefresh(scoreToSave.getMapName());

        // Falls es davor noch keine HighScores gab, erstelle eine neue Liste, damit sie von nun an gespeichert werden
        // können
        if (highScores == null) {
            highScores = new ArrayList<>();
        }

        // TODO: Speichere das Replay und sorge dafür, dass es mit dem Highscore in Verbindung gebracht werden kann

        // Füge den neuen High-Score ein und sortiere die Liste, damit er an der richtigen Stelle steht
        highScores.add(scoreToSave);
        Collections.sort(highScores);

        // Speichere die angepasste oder erzeugte Liste in der zugehörigen Highscore-Datei
        try {
            File file = new File(SCORE_FOLDER + scoreToSave.getMapName() + ".score");

            if (file.exists()) {
                file.delete();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (HighScore score : highScores) {
                writer.write(score.getName() + ", ");
                writer.write(score.getScore() + ", ");
                writer.write(score.getReplayName() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("HighScore-Datei konnte nicht geschrieben werden");
            e.printStackTrace();
        }
    }

    /**
     * Löscht die komplette Bestenliste.
     *
     * @param mapName Der Name der Karte, dessen Datei zurückgesetzt werden soll.
     */
    public void resetHighScores(String mapName) {
        File file = new File(SCORE_FOLDER + mapName + ".score");

        if (file.exists()) {
            file.delete();
        }

        if (this.highScores != null)
            this.highScores.clear();

        this.highScoresViewAUI.refreshList(this.highScores);
    }

    // Helferfunktionen ------------------------------------------------------------------------------------------------

    private static List<HighScore> loadScoresNoRefresh(String mapName) {
        try {
            File file = new File(SCORE_FOLDER, mapName + ".score");
            if (!file.exists()) return null;
            byte[] bytes = Files.readAllBytes(file.toPath());
            String scoresToString = new String(bytes, StandardCharsets.UTF_8);
            if (scoresToString.trim().isEmpty()) {
                return new ArrayList<>();
            }
            // Erstelle aus dem String eine Liste von einzelnen Zeilen und splitte diese dann mit ;, der CSV-Trennung.
            String[] scores = scoresToString.split("\n");
            String[][] scorecsv = new String[scores.length][];
            for (int i = 0; i < scores.length; ++i) {
                String[] split = scores[i].split(", ");
                scorecsv[i] = split;
            }

            List<HighScore> highScores = new ArrayList<>();
            for (String[] row : scorecsv) {
                if (row.length != 3) {
                    System.err.println("Highscores für " + mapName + " konnten nicht gelesen werden. Eine Zeile ist korrumpiert.");
                    return null;
                }

                HighScore highscore = new HighScore(row[0], mapName, Integer.parseInt(row[1]), row[2]);
                highScores.add(highscore);
            }

            Collections.sort(highScores);

            return highScores;
        } catch (IOException e) {
            System.err.println("Highscores konnten nicht eingelesen werden");
            e.printStackTrace();
        }

        return null;
    }
}
