package de.sopra.javagame.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MapTileTest {
    MapTile dry;
    MapTile flooded;
    MapTile gone;

    @Before
    public void setUp() {
        this.dry = new MapTile();
        this.dry.setState(MapTileState.DRY);

        this.flooded = new MapTile();
        this.flooded.setState(MapTileState.FLOODED);

        this.gone = new MapTile();
        this.gone.setState(MapTileState.GONE);
    }

    @Test
    public void drain() {
        this.dry.drain();
        Assert.assertEquals(MapTileState.DRY, this.dry.getState());

        this.flooded.drain();
        Assert.assertEquals(MapTileState.DRY, this.flooded.getState());

        this.gone.drain();
        Assert.assertEquals(MapTileState.GONE, this.gone.getState());
    }

    @Test
    public void flood() {
        this.dry.flood();
        Assert.assertEquals(MapTileState.FLOODED, this.dry.getState());

        this.flooded.flood();
        Assert.assertEquals(MapTileState.GONE, this.flooded.getState());

        this.gone.flood();
        Assert.assertEquals(MapTileState.GONE, this.gone.getState());
    }
}