package de.sopra.javagame.util;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.SimpleAction;
import de.sopra.javagame.control.ai1.ActionType;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.Courier;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.map.Map;
import de.sopra.javagame.view.abstraction.Notifications;

import java.util.ArrayList;
import java.util.List;

import static de.sopra.javagame.util.DebugUtil.debugAI;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 18.09.2019
 * @since 18.09.2019
 */
public class AIControllerUtil {

    private static ActionQueue lastQueue;

    public static synchronized void doSteps(ControllerChan controllerChan, AIController controller, ActionQueue queue) {
        if (lastQueue != null && queue.toString().equals(lastQueue.toString()) && lastQueue.actionIterator().next().getType() != ActionType.DISCARD_CARD) {
            controllerChan.getInGameViewAUI().showNotification(Notifications.error("AI collapsed... sad little thing"));
            throw new RuntimeException(" !! ERROR - want to do it twice: " + queue.toString()
                    + " (actions left: " + controllerChan.getCurrentAction().getPlayer(queue.getPlayer()).getActionsLeft() + ")");
        } else lastQueue = queue;
        queue.actionIterator().forEachRemaining(action -> {
            step(controllerChan, controller, queue, action);
        });
    }

    private static void step(ControllerChan controllerChan, AIController controller, ActionQueue queue, SimpleAction action) {
        debugAI(" ---> decided to do: " + action.toString());
        switch (action.getType()) {
            case MOVE:
                movePlayer(controllerChan, action, queue.getPlayer(), false);
                break;
            case DRAIN:
                drain(controllerChan, action);
                break;
            case DISCARD_CARD:
                discardCard(controllerChan, action, queue.getPlayer());
                break;
            case TRADE_CARD:
                tradeCard(controllerChan, controller, action, queue.getPlayer());
                break;
            case SPECIAL_CARD:
                specialCard(controllerChan, action, queue.getPlayer());
                break;
            case SPECIAL_ABILITY:
                specialAbility(controllerChan, controller, action, queue.getPlayer());
                break;
            case COLLECT_TREASURE:
                collectTreasure(controllerChan);
                break;
            case WAIT_AND_DRINK_TEA:
                endTurn(controllerChan);
                break;
        }
    }

    private static void movePlayer(ControllerChan controller, SimpleAction action, PlayerType playerType, boolean special) {
        Player player = controller.getCurrentAction().getPlayer(playerType);
        boolean isRescuing = controller.getAiController().getTile(player.getPosition()).getState() == MapTileState.GONE;
        if (isRescuing) {
            if (player.move(action.getTargetPoint(), false, playerType != PlayerType.PILOT)) {
                controller.getInGameViewAUI().refreshPlayerPosition(action.getTargetPoint(), player.getType());
                controller.getInGameViewAUI().refreshActionsLeft(player.getActionsLeft());
                controller.finishAction();
                debugAI("Trying to rescue a player");
            } else debugAI("Failed to rescue player");
        } else controller.getActivePlayerController().move(action.getTargetPoint(), special);
    }

    private static void drain(ControllerChan controllerChan, SimpleAction action) {
        controllerChan.getActivePlayerController().drain(action.getTargetPoint());
    }

    private static void endTurn(ControllerChan controllerChan) {
        controllerChan.getCurrentAction().getActivePlayer().setActionsLeft(0);
        controllerChan.getActivePlayerController().endActionPhase();
    }

    private static void specialCard(ControllerChan controllerChan, SimpleAction action, PlayerType playerType) {
        Player player = controllerChan.getCurrentAction().getPlayer(playerType);
        int index = -1;
        for (int i = 0; i < player.getHand().size(); i++) {
            if (player.getHand().get(i).getType() == action.getCardType())
                index = i;
        }
        if (index == -1) {
            for (Player currentPlayer : controllerChan.getCurrentAction().getPlayers()) {
                for (int i = 0; i < currentPlayer.getHand().size(); i++) {
                    if (currentPlayer.getHand().get(i).getType() == action.getCardType()) {
                        index = i;
                        player = currentPlayer;
                    }
                }
            }
        }
        if (action.getCardType() == ArtifactCardType.HELICOPTER) {
            controllerChan.getInGameUserController().playHelicopterCard(player.getType(), index,
                    new Pair<>(action.getStartingPoint(), action.getTargetPoint()),
                    new ArrayList<>(action.getTargetPlayers()));
            debugAI("playing " + action.getCardType().name() + " to " + action.getTargetPoint().toString()
                    + " from " + action.getStartingPoint().toString() + " with " + action.getTargetPlayers().toString());
        } else if (action.getCardType() == ArtifactCardType.SANDBAGS) {
            controllerChan.getInGameUserController().playSandbagCard(player.getType(), index, action.getTargetPoint());
            debugAI("playing " + action.getCardType().name() + " to " + action.getTargetPoint().toString());
        } else debugAI("THIS SHOULD NEVER HAPPEN!!! action: " + action.toString());
    }

    private static void collectTreasure(ControllerChan controllerChan) {
        controllerChan.getActivePlayerController().collectArtifact();
    }

    private static void specialAbility(ControllerChan controllerChan, AIController controller, SimpleAction action, PlayerType playerType) {
        Player targetPlayer;
        switch (playerType) {

            case COURIER:
                tradeCard(controllerChan, controller, action, playerType);
                break;
            case DIVER:
            case PILOT:
            case EXPLORER:
                movePlayer(controllerChan, action, playerType, true);
                break;
            case ENGINEER:
                drain(controllerChan, action);
                break;
            case NAVIGATOR:
                Player player = controllerChan.getCurrentAction().getPlayer(playerType);
                targetPlayer = controller.getCurrentAction().getPlayer(action.getTargetPlayers().stream().findFirst().get());
                Direction direction = targetPlayer.getPosition().getPrimaryDirection(action.getTargetPoint());
                player.forcePush(direction, targetPlayer);
                controllerChan.getInGameViewAUI().refreshPlayerPosition(targetPlayer.getPosition(), targetPlayer.getType());
                break;
            default:
                break;
        }
    }

    private static void discardCard(ControllerChan controllerChan, SimpleAction action, PlayerType playerType) {
        Player player = controllerChan.getCurrentAction().getPlayer(playerType);
        List<ArtifactCard> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getType() == action.getCardType()) {
                controllerChan.getInGameUserController().discardCard(player.getType(), i);
                return;
            }
        }
    }

    private static void tradeCard(ControllerChan controllerChan, AIController controller, SimpleAction action, PlayerType playerType) {
        Player player = controllerChan.getCurrentAction().getPlayer(playerType);
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

        //ugly NullPointer fix
        if (player == null && playerType == PlayerType.COURIER)
            player = new Courier("kannstMich", startPosition, action);

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