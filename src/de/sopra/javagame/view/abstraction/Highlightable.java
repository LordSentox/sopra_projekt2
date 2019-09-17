package de.sopra.javagame.view.abstraction;

import javafx.scene.Node;

import static de.sopra.javagame.view.abstraction.AbstractViewController.HIGHLIGHT;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 17.09.2019
 * @since 17.09.2019
 */
public interface Highlightable {

    Node node();

    default boolean isHighlighted() {
        return node().getStyleClass().contains(HIGHLIGHT);
    }

    default void highlight() {
        if (!isHighlighted())
            node().getStyleClass().add(HIGHLIGHT);
    }

    default void dehighlight() {
        if (isHighlighted())
            node().getStyleClass().remove(HIGHLIGHT);
    }

}