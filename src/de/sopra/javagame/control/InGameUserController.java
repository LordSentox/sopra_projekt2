package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapUtil;
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
     * @param flyFrom       die Start-Position des Helikopters
     * @param flyTo         die Ziel-Position des Helikopters
     * @param players       die Spieler, welche transportiert werden sollen, darf nicht leer sein
     */
    public void playHelicopterCard(PlayerType sourcePlayer, int handCardIndex, Point flyFrom, Point flyTo, List<PlayerType> players) {
        Turn currentTurn = controllerChan.getJavaGame().getPreviousTurn();
        MapTile[][] map = currentTurn.getTiles();
        Point heliPoint = MapUtil.getPlayerSpawnPoint(map, PlayerType.PILOT);
        EnumSet<ArtifactType> artifactsFound = currentTurn.getDiscoveredArtifacts();
        List<ArtifactCard> handCards = currentTurn.getPlayer(sourcePlayer).getHand();
        List<Player> selectedPlayers = new ArrayList<>();

        //Falls sich am handCardIndex des sourcePlayers keine Helicopter-Karte befindet war der Aufruf ungültig
        if (handCards.get(handCardIndex).getType() != ArtifactCardType.HELICOPTER) {
            throw new IllegalStateException("Es wurde keine Helikopter-Karte übergeben " +
                    "aber die playHelicopterCard Methode aufgerufen!");
        }

        //prüfe, ob alle zu bewegenden Spieler auf ein und demselben Punkt stehen
        Point oldPosition = null;
        for (PlayerType currentPlayerType : players) {
            Point currentPosition = currentTurn.getPlayer(currentPlayerType).getPosition();
            if (oldPosition != null && currentPosition != oldPosition) {
                throw new IllegalStateException("Die übergebenen Spieler standen nicht auf einem Feld. " +
                        "Sie hätten gar nicht gemeinsam übergeben werden dürfen!");
            } else {
                oldPosition = currentPosition;
            }
        }

        //Überprüfen, ob das Spiel gewonnen ist --> TODO refresh und weitere Funktionalitäten ergänzen
        boolean allOnHeliPoint = true;
        for (Player currentPlayer : currentTurn.getPlayers()) {
            if (currentPlayer.getPosition() != heliPoint) {
                allOnHeliPoint = false;
            }
        }

        if (artifactsFound.size() == 4 && !artifactsFound.contains(ArtifactType.NONE) && allOnHeliPoint) {
            currentTurn.setGameEnded(true);
            currentTurn.setGameWon(true);
            controllerChan.endTurn();
            controllerChan.getInGameViewAUI().showNotification("Herzlichen Glückwunsch! Ihr habt die Insel besiegt." +
                    "Euch allen eine sichere und schnelle Heimreise " +
                    "und auf ein baldiges Wiedersehen~");
            //TODO in HighScoreIO Methode zum speichern von High-Scores
            //dann View bescheid geben, dass Spiel vorbei (set as Replay)
            //controllerChan.getHighScoresController().save;
        }

        //wenn nicht alle Gewinnbedingungen erfüllt sind, bewege nun players von FlyFrom nach FlyTo
        if (flyFrom == null || flyTo == null) {
            throw new IllegalStateException("Mindestens einer der übergebenen Points war null. " +
                    "Fliegen ist so nicht möglich!");
        }

        //entferne die gespielte Karte von der Spieler-Hand
        currentTurn.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentTurn.getPlayer(sourcePlayer).getHand());

        //bewege die players
        for (PlayerType currentPlayerType : players) {
            Player currentPlayer = currentTurn.getPlayer(currentPlayerType);
            if (currentPlayer.getPosition() != flyFrom) {
                throw new IllegalStateException(currentPlayer.getName() + " stand nicht auf dem gewählten Feld. " +
                        "Fliegen ist so nicht möglich!");
            }

            currentPlayer.setPosition(flyTo);
            //fix point in view
            //controllerChan.getInGameViewAUI().refreshPlayerPosition(flyTo, currentPlayer.getType());
        }
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
        Turn currentTurn = controllerChan.getJavaGame().getPreviousTurn();
        List<ArtifactCard> handCards = currentTurn.getPlayer(sourcePlayer).getHand();
        MapTile tileToDrain = currentTurn.getTile(destination);

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
        currentTurn.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentTurn.getPlayer(sourcePlayer).getHand());

        //lege das gewählte MapTile trocken
        currentTurn.getTile(destination).drain();
        //fix point in View
        //controllerChan.getInGameViewAUI().refreshMapTile(destination, tileToDrain);

    }

    /**
     * Wirft eine Karte von der Hand des Spielers auf den Artifaktstapel ab.
     *
     * @param sourcePlayer  die Spieler, welcher die Karte abwerfen soll
     * @param handCardIndex die Position der Karte in der Hand des sourcePlayer
     */
    public void discardCard(PlayerType sourcePlayer, int handCardIndex) {
        Turn currentTurn = controllerChan.getCurrentTurn();
        List<ArtifactCard> handCards = currentTurn.getPlayer(sourcePlayer).getHand();
        //Prüfe, ob genug Karten auf der Hand des Spielers vorhanden sind
        if (handCards.size() < 5) {
            controllerChan.getInGameViewAUI().showNotification("Es darf keine Karte abgeworfen werden!");
            return;
        }
        //Falls sich am handCardIndex des sourcePlayers keine Karte befindet war der Aufruf ungültig
        if (handCards.get(handCardIndex).getType() != ArtifactCardType.SANDBAGS) {
            throw new IllegalStateException("Es wurde keine Karte übergeben " +
                    "aber die discardCard Methode aufgerufen!");
        }

        //Falls alle Bedingungen korrekt wirf gewählte Karte ab
        currentTurn.getArtifactCardStack().discard(handCards.get(handCardIndex));
        currentTurn.getPlayer(sourcePlayer).getHand().remove(handCardIndex);
        controllerChan.getInGameViewAUI().refreshHand(sourcePlayer, currentTurn.getPlayer(sourcePlayer).getHand());
    }

}
