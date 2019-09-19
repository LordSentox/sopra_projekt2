package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.InGameViewController;

import java.util.HashSet;
import java.util.Set;

import static de.sopra.javagame.model.player.PlayerType.NONE;

public class RescueHelper {
    private final InGameViewController viewController;
    private Set<PlayerType> toRescue;
    private PlayerType currentlyRescueing;

    public RescueHelper(InGameViewController viewController) {
        this.toRescue = new HashSet<>();
        this.viewController = viewController;
        this.currentlyRescueing = NONE;
    }

    public void rescue(Point to) {
        viewController.getGameWindow().getControllerChan().getInGameUserController().rescueMove(this.currentlyRescueing, to);
    }

    public boolean isCurrentlyRescueing() {
        return !toRescue.isEmpty();
    }

    public void refreshPlayersToRescue(Set<PlayerType> toRescue) {
        this.toRescue = toRescue;
    }

    public void setCurrentlyRescueing(PlayerType currentlyRescueing) {
        this.currentlyRescueing = currentlyRescueing;
    }

    public PlayerType getCurrentlyRescueing() {
        return currentlyRescueing;
    }
}
