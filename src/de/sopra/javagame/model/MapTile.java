package de.sopra.javagame.model;

import de.sopra.javagame.model.player.PlayerType;

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
