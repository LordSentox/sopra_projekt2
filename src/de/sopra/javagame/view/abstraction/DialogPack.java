package de.sopra.javagame.view.abstraction;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 17.09.2019
 * @since 29.08.2019
 */
public class DialogPack {

    private String title, header, text;
    private Alert.AlertType alertType;
    private StageStyle stageStyle;

    private List<String> buttons;
    private List<Runnable> functions;
    private Window window;

    public DialogPack(Window owner, String title, String header, String text) {
        this.title = title;
        this.window = owner;
        this.header = header;
        this.text = text;
        alertType = Alert.AlertType.CONFIRMATION;
        stageStyle = StageStyle.DECORATED;
        buttons = new LinkedList<>();
        functions = new LinkedList<>();
    }

    public void setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
    }

    public void setStageStyle(StageStyle stageStyle) {
        this.stageStyle = stageStyle;
    }

    public void addButton(String text, Runnable function) {
        if (function == null)
            function = () -> {
            };
        buttons.add(text);
        functions.add(function);
    }

    public void open() {
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(window);
        alert.initStyle(stageStyle);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        if (buttons.size() != 0)
            alert.getButtonTypes().setAll(buttons.stream().map(ButtonType::new).collect(Collectors.toList()));

        Optional<ButtonType> result = alert.showAndWait();
        Runnable run = functions.get(buttons.indexOf(result.get()));
        run.run();
    }

}
