package de.sopra.javagame.model;

import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.*;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

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

    private boolean gameEnded;

    private boolean gameWon;


    public CardStack<ArtifactCard> getArtifactCardStack() {
        return artifactCardStack;
    }

    public CardStack<FloodCard> getFloodCardStack() {
        return floodCardStack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(PlayerType type) {
        for (Player currentPlayer : getPlayers()) {
            if (currentPlayer.getType() == type) {
                return currentPlayer;
            }
        }
        return null;
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

    private Turn() {}


    /**
     * Erstellt einen neuen {@link Turn} als Anfangszustand des Spiels
     *
     * @param difficulty Die Startschwierigkeit des Spiels
     * @param tiles      Die Map des Spiels
     */
    public static Turn createInitialTurn(Difficulty difficulty, List<Pair<PlayerType, Boolean>> players, MapTile[][] tiles) {
        Turn turn = new Turn();
        turn.discoveredArtifacts = EnumSet.noneOf(ArtifactType.class);
        turn.description = "";
        turn.tiles = tiles;
        turn.waterLevel = new WaterLevel(difficulty);
        turn.floodCardStack = CardStackUtil.createFloodCardStack(tiles);
        turn.artifactCardStack = CardStackUtil.createArtifactCardStack();

        turn.players = players.stream().map(pair -> {
            Point start = MapUtil.getPlayerSpawnPoint(tiles, pair.getLeft());
            switch (pair.getLeft()) {
                case COURIER:
                    return new Courier("Hartmut Kurier", start, turn);
                case DIVER:
                    return new Diver("Hartmut im Spanienurlaub", start, turn);
                case PILOT:
                    return new Pilot("Hartmut auf dem Weg in den Urlaub", start, turn);
                case NAVIGATOR:
                    return new Navigator("Hartmut Verlaufen", start, turn);
                case EXPLORER:
                    return new Explorer("Hartmut im Dschungel", start, turn);
                case ENGINEER:
                    return new Engineer("Hartmut Auto Kaputt", start, turn);
                default:
                    throw new IllegalArgumentException("Illegal Player Type: " + pair.getLeft());
            }
        }).collect(Collectors.toList());
        turn.state = TurnState.FLOOD;
        return turn;
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
        if (source.legalReceivers().contains(receiver.getType())) {
            source.getHand().remove(card);
            receiver.getHand().add(card);
            return true;
        } else {
            return false;
        }
    }

    public void nextPlayerActive() {
        this.players.get(this.activePlayer).setActionsLeft(0);
        this.activePlayer = (this.activePlayer + 1) % this.players.size();
        this.players.get(this.activePlayer).setActionsLeft(3);
    }

    /**
     * Die Tile, welche an der übergebenen Position liegt wird zurückgegeben. Ist an der Stelle
     * kein Inselfeld wird <code>null</code> übergeben.
     *
     * @param position Die Position, von der man das Tile wissen möchte.
     * @return Tile an der Position
     */
    public MapTile getTile(Point position) {
        return this.tiles[position.y][position.x];
    }


    /**
     * Die Tile, welche an der übergebenen Position liegt wird zurückgegeben. Ist an der Stelle
     * kein Inselfeld wird <code>null</code> übergeben.
     *
     *
     * @return Tile an der Position
     */
    public MapTile getTile(int posX, int posY) {
        return this.tiles[posY][posX];
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
        for(Player player : turn.players) {
            player.setActiveTurn(turn);
        }
        turn.state = this.state;
        turn.tiles = new MapTile[this.tiles.length][this.tiles[0].length];
        CopyUtil.copyArr(this.tiles, turn.tiles);
        turn.waterLevel = this.waterLevel.copy();
        return turn;
    }

    public boolean isGameEnded() {
        return this.gameEnded;
    }

    public boolean isGameWon() {
        return this.gameWon;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public void setState(TurnState state) {
        this.state = state;
    }

    public void changeDescription(String description) {
        this.description = description;
    }
}
