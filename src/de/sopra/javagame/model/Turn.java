package de.sopra.javagame.model;

import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.Direction;

import java.util.Collection;

/**
 * enthält die Informationen über den aktuellen Spielzug des zugehörigen {@link JavaGame}s
 * @author Lisa, Hannah
 *
 */
public class Turn {

    /**
     * ein Array der Größe 4.Der Index bestimmt die Art des Artefakts
     * true: Artefakt wurde bereits gefunden
     */
    private boolean[] discoveredArtifacts;

    /**
     * eine Beschreibung der ausgeführten Aktion
     */
    private String description;

    /**
     * Der Index des aktiven Spielers für den zugriff auf Spielerlistli
     */
    private int activePlayer;

    /**
     * Die Verteilung der {@link MapTile} 
     */
    private MapTile[][] tiles;

    /**
     * Der {@link WaterLevel}
     */
    private WaterLevel waterLevel;

    /**
     * Nachziehstapel mit den {@link FloodCard}
     */
    private Collection<CardStack<FloodCard>> floodCardStack;
    

    /**
     * Nachziehstapel mit den {@link ArtifactCard}
     */
    private Collection<CardStack<ArtifactCard>> artifactCardStack;

    /**
     * Listli mit den {@link Player}
     */
    private Collection<Player> players;

    /**
     * Enum das Auskunft über die aktuelle Phase gibt
     */
    private TurnState state;

    /**
     * Methode um einen Spieler ohne Kosten von seinen Aktionspunkten zu bewegen
     * @param direction (oben, unten, rechts, links) Richtung in die bewegt werden darf
     * @param caster Spieler welcher den anderen bewegt
     * @param other der zu bewegende Spieler
     * @return gibt zurück, ob das Bewegen erfolgreich war
     */
    boolean forcePush(Direction direction, Player caster, Player other) {
        return false;
    }

    /**
     * Methode um Artefakkarten an andere Spieler zu übergeben 
     * @param card die zu übergebende Karte
     * @param source Spieler, der die Karte abgibt (aktiver Spieler)
     * @param receiver Spieler, der die Karte erhalten soll
     * @return gibt zurück, ob das Übergeben erfolgreich war
     */
    boolean transferArtifactCard(ArtifactCard card, Player source, Player receiver) {
        return false;
    }

}
