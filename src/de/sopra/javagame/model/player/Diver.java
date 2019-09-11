package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.util.CopyUtil;
import de.sopra.javagame.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static de.sopra.javagame.model.MapTileState.DRY;

/**
 * Diver implementiert die Spielfigur "Taucher".
 *
 * @author Max Bühmann, Melanie Arnds
 */
public class Diver extends Player {

    public Diver(String name, Point position, Action action) {
        super(PlayerType.DIVER, name, action);
        this.position = position;
        this.isAI = false;
    }

    public Diver(String name, Point position, Action action, boolean isAI) {
        super(PlayerType.DIVER, name, action);
        this.position = position;
        this.isAI = isAI;
    }

    /**
     * legalMoves übergibt ein Listli von Koordinaten-Punkten, die der Taucher regelkonform erreichen kann.
     *
     * @return ein Listli mit legal erreichbaren {@link MapTile}
     */
    @Override
    public List<Point> legalMoves(boolean specialActive) {
        if (!specialActive) {
            return super.legalMoves(false);
        }

        boolean[][] reachable = this.reachableDestinations();

        // Gebe alle Positionen zurück, welche der Taucher erreichen kann, aber filtere alle heraus, welche überflutet
        // sind, oder kein Inselfeld sind.
        List<Point> legalMoves = new ArrayList<>();
        for (int y = 0; y < reachable.length; ++y) {
            for (int x = 0; x < reachable[y].length; ++x) {
                if (reachable[y][x] && this.action.getTile(x, y) != null && this.action.getTile(x, y).getState() == DRY) {
                    legalMoves.add(new Point(x, y));
                }
            }
        }

        return legalMoves;
    }

    /**
     * Findet alle Positionen, die der Taucher von seiner jetzigen Position aus erreichen kann. An diesen wird er aber
     * nicht unbedingt verweilen können, da sie auch untergegangen sein können, was bedeutet, dass er nicht auf einem
     * Tile stehen bleiben kann.
     *
     * @return Zweidimensionales Array, was über die Karte des Turns gelegt die vom Taucher erreichbaren Positionen anzeigt
     */
    private boolean[][] reachableDestinations() {
        boolean[][] reachable = new boolean[this.action.getTiles().length][12];
        // Initialisieren der Wasserwege, sofern es welche gibt
        this.setTrueAroundWithTargetPremise(reachable, this.position, tile -> tile != null && tile.getState() != DRY);
        // Suche dynamisch heraus, welche Positionen der Taucher alles erreichen kann. Dies beinhaltet allerdings
        // insbesondere alle Positionen auf untergegangenen Inselteilen, auf denen er nicht enden darf.
        boolean somethingChanged;
        do {
            somethingChanged = false;
            for (int y = 0; y < reachable.length; ++y) {
                for (int x = 0; x < reachable[y].length; ++x) {
                    if (reachable[y][x] && this.action.getTile(x, y) != null && this.action.getTile(x, y).getState() != DRY) {
                        somethingChanged |= this.setTrueAround(reachable, new Point(x, y));
                    }
                }
            }
        } while (somethingChanged);

        return reachable;
    }

    /**
     * Helferfunktion um in reachable um around die Werte oben, links, unten und rechts auf true zu setzen, falls die
     * Bedingung im Zielfeld gegeben ist.
     *
     * @param reachable Der reachable-Array, der gesetzt werden soll
     * @param around Der Startpunkt im reachable-Array, um den herum gesetzt werden soll
     * @param premise Die Prämisse, die auf dem Zielfeld erfüllt sein muss.
     */
    private void setTrueAroundWithTargetPremise(boolean[][] reachable, Point around, Function<MapTile, Boolean> premise) {
        MapTile upper = this.action.getTile(around.xPos, around.yPos - 1);
        MapTile left = this.action.getTile(around.xPos - 1, around.yPos);
        MapTile down = this.action.getTile(around.xPos, around.yPos + 1);
        MapTile right = this.action.getTile(around.xPos + 1, around.yPos);
        if (premise.apply(upper)) {
            reachable[around.yPos - 1][around.xPos] = true;
        }
        if (left != null && left.getState() != DRY) {
            reachable[around.yPos][around.xPos - 1] = true;
        }
        if (down != null && down.getState() != DRY) {
            reachable[around.yPos + 1][around.xPos] = true;
        }
        if (right != null && right.getState() != DRY) {
            reachable[around.yPos][around.xPos + 1] = true;
        }
    }

    /**
     * Helferfunktion um in reachable um around die Werte oben, links, unten und rechts auf true zu setzen. Gibt zurück,
     * ob sich etwas geändert hat
     *
     * @param reachable Der reachable-Array, der gesetzt werden soll
     * @param around Der Startpunkt im reachable-Array, um den herum gesetzt werden soll
     * @return True, wenn mindestens eines der Felder umgesetzt wurde, sonst false
     */
    private boolean setTrueAround(boolean[][] reachable, Point around) {
        boolean somethingChanged = false;
        if (!reachable[around.yPos - 1][around.xPos]) {
            reachable[around.yPos - 1][around.xPos] = true;
            somethingChanged = true;
        }
        if (!reachable[around.yPos][around.xPos - 1]) {
            reachable[around.yPos][around.xPos - 1] = true;
            somethingChanged = true;
        }
        if (!reachable[around.yPos + 1][around.xPos]) {
            reachable[around.yPos + 1][around.xPos] = true;
            somethingChanged = true;
        }
        if (!reachable[around.yPos][around.xPos + 1]) {
            reachable[around.yPos][around.xPos + 1] = true;
            somethingChanged = true;
        }

        return somethingChanged;
    }

    @Override
    public Player copy() {
        Player player = new Diver(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
