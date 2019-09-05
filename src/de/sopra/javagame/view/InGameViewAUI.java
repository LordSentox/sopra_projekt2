package de.sopra.javagame.view;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.CardStack;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.PlayerType;

import java.awt.*;
import java.util.List;

/**
 * bietet Methoden zum aktualisieren der InGameView 
 * @author Hannah, Lisa
 *
 */
public interface InGameViewAUI {

    /**
     * aktualisiert die Anzeige der möglichen Bewegungen
     * @param points Liste der Positionen an die der Spieler sich bewegen darf
     */
    public abstract void refreshMovementOptions(List<Point> points);

    /**
     * aktualisiert die Anzeige der möglichen Felder die trockengelegt werden können
     * @param points Liste der Felder die der Spieler trockenlegen darf
     */
    public abstract void refreshDrainOptions(List<Point> points);

    /**
     * zeigt dem Spieler die übergebene Nachricht in dem dafür vorgesehenen Fenster
     * @param notification Nachricht an den Spieler
     */
    public abstract void showNotification(String notification);

    /**
     *  aktualisiert die Anzeige, ob Karten abgegeben werden können
     * @param transferable true: Karten können übergeben werden
     * 					   false: Karten können nicht übergeben werden
     */
    public abstract void refreshCardsTransferable(boolean transferable);

    /**
     * aktualisiert die Anzeige des aktuellen Wasserpegels
     * @param level neuer Pegel
     */
    public abstract void refreshWaterLevel(int level);

    /**
     * aktualisiert die Anzeige der Handkarten eines Spielers
     * @param player Spieler dessen Handkarten aktualisiert werden sollen
     * @param cards Liste der neuen Handkarten
     */
    public abstract void refreshHand(PlayerType player, List cards);

    /**
     * aktualisiert die Anzeige der gefundenen Artefakte. Gefundene Artefakte sind nicht mehr ausgegraut.
     * @param artifacts[] Array der Größe 4, index gibt Art des Artefakt an, wenn true ist es gefunden 
     */
    public abstract void refreshArtifactsFound(boolean[] artifacts);

    /** 
     * aktualisiert die Anzeige des Ziehstapels (Höhe) und des Ablagestapels der Artefaktkarten
     * @param stack Ziehstapel der Artefaktkarten
     */
    public abstract void refreshArtifactStack(CardStack<ArtifactCard> stack);

    /**
     * aktualisiert die Anzeige des Ziehstapels (Höhe) und des Ablagestapels der Flutkarten
     * @param stack Ziehstapel der Flutkarten
     */
    public abstract void refreshFloodStack(CardStack<FloodCard> stack);

    /**
     * aktualisiert die Anzeige der Position des übergebenen Spielers
     * @param position neue Position
     * @param player zu bewegender Spielertyp
     */
    public abstract void refreshPlayerPosition(Point position, PlayerType player);

    /**
     * aktualisiert die Anzeige des übergebenden Kartenstücks(trocken, geflutet, versunken)
     * @param position Position des Kartenstücks
     * @param tile Kartenstück welches aktualisiert werden muss
     */
    public abstract void refreshMapTile(Point position, MapTile tile);

    /**
     * aktualisiert die Anzeige welcher Spieler an der Reihe ist
     * @param player Spieler der zurzeit am Zug ist
     */
    public abstract void refreshActivePlayer(PlayerType player);

    /**
     * aktualisiert die Anzeige der übrigen Aktionen
     * @param actionsLeft Anzahl der verbleibenden Anktionen
     */
    public abstract void refreshActionsLeft(int actionsLeft);

    /**
     * aktualisiert die Anzeige des Spielernamens für den übergebenen Spielertyp
     * @param name neuer Name
     * @param player Spielertyp dessen Name geändert werden muss
     */
    public abstract void refreshPlayerName(String name, PlayerType player);

    /**
     * aktualisiert die Anzeige des gesamten Spielfelds
     */
    public abstract void refreshAll();

    /**
     * setzt das Fenster auf ein Replay Fenster um vergangene Partien anzusehen
     * @param replay true: eine alte Partie kann abgespielt werden
     * 				 false: eine Partie kann gespielt werden
     */
    public abstract void setIsReplayWindow(boolean replay);

}
