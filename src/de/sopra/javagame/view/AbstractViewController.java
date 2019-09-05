package de.sopra.javagame.view;

import javafx.stage.Stage;

public abstract class AbstractViewController {

    private GameWindow gameWindow;

    private ViewState viewState;

    abstract ViewState getType();

    abstract void reset();

    abstract void show(Stage stage);

    void changeState(ViewState next) {

    }

}
