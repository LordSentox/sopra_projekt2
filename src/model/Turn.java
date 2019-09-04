package model.model;

import java.util.Collection;

public class Turn {

	private boolean[] discoveredArtifacts;

	private String description;

	private int activePlayer;

	private MapTile tiles[12][12];

	private WaterLevel waterLevel;

	private Collection<CardStack> cardStack;

	private Collection<Player> players;

	private TurnState state;

	boolean forcePush(Direction direction, Player caster, Player other) {
		return false;
	}

	boolean transferArtifactCard(ArtifactCard card, Player source, Player receiver) {
		return false;
	}

}
