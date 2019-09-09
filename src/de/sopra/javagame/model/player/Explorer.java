package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Point;

import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * Explorer implementiert die Spielfigur "Forscher".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Explorer extends Player {

    public Explorer(String name, Point position, Turn turn) {
        super(PlayerType.EXPLORER, name, turn);
        this.position = position;
        this.isAI = false;
    }

    public Explorer(String name, Point position, Turn turn, boolean isAI) {
        super(PlayerType.EXPLORER, name, turn);
        this.position = position;
        this.isAI = isAI;
    }

    /**
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der Spieler sich regelkonform hinbewegen darf.
     * Der Forscher darf auch diagonal laufen.
     *
     * @param specialActive gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja, wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */
    @Override
    public List<Point> legalMoves(boolean specialActive) {
        List<Point> moves = super.legalMoves(false);
        if (!specialActive)
            return moves;

        // Die Spezialbewegungen des Explorers sind aktiviert,
        MapTile topLeft = this.turn.getTiles()[position.y - 1][position.x - 1];
        if (topLeft != null && topLeft.getState() != GONE) {
            moves.add(new Point(position.y - 1, position.x - 1));
        }
        MapTile topRight = this.turn.getTiles()[position.y - 1][position.x + 1];
        if (topRight != null && topRight.getState() != GONE) {
            moves.add(new Point(position.y - 1, position.x + 1));
        }
        MapTile bottomLeft = this.turn.getTiles()[position.y + 1][position.x - 1];
        if (bottomLeft != null && bottomLeft.getState() != GONE) {
            moves.add(new Point(position.y + 1, position.x - 1));
        }
        MapTile bottomRight = this.turn.getTiles()[position.y + 1][position.x + 1];
        if (bottomRight != null && bottomRight.getState() != GONE) {
            moves.add(new Point(position.y + 1, position.x + 1));
        }

        return moves;
    }

    /**
     * drainablePositions gibt ein Listli von Koordinaten-Punkten zurück, deren {@link MapTile} trockengelegt werden können.
     * Der Forscher darf Felder auch diagonal trocken legen.
     *
     * @return Listli
     */
    public List<Point> drainablePositions() {
        // Alle Positionen, zu denen sich der Forscher bewegen darf, darf er auch trockenlegen
        List<Point> drainable = this.legalMoves(true);

        // Das Feld unter sich darf er ebenfalls trockenlegen
        drainable.add(this.position);

        // Entferne alle Positionen, wo die Map eigentlich keine Felder hat, oder sie nicht mehr trockengelegt werden
        // können
        // FIXME: Das wird bereits bei legalMoves getestet. Wie ist es besser?
        drainable = drainable.stream().filter(point -> this.turn.getTile(point) != null && this.turn.getTile(point).getState() != GONE).collect(Collectors.toList());

        return drainable;
    }

    @Override
    public Player copy() {
        Player player = new Explorer(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
