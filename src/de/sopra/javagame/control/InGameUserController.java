package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.ArrayList;
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
        if (handCards.get(handCardIndex).getType() != ArtifactCardType.HELICOPTER) {
            throw new IllegalStateException("Es wurde keine Helikopter-Karte übergeben " +
                    "aber die playHelicopterCard Methode aufgerufen!");
        }
        //prüfe, ob alle zu bewegenden Spieler auf ein und demselben Punkt stehen
        boolean allOnFlightRouteStartPoint = playersOnPoint(flightRoute.getLeft(), players);
        if (!allOnFlightRouteStartPoint) {
            throw new IllegalStateException("Die übergebenen Spieler standen nicht auf einem Feld. " +
                    "Sie hätten gar nicht gemeinsam übergeben werden dürfen!");
        }
        //Überprüfen, ob das Spiel gewonnen ist --> TODO refresh und weitere Funktionalitäten ergänzen
        checkWonOnHelicopter(currentAction);
        //wenn nicht alle Gewinnbedingungen erfüllt sind, bewege nun players von FlyFrom nach FlyTo
        if (flightRoute.getLeft() == null || flightRoute.getRight() == null
                || controllerChan.getCurrentAction().getTile(flightRoute.getRight()) == null
                || controllerChan.getCurrentAction().getTile(flightRoute.getRight()).getState() == MapTileState.GONE) {
            throw new IllegalStateException("Mindestens einer der übergebenen Points war null. " +
                    "Fliegen ist so nicht möglich!");
        }
        //entferne die gespielte Karte von der Spieler-Hand
        currentAction.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentAction.getPlayer(sourcePlayer).getHand());
        Player derHARTMUT = null;
        //bewege die players
        for (PlayerType currentPlayerType : players) {
            Player currentPlayer = currentAction.getPlayer(currentPlayerType);
            if (!currentPlayer.getPosition().equals(flightRoute.getLeft())) {
                throw new IllegalStateException(currentPlayer.getName() + " stand nicht auf dem gewählten Feld. " +
                        "Fliegen ist so nicht möglich!");
            }
            currentPlayer.setPosition(flightRoute.getRight());
            derHARTMUT = currentPlayer;
            controllerChan.getInGameViewAUI().refreshPlayerPosition(currentPlayer.getPosition(), currentPlayer.getType());
        }
        if (derHARTMUT == currentAction.getPlayer(PlayerType.EXPLORER)){
            System.out.println("U Serious?");
        }
        if (derHARTMUT != controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER)) {
           System.out.println("WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY?");
        }
        Point hartmuht = controllerChan.getCurrentAction().getPlayer(PlayerType.EXPLORER).getPosition();
        controllerChan.finishAction();
    }

    /**
     * Prüft, ob die Gewinnbedingungen erfüllt sind (alle Artefakte gefunden, alle Spieler stehen auf dem Helikopter-Punkt)
     * @param currentAction aktuelle Aktion, in der die Helikopterkarte gespielt wurde
     */
    private void checkWonOnHelicopter(Action currentAction) {
        MapTile[][] map = currentAction.getTiles();
        Point heliPoint = MapUtil.getPlayerSpawnPoint(map, PlayerType.PILOT);
        EnumSet<ArtifactType> artifactsFound = currentAction.getDiscoveredArtifacts();
        List<Player> players = controllerChan.getCurrentAction().getPlayers();
        List<PlayerType> allPlayersTypes = new ArrayList<>();
        for(Player currentPlayer : players) {
            allPlayersTypes.add(currentPlayer.getType());
        }
        boolean allOnHeliPoint =  playersOnPoint(heliPoint, allPlayersTypes);

        if (artifactsFound.size() == 4 && !artifactsFound.contains(ArtifactType.NONE) && allOnHeliPoint) {
            currentAction.setGameEnded(true);
            currentAction.setGameWon(true);
            controllerChan.finishAction();
            controllerChan.getInGameViewAUI().showNotification("Herzlichen Glückwunsch! Ihr habt die Insel besiegt." +
                    "Euch allen eine sichere und schnelle Heimreise " +
                    "und auf ein baldiges Wiedersehen~");
            //TODO in HighScoreIO Methode zum speichern von High-Scores
            //dann View bescheid geben, dass Spiel vorbei (set as Replay)
            //controllerChan.getHighScoresController().save;
        }
    }

    /**
     * Überprüft, ob die übergebenen Spieler alle auf dem übergebenen Punkt stehen
     * @param positionToCheck Der übergebene Punkt, auf dem die übergebenen Spieler stehen sollen
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
        Action currentAction = controllerChan.getJavaGame().getPreviousAction();
        List<ArtifactCard> handCards = currentAction.getPlayer(sourcePlayer).getHand();
        MapTile tileToDrain = currentAction.getTile(destination);

        //Falls sich am handCardIndex des sourcePlayers keine Helicopter-Karte befindet war der Aufruf ungültig
        if (handCards.get(handCardIndex).getType() != ArtifactCardType.SANDBAGS) {
            throw new IllegalStateException("Es wurde keine Sandsack-Karte übergeben " +
                    "aber die playSandbagCard Methode aufgerufen!");
        }

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
        currentAction.getTile(destination).drain();
        //fix point in View
        //controllerChan.getInGameViewAUI().refreshMapTile(destination, tileToDrain);
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
        if (handCards.size() < Player.MAXIMUM_HANDCARDS) {
            controllerChan.getInGameViewAUI().showNotification("Es darf keine Karte abgeworfen werden!");
            return;
        }
        //Falls sich am handCardIndex des sourcePlayers keine Karte befindet war der Aufruf ungültig
        if (handCards.get(handCardIndex).getType() != ArtifactCardType.SANDBAGS) {
            throw new IllegalStateException("Es wurde keine Karte übergeben " +
                    "aber die discardCard Methode aufgerufen!");
        }

        //Falls alle Bedingungen korrekt wirf gewählte Karte ab
        currentAction.getArtifactCardStack().discard(handCards.get(handCardIndex));
        currentAction.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentAction.getPlayer(sourcePlayer).getHand());
        controllerChan.finishAction();
    }

}
