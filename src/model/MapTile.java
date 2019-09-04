package model.model;

public class MapTile {

	private String name;

	private PlayerType playerSpawn;

	private MapTileState state;

	private ArtifactType hiddenArtifact;

	void drain() {

	}

	boolean flood() {
		return false;
	}

}
