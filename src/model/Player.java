package model.model;

import java.util.Collection;
import java.util.List;

public class Player {

	private String name;

	private Point position;

	private int actionsLeft;

	private boolean isAI;

	private PlayerType type;

	private Turn turn;

	private Collection<ArtifactCard> hand;

	List legalMoves(boolean specialActive) {
		return null;
	}

	boolean move(Point destination, boolean costsAction) {
		return false;
	}

	boolean canMoveOthers() {
		return false;
	}

	List drainablePositions() {
		return null;
	}

	boolean drain(Point position) {
		return false;
	}

	ArtifactCardType collectArtifact() {
		return null;
	}

	List legalReceivers() {
		return null;
	}

}
