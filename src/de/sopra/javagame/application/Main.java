package de.sopra.javagame.application;

import de.sopra.javagame.view.GameWindow;
import de.sopra.javagame.view.ViewState;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameWindow window = new GameWindow(primaryStage);
        window.init();
        window.setState(ViewState.MENU);
    }
}
