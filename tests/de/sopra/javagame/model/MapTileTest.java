package de.sopra.javagame.model;

import de.sopra.javagame.model.player.PlayerType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class MapTileTest {
    MapTile dry;
    MapTile flooded;
    MapTile gone;

    @Before
    public void setUp() {
        this.dry = new MapTile("Ne Höhle", PlayerType.DIVER, ArtifactType.NONE);
        this.dry.setState(MapTileState.DRY);

        this.flooded = new MapTile("Nen Strand", PlayerType.ENGINEER, ArtifactType.NONE);
        this.flooded.setState(MapTileState.FLOODED);

        this.gone = new MapTile("So'n Tempel", PlayerType.NONE, ArtifactType.EARTH);
        this.gone.setState(MapTileState.GONE);
    }

    @Test
    public void drain() {
        this.dry.drain();
        assertEquals(MapTileState.DRY, this.dry.getState());

        this.flooded.drain();
        assertEquals(MapTileState.DRY, this.flooded.getState());

        this.gone.drain();
        assertEquals(MapTileState.GONE, this.gone.getState());
    }

    @Test
    public void flood() {
        this.dry.flood();
        assertEquals(MapTileState.FLOODED, this.dry.getState());

        this.flooded.flood();
        assertEquals(MapTileState.GONE, this.flooded.getState());

        this.gone.flood();
        assertEquals(MapTileState.GONE, this.gone.getState());
    }

    @Test
    public void copyTest() {
        MapTile dryCopy = this.dry.copy();
        assertEquals(dryCopy.getState(), this.dry.getState()); //check copy
        assertEquals(dryCopy.getName(), this.dry.getName());
        assertFalse(dryCopy.getName() == this.dry.getName()); //check different instances
        assertEquals(dryCopy.getHiddenArtifact(), this.dry.getHiddenArtifact());
        assertEquals(dryCopy.getPlayerSpawn(), this.dry.getPlayerSpawn());
        dryCopy.flood(); //change copy
        assertNotEquals(dryCopy.getState(), this.dry.getState()); //check independent instances
    }

}