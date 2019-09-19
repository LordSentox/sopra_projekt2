package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.view.abstraction.Notifications;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * Kontrolliert den Spielablauf, auf den kein Spieler einen direkten Einfluss hat.
 */
public class GameFlowController {

    private final ControllerChan controllerChan;

    GameFlowController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    /**
     * Zieht zwei Artefaktkarten vom Artefaktkartenstapel und gibt sie dem aktiven Spieler auf die Hand. Die Karten
     * werden atomar gezogen. Behandelt "Die Flut steigt"-Karten, falls sie gezogen werden.
     */
    public void drawArtifactCards() {
        WaterLevel waterLevel = controllerChan.getCurrentAction().getWaterLevel();
        CardStack<ArtifactCard> artifactCardStack = controllerChan.getCurrentAction().getArtifactCardStack();
        List<ArtifactCard> drawnCards = artifactCardStack.draw(2, false);
        Player activePlayer = controllerChan.getCurrentAction().getActivePlayer();
        boolean shuffleBack = false;
        for (ArtifactCard currentCard : drawnCards) {
            if (currentCard.getType() == ArtifactCardType.WATERS_RISE) {
                waterLevel.increment();
                shuffleBack = true;
                if (waterLevel.isGameLost()) {
                    gameLostNotification();
                    return;
                } else {
                    artifactCardStack.discard(currentCard);
                }
                controllerChan.getInGameViewAUI().refreshWaterLevel(waterLevel.getLevel());
            } else {
                activePlayer.getHand().add(currentCard);
            }
            controllerChan.getInGameViewAUI().refreshArtifactStack(artifactCardStack);
            controllerChan.finishAction();
        }
        controllerChan.getInGameViewAUI().refreshHand(activePlayer.getType(), activePlayer.getHand());
        if (activePlayer.getHand().size() > Player.MAXIMUM_HANDCARDS) {
            controllerChan.getInGameViewAUI().showNotification("Du hast zu viele Handkarten. Bitte wähle " +
                    (activePlayer.getHand().size() - 5) + " Karten zum Abwerfen aus.");
        }
        if (shuffleBack) {
            CardStack<FloodCard> stack = controllerChan.getCurrentAction().getFloodCardStack();
            stack.shuffleBack();
            controllerChan.getInGameViewAUI().refreshFloodStack(stack);
        }
    }

    private void gameLostNotification() {
        controllerChan.getCurrentAction().setGameWon(false);
        controllerChan.getCurrentAction().setGameEnded(true);
        controllerChan.getInGameViewAUI().showNotification(Notifications.gameLost("Ihr habt leider verloren!\n"
                + "Das Wasser ist viel zu schnell gestiegen \n"
                + "und nun ist die ganze Insel versunken.\n"
                + "Keiner von euch konnte sich retten.\n"
                + "Ihr alle seid einen qualvollen Tod "
                + "gestorben."));
    }

    /**
     * Zieht die Menge an Flutkarten, die laut {@link de.sopra.javagame.model.WaterLevel} gezogen werden müssen und
     * überflutet bzw. versenkt die entsprechenden Felder.
     */
    public void drawFloodCard() {
        // Wenn keine Flutkarten mehr gezogen werden müssen soll diese Funktion nichts ändern
        if (this.controllerChan.getCurrentAction().getFloodCardsToDraw() <= 0) {
            return;
        }

        // Ziehe die nächste Flutkarte
        CardStack<FloodCard> floodCardCardStack = controllerChan.getCurrentAction().getFloodCardStack();
        FloodCard card = floodCardCardStack.draw(false);
        MapFull map = controllerChan.getCurrentAction().getMap();
        card.flood(map);

        // Lege die Karte auf den Ablagestapel, wenn die Insel nur überflutet wurde, ansonsten kann sie aus dem
        // Spiel entfernt werden
        if (map.get(card.getTile()).getState() != GONE) {
            floodCardCardStack.discard(card);
        }

        Point position = map.getPositionForTile(card.getTile());
        MapTile tile = map.get(card.getTile());
        controllerChan.getInGameViewAUI().refreshMapTile(position, tile);
        controllerChan.getInGameViewAUI().refreshTurnState(controllerChan.getCurrentAction().getState());
        // Refreshe, welche Spieler gerettet werden müssen
        Set<PlayerType> rescuesNeeded = playersToRescue(controllerChan.getCurrentAction().getMap().getPositionForTile(card.getTile()));
        controllerChan.getInGameViewAUI().refreshPlayersToRescue(rescuesNeeded);

        //Spiel ist verloren
        if (card.getTile().getSpawn() == PlayerType.PILOT && map.get(card.getTile()).getState() == GONE) {
            gameLostNotification();
            return;
        }

        // Wenn der Spieler keine Flutkarten mehr ziehen muss ended der Zug.
        endFloodCardDrawAction(floodCardCardStack);
    }


    private Set<PlayerType> playersToRescue(Point positionToCheck) {
        MapFull map = controllerChan.getCurrentAction().getMap();
        Set<PlayerType> playersToRescue = new HashSet<>();
        if (map.get(positionToCheck).getState() == GONE) {
            for (Player currentPlayer : controllerChan.getCurrentAction().getPlayers()) {
                if (currentPlayer.getPosition().equals(positionToCheck)) {
                    playersToRescue.add(currentPlayer.getType());
                }
            }
        }
        return playersToRescue;
    }

    private void endFloodCardDrawAction(CardStack<FloodCard> floodCardCardStack) {
        Action nextAction = controllerChan.finishAction();
        nextAction.setFloodCardsToDraw(nextAction.getFloodCardsToDraw() - 1);
        controllerChan.getInGameViewAUI().refreshFloodStack(floodCardCardStack);
        //Nachdem ne Flutkarte gezogen wurde, soll KI karten schmeißen dürfen
        letAIAct(nextAction.getActivePlayer().getType());

        if (nextAction.getFloodCardsToDraw() <= 0) {
            nextAction.nextPlayerActive();
            nextAction.setState(TurnState.PLAYER_ACTION);

            controllerChan.getInGameViewAUI().refreshTurnState(TurnState.PLAYER_ACTION);
            controllerChan.getInGameViewAUI().refreshActivePlayer();
            controllerChan.getInGameViewAUI().refreshActionsLeft(nextAction.getActivePlayer().getActionsLeft());
            nextAction.getPlayers().forEach(player -> controllerChan.getInGameViewAUI().refreshHand(player.getType(), player.getHand()));
            //Wenn die KI am Zug ist, soll sie einfach alle Aktionen aufbrauchen, 10 einfach so, hat keine Bedeutung
            for (int i = 0; i < 10; i++) {
                letAIAct(nextAction.getActivePlayer().getType());
            }
        }
    }

    /**
     * Wenn mindestens eine Aktion gemacht wurde, wird eine Aktion zurückgenommen. Eine Aktion heißt in diesem Fall
     * genau ein {@link Action}. Wurde keine gemacht passiert nichts.
     */
    public void undo() {
        if (controllerChan.getJavaGame().canUndo()) {
            controllerChan.getJavaGame().undoAction();
            controllerChan.getJavaGame().markCheetah();
            controllerChan.setAction(controllerChan.getJavaGame().getPreviousAction().copy());
            controllerChan.getInGameViewAUI().refreshHopefullyAll(controllerChan.getCurrentAction());
        }
    }

    /**
     * Wenn mindestens ein {@link #undo()} aufgerufen wurde, wird die zuletzt rückgängig gemachte Aktion wiederholt.
     * Ist das Spiel bereits auf der letzten gemachten Aktion passiert nichts.
     *
     * @return true, falls eine Aktion wiederholt wurde, false, falls keine wiederholt werden konnte.
     */
    public boolean redo() {
        if (controllerChan.getJavaGame().canRedo()) {
            controllerChan.getJavaGame().redoAction();
            controllerChan.setAction(controllerChan.getJavaGame().getPreviousAction().copy());
            controllerChan.getInGameViewAUI().refreshHopefullyAll(controllerChan.getCurrentAction());
            return true;
        }
        return false;
    }

    public boolean isPausedToDiscard() {
        // Überprüfe, ob einer der Spieler mehr als 5 Handkarten hat. In diesem Fall muss erst abgelegt werden,
        // bevor weitergespielt werden kann.
        for (Player player : controllerChan.getCurrentAction().getPlayers()) {
            if (player.getHand().size() > Player.MAXIMUM_HANDCARDS) {
                return true;
            }
        }

        return false;
    }

    public void letAIAct(PlayerType playerType) {
        if (controllerChan.getCurrentAction().getPlayer(playerType).isAi()) {
            controllerChan.getAiController().makeStep(() -> controllerChan.getCurrentAction().getPlayer(playerType));
        }
    }

}
