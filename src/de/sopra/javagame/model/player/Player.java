package de.sopra.javagame.model.player;

import de.sopra.javagame.model.*;
import de.sopra.javagame.util.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Bühmann, Melanie Arnds
 * Player beschreibt die Basisfunktionen, die jede Spielfigur ausführen kann.
 */

public abstract class Player implements Copyable<Player> {
    
    protected final PlayerType type;
    
    protected final String name;
    
    protected final Turn turn;
    
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
     * legalMoves erstellt eine Liste an Koordinaten Punkten, zu welchen der Spieler sich regelkonform hinbewegen darf
     *
     * @param specialActive gibt an, ob eine Spezialfähigkeit aktiviert wurde, wenn ja, wird die Liste um zusätzlich erreichbare Punkte erweitert
     * @return das erstellte Listli
     */
    public List<Point> legalMoves(boolean specialActive) {
        return null;
    }

    /**
     * move bewegt den Spieler zur angegebenen destination, zieht dabei eine Aktion ab, wenn costsAction true ist
     *
     * @param destination Zielkoordinaten
     * @param costsAction wenn false, wird keine Action abgezogen, wenn true, wird eine abgezogen
     * @return false, wenn es einen Fehler gab, true, sonst
     */
    public boolean move(Point destination, boolean costsAction) {
        return false;
    }

    /**
     * canMoveOthers gibt an, ob der Spieler andere bewegen kann. Spezialfähigkeit des {@link Navigator}
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
     * drainablePositions gibt ein Listli von Koordinaten-Punkten zurück, deren {@link MapTile} trockengelegt werden können.
     *
     * @return Listli
     */

    public List<Point> drainablePositions() {
        return null;
    }

    /**
     * drain wandelt den State des {@link MapTile} in DRY um. {@link MapTileState}
     *
     * @param position Koordinate des zu verändernden MapTiles
     * @return false, wenn Fehler eingetroffen, true sonst
     */
    public boolean drain(Point position) {
        return false;
    }

    /**
     * collectArtifact prüft, ob und auf welchem Typ eines {@link MapTile} der Spieler steht.
     * Es wird geprüft, ob der Spieler mindestens vier zum Typ passende Karten auf der Hand hat.
     * Wenn ja, werden vier passende Karten abgeworfen.
     *
     * @return den betroffenen ArtefaktTypen, wenn ein Artefakt collected wurde, none, sonst
     */

    public ArtifactType collectArtifact() {
        return null;
    }

    /**
     * legalReceivers legt ein Listli von Player an, denen Handkarten regelkonform übergeben werden dürfen.
     *
     * @return das erstellte Listli, wenn Player exisitieren, denen Handkarten übergeben werden dürfen. Null, sonst.
     */
    public List<Player> legalReceivers() {
        return null;
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
}
