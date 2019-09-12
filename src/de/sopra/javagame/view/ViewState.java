package de.sopra.javagame.view;

public enum ViewState {
    MENU,
    SETTINGS,
    IN_GAME_SETTINGS,
    IN_GAME,
    MAP_EDITOR,
    GAME_PREPARATIONS,
    HIGH_SCORES,
    CLOSE;

    public boolean isFullscreen() {
        switch (this) {
        case CLOSE:
            return false;
        case GAME_PREPARATIONS:
            return false;
        case HIGH_SCORES:
            return false;
        case IN_GAME:
            return true;
        case MAP_EDITOR:
            return true;
        case MENU:
            return false;
        case SETTINGS:
            return false;
        case IN_GAME_SETTINGS:
            return false;
        default: 
            return false;
        }
    }
}
