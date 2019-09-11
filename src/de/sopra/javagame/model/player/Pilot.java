package de.sopra.javagame.model.player;

import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Pilot implementiert die gleichnamige Spielfigur.
 *
 * @author Georg Bühmann, Melanie Arnds
 */
public class Pilot extends Player {

    /**
     * Der Pilot kann seine Spezialfähigkeit nur einmal pro Zug nutzen, also
     * wird sie nach benutzen auf false gesetzt
     */
    private boolean hasSpecialMove;

    public Pilot(String name, Point position, Turn turn) {
        super(PlayerType.PILOT, name, turn);
        this.position = position;
        this.isAI = false;
        this.hasSpecialMove = false;
    }

    public Pilot(String name, Point position, Turn turn, boolean isAI) {
        super(PlayerType.PILOT, name, turn);
        this.position = position;
        this.isAI = isAI;
        this.hasSpecialMove = false;
    }

    public void onTurnStarted() {
        this.hasSpecialMove = true;
    }

    /**
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der
     * Spieler sich regelkonform hinbewegen darf. Wenn specialActive, dann
     * werden alle {@link MapTile} der Liste hinzugefügt, die nicht GONE sind
     *
     * @param specialActive
     *            gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja,
     *            wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */
    @Override
    public List<Point> legalMoves(boolean specialActive) {
        if (!specialActive)
            return super.legalMoves(specialActive);

        List<Point> movement = new ArrayList<>();
        MapTile[][] map = this.turn.getTiles();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y] != null && map[x][y].getState() != MapTileState.GONE) {
                    movement.add(new Point(x, y));
                }
            }
        }

        return movement;
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
    @Override
    public boolean move(Point destination, boolean costsAction, boolean specialActive) {
        if (!specialActive || !this.hasSpecialMove)
            return super.move(destination, costsAction, specialActive);

        List<Point> legelMovement = legalMoves(specialActive);
        if (actionsLeft < 1 || !legelMovement.contains(destination)) {
            return false;
        } else {
            position = destination;
            if (costsAction) {
                actionsLeft -= 1;
            }
            this.hasSpecialMove = false;

            return true;
        }
    }

    @Override
    public Player copy() {
        Player player = new Pilot(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
