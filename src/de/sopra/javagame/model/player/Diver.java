package de.sopra.javagame.model.player;

import java.awt.Point;
import java.util.List;

import de.sopra.javagame.model.MapTile;
/**
 * Diver implementiert die Spielfigur "Taucher".
 * @author Max Bühmann, Melanie Arnds
 *
 */
public class Diver extends Player {

	/**
	 * legalMoves übergibt ein Listli von Koordinaten-Punkten, die der Taucher regelkonform erreichen kann.
	 * @return ein Listli mit legal erreichbaren {@link MapTile}
	 */
	
	@Override
    public List<Point> legalMoves(boolean specialActive) {
        return null;
    }

}
