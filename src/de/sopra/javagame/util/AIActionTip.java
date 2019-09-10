package de.sopra.javagame.util;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.PlayerType;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public enum AIActionTip {

    MOVE("MOVE %direction"),
    DRAIN("DRAIN %direction"),
    COLLECT("COLLECT %cardtype"),
    TRADE("TRADE %cardtype %who"),
    DISCARD("DISCARD %cardtype"),
    SPECIALCARD("PLAY %cardtype"),
    SPECIALMOVE("SPECIAL %point %direction %cardtype %who");

    /*
     * Syntax for Specialmove:
     * Pilot: SPECIAL %point
     * Diver: SPECIAL %point
     * Engineer: SPECIAL %direction
     * Navigator: SPECIAL %direction %who
     * Courier: SPECIAL %cardtype %who
     * Explorer: SPECIAL %point
     */

    final String DIRECTION = "%direction";
    final String POINT = "%point";
    final String CARDTYPE = "%cardtype";
    final String WHO = "%who";

    private String syntax;

    AIActionTip(String syntax) {
        this.syntax = syntax;
    }

    /**
     * Baut einen String aus den gegebenen Attributen.
     * Alle Werte auf <code>null</code> werden als "nicht angegeben" betrachtet.
     *
     * @param direction der Richtungs-Parameter
     * @param point     der Ziel-Punkt
     * @param player    der Ziel-Spieler
     * @param card      die Karte
     * @return ein String der einen Befehl/Tip repräsentiert
     */
    public String build(Direction direction, Point point, PlayerType player, ArtifactCardType card) {
        String result = syntax;
        if (direction != null)
            result = result.replace(DIRECTION, DIRECTION + ":" + direction.name());
        else result = result.replace(DIRECTION, "");
        if (point != null)
            result = result.replace(POINT, POINT + ":" + point.xPos + "," + point.yPos);
        else result = result.replace(POINT, "");
        if (player != PlayerType.NONE && player != null)
            result = result.replace(WHO, WHO + ":" + player.name());
        else result = result.replace(WHO, "");
        if (card != null)
            result = result.replace(CARDTYPE, CARDTYPE + ":" + card.name());
        else result = result.replace(CARDTYPE, "");
        return result;
    }

}