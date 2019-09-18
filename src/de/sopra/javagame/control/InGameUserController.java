package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapFull;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class InGameUserController {

    private final ControllerChan controllerChan;

    InGameUserController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
    }

    /**
     * Spielt eine Helikopterkarte in der aktuellen Situation im Spiel mit den gegebenen Funktionen.
     * Dabei werden alle Spieler in der Liste von der Start-Position an die gegebene Ziel-Position bewegt
     * und die Karte aus der Hand des sourcePlayer entfernt.
     *
     * @param sourcePlayer  der Spieler, welcher die Karte ausspielt, darf nicht <code>null</code> sein
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     * @param flightRoute   Der Startpunkt und der Endpunkt, welche die Route beschreibt, die die Spieler fliegen sollen
     * @param players       die Spieler, welche transportiert werden sollen, darf nicht leer sein
     */
    public void playHelicopterCard(PlayerType sourcePlayer, int handCardIndex, Pair<Point, Point> flightRoute, List<PlayerType> players) {
        Action currentAction = controllerChan.getCurrentAction();
        List<ArtifactCard> handCards = currentAction.getPlayer(sourcePlayer).getHand();

        //Falls sich am handCardIndex des sourcePlayers keine Helicopter-Karte befindet war der Aufruf ungültig
        checkHandForCard(handCardIndex, handCards, ArtifactCardType.HELICOPTER);

        //prüfe, ob alle zu bewegenden Spieler auf ein und demselben Punkt stehen
        checkAllPlayersOnStartPoint(flightRoute, players);

        //Überprüfen, ob das Spiel gewonnen ist --> TODO inGameViewAUI.setGameEnded(gameWon = true) nutzen, Speichern abfangen!
        if (checkWonOnHelicopter(currentAction)) {
            controllerChan.getInGameViewAUI().showNotification("Herzlichen Glückwunsch! Ihr habt die Insel besiegt." +
                    "Euch allen eine sichere und schnelle Heimreise " +
                    "und auf ein baldiges Wiedersehen~");
            return;
        }

        //Nur, wenn vorher nicht abgebrochen wurde waren alle Werte korrekt.
        //Bewege die Spieler, wie vorgesehen
        Pair sourceInformation = new Pair<PlayerType, Integer> (sourcePlayer, handCardIndex);
        actuallyMovePlayers(sourceInformation, flightRoute, players, currentAction);
        controllerChan.finishAction();
    }

    /**
     * bewegt die Spieler, entfernt die gespielte Karte aus der Hand des Spielers und sendet refreshs an die AUI
     *
     * @param sourcePlayer  Spieler, der die Helikopterkarte spielt
     * @param handCardIndex der Index, an dem sich die Karte befindet
     * @param flightRoute   Start- und Endpunkt der Flugroute
     * @param players       die Spieler, die bewegt werden sollen
     * @param currentAction die aktuelle Action, die dann beendet werden muss
     */
    private void actuallyMovePlayers(Pair<PlayerType, Integer>sourceInformation, Pair<Point, Point> flightRoute, List<PlayerType> players, Action currentAction) {
        //wenn nicht alle Gewinnbedingungen erfüllt sind, bewege nun players von FlyFrom nach FlyTo
        if (flightRoute.getLeft() == null || flightRoute.getRight() == null
                || currentAction.getMap().get(flightRoute.getRight()) == null
                || currentAction.getMap().get(flightRoute.getRight()).getState() == MapTileState.GONE) {
            throw new IllegalStateException("Mindestens einer der übergebenen Points war null. " +
                    "Fliegen ist so nicht möglich!");
        }
        
        PlayerType sourcePlayer = sourceInformation.getLeft();
        int handCardIndex = sourceInformation.getRight();
        //entferne die gespielte Karte von der Spieler-Hand
        currentAction.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentAction.getPlayer(sourcePlayer).getHand());
        //bewege die players
        for (PlayerType currentPlayerType : players) {
            Player currentPlayer = currentAction.getPlayer(currentPlayerType);
            if (!currentPlayer.getPosition().equals(flightRoute.getLeft())) {
                throw new IllegalStateException(currentPlayer.getName() + " stand nicht auf dem gewählten Feld. " +
                        "Fliegen ist so nicht möglich!");
            }
            currentPlayer.setPosition(flightRoute.getRight());
            controllerChan.getInGameViewAUI().refreshPlayerPosition(currentPlayer.getPosition(), currentPlayer.getType());
        }
    }

    /**
     * Zeigt alle Bewegungsmöglichkeiten des gegebenen Spieler an.
     * Wenn der gegebene Spieler nicht der aktive Spieler ist und der aktive Spieler der Navigator ist,
     * dann werden zustätzlich die aktuellen drain-options zurückgesetzt.
     *
     * @param playerType    Der Zielspieler, wessen Movement gezeigt werden soll
     * @param specialActive Falls <code>true</code>, werden die
     *                      Spezialbewegungsfähigkeiten des jeweiligen Spielers
     *                      miteinberechnet. Sonst. weiden nur die
     *                      Standardbewegungsmöglichkeiten angezeigt.
     */
    public void showMovements(PlayerType playerType, boolean specialActive) {
        Action currentAction = controllerChan.getCurrentAction();
        Player player = currentAction.getPlayer(playerType);
        List<Point> movements = player.legalMoves(specialActive);
        controllerChan.getInGameViewAUI().refreshMovementOptions(movements);
        if (currentAction.getActivePlayer().getType() == PlayerType.NAVIGATOR
                && currentAction.getActivePlayer().getType() != playerType) {
            controllerChan.getInGameViewAUI().refreshDrainOptions(Collections.emptyList()); //reset drain-options
        }
    }

    /**
     * prüft, ob alle Spieler, die bewegt werden sollen auch auf dem Startpunkt der Fugroute stehen
     *
     * @param flightRoute der Start- und Endpunkt der gewählten Route
     * @param players     die Spieler, die bewegt werden sollen
     */
    private void checkAllPlayersOnStartPoint(Pair<Point, Point> flightRoute, List<PlayerType> players) {
        boolean allOnFlightRouteStartPoint = playersOnPoint(flightRoute.getLeft(), players);
        if (!allOnFlightRouteStartPoint) {
            throw new IllegalStateException("Die übergebenen Spieler standen nicht auf einem Feld. " +
                    "Sie hätten gar nicht gemeinsam übergeben werden dürfen!");
        }
    }

    /**
     * prüft, ob die Handkarten alle Bedingungen erfüllen, um gewählte Karte zu spielen
     *
     * @param handCardIndex der Index, an dem die Karte sich befinden soll
     * @param handCards     die Handkarten des Spielers
     * @param cardType      der Kartentyp, der gespielt werden soll
     */
    private void checkHandForCard(int handCardIndex, List<ArtifactCard> handCards, ArtifactCardType cardType) {
        if (handCards.size() <= handCardIndex ||
                handCards.get(handCardIndex) == null ||
                handCards.get(handCardIndex).getType() != cardType) {
            throw new IllegalStateException("Es wurde keine Helikopter-Karte übergeben " +
                    "aber die playHelicopterCard Methode aufgerufen!");
        }
    }

    /**
     * Prüft, ob die Gewinnbedingungen erfüllt sind (alle Artefakte gefunden, alle Spieler stehen auf dem Helikopter-Punkt)
     *
     * @param currentAction aktuelle Aktion, in der die Helikopterkarte gespielt wurde
     */
    private boolean checkWonOnHelicopter(Action currentAction) {
        MapFull map = currentAction.getMap();

        Point heliPoint = map.getPlayerSpawnPoint(PlayerType.PILOT);
        EnumSet<ArtifactType> artifactsFound = currentAction.getDiscoveredArtifacts();
        List<Player> players = controllerChan.getCurrentAction().getPlayers();
        List<PlayerType> allPlayersTypes = new ArrayList<>();
        for (Player currentPlayer : players) {
            allPlayersTypes.add(currentPlayer.getType());
        }
        boolean allOnHeliPoint = playersOnPoint(heliPoint, allPlayersTypes);

        if (artifactsFound.size() == 4 && !artifactsFound.contains(ArtifactType.NONE) && allOnHeliPoint) {
            currentAction.setGameEnded(true);
            currentAction.setGameWon(true);
            controllerChan.finishAction();
            return true;
        }
        return false;
    }

    /**
     * Überprüft, ob die übergebenen Spieler alle auf dem übergebenen Punkt stehen
     *
     * @param positionToCheck    Der übergebene Punkt, auf dem die übergebenen Spieler stehen sollen
     * @param playerTypesToCheck Die übergebenen Spieler, die alle auf dem übergebenen Punkt stehen sollen
     * @return
     */
    public boolean playersOnPoint(Point positionToCheck, List<PlayerType> playerTypesToCheck) {
        List<Player> playersToCheck = new ArrayList<>();

        for (PlayerType currentPlayerType : playerTypesToCheck) {
            Player currentPlayer = controllerChan.getCurrentAction().getPlayer(currentPlayerType);
            if (currentPlayer.getPosition().equals(positionToCheck)) {
                playersToCheck.add(currentPlayer);
            }
        }
        return playersToCheck.size() == playerTypesToCheck.size();
    }

    /**
     * Spielt eine Sandsackkarte in der aktuelle Situation im Spiel mit den gegebenen Funktionen.
     * Dabei wird das Zielfeld trockengelegt und die Karte aus der Hand des sourcePlayer entfernt.
     *
     * @param sourcePlayer  der Spieler, welcher die Karte ausspielt, darf nicht <code>null</code> sein
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     * @param destination   die Position auf der Karte, welche trockengelegt werden soll
     */
    public void playSandbagCard(PlayerType sourcePlayer, int handCardIndex, Point destination) {
        Action currentAction = controllerChan.getCurrentAction();
        List<ArtifactCard> handCards = currentAction.getPlayer(sourcePlayer).getHand();
        MapTile tileToDrain = currentAction.getMap().get(destination);

        //Falls sich am handCardIndex des sourcePlayers keine Helicopter-Karte befindet war der Aufruf ungültig
        checkHandForCard(handCardIndex, handCards, ArtifactCardType.SANDBAGS);

        if (tileToDrain.getState() == MapTileState.DRY) {
            throw new IllegalStateException("Das übergebene MapTile ist schon trocken. " +
                    "Trockenlegen ist so nicht möglich!");
        }
        if (tileToDrain.getState() == MapTileState.GONE) {
            throw new IllegalStateException("Das übergebene MapTile ist schon versunken. " +
                    "Trockenlegen ist so nicht möglich!");
        }

        //entferne die gespielte Karte von der Spieler-Hand
        currentAction.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentAction.getPlayer(sourcePlayer).getHand());

        //lege das gewählte MapTile trocken
        currentAction.getMap().get(destination).drain();
        controllerChan.getInGameViewAUI().refreshMapTile(destination, tileToDrain);
        controllerChan.finishAction();
    }


    /**
     * Wirft eine Karte von der Hand des Spielers auf den Artifaktstapel ab.
     *
     * @param sourcePlayer  die Spieler, welcher die Karte abwerfen soll
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     */
    public void discardCard(PlayerType sourcePlayer, int handCardIndex) {
        Action currentAction = controllerChan.getCurrentAction();
        List<ArtifactCard> handCards = currentAction.getPlayer(sourcePlayer).getHand();
        //Prüfe, ob genug Karten auf der Hand des Spielers vorhanden sind
        if (handCards.size() <= Player.MAXIMUM_HANDCARDS) {
            controllerChan.getInGameViewAUI().showNotification("Es darf keine Karte abgeworfen werden!");
            throw new IllegalStateException("Es darf keine Karte abgeworfen werden!");
        }
        //Falls sich am handCardIndex des sourcePlayers keine Karte befindet war der Aufruf ungültig
        if (handCards.size() <= handCardIndex || handCards.get(handCardIndex) == null) {
            throw new IllegalStateException("Es wurde keine Karte übergeben " +
                    "aber die discardCard Methode aufgerufen!");
        }

        //Falls alle Bedingungen korrekt wirf gewählte Karte ab
        currentAction.getArtifactCardStack().discard(handCards.get(handCardIndex));
        currentAction.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        currentAction = controllerChan.finishAction();

        // Wenn der Spieler gerade Artefaktkarten gezogen und abgeworfen hat muss nun mit der Flutphase fortgefahren
        // werden.
        if (!controllerChan.getGameFlowController().isPausedToDiscard() && controllerChan.getCurrentAction().getState() == TurnState.DRAW_ARTIFACT_CARD) {
            currentAction.setState(TurnState.FLOOD);
            currentAction.setFloodCardsToDraw(currentAction.getWaterLevel().getDrawAmount());
            controllerChan.getInGameViewAUI().refreshTurnState(TurnState.FLOOD);
        }

        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentAction.getPlayer(sourcePlayer).getHand());
        controllerChan.getInGameViewAUI().refreshArtifactStack(currentAction.getArtifactCardStack());

    }
}
