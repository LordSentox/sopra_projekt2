package de.sopra.javagame.model.player;

import java.util.List;

/**
 * Courier implementiert die Spielfigur "Bote".
 * @author Max Bühmann, Melanie Arnds
 *
 */
public class Courier extends Player {

	/**
	 * legalReceivers gibt eine Liste aller anderen Spieler zurück. 
	 * (Der Bote darf allen anderen Spielern etwas übergeben, egal, wo sie sich befinden.)
	 * @return das Listli aller Spieler außer dem Boten selbst.
	 */
	
	@Override
    public List<Player> legalReceivers() {
        return null;
    }

}
