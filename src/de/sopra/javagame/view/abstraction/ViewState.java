package de.sopra.javagame.view.abstraction;

public enum ViewState {

    MENU,
    SETTINGS,
    LOAD_GAME,
    SAVE_GAME,
    IN_GAME_SETTINGS,
    IN_GAME(true),
    MAP_EDITOR(true),
    GAME_PREPARATIONS,
    HIGH_SCORES,
    CLOSE;

    private final boolean fullscreen;

    ViewState(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    ViewState() {
        this(false);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }
}
