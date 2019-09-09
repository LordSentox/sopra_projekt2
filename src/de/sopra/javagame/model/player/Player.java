package de.sopra.javagame.model.player;

import de.sopra.javagame.model.*;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Bühmann, Melanie Arnds Player beschreibt die Basisfunktionen, die
 *         jede Spielfigur ausführen kann.
 */

public abstract class Player implements Copyable<Player> {

    protected final PlayerType type;

    protected final String name;

    protected Turn turn;

    protected Point position;

    protected int actionsLeft;

    protected boolean isAI;

    protected List<ArtifactCard> hand;

    Player(PlayerType type, String name, Turn turn) {
        this.type = type;
        this.name = name;
        this.turn = turn;
        this.actionsLeft = 0;
        this.hand = new ArrayList<>();
    }

    /**
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der
     * Spieler sich regelkonform hinbewegen darf
     *
     * @param specialActive
     *            gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja,
     *            wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */

    List<Point> legalMoves(boolean specialActive) {
        if (actionsLeft >= 1) {
            List<Point> movement = new ArrayList<>();
            MapTile right = this.turn.getTiles()[position.y][position.x + 1];
            if (right != null && right.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y, position.x + 1));
            }
            MapTile left = this.turn.getTiles()[position.y][position.x - 1];
            if (left != null && left.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y, position.x - 1));
            }
            MapTile up = this.turn.getTiles()[position.y - 1][position.x];
            if (up != null && up.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y - 1, position.x));
            }
            MapTile down = this.turn.getTiles()[position.y + 1][position.x];
            if (down != null && down.getState() != MapTileState.GONE) {
                movement.add(new Point(position.y + 1, position.x));
            }
            return movement;
        } else {

            return null;
        }
    }

    /**
     * move bewegt den Spieler zur angegebenen destination, zieht dabei eine
     * Aktion ab, wenn costsAction true ist
     *
     * @param destination
     *            Zielkoordinaten
     * @param costsAction
     *            wenn false, wird keine Action abgezogen, wenn true, wird eine
     *            abgezogen
     * @return false, wenn es einen Fehler gab, true, sonst
     */

    boolean move(Point destination, boolean costsAction, boolean specialActive) {
        List<Point> legelMovement = legalMoves(specialActive);
        if (actionsLeft < 1 || !legelMovement.contains(destination)) {
            return false;
        } else {
            position = destination;
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

    List<Point> drainablePositions() {
        if (actionsLeft >= 1) {
            List<Point> drainable = new ArrayList();
            MapTile right = this.turn.getTiles()[position.y][position.x + 1];
            if (right != null && right.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y, position.x + 1));
            }
            MapTile left = this.turn.getTiles()[position.y][position.x - 1];
            if (left != null && left.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y, position.x - 1));
            }
            MapTile up = this.turn.getTiles()[position.y - 1][position.x];
            if (up != null && up.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y - 1, position.x));
            }
            MapTile down = this.turn.getTiles()[position.y + 1][position.x];
            if (down != null && down.getState() != MapTileState.GONE) {
                drainable.add(new Point(position.y + 1, position.x));
            }
            return drainable;
        } else {
            return null;
        }
    }

    /**
     * drain wandelt den State des {@link MapTile} in DRY um.
     * {@link MapTileState}
     *
     * @param position
     *            Koordinate des zu verändernden MapTiles
     * @return false, wenn Fehler eingetroffen, true sonst
     */

    boolean drain(Point position) {
        MapTile mapTile = this.turn.getTiles()[position.y][position.x];
        if (mapTile.getState() == MapTileState.GONE || mapTile.getState() == MapTileState.DRY) {
            return false;
        } else {
            mapTile.drain();
            actionsLeft -= 1;
            return true;
        }
    }

    /**
     * collectArtifact prüft, ob und auf welchem Typ eines {@link MapTile} der
     * Spieler steht. Es wird geprüft, ob der Spieler mindestens vier zum Typ
     * passende Karten auf der Hand hat. Wenn ja, werden vier passende Karten
     * abgeworfen.
     *
     * @return den betroffenen ArtefaktTypen, wenn ein Artefakt collected wurde,
     *         none, sonst
     */

    ArtifactType collectArtifact() {
        MapTile mapTile = this.turn.getTiles()[position.y][position.x];
        ArtifactType hiddenArtifact = mapTile.getHiddenArtifact();
        int count = 0;
        for (ArtifactCard card : hand) {
            if (card.getType().toArtifactType() == hiddenArtifact) {
                count++;
            }

        }

        if (hiddenArtifact == ArtifactType.NONE || count < 4) {

            return ArtifactType.NONE;
        } else {
            return hiddenArtifact;
        }
    }

    /**
     * legalReceivers legt ein Listli von Player an, denen Handkarten
     * regelkonform übergeben werden dürfen.
     *
     * @return das erstellte Listli, wenn Player exisitieren, denen Handkarten
     *         übergeben werden dürfen. Null, sonst.
     */

    List<Player> legalReceivers() {
        List<Player> receivers = new ArrayList();
        MapTile mapTile = this.turn.getTiles()[position.y][position.x];
        List<Player> players = turn.getPlayers();
        for (Player player : players) {
            if (mapTile == this.turn.getTiles()[player.position.y][player.position.x] && player != this) {
                receivers.add(player);
            }
        }
        return receivers;
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

    public Turn getTurn() {
        return turn;
    }

    public void setActiveTurn(Turn turn) {
        this.turn = turn;
    }

}
