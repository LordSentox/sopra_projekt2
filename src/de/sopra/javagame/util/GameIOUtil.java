package de.sopra.javagame.util;

import de.sopra.javagame.model.JavaGame;

import java.io.*;

public class GameIOUtil {
    public static JavaGame loadFromFile(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            JavaGame game = (JavaGame) objectInputStream.readObject();

            // Falls das Spiel nicht bis zum letzten Zug vorgespult wurde, spule es vor.
            while (game.canRedo())
                game.redoAction();

            // Das Spiel wurde erfolgreich geladen.
            return game;
        } catch (FileNotFoundException e) {
            System.out.println("Es gab keine solche Datei.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Beim Import ist ein Fehler aufgetreten!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Die Klasse wurde nicht gefunden!");
            e.printStackTrace();
        }

        return null;
    }

    public static boolean saveToFile(JavaGame game, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(game);

            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Es gab keine solche Datei.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Beim Export ist ein Fehler aufgetreten.");
            e.printStackTrace();
        }

        return false;
    }
}
