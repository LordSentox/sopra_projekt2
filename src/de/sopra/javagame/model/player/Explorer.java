package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Point;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.MapTileState.DRY;
import static de.sopra.javagame.model.MapTileState.GONE;

/**
 * Explorer implementiert die Spielfigur "Forscher".
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Explorer extends Player {

    public Explorer(String name, Point position, Action action) {
        super(PlayerType.EXPLORER, name, action);
        this.position = position;
        this.isAI = false;
    }

    public Explorer(String name, Point position, Action action, boolean isAI) {
        super(PlayerType.EXPLORER, name, action);
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

        // Diagonale Bewegungen
        List<Point> additional = Arrays.asList(
                new Point(this.position.add(-1, -1)),
                new Point(this.position.add(-1, 1)),
                new Point(this.position.add(1, 1)),
                new Point(this.position.add(1, -1)));

        for (Point point : additional) {
            MapTile tile = this.action.getMap().get(point);
            if (tile != null && tile.getState() != GONE) {
                moves.add(point);
            }
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
        drainable = drainable.stream().filter(point ->
                this.action.getMap().get(point) != null &&
                        this.action.getMap().get(point).getState() != GONE &&
                        this.action.getMap().get(point).getState() != DRY).collect(Collectors.toList());

        return drainable;
    }

    @Override
    public Player copy() {
        Explorer player = new Explorer(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
