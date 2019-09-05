package de.sopra.javagame.util;

import java.io.*;
import java.util.HashMap;

public class Translation {
    public static Translation ORIGINAL = new Translation();
    private HashMap<String, String> translations;

    /**
     * Erstellt ein neues Übersetzungsobjekt, welches noch keine Übersetzungen registriert hat. Sollen Übersetzungen
     * benutzt werden, müssen sie hinzugefügt werden.
     */
    public Translation() {
        this.translations = null;
    }

    /**
     * Erstellt ein neues Übersetzungsobjekt aus der in der Datei aufgeführten Übersetzungen. Kann die Datei nicht
     * gelesen werden, werden keine Übersetzungen eingetragen.
     *
     * @param file Die Datei, die eingelesen werden soll
     */
    public Translation(File file) {
        addTranslationsFromFile(file);
    }

    /**
     * Fügt die Übersetzungen, die in der angegebenen Datei vorkommen der bereits bestehenden Datenbank hinzu.
     *
     * @param file Die Datei, die eingelesen werden soll
     */
    public void addTranslationsFromFile(File file) {
        if (this.translations == null) {
            this.translations = new HashMap<>();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // Lies die einzelnen Zeilen aus und versuche die Übersetzungen zu parsen. Zeilen, bei denen es nicht
            // möglich ist, werden übersprungen
            String line = reader.readLine();
            while (line != null) {
                // Trenne das Original auf der linken von der Übersetzung auf der rechten Seite
                String[] translation = line.split("~>", 2);
                boolean detectedTranslation = translation.length == 2;
                if (detectedTranslation) {
                    if (!this.translations.containsKey(translation[0].trim())) {
                        translations.put(translation[0].trim(), translation[1].trim());
                    } else {
                        System.out.println("[WARNUNG] Ignoriere doppelte Übersetzung bei " + translation[0].trim());
                    }
                }

                line = reader.readLine();
            }

            reader.close();
            System.out.println(this.translations.size() + " übersetzungen wurden eingelesen und registriert.");
        } catch (FileNotFoundException e) {
            System.err.println("Die Datei " + file + " konnte nicht gefunden werden. Es werden keine Übersetzungen hinzugefügt.");
        } catch (IOException e) {
            System.err.print("Es gab einen Fehler beim Einlesen von Übersetzungen aus " + file);
            e.printStackTrace();
        }
    }

    /**
     * Erstellt ein neues Übersetzungsobjekt mit den in der HashMap aufgeführten Übersetzungen. Wird <code>null</code>
     * übergeben, werden keine Übersetzungen eingetragen. (Das gleiche wie bei {@link Translation.ORIGINAL})
     *
     * @param translations HashMap mit den Originaltexten als keys und den Übersetzungen als values
     */
    public Translation(HashMap<String, String> translations) {
        this.translations = translations;
    }

    /**
     * Erhält einen unübersetzten String und versucht ihn zu übersetzen. Die unübersetzte Sprache kann sich ändern,
     * sollte aber in jedem Projekt einheitlich sein, um Verwirrung zu vermeiden.
     *
     * @param original Der originale, unübersetzte String.
     * @return Die Übersetzung, oder das übergebene Original, falls keine Übersetzung gefunden werden konnte.
     */
    public String translate(String original) {
        if (this.translations == null) {
            return original;
        }

        if (!this.translations.containsKey(original)) {
            System.out.println("[WARNUNG] Übersetzung für \'" + original + "\' wurde nicht gefunden. Es wird das Original benutzt.");
            return original;
        }

        return this.translations.get(original);
    }

    /**
     * Macht eine Kopie von den momentan verfügbaren Übersetzungen, die gerade gespeichert sind.
     *
     * @return HashMap mit den Übersetzungen. Gibt es keine, wird eine leere zurückgegeben
     */
    public final HashMap<String, String> copyTranslations() {
        return (HashMap<String, String>) this.translations.clone();
    }
}