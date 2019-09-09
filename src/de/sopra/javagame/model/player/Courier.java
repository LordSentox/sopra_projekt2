package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Turn;
import de.sopra.javagame.util.CopyUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Courier implementiert die Spielfigur "Bote".
 *
 * @author Max Bühmann, Melanie Arnds
 */
public class Courier extends Player {

    public Courier(String name, Point position, Turn turn) {
        super(PlayerType.COURIER, name, turn);
        this.position = position;
        this.isAI = false;
    }

    public Courier(String name, Point position, Turn turn, boolean isAI) {
        super(PlayerType.COURIER, name, turn);
        this.position = position;
        this.isAI = isAI;
    }

    /**
     * legalReceivers gibt eine Liste aller anderen Spieler zurück.
     * (Der Bote darf allen anderen Spielern etwas übergeben, egal, wo sie sich befinden.)
     *
     * @return das Listli aller Spieler außer dem Boten selbst.
     */
    @Override
    public List<PlayerType> legalReceivers() {
        List<PlayerType> receivers = new ArrayList<>();
        for(Player currentPlayer : turn.getPlayers()) {
            if (currentPlayer.getType() != this.getType()) {
                receivers.add(currentPlayer.getType());
            }
        }
        return receivers;
    }

    @Override
    public Player copy() {
        Player player = new Courier(CopyUtil.copy(this.name), new Point(position), null);
        player.hand = this.hand;
        player.actionsLeft = this.actionsLeft;
        player.isAI = this.isAI;
        return player;
    }
}
