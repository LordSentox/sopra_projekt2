package de.sopra.javagame.control;

import de.sopra.javagame.control.ai.SimpleAction;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Pilot;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.DebugUtil;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.HighScore;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.view.InGameViewAUI;

import java.util.List;

import static de.sopra.javagame.model.player.PlayerType.DIVER;
import static de.sopra.javagame.model.player.PlayerType.PILOT;

public class ActivePlayerController {

    private final ControllerChan controllerChan;

    ActivePlayerController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    /**
     * Zeigt alle Felder an, die vom aktiven Spieler trockengelegt werden
     * können.
     */
    public void showDrainOptions() {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();
        List<Point> drainable = player.drainablePositions();
        controllerChan.getInGameViewAUI().refreshDrainOptions(drainable);
    }

    /**
     * Zeigt die Möglichkeiten an, die Spezialfähigkeit des aktiven Spielers
     * anzuwenden. Zum Beispiel werden beim Piloten alle Felder angezeigt, auf
     * die er mit seiner Spezialfähigkeit ziehen kann.
     *
     * @see PlayerType
     */
    // TODO: Wird hierfür tatsächlich jedes Mal eine Aktion benötigt?
    public void showSpecialAbility() {

        // XXX: Nicht immer Spieler kopieren
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();
        InGameViewAUI aui = controllerChan.getInGameViewAUI();

        if (player.getType() == PILOT) {
            if (!((Pilot) player).hasSpecialMove())
                aui.showNotification("Der Pilot darf einmal pro Zug fliegen. Du bist diesen Zug bereits geflogen");
            else aui.refreshMovementOptions(player.legalMoves(true));
        } else if (player.getType() == DIVER) {
            aui.showNotification("Der Taucher darf durch überflutete Inselfelder tauchen!");
        } else if (player.getType() == PlayerType.COURIER) {
            aui.showNotification("Der Bote darf die Artefaktkarten an einen beliebigen Mitspieler übergeben!");
        } else if (player.getType() == PlayerType.EXPLORER) {
            List<Point> movements = player.legalMoves(true);
            List<Point> drainables = player.drainablePositions();

            aui.refreshMovementOptions(movements);
            aui.refreshDrainOptions(drainables);
            //aui.showNotification("Der Abenteurer kann sich diagonal bewegen und diagonal Felder trockenlegen!");
        } else if (player.getType() == PlayerType.ENGINEER) {
            aui.showNotification("Der Ingenieur darf gleichzeitig zwei Insel-Felder trocken legen!");
        } else if (player.getType() == PlayerType.NAVIGATOR) {
            aui.showNotification("Navigator darf einen anderen Abenteurer um bis zu 2 oder 2 andere Abenteurer um jeweils 1 Inselfeld bewegen!");
        }
    }

    /**
     * Bricht das einsetzen einer Spezialfähigkeit ab, das vorher durch
     * {@link #showSpecialAbility()} eingeleitet wurde.
     *
     * @see PlayerType
     * @see #showSpecialAbility
     */
    public void cancelSpecialAbility() {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();
        List<Point> movements = player.legalMoves(false);
        List<Point> drainable = player.drainablePositions();
        controllerChan.getInGameViewAUI().refreshDrainOptions(drainable);
        controllerChan.getInGameViewAUI().refreshMovementOptions(movements);
    }

    /**
     * Zeigt an, ob der aktive Spieler dem gegebenen Spieler eine Karte geben
     * kann
     *
     * @param targetPlayer Der Spieler, dem eine Karte gegeben werden soll.
     */
    public void showTransferable(PlayerType targetPlayer) {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();
        controllerChan.getInGameViewAUI().refreshCardsTransferable(player.legalReceivers().contains(targetPlayer));
    }

    /**
     * Gibt die Karte mit dem gegebenen Index aus der Hand des aktiven Spielers,
     * dem gegebenen anderen Spieler
     *
     * @param handCardIndex Der Index der Karte, die übergeben werden soll.
     * @param targetPlayer  Der Spieler, dem die Karte gegeben werden soll
     */
    public void transferCard(int handCardIndex, PlayerType targetPlayer) {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();
        Player target = currentAction.getPlayer(targetPlayer);
        ArtifactCard card = player.getHand().get(handCardIndex);
        if (player.getActionsLeft() <= 0) {
            return;
        }
        DebugUtil.debug("handIndex in transfer: " + handCardIndex);
        DebugUtil.debug("targetplayer in transfer: " + targetPlayer.toString());
        if (currentAction.transferArtifactCard(card, player, target)) {
            controllerChan.getInGameViewAUI().refreshHand(player.getType(), player.getHand());
            controllerChan.getInGameViewAUI().refreshHand(targetPlayer, target.getHand());
            if (controllerChan.getGameFlowController().isPausedToDiscard()) {
                List<Player> players = controllerChan.getGameFlowController().playersPausedToDiscard();
                for (Player playersPaused : players) {
                    int amountOfSurplusCards = playersPaused.getHand().size() - Player.MAXIMUM_HANDCARDS;
                    if (amountOfSurplusCards == 1) {
                        controllerChan.getInGameViewAUI()
                                .showNotification("Der Spieler " + playersPaused.getName() + " (" + playersPaused.getType() + ")"
                                        + "\nhat eine Karte zu viel!\nWirf eine Karte von " + playersPaused.getName() + " ab,\num weiterspielen zu können.");
                    } else {
                        controllerChan.getInGameViewAUI()
                                .showNotification("Der Spieler " + playersPaused.getName() + " (" + playersPaused.getType() + ")"
                                        + "\nhat " + amountOfSurplusCards + " Karte zu viel!\nWirf "
                                        + amountOfSurplusCards + " Karte bei " + playersPaused.getName() + " ab,\num weiterspielen zu können.");
                    }
                }
            }

            //  Wird hierfür tatsächlich jedes Mal eine Aktion benötigt? JA
            player.setActionsLeft(player.getActionsLeft() - 1);
            controllerChan.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());

            controllerChan.finishAction();
        }
    }

    /**
     * Sammelt das Artefakt, das auf dem {@link MapTile} erhältlich ist, falls
     * der Spieler die nötigen Artefaktkarten besitzt
     *
     * @see ArtifactCard
     */
    public void collectArtifact() {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();

        if (player.collectArtifact() != ArtifactType.NONE) {
            controllerChan.getInGameViewAUI().refreshArtifactsFound();
            controllerChan.getInGameViewAUI().refreshArtifactStack(currentAction.getArtifactCardStack());
            controllerChan.getInGameViewAUI().refreshHand(player.getType(), player.getHand());
            controllerChan.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());

            controllerChan.finishAction();
        }
    }

    /**
     * Bewegt den aktiven Spieler auf das von dem {@link Point} angegebenen
     * {@link MapTile}
     *
     * @param destination Die Position des {@link MapTile}, auf das der Spieler bewegt
     *                    werden soll
     * @param useSpecial  Falls true, benutzt der Spieler seine Spezialfähigkeit, um
     *                    sich auf das Feld zu bewegen
     */
    public void move(Point destination, boolean useSpecial) {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();

        if (player.move(destination, true, useSpecial)) {
            controllerChan.getInGameViewAUI().refreshPlayerPosition(destination, player.getType());
            controllerChan.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());

            controllerChan.finishAction();
        } else {
            DebugUtil.debug("du dulli");
        }
    }

    /**
     * Bewegt einen anderen Spieler in eine Richtung.
     *
     * @param direction Die Richtung in die sich der Spieler um ein Feld bewegt werden
     *                  soll.
     * @param target    Der Spieler, der Bewegt werden soll.
     * @see Direction
     * @see PlayerType
     */
    public void moveOther(Direction direction, PlayerType target) {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();
        Player forcePlayer = currentAction.getPlayer(target);

        if (player.canMoveOthers() && player.forcePush(direction, forcePlayer)) {
            Point position = forcePlayer.getPosition();

            controllerChan.getInGameViewAUI().refreshPlayerPosition(position, target);
            controllerChan.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());

            this.controllerChan.finishAction();
        }
    }

    /**
     * Legt alle Felder trocken an den gegebenen Positionen.
     *
     * @param position next =
     *                 Die Position des Feldes, welches trockengelegt werden sollen.
     */
    public void drain(Point position) {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getActivePlayer();
        if (player.drain(position)) {
            controllerChan.getInGameViewAUI().refreshMapTile(position, currentAction.getMap().get(position));
            controllerChan.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());
            controllerChan.getInGameViewAUI().refreshDrainOptions(player.drainablePositions());
            this.controllerChan.finishAction();
        }
    }

    /**
     * Zeigt einen Spielhinweis an. Danach wird dieses Spiel nicht mehr in die
     * Bestenliste aufgenommen
     *
     * @param player welche Spieler ruft showTip() auf.
     * @see HighScore
     */
    public void showTip(Player player) {
        controllerChan.getJavaGame().markCheetah();
        SimpleAction tipp = controllerChan.getAiController().getTip(() -> player);
        controllerChan.getInGameViewAUI().showTip(tipp);
    }

    /**
     * Beendet den Zug und startet den nächsten Zug.
     */
    public void endActionPhase() {
        Action currentAction = controllerChan.finishAction();
        currentAction.setState(TurnState.DRAW_ARTIFACT_CARD);
        controllerChan.getGameFlowController().drawArtifactCards();

        // Wenn keine Karten abgeworfen werden müssen, kann direkt in den Flutkartenziehstatus gewechselt werden
        if (!controllerChan.getGameFlowController().isPausedToDiscard()) {
            currentAction = controllerChan.finishAction();
            currentAction.setState(TurnState.FLOOD);
            int drawAmount = currentAction.getWaterLevel().getDrawAmount();
            currentAction.setFloodCardsToDraw(drawAmount);
            controllerChan.getInGameViewAUI().refreshTurnState(TurnState.FLOOD);
            if (currentAction.getPlayers().stream().allMatch(Player::isAi)) {
                for (int i = 0; i < drawAmount; i++) {
                    controllerChan.getGameFlowController().drawFloodCard();
                }
            }
        }

        controllerChan.getInGameViewAUI().refreshSome();
    }
}
