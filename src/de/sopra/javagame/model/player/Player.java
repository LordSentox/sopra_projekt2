package de.sopra.javagame.model.player;

import de.sopra.javagame.model.*;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.MapTileState.DRY;
import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * @author Max Bühmann, Melanie Arnds
 * Player beschreibt die Basisfunktionen, die jede Spielfigur ausführen kann.
 */

public abstract class Player implements Copyable<Player> {

    public static final int MAXIMUM_HANDCARDS = 5;

    protected final PlayerType type;

    protected final String name;

    protected Action action;

    protected Point position;

    protected int actionsLeft;

    protected boolean isAI;

    protected List<ArtifactCard> hand;

    Player(PlayerType type, String name, Action action) {
        this.type = type;
        this.name = name;
        this.action = action;
        this.actionsLeft = 0;
        this.hand = new ArrayList<>();
    }

    public void onTurnStarted() {}

    /**
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der
     * Spieler sich regelkonform hinbewegen darf
     *
     * @param specialActive gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja,
     *                      wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */

    public List<Point> legalMoves(boolean specialActive) {
        List<Point> moves = this.position.getNeighbours();
        List <Point> legalTiles = new ArrayList<>();
        for (Point currentPoint : moves) {
            MapTile tile = this.action.getTile(currentPoint);
            if (tile != null && tile.getState() != GONE) {
                legalTiles.add(currentPoint);
            }

        }
        moves = moves.stream().filter(point -> {
            MapTile tile = this.action.getTile(point);
            //System.out.println(tile.getProperties().getName());
            return tile != null && tile.getState() != GONE;
        }).collect(Collectors.toList());

        return moves;
    }

    /**
     * move bewegt den Spieler zur angegebenen destination, zieht dabei eine
     * Aktion ab, wenn costsAction true ist
     *
     * @param destination Zielkoordinaten
     * @param costsAction wenn false, wird keine Action abgezogen, wenn true, wird eine
     *                    abgezogen
     * @return false, wenn es einen Fehler gab, true, sonst
     */
    public boolean move(Point destination, boolean costsAction, boolean specialActive) {
        List<Point> legalMovement = legalMoves(specialActive);
        if ((costsAction && actionsLeft < 1) || !legalMovement.contains(destination)) {
            return false;
        } else {
            this.setPosition(destination);
           
            if (costsAction) {
                actionsLeft -= 1;
            }
            return true;
        }
    }

    /**
     * canMoveOthers gibt an, ob der Spieler andere bewegen kann.
     * Spezialfähigkeit des {@link Navigator}
     *
     * @return false, wenn Spieler andere nicht bewegen kann, true, sonst.
     */
    public boolean canMoveOthers() {
        return false;
    }

    public boolean forcePush(Direction direction, Player other) {
        return false;
    }

    /**
     * drainablePositions gibt ein Listli von Koordinaten-Punkten zurück, deren
     * {@link MapTile} trockengelegt werden können.
     *
     * @return Listli
     */
    public List<Point> drainablePositions() {
        // Alle Felder, zu denen sich der Spieler auf normalem Wege hinbewegen darf
        List<Point> drainable = this.legalMoves(false);

        // Das Feld unter sich darf er ebenfalls trockenlegen
        drainable.add(this.position);

        // Entferne alle Positionen, wo die Map eigentlich keine Felder hat, oder sie nicht mehr trockengelegt werden
        // können
        // FIXME: Das wird bereits bei legalMoves getestet. Wie ist es besser?
        drainable = drainable.stream().filter(point ->
                this.action.getTile(point) != null &&
                        this.action.getTile(point).getState() != GONE &&
                        this.action.getTile(point).getState() != DRY).collect(Collectors.toList());

        return drainable;
    }

    /**
     * drain wandelt den State des {@link MapTile} in DRY um.
     * {@link MapTileState}
     *
     * @param position Koordinate des zu verändernden MapTiles
     * @return false, wenn Fehler eingetroffen, true sonst
     */
    public boolean drain(Point position) {
        // Kann man sie trockenlegen und sind noch ausreichend Aktionen vorhanden?
        if (!this.drainablePositions().contains(position) || this.actionsLeft < 1) {
            return false;
        }

        MapTile toDrain = this.action.getTile(position);

        if (!this.drainablePositions().contains(position) || this.actionsLeft < 1) {
            return false;
        }
        
        // Muss überhaupt noch etwas getan werden?
        if (toDrain.getState() == DRY) {
            return false;
        } else {
            toDrain.drain();
            --actionsLeft;
            return true;
        }
    }

    /**
     * collectArtifact prüft, ob und auf welchem Typ eines {@link MapTile} der Spieler steht.
     * Es wird geprüft, ob der Spieler mindestens vier zum Typ passende Karten auf der Hand hat.
     * Wenn ja, werden vier passende Karten abgeworfen.
     *
     * @return den betroffenen ArtefaktTypen, wenn ein Artefakt collected wurde, none, sonst
     */
    public ArtifactType collectArtifact() {
        MapTile mapTile = this.action.getTile(this.position);
        ArtifactType hiddenArtifact = mapTile.getProperties().getHidden();

        // Abbrechen, falls hier gar kein Artefakt versteckt ist.
        if (hiddenArtifact == ArtifactType.NONE) {
            return ArtifactType.NONE;
        }

        // Finde wenn möglich vier passende Artefaktkarten, die man gegen das Artefakt eintauschen kann.
        final int cardsNeeded = 4;
        List<ArtifactCard> correspondingHandCards = new ArrayList<>();
        for (ArtifactCard card : hand) {
            if (card.getType().toArtifactType() == hiddenArtifact && correspondingHandCards.size() < cardsNeeded) {
                correspondingHandCards.add(card);
            }
        }

        // Überprüfe, ob das Artefakt geborgen werden kann
        if (correspondingHandCards.size() != cardsNeeded) {
            return ArtifactType.NONE;
        } else {
            // Lege die vier Karten auf den Ablagestapel
            this.hand.removeAll(correspondingHandCards);
            this.action.getArtifactCardStack().discard(correspondingHandCards);
            this.action.getDiscoveredArtifacts().add(hiddenArtifact);

            return hiddenArtifact;
        }
    }

    /**
     * legalReceivers legt ein Listli von Player an, denen Handkarten
     * regelkonform übergeben werden dürfen.
     *
     * @return das erstellte Listli, wenn Player exisitieren, denen Handkarten
     * übergeben werden dürfen. Null, sonst.
     */
    public List<PlayerType> legalReceivers() {
        List<PlayerType> receivers = new ArrayList<>();
        MapTile mapTile = this.action.getTiles()[position.yPos][position.xPos];
        List<Player> players = action.getPlayers();
        for (Player player : players) {
            if (mapTile == this.action.getTiles()[player.position.yPos][player.position.xPos] && player != this) {
                receivers.add(player.getType());
            }
        }
        return receivers;
    }

    public void setPosition(Point position) {
        this.position.setLocation(position);
    }

    public void setActionsLeft(int actionsLeft) {
        this.actionsLeft = actionsLeft;
    }

    public int getActionsLeft() {
        return actionsLeft;
    }

    public List<ArtifactCard> getHand() {
        return hand;
    }

    public PlayerType getType() {
        return type;
    }

    public Point getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
