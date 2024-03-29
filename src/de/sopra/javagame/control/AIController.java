package de.sopra.javagame.control;

import de.sopra.javagame.control.ai.*;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.AIControllerUtil;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.ArtifactType.NONE;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class AIController {

    private final ControllerChan controllerChan;

    private AIProcessor processor;

    private CardStackTracker<ArtifactCard> artifactCardStackTracker;
    private CardStackTracker<FloodCard> floodCardStackTracker;

    private Supplier<Player> activePlayerSupplier;

    private boolean active;

    public AIController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
        this.artifactCardStackTracker = new CardStackTracker<>();
        this.floodCardStackTracker = new CardStackTracker<>();
        this.processor = null;
        this.active = false;
    }

    /**
     * Ob eine KI eingesetzt wurde oder nicht.
     * Wenn keine KI eingesetzt wurde, werden einige Methoden des {@link AIController} nicht korrekt funktionieren
     *
     * @return <code>false</code> falls die KI noch nicht gesetzt wurde
     */
    public boolean hasAI() {
        return processor != null;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Setzt und überschreibt die aktuell gewählte KI.
     * Das beinflusst das Tracking der Ziehstapel nicht, d.h. diese Methode darf auch im laufenden Spiel verwendet werden
     * und erfordert keinerlei Sicherheitsüberprüfung.
     *
     * @param gameAI die KI per Auswahl als Enum
     */
    public void setAI(GameAI gameAI) {
        processor = gameAI.get();
        processor.init();
    }

    /**
     * Wird aufgerufen, wenn die Cardstack komplett neu erstellt werden
     */
    public void connectTrackers() {
        Action currentAction = controllerChan.getCurrentAction();
        currentAction.getArtifactCardStack().setObserver(artifactCardStackTracker);
        currentAction.getFloodCardStack().setObserver(floodCardStackTracker);
    }

    public void setActivePlayerSupplier(Supplier<Player> supplier) {
        if (supplier != null)
            this.activePlayerSupplier = supplier;
    }

    /**
     * Führt eine ActionQueue Schritt für Schritt durch.
     *
     * @param queue die Queue, welche alle Schritt nacheinander enthält
     */
    public void doSteps(ActionQueue queue) {
        AIControllerUtil.doSteps(controllerChan, this, queue);
    }

    public CardStackTracker<ArtifactCard> getArtifactCardStackTracker() {
        return artifactCardStackTracker;
    }

    public CardStackTracker<FloodCard> getFloodCardStackTracker() {
        return floodCardStackTracker;
    }

    /**
     * Ob der aktive Spieler Karten abwerfen muss
     *
     * @return <code>true</code> wenn der Spieler Karten abwerfen muss, sonst <code>false</code>
     * @see #getActivePlayer()
     */
    public boolean isCurrentlyDiscarding() {
        return getActivePlayer().getHand().size() > Player.MAXIMUM_HANDCARDS;
    }

    /**
     * Ob der aktive Spieler am Zug ist oder er außerhalb seines Zuges etwas ausführen soll
     *
     * @return <code>true</code> wenn der aktive Spieler gleich dem aktiven Spieler im Spiel ist
     */
    public boolean isAIsTurn() {
        return getCurrentAction().getActivePlayer().getType() == getActivePlayer().getType();
    }

    /**
     * Ob der aktive Spieler sich selber retten muss, d.h. auf einem versunkenen Feld steht
     *
     * @return <code>true</code> wenn der Spieler sich in seiner aktuellen Situation selbst retten muss, sonst <code>false</code>
     */
    public boolean isCurrentlyRescueingHimself() {
        MapTile tile = getTile(getActivePlayer().getPosition());
        return tile == null || tile.getState() == MapTileState.GONE;
    }

    /**
     * Der aktive Aktion, an welcher keine Änderungen vorgenommen werden dürfen
     *
     * @return Die aktuelle Aktion
     */
    public Action getCurrentAction() {
        return controllerChan.getCurrentAction();
    }

    /**
     * Der aktive Spieler, für welchen die KI berechnen soll.
     * Über einen Supplier ist dieser austauschbar,
     * damit auch Schritte für Spieler berechnet werden können, die aktuell nicht am Zug sind.
     * Die Anzahl an möglichen Aktionen sollte jedoch darüber konsistent bleiben,
     * d.h. ist der Spieler nicht am Zug, hat er auch keine freien Aktionen.
     *
     * @return der für die KI aktive Spieler
     */
    public Player getActivePlayer() {
        return activePlayerSupplier != null ? activePlayerSupplier.get() : getCurrentAction().getActivePlayer();
    }

    /**
     * Berechne MapTile und Startpunkt in dem aktuellen Spiel für einen Spieler
     *
     * @param playerType der Typ des Spielers
     * @return ein Pair aus Punkt und MapTile
     */
    public Pair<Point, MapTile> getTile(PlayerType playerType) {
        Point playerSpawnPoint = getCurrentAction().getMap().getPlayerSpawnPoint(playerType);
        return new Pair<>(playerSpawnPoint, getTile(playerSpawnPoint));
    }

    /**
     * Berechne beide Tempel Positionen und deren MapTiles
     *
     * @param artifactType der Typ der Tempel
     * @return ein Pair bestehend aus wiederum zwei Pairs,
     * die jeweils die Position und das zugehörige MapTile eines Tempels beinhalten
     */
    public Pair<Pair<Point, MapTile>, Pair<Point, MapTile>> getTile(ArtifactType artifactType) {
        Point point1 = null, point2 = null;
        for (MapTileProperties properties : MapTileProperties.values()) {
            if (properties.getHidden() == artifactType) {
                if (point1 == null) {
                    point1 = getCurrentAction().getMap().getPositionForTile(properties);
                } else {
                    point2 = getCurrentAction().getMap().getPositionForTile(properties);
                    break;
                }
            }
        }
        return new Pair<>(new Pair<>(point1, getTile(point1)), new Pair<>(point2, getTile(point2)));
    }

    /**
     * Berechne eine Liste aller Tempelpunkte
     *
     * @return eine Liste aller Tempelpunkte (erwartete Länge: 4x2=8)
     */
    public List<Pair<Point, MapTile>> getTemples() {
        List<Point> templePoints = new LinkedList<>();
        for (MapTileProperties properties : MapTileProperties.values()) {
            if (properties.getHidden() != NONE) {
                templePoints.add(getCurrentAction().getMap().getPositionForTile(properties));
            }
        }
        return templePoints.stream()
                .map(point -> new Pair<>(point, getTile(point)))
                .collect(Collectors.toList());
    }

    /**
     * Berechne das MapTile zu einem Punkt
     *
     * @param point der Punkt
     * @return das MapTile zu einem Punkt
     */
    public MapTile getTile(Point point) {
        MapTile mapTile = getCurrentAction().getMap().get(point);
        return mapTile != null ? mapTile.copy() : null;
    }

    /**
     * Eine Liste aller Spieler
     *
     * @return die Liste der Spieler im aktuellen Zustand (Action)
     */
    public List<Player> getAllPlayers() {
        List<Player> players = CopyUtil.copyAsList(getCurrentAction().getPlayers());
        for (Player player : players)
            player.setAction(getCurrentAction());
        return players;
    }

    /**
     * Eine Liste alle drainable positions nachdem ein Schritt auf jede Position von {@link Player#legalMoves(boolean)}
     * vorgenommen wurde. Positionen, die vom Startpunkt aus drainable sind sollen nicht in der Liste enthalten sein
     *
     * @param position   der Startpunkt
     * @param playerType der Spielertyp, welcher die legalMoves berechnen soll
     * @return Eine Liste mit den Positionen, die trockengelegt werden können (rechts) und die Positionen von der aus das getan werden kann (links)
     */
    public List<Pair<Point, Point>> getDrainablePositionsOneMoveAway(Point position, PlayerType playerType) {
        // Erstelle eine Kopie der momentanen Action, auf der gearbeitet werden kann
        Action currentAction = getCurrentAction().copy();

        // Setze den Spieler an die entsprechende Stelle.
        Player player = currentAction.getPlayer(playerType);
        player.setPosition(position);

        // Liste der Positionen, die der Spieler schon vom Startpunkt aus trockenlegen kann.
        // Diese wird später zum Abgleich benutzt, um sicherzustellen, dass diese Positionen nicht Teil der Rückgabe sind.
        List<Pair<Point, Point>> drainableFromStart = player.drainablePositions().stream()
                .map(pos -> new Pair<>(position, pos))
                .collect(Collectors.toList());

        // Bewege den Spieler zu allen Positionen, zu denen er darf und schaue, welche Felder er dann trockenlegen darf
        Set<Pair<Point, Point>> drainableOneMoveAway = new HashSet<>();
        List<Point> oneMoveAway = player.legalMoves(true);
        for (Point possiblePosition : oneMoveAway) {
            final Point finalPos = possiblePosition;
            player.setPosition(finalPos);
            List<Pair<Point, Point>> drainableFromHere = player.drainablePositions().stream()
                    .map(pos -> new Pair<>(finalPos, pos))
                    .collect(Collectors.toList());
            drainableOneMoveAway.addAll(drainableFromHere);
        }

        // Entferne die Felder, die er auch ohne zusätzliche Bewegung trockenlegen konnte und gib die Übrigen zurück
        drainableOneMoveAway.removeAll(drainableFromStart);
        return new ArrayList<>(drainableOneMoveAway);
    }

    /**
     * Prüft ob ein beliebiger aktiver Spieler eine Karte des gegebenen Types in der Hand hält
     *
     * @param artifactCardType der Typ der gesuchten Karte
     * @return <code>true</code> wenn ein beliebiger Spieler mindestens eine solche Karte in der Hand hält
     */
    public boolean anyPlayerHasCard(ArtifactCardType artifactCardType) {
        return getAllPlayers().stream().anyMatch(player -> EnhancedPlayerHand.ofPlayer(player).hasCard(artifactCardType));
    }

    /**
     * Gibt die Gesamtanzahl der angegebenen Artefaktkarte an, die alle Spieler zusammen haben
     *
     * @param artifactCardType gewünschter {@link ArtifactCardType}
     * @return Anzahl an insgesamt auf der Hand befindlichen gewünschten Artefaktkarten
     */
    public int getTotalAmountOfCardsOnHands(ArtifactCardType artifactCardType) {
        return getAllPlayers().stream()
                .map(player -> EnhancedPlayerHand.ofPlayer(player).getAmount(artifactCardType))
                .reduce(Integer::sum).get();
    }

    /**
     * Berechnet ein beliebiges Tile im gegebenen Zustands
     *
     * @param state der Zustand
     * @return ein beliebiges Tile im gegebenen Zustand
     */
    public MapTile anyTile(MapTileState state) {
        for (MapTile[] tileRow : getCurrentAction().getMap().raw()) {
            for (MapTile tile : tileRow) {
                if (tile != null && tile.getState() == state) {
                    return tile.copy();
                }
            }
        }
        return null;
    }

    /**
     * Fordert die KI auf mit dem aktuellen Spieler einen Zug durchzuführen
     *
     * @param player der supplier für den für die KI aktiven Spieler,
     *               beinhaltet den Spieler, welche die Aktion durchführen soll
     */
    public void makeStep(Supplier<Player> player) {
        if (!isActive()) return;
        setActivePlayerSupplier(player);
        processor.makeStep(this);
    }

    /**
     * Fordert die KI auf mit dem aktuellen Spieler einen Zug durchzuführen
     * Der hierfür verwendete aktive Spieler ist der aktive Spieler im aktiven Zug {@link Action#getActivePlayer()}
     *
     * @see #getCurrentAction()
     */
    public void makeStep() {
        makeStep(() -> getCurrentAction().getActivePlayer());
    }

    /**
     * Fordert einen Tipp als {@link ActionQueue} von der KI an.
     *
     * @param player der supplier für den für die KI aktiven Spieler,
     *               beinhaltet den Spieler, welche die Aktion durchführen soll
     * @return ein Tipp als {@link ActionQueue}
     */
    public SimpleAction getTip(Supplier<Player> player) {
        if (!isActive()) return null;
        setActivePlayerSupplier(player);
        return processor.getTip(this);
    }

    /**
     * Fordert einen Tipp als {@link ActionQueue} von der KI an.
     * Der hierfür verwendete aktive Spieler ist der aktive Spieler im aktiven Zug {@link Action#getActivePlayer()}
     *
     * @return ein Tipp als {@link ActionQueue}
     * @see #getCurrentAction()
     */
    public SimpleAction getTip() {
        controllerChan.getJavaGame().markCheetah();
        return getTip(() -> getCurrentAction().getActivePlayer());
    }

    /**
     * Gibt an, ob der Landeplatz überflutet ist
     *
     * @return <code>true</code> wenn der Landeplatz überflutet ist, sonst <code>false</code>
     */
    public boolean landingSiteIsFlooded() {
        Point landingSite = getCurrentAction().getMap().getPositionForTile(MapTileProperties.FOOLS_LANDING);
        if (landingSite == null)
            throw new IllegalStateException(); // Unerreichbar auf nicht korrumpierter map, sollte vorher gecheckt worden sein

        return getCurrentAction().getMap().get(landingSite).getState() != MapTileState.DRY;
    }

    /**
     * Berechnet den Punkt aus der Liste, welcher am nächsten am gegebenen Zielpunkt liegt.
     *
     * @param pointList          eine Liste aller Punkte die als Ergebnis in Betracht gezogen werden sollen
     * @param targetForDirection der Zielpunkt, welche angesteuert werden soll
     * @param playerType         der Spieler, um die Bewegungsmöglichkeiten mit einzurechnen
     * @return der Punkt aus der Liste, welcher am schnellsten zum Ziel führt
     * @see #getMinimumActionsNeededToReachTarget(Point, Point, PlayerType)
     */
    public Point getClosestPointInDirectionOf(List<Point> pointList, Point targetForDirection, PlayerType playerType) {
        if (pointList.contains(targetForDirection)) return targetForDirection;
        Point point = null;
        int min = 100; //just a higher value than we would expect
        for (Point current : pointList) {
            int minimum = getMinimumActionsNeededToReachTarget(current, targetForDirection, playerType);
            if (point == null || minimum < min) {
                point = current;
                min = minimum;
            }
        }
        return point;
    }

    /**
     * Berechnet die minimal nötige Anzahl an Aktionen, um vom gegebenen Startpunkt den Zielpunkt zu erreichen.
     *
     * @param startPosition  der Startpunkt
     * @param targetPosition der Zielpunkt
     * @return die minimal Anzahl an Aktionen für den Weg
     */
    public int getMinimumActionsNeededToReachTarget(Point startPosition, Point targetPosition, PlayerType playerType) {
        return AIControllerUtil.getMinimumActionsNeededToReachTarget(getCurrentAction(), startPosition, targetPosition, playerType);
    }

}