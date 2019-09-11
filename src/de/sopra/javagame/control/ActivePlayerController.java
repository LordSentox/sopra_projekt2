package de.sopra.javagame.control;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Engineer;
import de.sopra.javagame.model.player.Navigator;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
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
     * Zeigt alle Bewegungsmöglichkeiten des aktiven Spieler an.
     *
     * @param specialActive
     *            Falls <code>true</code>, werden die
     *            Spezialbewegungsfähigkeiten des jeweiligen Spielers
     *            miteinberechnet. Sonst. weiden nur die
     *            Standardbewegungsmöglichkeiten angezeigt.
     */
    public void showMovements(boolean specialActive) {
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        List<Point> movements = player.legalMoves(specialActive);
        controllerChan.getInGameViewAUI().refreshMovementOptions(movements);

    }

    /**
     * Zeigt alle Felder an, die vom aktiven Spieler trockengelegt werden
     * können.
     */
    public void showDrainOptions() {
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
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
    public void showSpecialAbility() {
        // TODO: ergänzen die Methode später.
        // XXX: Nicht immer Spieler kopieren
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        InGameViewAUI aui = controllerChan.getInGameViewAUI();

        if (player.getType() == PILOT || player.getType() == DIVER) {
            List<Point> movements = player.legalMoves(true);
            aui.refreshMovementOptions(movements);
        }
        else if (player.getType() == PlayerType.COURIER) {
            aui.refreshCardsTransferable(true);
            aui.showNotification("Der Bote darf die Artefaktkarten an einen beliebigen Mitspieler übergeben!");
        }
        else if (player.getType() == PlayerType.EXPLORER) {
            List<Point> movements = player.legalMoves(true);
            List<Point> drainables = player.drainablePositions();

            aui.refreshMovementOptions(movements);
            aui.refreshDrainOptions(drainables);
            aui.showNotification("Der Abenteurer kann sich diagonal bewegen und diagonal Felder trockenlegen!");
        }
        else if (player.getType() == PlayerType.ENGINEER) {
            aui.showNotification("Der Ingenieur darf gleichzeitig zwei Insel-Felder trocken legen!");
        }
        else if (player.getType() == PlayerType.NAVIGATOR) {
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
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        List<Point> movements = player.legalMoves(false);
        List<Point> drainable = player.drainablePositions();
        controllerChan.getInGameViewAUI().refreshDrainOptions(drainable);
        controllerChan.getInGameViewAUI().refreshMovementOptions(movements);

    }

    /**
     * Zeigt an, ob der aktive Spieler dem gegebenen Spieler eine Karte geben
     * kann
     *
     * @param targetPlayer
     *            Der Spieler, dem eine Karte gegeben werden soll.
     */
    public void showTransferable(PlayerType targetPlayer) {
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        if (player.getType() == PlayerType.COURIER) {
            controllerChan.getInGameViewAUI().refreshCardsTransferable(true);
        } else {
            Player target = currentTurn.getPlayer(targetPlayer);
            if (player.getPosition() == target.getPosition()) {
                controllerChan.getInGameViewAUI().refreshCardsTransferable(true);
            } else {
                controllerChan.getInGameViewAUI().refreshCardsTransferable(false);
            }
        }

    }

    /**
     * Gibt die Karte mit dem gegebenen Index aus der Hand des aktiven Spielers,
     * dem gegebenen anderen Spieler
     *
     * @param handCardIndex
     *            Der Index der Karte, die übergeben werden soll.
     * @param targetPlayer
     *            Der Spieler, dem die Karte gegeben werden soll
     */
    public void transferCard(int handCardIndex, PlayerType targetPlayer) {
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        Player target = currentTurn.getPlayer(targetPlayer);
        ArtifactCard card = player.getHand().get(handCardIndex);
        if (currentTurn.transferArtifactCard(card, player, target)) {
            controllerChan.getInGameViewAUI().refreshHand(player.getType(), player.getHand());
            controllerChan.getInGameViewAUI().refreshHand(targetPlayer, target.getHand());
        }
    }

    /**
     * Sammelt das Artefakt, das auf dem {@link MapTile} erhältlich ist, falls
     * der Spieler die nötigen Artefaktkarten besitzt
     *
     * @see ArtifactCard
     */
    public void collectArtifact() {
        Turn currentTurn = controllerChan.getCurrentTurn();
        controllerChan.getInGameViewAUI().refreshArtifactsFound(currentTurn.getDiscoveredArtifacts());
        controllerChan.getInGameViewAUI().refreshArtifactStack(currentTurn.getArtifactCardStack());

    }

    /**
     * Bewegt den aktiven Spieler auf das von dem {@link Point} angegebenen
     * {@link MapTile}
     *
     * @param destination
     *            Die Position des {@link MapTile}, auf das der Spieler bewegt
     *            werden soll
     * @param useSpecial
     *            Falls true, benutzt der Spieler seine Spezialfähigkeit, um
     *            sich auf das Feld zu bewegen
     */
    public void move(Point destination, boolean useSpecial) {
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        int actionsLeft = player.getActionsLeft();

        if (player.move(destination, true, useSpecial)) {
            controllerChan.getInGameViewAUI().refreshActionsLeft(actionsLeft);
            controllerChan.getInGameViewAUI().refreshPlayerPosition(destination, player.getType());

            controllerChan.endTurn();
        }
    }

    /**
     * Bewegt einen anderen Spieler in eine Richtung.
     *
     * @param direction
     *            Die Richtung in die sich der Spieler um ein Feld bewegt werden
     *            soll.
     * @param target
     *            Der Spieler, der Bewegt werden soll.
     * @see Direction
     * @see PlayerType
     */
    public void moveOther(Direction direction, PlayerType target) {
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        Player forcePlayer = currentTurn.getPlayer(target);
        if (player.getType() == PlayerType.NAVIGATOR) {
            player = new Navigator(player.getName(), player.getPosition(), currentTurn);
            player.forcePush(direction, forcePlayer);
            Point position = forcePlayer.getPosition();
            controllerChan.getInGameViewAUI().refreshPlayerPosition(position, target);
            int actionsLeft = player.getActionsLeft();
            controllerChan.getInGameViewAUI().refreshActionsLeft(actionsLeft);
        }

    }

    /**
     * Legt alle Felder trocken an den gegebenen Positionen.
     *
     * @param position
     *            Die Positionen aller Felder die Trockengelegt werden sollen.
     */
    public void drain(Point... position) {
        Turn currentTurn = controllerChan.getCurrentTurn();
        Player player = currentTurn.getActivePlayer();
        for (Point point : position){
            if(player.getType() != PlayerType.ENGINEER) {
                if (player.drain(point)){
                controllerChan.getInGameViewAUI().refreshMapTile(point, currentTurn.getTile(point));
                controllerChan.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());
                }
            }else {
                player = new Engineer(player.getName(), player.getPosition(), currentTurn);
                if (player.drain(point)){
                    controllerChan.getInGameViewAUI().refreshMapTile(point, currentTurn.getTile(point));
                    controllerChan.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());
                }
            }
       
         }
    }

    /**
     * Zeigt einen Spielhinweis an. Danach wird dieses Spiel nicht mehr in die
     * Bestenliste aufgenommen
     *
     * @see HighScore
     */
    public void showTip() {
        Turn currentTurn = controllerChan.getCurrentTurn();
        controllerChan.getJavaGame().markCheetah();
        // TODO: KI nach Möglichkeiten fragen
        // controllerChan.getAIController.getTip
        controllerChan.getInGameViewAUI().showNotification("Tipps hier bitte!");
        // TODO: refresh Maptile oder Karte in der Hand

    }

    /**
     * Beendet den Zug und startet den nächsten Zug.
     */
    public void endTurn() {
        Turn currentTurn = controllerChan.getCurrentTurn();
        controllerChan.getJavaGame().endTurn(currentTurn);

    }
}
