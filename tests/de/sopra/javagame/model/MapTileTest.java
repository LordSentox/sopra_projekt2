package de.sopra.javagame.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class MapTileTest {
    MapTile dry;
    MapTile flooded;
    MapTile gone;

    @Before
    public void setUp() {
        this.dry = MapTile.fromNumber(2);
        this.dry.setState(MapTileState.DRY);

        this.flooded = MapTile.fromNumber(1);
        this.flooded.setState(MapTileState.FLOODED);

        this.gone = MapTile.fromNumber(3);
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
        assertEquals(dryCopy.getProperties().getName(), this.dry.getProperties().getName());
        assertSame(dryCopy.getProperties(), this.dry.getProperties());
        dryCopy.flood(); //change copy
        assertNotEquals(dryCopy.getState(), this.dry.getState()); //check independent instances
    }

}