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
//FIXME Action umbenennen zu Action --> Action bedeutet genau eine Aktion von dem Spiel, dem aktiven oder einem anderen Spieler
//    --> dazu alle Aufrufe, die Action beinhalten umbenennen

public class Action implements Copyable<Action> {

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

    private Action() {
    }

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

    public int getActivePlayerIndex() {
        return activePlayer;
    }

    public Player getActivePlayer() {
        return this.players.get(activePlayer);
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

    /**
     * Erstellt einen neuen {@link Action} als Anfangszustand des Spiels
     *
     * @param difficulty Die Startschwierigkeit des Spiels
     * @param tiles      Die Map des Spiels
     */
    public static Action createInitialAction(Difficulty difficulty, List<Pair<PlayerType, Boolean>> players, MapTile[][] tiles)
        throws NullPointerException, IllegalArgumentException {
        Action action = new Action();
        action.discoveredArtifacts = EnumSet.noneOf(ArtifactType.class);
        action.description = "Spielstart";
        if (tiles == null || difficulty == null || players == null)
            throw new NullPointerException();

        if (players.isEmpty() || players.size() <2 || players.size() > 4)
            throw new IllegalStateException();

        action.tiles = tiles;
        action.floodCardStack = CardStackUtil.createFloodCardStack(tiles);
        action.waterLevel = new WaterLevel(difficulty);
        action.artifactCardStack = CardStackUtil.createArtifactCardStack();

        action.players = players.stream().map(pair -> {
            Point start = MapUtil.getPlayerSpawnPoint(tiles, pair.getLeft());
            switch (pair.getLeft()) {
            case COURIER:
                return new Courier("Hartmut Kurier", start, action);
            case DIVER:
                return new Diver("Hartmut im Spanienurlaub", start, action);
            case PILOT:
                return new Pilot("Hartmut auf dem Weg in den Urlaub", start, action);
            case NAVIGATOR:
                return new Navigator("Hartmut Verlaufen", start, action);
            case EXPLORER:
                return new Explorer("Hartmut im Dschungel", start, action);
            case ENGINEER:
                return new Engineer("Hartmut Auto Kaputt", start, action);
            default:
                throw new IllegalArgumentException("Illegal Player Type: " + pair.getLeft());
            }
        }).collect(Collectors.toList());
        action.state = TurnState.FLOOD;
        return action;
    }

    /**
     * Methode um Artefakkarten an andere Spieler zu übergeben
     *
     * @param card     die zu übergebende Karte
     * @param source   Spieler, der die Karte abgibt (aktiver Spieler)
     * @param receiver Spieler, der die Karte erhalten soll
     * @return gibt zurück, ob das Übergeben erfolgreich war
     */
    public boolean transferArtifactCard(ArtifactCard card, Player source, Player receiver) {
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
        this.players.get(this.activePlayer).onTurnStarted();
    }

    public Player getNextPlayer() {
        return this.players.get(this.activePlayer + 1 % this.players.size());
    }

    /**
     * Die Tile, welche an der übergebenen Position liegt wird zurückgegeben. Ist an der Stelle
     * kein Inselfeld wird <code>null</code> übergeben.
     *
     * @param position Die Position, von der man das Tile wissen möchte.
     * @return Tile an der Position
     */
    public MapTile getTile(Point position) {
        return this.tiles[position.yPos][position.xPos];
    }


    /**
     * Die Tile, welche an der übergebenen Position liegt wird zurückgegeben. Ist an der Stelle
     * kein Inselfeld wird <code>null</code> übergeben.
     *
     * @return Tile an der Position
     */
    public MapTile getTile(int posX, int posY) {
        return this.tiles[posY][posX];
    }

    @Override
    public Action copy() {
        Action action = new Action();
        action.activePlayer = this.activePlayer;
        action.artifactCardStack = this.artifactCardStack.copy();
        action.description = CopyUtil.copy(this.description);
        action.discoveredArtifacts = EnumSet.copyOf(this.discoveredArtifacts);
        action.floodCardStack = this.floodCardStack.copy();
        action.players = CopyUtil.copyAsList(this.players);
        for (Player player : action.players) {
            player.setAction(action);
        }
        action.state = this.state;
        action.tiles = new MapTile[this.tiles.length][this.tiles[0].length];
        CopyUtil.copyArr(this.tiles, action.tiles);
        action.waterLevel = this.waterLevel.copy();
        return action;
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
