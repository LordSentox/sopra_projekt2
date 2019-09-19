package de.sopra.javagame.view;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai.SimpleAction;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.view.abstraction.Notifications;

import java.util.List;

/**
 * bietet Methoden zum aktualisieren der InGameView
 *
 * @author Hannah, Lisa
 */
public interface InGameViewAUI extends NotificationAUI {

    /**
     * aktualisiert die Anzeige der möglichen Bewegungen
     *
     * @param points Liste der Positionen an die der Spieler sich bewegen darf
     */
    void refreshMovementOptions(List<Point> points);

    /**
     * aktualisiert die Anzeige der möglichen Felder die trockengelegt werden können
     *
     * @param points Liste der Felder die der Spieler trockenlegen darf
     */
    void refreshDrainOptions(List<Point> points);

    /**
     * zeigt dem Spieler die übergebene Nachricht in dem dafür vorgesehenen Fenster
     *
     * @param infoMessage Nachricht an den Spieler
     */
    default void showNotification(String infoMessage) {
        showNotification(Notifications.info(infoMessage));
    }

    /**
     * aktualisiert die Anzeige, ob Karten abgegeben werden können
     *
     * @param transferable true: Karten können übergeben werden
     *                     false: Karten können nicht übergeben werden
     */
    void refreshCardsTransferable(boolean transferable);

    /**
     * aktualisiert die Anzeige des aktuellen Wasserpegels
     *
     * @param level neuer Pegel
     */
    void refreshWaterLevel(int level);

    /**
     * aktualisiert die Anzeige der Handkarten eines Spielers
     *
     * @param player Spieler dessen Handkarten aktualisiert werden sollen
     * @param cards  Liste der neuen Handkarten
     */
    void refreshHand(PlayerType player, List<ArtifactCard> cards);

    /**
     * aktualisiert die Anzeige der gefundenen Artefakte. Gefundene Artefakte sind nicht mehr ausgegraut.
     */
    void refreshArtifactsFound();

    /**
     * aktualisiert die Anzeige des Ziehstapels (Höhe) und des Ablagestapels der Artefaktkarten
     *
     * @param stack Ziehstapel der Artefaktkarten
     */
    void refreshArtifactStack(CardStack<ArtifactCard> stack);

    /**
     * aktualisiert die Anzeige des Ziehstapels (Höhe) und des Ablagestapels der Flutkarten
     *
     * @param stack Ziehstapel der Flutkarten
     */
    void refreshFloodStack(CardStack<FloodCard> stack);

    /**
     * aktualisiert die Anzeige der Position des übergebenen Spielers
     *
     * @param position neue Position
     * @param player   zu bewegender Spielertyp
     */
    void refreshPlayerPosition(Point position, PlayerType player);

    /**
     * aktualisiert die Anzeige des übergebenden Kartenstücks(trocken, geflutet, versunken)
     *
     * @param position Position des Kartenstücks
     * @param tile     Kartenstück welches aktualisiert werden muss
     */
    void refreshMapTile(Point position, MapTile tile);

    /**
     * aktualisiert die Anzeige welcher Spieler an der Reihe ist
     */
    void refreshActivePlayer();

    /**
     * aktualisiert die Anzeige der übrigen Aktionen
     *
     * @param actionsLeft Anzahl der verbleibenden Anktionen
     */
    void refreshActionsLeft(int actionsLeft);

    /**
     * aktualisiert die Anzeige des Spielernamens für den übergebenen Spielertyp
     *
     * @param name   neuer Name
     * @param player Spielertyp dessen Name geändert werden muss
     */
    void refreshPlayerName(String name, PlayerType player);

    /**
     * Aktualisiert einiges in der Anzeige des gesamten Spielfelds.
     * Hierbei wird die Map neu initialisiert.
     *
     * @see #refreshArtifactsFound()
     * @see #refreshActivePlayer()
     * @see #refreshArtifactStack(CardStack)
     * @see #refreshFloodStack(CardStack)
     * @see #refreshTurnState(TurnState)
     */
    void refreshSome();

    /**
     * Aktualisiert die Ansicht mittels der kompletten Aktion.
     * Ist mit Vorsicht zu genießen!
     */
    void refreshHopefullyAll(Action action);

    /**
     * setzt das Fenster auf ein Replay Fenster um vergangene Partien anzusehen
     *
     * @param replay true: eine alte Partie kann abgespielt werden
     *               false: eine Partie kann gespielt werden
     */
    void setIsReplayWindow(boolean replay);

    /**
     * Soll die Aktionen in der Queue visuell darstellen, als Tipp.
     * Eventuell kann dieser auch direkt ausgeführt werden: {@link AIController#doSteps(ActionQueue)}
     *
     * @param queue die queue an Aktionen, welche als Tipp ausgeführt würden (KI erstellt den Tipp)
     */
    void showTip(SimpleAction queue);

    /**
     * Soll den aktuellen Turnstate anzeigen.
     *
     * @param turnState der aktuelle state, der angezeigt werden soll.
     */
    void refreshTurnState(TurnState turnState);
}
