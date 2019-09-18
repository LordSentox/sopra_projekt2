package de.sopra.javagame.application;

import de.sopra.javagame.view.abstraction.GameWindow;
import de.sopra.javagame.view.abstraction.Notifications;
import de.spaceparrots.translator.api.Translator;
import de.spaceparrots.translator.core.Dictionary;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Translator.setDictionary(new Dictionary());
//        Dictionary dictionary = new Dictionary();
//        dictionary.registerDictionaryFile(new DictionaryFile(getClass().getResourceAsStream("/lang/de_DE.xml")));
//        Translator.setDictionary(dictionary);
        GameWindow window = new GameWindow(primaryStage);
        try {
            window.init();
        } catch (Exception e) {
            e.printStackTrace();
            window.showNotification(Notifications.info("Das Spiel konnte nicht gestartet werden! (" + e.toString() + ")"));
        }
//        window.setState(ViewState.MENU);
    }

}
