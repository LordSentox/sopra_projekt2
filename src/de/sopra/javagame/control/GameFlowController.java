package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.view.abstraction.Notifications;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.ArtifactType.NONE;
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
                controllerChan.getInGameViewAUI().refreshWaterLevel(waterLevel.getLevel());
                shuffleBack = true;
                controllerChan.getCurrentAction().getArtifactCardStack().discard(currentCard);
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
            activePlayer = controllerChan.getCurrentAction().getActivePlayer();
        }
        controllerChan.getInGameViewAUI().refreshHand(activePlayer.getType(), activePlayer.getHand());
        if (shuffleBack) {
            CardStack<FloodCard> stack = controllerChan.getCurrentAction().getFloodCardStack();
            stack.shuffleBack();
            controllerChan.getInGameViewAUI().refreshFloodStack(stack);
        }
        maybeTellPlayerToDiscardCards(activePlayer); //als letztes, da hier die KI ausgelöst wird
    }

    private void maybeTellPlayerToDiscardCards(Player activePlayer) {
        if (activePlayer.getHand().size() > Player.MAXIMUM_HANDCARDS) {

            if (activePlayer.isAi()) {
                for (int i = 0; i < activePlayer.getHand().size() - Player.MAXIMUM_HANDCARDS; i++)
                    letAIAct(activePlayer.getType()); //Lass AI über Discard entscheiden
                return;
            }

            int amountOfSurplusCards = activePlayer.getHand().size() - Player.MAXIMUM_HANDCARDS;
            int oneCardToDiscard = 1;
            if (amountOfSurplusCards == oneCardToDiscard) {
                controllerChan.getInGameViewAUI()
                        .showNotification("Der Spieler " + activePlayer.getName() + " (" + activePlayer.getType() + ")"
                                + "\nhat eine Karte zu viel!\nWirf eine Karte von " + activePlayer.getName() + " ab,\num weiterspielen zu können.");
            } else {
                controllerChan.getInGameViewAUI()
                        .showNotification("Der Spieler " + activePlayer.getName() + " (" + activePlayer.getType() + ")"
                                + "\nhat " + amountOfSurplusCards + " Karten zu viel!\nWirf "
                                + amountOfSurplusCards + " Karten bei " + activePlayer.getName() + " ab,\num weiterspielen zu können.");
            }
        }
    }

    private void gameLostNotification() {
        controllerChan.getCurrentAction().setGameWon(false);
        controllerChan.getCurrentAction().setGameEnded(true);
        System.out.println("Verloren");
        controllerChan.getInGameViewAUI().showNotification(Notifications.gameLost("Das Wasser ist viel zu schnell gestiegen \n"
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

        if (checkIsLost(position, tile)) {
            controllerChan.getInGameViewAUI().refreshHopefullyAll(controllerChan.getCurrentAction());
            controllerChan.getCurrentAction().setGameEnded(true);
            controllerChan.getCurrentAction().setGameWon(false);
            gameLostNotification();
        }

        // Wenn der Spieler keine Flutkarten mehr ziehen muss ended der Zug.
        endFloodCardDrawAction(floodCardCardStack);
    }

    private boolean checkIsLost(Point position, MapTile tile) {
        // Überprüfe, ob das Spiel verloren ist, wenn das Tile untergeht und ein Spieler darauf sich nicht retten kann
        boolean lost = false;
        if (tile.getState() == GONE) {
            // Finde alle Spieler auf diesem Tile und setze das Spiel auf verloren, falls sich einer von ihnen nicht
            // retten konnte
            List<Player> playersOnTile = controllerChan.getCurrentAction().getPlayers().stream().filter(player -> player.getPosition().equals(position)).collect(Collectors.toList());
            for (Player player : playersOnTile) {
                // Wenn der Spieler keinen legalen Zug hat, ist das Spiel verloren.
                lost |= player.legalMoves(true).isEmpty();
            }

            // Wenn beide Tempel eines Artefaktes versinken und dieses noch nicht eingesammelt war ist das Spiel verloren
            ArtifactType hidden = tile.getProperties().getHidden();
            if (!controllerChan.getCurrentAction().getDiscoveredArtifacts().contains(hidden))
                lost |= hidden != NONE && controllerChan.getCurrentAction().getMap().stream().filter(til -> til.getProperties().getHidden() == hidden && til.getState() == GONE).count() == 2;
        }

        return lost;
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
        PlayerType oldActivePlayer = controllerChan.getCurrentAction().getActivePlayer().getType();
        Action nextAction = controllerChan.finishAction();
        if (nextAction == null) {
            return;
        }
        nextAction.setFloodCardsToDraw(nextAction.getFloodCardsToDraw() - 1);
        controllerChan.getInGameViewAUI().refreshFloodStack(floodCardCardStack);
        //Nachdem ne Flutkarte gezogen wurde, soll KI karten schmeißen dürfen - Jemand hat etwas getan
        someoneDidSomething(oldActivePlayer);
        //da karten schmeißen durch KI etc die Aktion verändern kann, hier neu holen
        nextAction = controllerChan.getCurrentAction();

        final int EMPTY_STACK = 0;

        if (nextAction.getFloodCardStack().size() == EMPTY_STACK) {
            nextAction.getFloodCardStack().shuffleBack();
            controllerChan.getInGameViewAUI().refreshFloodStack(nextAction.getFloodCardStack());
        }

        if (nextAction.getFloodCardsToDraw() <= 0) {
            //der nächste Spieler ist aktiv am Zug
            nextAction.nextPlayerActive();
            nextAction.setState(TurnState.PLAYER_ACTION);

            controllerChan.getInGameViewAUI().refreshTurnState(TurnState.PLAYER_ACTION);
            controllerChan.getInGameViewAUI().refreshActivePlayer();
            controllerChan.getInGameViewAUI().refreshActionsLeft(nextAction.getActivePlayer().getActionsLeft());
            nextAction.getPlayers().forEach(player -> controllerChan.getInGameViewAUI().refreshHand(player.getType(), player.getHand()));
            //Wenn die KI am Zug ist, soll sie einfach alle Aktionen aufbrauchen
            if (nextAction.getActivePlayer().isAi()) {
                beginNewTurn();
                //return weil die Aktion sich wieder ändert
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

    public List<Player> playersPausedToDiscard() {
        //Überprüfe, welcher der Spieler mehr als 5 Handkarten hat
        List<Player> players = new LinkedList<Player>();
        for (Player player : controllerChan.getCurrentAction().getPlayers()) {
            if (player.getHand().size() > Player.MAXIMUM_HANDCARDS) {
                players.add(player);
            }
        }
        return players;
    }

    private boolean isPlayersTurn(PlayerType playerType) {
        return controllerChan.getCurrentAction().getActivePlayer().getType() == playerType
                && controllerChan.getCurrentAction().getState() == TurnState.PLAYER_ACTION;
    }

    //KI wenn sie sich retten soll
    private void makeMoveToRescue(PlayerType type) {
        //KI auffordern sich selbst zu retten
        //KI erkennt die Rettungssituation indem der gegebene Spieler auf MapTile mit GONE steht
        letAIAct(type);
    }

    //KI - Neuer Zug
    public void beginNewTurn() {
        //wenn am Anfang des Zuges die KI dran ist, dann soll die KI ihren Zug machen
        PlayerType activePlayer = controllerChan.getCurrentAction().getActivePlayer().getType();
        //solange dieser Spieler am Zug ist, sollen Aktionen ausgeführt werden
        //Die KI sollte ihren Zug über WAIT_AND_DRINK_TEA von selbst über AIControllerUtil abgeben können
        while (isPlayersTurn(activePlayer)) {
            letAIAct(activePlayer);
        }
    }

    //wake up AI as the command does
    public void wakeUpAI() {
        controllerChan.getCurrentAction().getPlayers()
                .stream()
                .filter(player -> player.isAi())
                .filter(player -> player.getActionsLeft() == 0) //aktiver Spieler soll die Klappe halten xD
                .map(Player::getType)
                .forEach(this::letAIAct);
    }

    //Fordere Spieler auf Spezialkarten ausspielen zu können
    //Das ist nur nötig, wenn der ausführende Spieler keine KI ist,
    //denn die KI wird nicht zwischen ihre eigenen Züge schmeißen.
    public void someoneDidSomething(PlayerType playerWhoDidIt) {
        //Wenn ein Spieler, der nicht die KI ist, etwas gemacht hat, dann soll die KI darauf reagieren können
        if (controllerChan.getCurrentAction().getPlayer(playerWhoDidIt).isAi()) return;
        //Für alle KI Spieler, die nicht der ausführende Spieler sind
        //Ob der Spieler eine KI ist, wird in letAIAct gefiltert
        controllerChan.getCurrentAction().getPlayers()
                .stream()
                .filter(player -> player.getActionsLeft() == 0) //aktiver Spieler soll entfernt werden, falls dieser noch Züge hat
                .map(Player::getType)
                .forEach(this::letAIAct);
    }

    public void letAIAct(PlayerType playerType) {
        if (controllerChan.getCurrentAction().getPlayer(playerType).isAi()) {
            controllerChan.getAiController().makeStep(() -> controllerChan.getCurrentAction().getPlayer(playerType));
        }
    }

}
