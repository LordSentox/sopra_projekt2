package de.sopra.javagame.model;

import java.util.Collection;

public class JavaGame {

    private boolean cheetah;

    private String mapName;

    private Collection<Turn> undoTurns;

    private Collection<Turn> redoTurns;

    private Difficulty difficulty;

    public int calculateScore() {
        return 0;
    }

}
