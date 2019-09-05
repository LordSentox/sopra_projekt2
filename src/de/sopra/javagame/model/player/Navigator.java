package de.sopra.javagame.model.player;

/**
 *
 * Navigator implementiert die gleichnamige Spielfigur.
 * @author Georg Bühmann, Melanie Arnds
 *
 */
public class Navigator extends Player {
	/**
	 * er kann zwei spieler (auch zweimal den gleichen) pro aktion pushen
	 */
    private boolean hasExtraPush;
    /**
     * Der Navigator kann auch andere Spieler bewegen.
     */
    public boolean canMoveOthers() {
        return true;
    }

}
