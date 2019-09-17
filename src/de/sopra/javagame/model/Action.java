package de.sopra.javagame.model;

import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * enthält die Informationen über den aktuellen Spielzug des zugehörigen {@link JavaGame}s
 *
 * @author Lisa, Hannah
 */
//FIXME Action umbenennen zu Action --> Action bedeutet genau eine Aktion von dem Spiel, dem aktiven oder einem anderen Spieler
//    --> dazu alle Aufrufe, die Action beinhalten umbenennen

public class Action implements Copyable<Action>, Serializable {

    private static final long serialVersionUID = -2000089597610909945L;

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
    private MapFull map;

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

    public WaterLevel getWaterLevel() {
        return waterLevel;
    }

    /**
     * Erstellt einen neuen {@link Action} als Anfangszustand des Spiels
     *
     * @param difficulty Die Startschwierigkeit des Spiels
     * @param map        Die Map des Spiels
     */
    public static Action createInitialAction(Difficulty difficulty, List<Triple<PlayerType, String, Boolean>> players, MapFull map) throws NullPointerException, IllegalArgumentException {
        if (map == null || difficulty == null || players == null)
            throw new IllegalArgumentException("Argument is null");

        if (players.isEmpty() || players.size() < 2 || players.size() > 4)
            throw new IllegalStateException();
        
        //players.replaceAll(pair -> pair.getFirst().equals(other));
        
        Action action = new Action();
        action.discoveredArtifacts = EnumSet.noneOf(ArtifactType.class);
        action.description = "Spielstart";
        action.map = map;
        action.floodCardStack = CardStackUtil.createFloodCardStack(map.raw());
        action.floodCardStack.shuffleDrawStack();
        action.waterLevel = new WaterLevel(difficulty);
        action.artifactCardStack = CardStackUtil.createArtifactCardStack();
        action.artifactCardStack.shuffleDrawStack();
        action.players = new LinkedList<>();
        players.forEach(triple -> {
            if(triple.getFirst() != PlayerType.NONE)
                action.players.add(createPlayerByType(triple.getFirst(), triple.getSecond(), map.getPlayerSpawnPoint(triple.getFirst()), action));
            else
                action.players.add(null);
        });
        
        
        for (int i = 0; i < players.size(); i++) {
            Player player = action.players.get(i);
            if(player != null) continue;
            Triple<PlayerType, String, Boolean> triple = players.get(i);
            List<PlayerType> list = EnumSet.allOf(PlayerType.class).stream()
                  .filter(pType -> !pType.equals(PlayerType.NONE))
                  .filter(pType -> action.players.stream().filter(Objects::nonNull).noneMatch(p -> p.getType() == pType))
                  .sorted((item1, item2) -> (new Random()).nextInt()).collect(Collectors.toList()); 
                 
            Collections.shuffle(list);
            Player p = createPlayerByType(list.get(0), triple.getSecond(), map.getPlayerSpawnPoint(list.get(0)), action);
            action.players.set(i, p);
        }
        //TODO darf nicht initial auf FLOOD stehen, sondern soll ordentlich die 6 felder zu beginn fluten
        
        action.state = TurnState.PLAYER_ACTION;
        
        return action;
    }
    
    public static Player createPlayerByType(PlayerType type, String name, Point start, Action action) {
        switch (type) {
            case COURIER:
                return new Courier(name, start, action);
            case DIVER:
                return new Diver(name, start, action);
            case PILOT:
                return new Pilot(name, start, action);
            case NAVIGATOR:
                return new Navigator(name, start, action);
            case EXPLORER:
                return new Explorer(name, start, action);
            case ENGINEER:
                return new Engineer(name, start, action);
            default:
                return null;
        }
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
        action.map = new MapFull(this.map);
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

    public MapFull getMap() {
        return this.map;
    }


}
