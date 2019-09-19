package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.util.Pair;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 18.09.2019
 * @since 18.09.2019
 */
public class CardViewer {

    private final int COLUMNS = 10;
    private final int CARD_SIZE = 150;

    private List<Pair<CardView, Integer>> viewedCards;

    public CardViewer() {
        viewedCards = new LinkedList<>();
    }

    public void add(CardView cardView, int amount) {
        viewedCards.add(new Pair<>(cardView, amount));
    }

    public void show() {
        boolean hasMoreThanOne = viewedCards.stream().anyMatch(pair -> pair.getRight() > 1);
        GridPane pane = new GridPane();
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(5));
        pane.setMinWidth(COLUMNS * CARD_SIZE + COLUMNS * 5);
        int column = 0, row = 0;
        for (Pair<CardView, Integer> cardPair : viewedCards) {
            CardView left = cardPair.getLeft();
            left.setFitWidth(CARD_SIZE);
            pane.add(left, column, row);
            if (column + 1 == COLUMNS) {
                column = 0;
                row++;
            } else column++;
        }
        //TODO
    }

}