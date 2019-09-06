package de.sopra.javagame.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class TranslationTest {
    private final File testFile = new File("test_translations.set");

    @Before
    public void setUp() throws IOException {
        // Erstelle eine Übersetzungsdatei, mit der gut getestet werden kann.
        testFile.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testFile)));
        // Zwei unterschiedliche Übersetzungen für den selben Eintrag
        writeTranslation("Why are you running?", "Was läufst du weg?", writer);
        writeTranslation("Why are you running?", "なぜ逃げてるの？", writer);

        // Eindeutige Übersetzungen.
        writeTranslation("Is it really already this late?", "Ist es wirklich schon so spät?", writer);
        writeTranslation("Why are we still here? Just to suffer?", "我々はまだいるのはね。どうして? ひどい目にあうためだけの?", writer);
        writeTranslation("The bee movie is actually a masterpiece", "\"Bee Movie - Das Honigkomplott\" ist einfach nur ein genialer Film", writer);

        writer.flush();
        writer.close();
    }

    private void writeTranslation(String original, String translation, BufferedWriter out) throws IOException {
        out.write(original);
        out.write(" ");
        out.write("~>");
        out.write(" ");
        out.write(translation);
        out.newLine();
    }

    @Test
    public void addTranslationsFromFile() {
        // Übersetzungen aus der TestDatei
        Translation translations = new Translation();
        translations.addTranslationsFromFile(this.testFile);

        // Überprüfe, ob alle eindeutigen Übersetzungen eingetragen wurden
        Assert.assertEquals("Ist es wirklich schon so spät?", translations.translate("Is it really already this late?"));
        Assert.assertEquals("我々はまだいるのはね。どうして? ひどい目にあうためだけの?", translations.translate("Why are we still here? Just to suffer?"));
        Assert.assertEquals("\"Bee Movie - Das Honigkomplott\" ist einfach nur ein genialer Film", translations.translate("The bee movie is actually a masterpiece"));

        Translation noTranslations = new Translation(new File("IMUSTNEVEREXIST.haobxlaoeu"));
        Assert.assertEquals("Beim Einlesen einer nicht existierenden Datei wurden Übersetzungen hinzugefügt",
                0, noTranslations.copyTranslations().size());
    }

    @Test
    public void translate() {
        // Die Normalfälle werden bereits von addTranslationsFromFile abgedeckt. Deshalb werden in dieser Funktion nur
        // noch die anderen Fälle betrachtet.
        Translation translations = new Translation(this.testFile);

        // Wurde keine Übersetzung eingetragen, so muss das Original zurückgegeben werden, auch wenn die Übersetzungen
        // niemals initialisiert wurden
        Assert.assertEquals("Why art thou running?", translations.translate("Why art thou running?"));
        Assert.assertEquals("Wat?", new Translation().translate("Wat?"));

        // Gibt es zwei Einträge, soll der zuerst hinzugefügte Eintrag wiedergegeben werden
        Assert.assertEquals("Der zuerst hinzugefügte Eintrag ist nicht der, der zur Übersetzung benutzt wurde.",
                "Was läufst du weg?", translations.translate("Why are you running?"));
    }

    @After
    public void tearDown() {
        // Entferne die Übersetzungsdatei, mit der getestet wurde.
        testFile.delete();
    }
}