package de.sopra.javagame.control;

import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.MapFull;
import de.sopra.javagame.util.Point;

import java.util.ArrayList;
import java.util.List;

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
        for (ArtifactCard currentCard : drawnCards) {
            if (currentCard.getType() == ArtifactCardType.WATERS_RISE) {
                waterLevel.increment();
                if (waterLevel.isGameLost()) {
                    controllerChan.getInGameViewAUI().showNotification("Ihr habt leider verloren!" +
                            "Das Wasser ist viel zu schnell gestiegen und nun ist die ganze Insel versunken." +
                            "Keiner von euch konnte sich retten. Ihr alle seid einen qualvollen Tod " +
                            "gestorben.");
                    //TODO in HighScoreIO Methode zum speichern von High-Scores
                    //dann View bescheid geben, dass Spielvorbei (set as Replay)
                    //controllerChan.getHighScoresController().save;
                } else {
                    artifactCardStack.discard(currentCard);
                }
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
            //TODO in HighScoreIO Methode zum speichern von High-Scores
            //                    //dann View bescheid geben, dass Spielvorbei (set as Replay)
        }
    }

    /**
     * Zieht die Menge an Flutkarten, die laut {@link de.sopra.javagame.model.WaterLevel} gezogen werden müssen und
     * überflutet bzw. versenkt die entsprechenden Felder.
     */
    //TODO refreshCardStack?
	public void drawFloodCards() {
        WaterLevel waterLevel = controllerChan.getCurrentAction().getWaterLevel();
        CardStack<FloodCard> floodCardCardStack = controllerChan.getCurrentAction().getFloodCardStack();
        List<FloodCard> floodCards = floodCardCardStack.draw(waterLevel.getDrawAmount(), true);
        for (FloodCard currentCard : floodCards) {
            currentCard.flood();
            //check if one or more Players are drowning
            List<Player> rescuesNeeded = playersNeedRescue(controllerChan.getCurrentAction().getMap().getPositionForTile(currentCard.getTile().getProperties()));
            for(Player rescuePlayer : rescuesNeeded) {
                controllerChan.getInGameViewAUI().refreshMovementOptions(rescuePlayer.legalMoves(false));
            }
            controllerChan.getInGameViewAUI().refreshMapTile(controllerChan.getCurrentAction().getMap().getPositionForTile(currentCard.getTile().getProperties()),
                                                            currentCard.getTile());
            controllerChan.finishAction();
        }
    }


    List<Player> playersNeedRescue(Point positionToCheck) {
        MapFull map = controllerChan.getCurrentAction().getMap();
        List<Player> playersToRescue = new ArrayList<>();
        if (map.get(positionToCheck).getState() == MapTileState.GONE) {
            for (Player currentPlayer : controllerChan.getCurrentAction().getPlayers()) {
                if (currentPlayer.getPosition().equals(positionToCheck)) {
                    playersToRescue.add(currentPlayer);
                }
            }
        }
        return playersToRescue;
    }

    /**
     * Wenn mindestens eine Aktion gemacht wurde, wird eine Aktion zurückgenommen. Eine Aktion heißt in diesem Fall
     * genau ein {@link Action}. Wurde keine gemacht passiert nichts.
     */
    public void undo() {
        if (controllerChan.getJavaGame().canUndo()) {
            controllerChan.getJavaGame().markCheetah();
            controllerChan.getJavaGame().undoAction();
            controllerChan.getJavaGame().markCheetah();
            controllerChan.getInGameViewAUI().refreshAll();
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
            controllerChan.getInGameViewAUI().refreshAll();
            return true;
        }
        return false;
    }
}
