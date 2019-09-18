package de.sopra.javagame.view.customcontrol;

import de.sopra.javagame.model.player.PlayerType;

import java.util.HashSet;
import java.util.Set;

public class HelicopterHelper {
    private final MapPane mapPane;

    private final PlayerType caster;
    private final int cardIndex;

    private Set<PlayerType> toTransport;
    private MapPaneTile startTile;
    private MapPaneTile destinationTile;

    /**
     * Erstelle einen neuen Helikopterkartenspielhelfer. Er wird gesetzt, wenn eine Helikopterkarte gesetzt wird.
     *
     * @param caster Spieler, der die Helikopterkarte benutzt
     * @param cardIndex Die Handkarte des helikopterkartenspielenden Spielers
     */
    public HelicopterHelper(PlayerType caster, int cardIndex, MapPane mapPane) {
        this.mapPane = mapPane;
        this.caster = caster;
        this.cardIndex = cardIndex;
        this.toTransport = new HashSet<>();

        this.startTile = null;
        this.destinationTile = null;
    }

    public boolean addToTransport(PlayerType toTransport) {
        // Die Start-Tile wurde noch nicht festgelegt, also kann es über den ersten Spieler gesetzt werden
        if (this.toTransport.isEmpty()) {
            this.toTransport.add(toTransport);
            this.startTile = this.mapPane.playerPosition(toTransport).get();
            return true;
        }

        // Es wurden bereits Spieler ausgewählt. Wenn der Spieler auf der gleichen Tile steht, kann er
        // hinzugefügt werden
        if (this.mapPane.playerPosition(toTransport).get() == this.startTile) {
            this.toTransport.add(toTransport);
            return true;
        }

        // Der Spieler war auf einer anderen Tile und kann deshalb nicht hinzugefügt werden
        return false;
    }

    public void removeFromTransport(PlayerType stayingBehindPlayer) {
        this.toTransport.remove(stayingBehindPlayer);

        // Wenn keine Spieler mehr transportiert werden sollen, kann die Transportroute zurückgesetzt werden
        if (this.toTransport.isEmpty()) {
            this.startTile = null;
            this.destinationTile = null;
        }
    }

    public boolean readyToTransport() {
        return this.destinationTile != null && !this.toTransport.isEmpty();
    }

    public Set<PlayerType> getToTransportConst() {
        return this.toTransport;
    }

    public void setDestinationTile(MapPaneTile destinationTile) {
        this.destinationTile = destinationTile;
    }

    public  MapPaneTile getStartTile() {
        return this.startTile;
    }

    public MapPaneTile getDestinationTile() {
        return this.destinationTile;
    }

    public PlayerType getCaster() {
        return caster;
    }

    public int getCardIndex() {
        return cardIndex;
    }
}
