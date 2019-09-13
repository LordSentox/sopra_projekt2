package de.sopra.javagame.control;

import de.sopra.javagame.control.ai.AIProcessor;
import de.sopra.javagame.control.ai.CardStackTracker;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.control.ai.GameAI;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.AIActionTip;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck, Melanie Arnds
 * @version 09.09.2019
 * @since 09.09.2019
 */
public class AIController {

    private ControllerChan controllerChan;

    private AIProcessor processor;

    private CardStackTracker<ArtifactCard> artifactCardStackTracker;
    private CardStackTracker<FloodCard> floodCardStackTracker;

    private Supplier<Player> activePlayerSupplier;

    public AIController(ControllerChan controllerChan) {
        this.controllerChan = controllerChan;
        artifactCardStackTracker = new CardStackTracker<>();
        floodCardStackTracker = new CardStackTracker<>();
        processor = null;
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

    /**
     * Der aktive Tracker des Artifaktkartenstapels
     *
     * @return den aktiven Tracker des Artifaktkartenstapels
     */
    public CardStackTracker<ArtifactCard> getArtifactCardStackTracker() {
        return artifactCardStackTracker;
    }

    /**
     * Der aktive Tracker des Flutkartenstapels
     *
     * @return den aktiven Tracker des Flutkartenstapels
     */
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
     * Ob der aktive Spieler sich selber retten muss, d.h. auf einem versunkenen Feld steht
     *
     * @return <code>true</code> wenn der Spieler sich in seiner aktuellen Situation selbst retten muss, sonst <code>false</code>
     */
    public boolean isCurrentlyRescueingHimself() {
        return getCurrentAction().getTile(getActivePlayer().getPosition()).getState() == MapTileState.GONE;
    }

    /**
     * Der aktive Aktion, an welcher Änderungen vorgenommen werden dürfen
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
        return activePlayerSupplier.get();
    }

    /**
     * Berechne MapTile und Startpunkt in dem aktuellen Spiel für einen Spieler
     *
     * @param playerType der Typ des Spielers
     * @return ein Pair aus Punkt und MapTile
     */
    public Pair<Point, MapTile> getTile(PlayerType playerType) {
        Point playerSpawnPoint = MapUtil.getPlayerSpawnPoint(getCurrentAction().getTiles(), playerType);
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
                    point1 = MapUtil.getPositionForTile(getCurrentAction().getTiles(), properties);
                } else {
                    point2 = MapUtil.getPositionForTile(getCurrentAction().getTiles(), properties);
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
            if (properties.getHidden() != ArtifactType.NONE) {
                templePoints.add(MapUtil.getPositionForTile(getCurrentAction().getTiles(), properties));
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
        return getCurrentAction().getTile(point);
    }

    /**
     * Eine Liste aller Spieler
     *
     * @return die Liste der Spieler im aktuellen Zustand (Action)
     */
    public List<Player> getAllPlayers() {
        return getCurrentAction().getPlayers();
    }

    /**
     * Eine Liste alle drainable positions nachdem ein Schritt auf jede Position von {@link Player#legalMoves(boolean)}
     * vorgenommen wurde. Positionen, die vom Startpunkt aus drainable sind sollen nicht in der Liste enthalten sein
     *
     * @param position   der Startpunkt
     * @param playerType der Spielertyp, welcher die legalMoves berechnen soll
     * @return eine Liste aller drainable positions nach einem Schritt
     */
    public List<Point> getDrainablePositionsOneMoveAway(Point position, PlayerType playerType) {
        // Erstelle eine Kopie der momentanen Action, auf der gearbeitet werden kann
        Action currentAction = getCurrentAction().copy();

        // Setze den Spieler an die entsprechende Stelle.
        Player player = currentAction.getPlayer(playerType);
        player.setPosition(position);

        // Liste der Positionen, die der Spieler schon vom Startpunkt aus trockenlegen kann.
        // Diese wird später zum Abgleich benutzt, um sicherzustellen, dass diese Positionen nicht Teil der Rückgabe sind.
        List<Point> drainableFromStart = player.drainablePositions();

        // Bewege den Spieler zu allen Positionen, zu denen er darf und schaue, welche Felder er dann trockenlegen darf
        Set<Point> drainableOneMoveAway = new HashSet<>();
        for (Point possiblePosition : player.legalMoves(true)) {
            player.setPosition(possiblePosition);
            drainableOneMoveAway.addAll(player.drainablePositions());
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
     * Berechnet ein beliebiges Tile im gegebenen Zustands
     *
     * @param state der Zustand
     * @return ein beliebiges Tile im gegebenen Zustand
     */
    public MapTile anyTile(MapTileState state) {
        for (MapTile[] tileRow : getCurrentAction().getTiles()) {
            for (MapTile tile : tileRow) {
                if (tile.getState() == state) {
                    return tile;
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
        this.activePlayerSupplier = player;
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
     * Fordert einen Tipp in textueller Befehlsform von der KI an.
     *
     * @param player der supplier für den für die KI aktiven Spieler,
     *               beinhaltet den Spieler, welche die Aktion durchführen soll
     * @return ein Tipp in textueller Befehlsform
     * @see AIActionTip
     */
    public String getTip(Supplier<Player> player) {
        this.activePlayerSupplier = player;
        return processor.getTip(this);
    }

    /**
     * Fordert einen Tipp in textueller Befehlsform von der KI an.
     * Der hierfür verwendete aktive Spieler ist der aktive Spieler im aktiven Zug {@link Action#getActivePlayer()}
     *
     * @see #getCurrentAction()
     */
    public String getTip() {
        return getTip(() -> getCurrentAction().getActivePlayer());
    }

    public boolean landingSiteIsFlooded() throws IllegalAccessException {
        Point landingSite = MapUtil.getPositionForTile(getCurrentAction().getTiles(), MapTileProperties.FOOLS_LANDING);
        if (landingSite == null)
            throw new IllegalAccessException(); // Unerreichbar auf nicht korrumpierter map, sollte vorher gecheckt worden sein

        return getCurrentAction().getTile(landingSite).getState() != MapTileState.DRY;
    }

}