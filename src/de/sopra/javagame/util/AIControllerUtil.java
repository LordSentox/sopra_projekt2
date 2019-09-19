package de.sopra.javagame.util;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.SimpleAction;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;

import java.util.ArrayList;
import java.util.List;

import static de.sopra.javagame.model.ArtifactType.NONE;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 18.09.2019
 * @since 18.09.2019
 */
public class AIControllerUtil {

    public static void doSteps(ControllerChan controllerChan, AIController controller, ActionQueue queue) {
        queue.actionIterator().forEachRemaining(action -> {
            Player player = controller.getCurrentAction().getPlayer(queue.getPlayer());
            switch (action.getType()) {
                case MOVE:
                    boolean isRescuing = controller.getTile(player.getPosition()).getState() == MapTileState.GONE;
                    player.move(action.getTargetPoint(), !isRescuing, isRescuing);
                    break;
                case DRAIN:
                    player.drain(action.getTargetPoint());
                    break;
                case DISCARD_CARD:
                    discardCard(controllerChan, action, player);
                    break;
                case TRADE_CARD:
                    tradeCard(controllerChan, controller, action, player);
                    break;
                case SPECIAL_CARD:
                    specialCard(controllerChan, action, player);
                    break;
                case SPECIAL_ABILITY:
                    specialAbility(controllerChan, controller, action, player);
                    break;
                case COLLECT_TREASURE:
                    collectTreasure(controllerChan, controller, player);
                    break;
                case WAIT_AND_DRINK_TEA:
                    //TODO remove action or end turn
                    break;
            }
        });
    }

    private static void specialCard(ControllerChan controllerChan, SimpleAction action, Player player) {
        int index = -1;
        for (int i = 0; i < player.getHand().size(); i++) {
            if (player.getHand().get(i).getType() == action.getCardType())
                index = i;
        }
        controllerChan.getInGameUserController().playHelicopterCard(player.getType(), index,
                new Pair<>(action.getStartingPoint(), action.getTargetPoint()),
                new ArrayList<>(action.getTargetPlayers()));
    }

    private static void collectTreasure(ControllerChan controllerChan, AIController controller, Player player) {
        ArtifactType artifactType = player.collectArtifact();
        if (artifactType != NONE) {
            controllerChan.getInGameViewAUI().refreshArtifactsFound();
            controllerChan.getInGameViewAUI().refreshArtifactStack(controller.getCurrentAction().getArtifactCardStack());
            controllerChan.getInGameViewAUI().refreshHand(player.getType(), player.getHand());
            controllerChan.getInGameViewAUI().refreshActionsLeft(controller.getCurrentAction().getActivePlayer().getActionsLeft());
        }
    }

    private static void specialAbility(ControllerChan controllerChan, AIController controller, SimpleAction action, Player player) {
        Player targetPlayer;
        switch (player.getType()) {

            case COURIER:
                tradeCard(controllerChan, controller, action, player);
                break;
            case DIVER:
            case PILOT:
            case EXPLORER:
                player.move(action.getTargetPoint(), true, true);
                break;
            case ENGINEER:
                player.drain(action.getTargetPoint());
                break;
            case NAVIGATOR:
                targetPlayer = controller.getCurrentAction().getPlayer(action.getTargetPlayers().stream().findFirst().get());
                Direction direction = targetPlayer.getPosition().getPrimaryDirection(action.getTargetPoint());
                player.forcePush(direction, targetPlayer);
                break;
            default:
                break;
        }
    }

    private static void discardCard(ControllerChan controllerChan, SimpleAction action, Player player) {
        int index;
        index = -1;
        for (int i = 0; i < player.getHand().size(); i++) {
            if (player.getHand().get(i).getType() == action.getCardType())
                index = i;
        }
        controllerChan.getInGameUserController().discardCard(player.getType(), index);
    }

    private static void tradeCard(ControllerChan controllerChan, AIController controller, SimpleAction action, Player player) {
        Player targetPlayer;
        ArtifactCard card = null;
        for (int i = 0; i < player.getHand().size(); i++) {
            if (player.getHand().get(i).getType() == action.getCardType())
                card = player.getHand().get(i);
        }
        targetPlayer = controller.getCurrentAction().getPlayer(action.getTargetPlayers().stream().findFirst().get());
        controllerChan.getCurrentAction().transferArtifactCard(card, player,
                targetPlayer);
        //geile refresh action
        controllerChan.getInGameViewAUI().refreshHand(player.getType(), player.getHand());
        controllerChan.getInGameViewAUI().refreshHand(targetPlayer.getType(), targetPlayer.getHand());
        controllerChan.getInGameViewAUI().refreshActionsLeft(controller.getCurrentAction().getActivePlayer().getActionsLeft());
    }

    /**
     * Berechnet die minimal nötige Anzahl an Aktionen, um vom gegebenen Startpunkt den Zielpunkt zu erreichen.
     *
     * @param startPosition  der Startpunkt
     * @param targetPosition der Zielpunkt
     * @return die minimal Anzahl an Aktionen für den Weg
     */
    public static int getMinimumActionsNeededToReachTarget(Action currentAction, Point startPosition, Point targetPosition, PlayerType playerType) {
        if (startPosition.equals(targetPosition))
            return 0; // Nix zu tun

        // Initialisiere einen Array, um die Anzahl der Aktionen, die benötigt werden zu zählen.
        Map<Integer> stepMap = new Map<Integer>() {
            @Override
            protected Integer[][] newEmptyRaw() {
                return new Integer[Map.SIZE_Y + 2][Map.SIZE_X + 2];
            }
        };

        // Die Startposition kann sofort erreicht werden, alle anderen müssen erst noch überprüft werden
        for (int y = 0; y < Map.SIZE_Y; ++y) {
            for (int x = 0; x < Map.SIZE_X; ++x) {
                if (new Point(x, y).equals(startPosition)) {
                    stepMap.set(0, x, y);
                }
            }
        }

        // Erstelle einen Fake-Spieler und eine Fake-Action, auf dem gearbeitet werden kann
        Action action = currentAction.copy();
        Player player = action.getPlayer(playerType);

        // Gehe solange durch die map, wie noch Möglichkeiten offen sind, die man gehen kann.
        checkPossibleWays(stepMap, player);

        // Zurückgeben der benötigten Aktionen, bzw. Integer-Maximum, falls die Position gar nicht erreicht werden kann
        Integer requiredSteps = stepMap.get(targetPosition);
        return requiredSteps != null ? requiredSteps : Integer.MAX_VALUE;
    }

    private static void checkPossibleWays(Map<Integer> stepMap, Player player) {
        boolean somethingChanged;
        do {
            somethingChanged = false;

            for (int y = 0; y < Map.SIZE_Y; ++y) {
                for (int x = 0; x < Map.SIZE_X; ++x) {
                    // Wenn die Position im letzten Zug noch nicht erreicht wurde, kann sie für's Erste
                    // übersprungen werden
                    if (stepMap.get(x, y) == null)
                        continue;

                    // Aktualisiere die Step-Map mit der Anzahl der Aktionen, die benötigt werden
                    player.setPosition(new Point(x, y));
                    List<Point> moves = player.legalMoves(false);
                    moves.addAll(player.legalMoves(true));
                    for (Point move : moves) {
                        if (stepMap.get(move) == null) {
                            stepMap.set(stepMap.get(x, y) + 1, move);
                            somethingChanged = true;
                        }
                    }
                }
            }

        } while (somethingChanged);
    }

}