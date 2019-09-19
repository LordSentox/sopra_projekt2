package de.sopra.javagame.view.command;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 19.09.2019
 * @since 19.09.2019
 */
public class ConsoleView {

    private PrintStream printStream;

    private AnchorPane root;

    private ListView<String> listView;

    private List<String> elements;

    public ConsoleView(PrintStream system) {
        this.printStream = system;
        this.elements = new LinkedList<>();
        injectNewStream();
    }

    private void injectNewStream() {
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String x) {
                printStream.println(x);
                elements.add(x);
                if (listView != null)
                    listView.getItems().setAll(elements);
            }
        });
    }

    public void prepare() {
        root = new AnchorPane();
        listView = new ListView<>();
        listView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                elements.clear();
                elements.add("Console cleared.");
                listView.getItems().setAll(elements);
            }
        });
        listView.setMinSize(600, 600);
        root.getChildren().add(listView);
    }

    public void show(Stage owner) {
        prepare();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.initOwner(owner);
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.DECORATED);
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.heightProperty().addListener((observable, oldValue, newValue) -> listView.setPrefHeight(newValue.doubleValue()));
        stage.widthProperty().addListener((observable, oldValue, newValue) -> listView.setPrefWidth(newValue.doubleValue()));
        stage.setOnHidden(event -> listView = null);
        stage.show();
    }

}