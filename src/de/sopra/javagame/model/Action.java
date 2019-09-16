package de.sopra.javagame.model;

import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.*;

import java.io.Serializable;
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

public class Action implements Copyable<Action>, Serializable {

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
    public static Action createInitialAction(Difficulty difficulty, List<Pair<Pair<PlayerType, String>, Boolean>> players, MapFull map) throws NullPointerException, IllegalArgumentException {
        if (map == null || difficulty == null || players == null)
            throw new IllegalArgumentException("Argument is null");

        if (players.isEmpty() || players.size() < 2 || players.size() > 4)
            throw new IllegalStateException();
        
        //players.replaceAll(pair -> pair.getLeft().equals(other));
        
        Action action = new Action();
        action.discoveredArtifacts = EnumSet.noneOf(ArtifactType.class);
        action.description = "Spielstart";
        action.map = map;
        action.floodCardStack = CardStackUtil.createFloodCardStack(map.raw());
        action.waterLevel = new WaterLevel(difficulty);
        action.artifactCardStack = CardStackUtil.createArtifactCardStack();
        action.players = players.stream().map(pair -> {
            Point start = map.getPlayerSpawnPoint(pair.getLeft().getLeft());
            boolean isHartmut = pair.getLeft().getRight() == null || pair.getLeft().getRight().isEmpty();
            switch (pair.getLeft().getLeft()) {
            case COURIER:
                String courierName;
                if (isHartmut){courierName = "Hartmut Kurier";}
                else {courierName = pair.getLeft().getRight();}
                Courier courier = new Courier(courierName, start, action);
                courier.setActionsLeft(3);
                return courier;
            case DIVER:
                String diverName;
                if (isHartmut){diverName = "Hartmut im Spanienurlaub";}
                else {diverName = pair.getLeft().getRight();}
                Diver diver = new Diver(diverName, start, action);
                diver.setActionsLeft(3);
                return diver;
            case PILOT:
                String pilotName;
                if (isHartmut){pilotName = "Hartmut auf dem Weg in den Urlaub";}
                else {pilotName = pair.getLeft().getRight();}
                Pilot pilot = new Pilot(pilotName, start, action);
                pilot.setActionsLeft(3);
                return pilot;
            case NAVIGATOR:
                String navigatorName;
                if (isHartmut){navigatorName = "Hartmut Verlaufen";}
                else {navigatorName = pair.getLeft().getRight();}
                Navigator navigator = new Navigator(navigatorName, start, action);
                navigator.setActionsLeft(3);
                return navigator;
            case EXPLORER:
                String explorerName;
                if (isHartmut){explorerName = "Hartmut im Dschungel";}
                else {explorerName = pair.getLeft().getRight();}
                Explorer explorer = new Explorer(explorerName, start, action);
                explorer.setActionsLeft(3);
                return explorer;
            case ENGINEER:
                String engineerName;
                if (isHartmut){engineerName = "Hartmut Auto kaputt";}
                else {engineerName = pair.getLeft().getRight();}
                Engineer engineer = new Engineer(engineerName, start, action);
                engineer.setActionsLeft(3);
                return engineer;
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
