package model.player;

import java.util.List;

public class Pilot extends Player {

	private boolean hasSpecialMove;

	public List legalMoves() {
		return null;
	}

	public boolean move(Point destination, boolean costsAction) {
		return false;
	}

}
