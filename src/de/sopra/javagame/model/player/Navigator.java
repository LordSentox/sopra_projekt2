package de.sopra.javagame.model.player;

public class Navigator extends Player {

    private boolean hasExtraPush;

    public boolean canMoveOthers() {
        return false;
    }

}
