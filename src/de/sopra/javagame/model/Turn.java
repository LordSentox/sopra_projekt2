package de.sopra.javagame.model;

import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.CardStackUtil;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * enthält die Informationen über den aktuellen Spielzug des zugehörigen {@link JavaGame}s
 *
 * @author Lisa, Hannah
 */
public class Turn implements Copyable<Turn> {

    /**
     * Eine Menge aller Artefakte, die bereits gefunden wurden.
     *
     * @see ArtifactType
     */
    private EnumSet<ArtifactType> discoveredArtifacts;

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
    private CardStack<FloodCard> floodCardStack;


    /**
     * Nachziehstapel mit den {@link ArtifactCard}
     */
    private CardStack<ArtifactCard> artifactCardStack;

    /**
     * Listli mit den {@link Player}
     */
    private List<Player> players;

    /**
     * Enum das Auskunft über die aktuelle Phase gibt
     */
    private TurnState state;

    public CardStack<ArtifactCard> getArtifactCardStack() {
        return artifactCardStack;
    }

    public CardStack<FloodCard> getFloodCardStack() {
        return floodCardStack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public String getDescription() {
        return description;
    }

    public EnumSet<ArtifactType> getDiscoveredArtifacts() {
        return discoveredArtifacts;
    }

    public TurnState getState() {
        return state;
    }

    public MapTile[][] getTiles() {
        return tiles;
    }

    public WaterLevel getWaterLevel() {
        return waterLevel;
    }


    private Turn(){}


    /**
     * Erstellt einen neuen {@link Turn} als Anfangszustand des Spiels
     * @param difficulty Die Startschwierigkeit des Spiels
     * @param tiles Die Map des Spiels
     */
    public static Turn createInitialTurn(Difficulty difficulty, List<Player> players, MapTile[][] tiles) {
        Turn turn = new Turn();
        turn.discoveredArtifacts = EnumSet.noneOf(ArtifactType.class);
        turn.description = "";
        turn.tiles = tiles;
        turn.waterLevel = new WaterLevel(difficulty);
        turn.floodCardStack = CardStackUtil.createFloodCardStack(tiles);
        turn.artifactCardStack = CardStackUtil.createArtifactCardStack();
        turn.players = new ArrayList<>();
        turn.state = TurnState.FLOOD;
        return turn;
    }


    /**
     * Methode um einen Spieler ohne Kosten von seinen Aktionspunkten zu bewegen
     *
     * @param direction (oben, unten, rechts, links) Richtung in die bewegt werden darf
     * @param caster    Spieler welcher den anderen bewegt
     * @param other     der zu bewegende Spieler
     * @return gibt zurück, ob das Bewegen erfolgreich war
     */
    boolean forcePush(Direction direction, Player caster, Player other) {
        return false;
    }

    /**
     * Methode um Artefakkarten an andere Spieler zu übergeben
     *
     * @param card     die zu übergebende Karte
     * @param source   Spieler, der die Karte abgibt (aktiver Spieler)
     * @param receiver Spieler, der die Karte erhalten soll
     * @return gibt zurück, ob das Übergeben erfolgreich war
     */
    boolean transferArtifactCard(ArtifactCard card, Player source, Player receiver) {
        return false;
    }

    @Override
    public Turn copy() {
        Turn turn = new Turn();
        turn.activePlayer = this.activePlayer;
        turn.artifactCardStack = this.artifactCardStack.copy();
        turn.description = CopyUtil.copy(this.description);
        turn.discoveredArtifacts = EnumSet.copyOf(this.discoveredArtifacts);
        turn.floodCardStack = this.floodCardStack.copy();
        turn.players = CopyUtil.copyAsList(this.players);
        turn.state = this.state;
        turn.tiles = new MapTile[this.tiles.length][this.tiles[0].length];
        CopyUtil.copyArr(this.tiles, turn.tiles);
        turn.waterLevel = this.waterLevel.copy();
        return turn;
    }
}
