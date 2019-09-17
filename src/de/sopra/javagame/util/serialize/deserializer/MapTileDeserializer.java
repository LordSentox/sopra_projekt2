package de.sopra.javagame.util.serialize.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;

import java.lang.reflect.Type;

public class MapTileDeserializer implements JsonDeserializer<MapTile> {
    @Override
    public MapTile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        MapTile tile = new MapTile((MapTileProperties.getByIndex(Integer.parseInt(json.getAsString().split(",")[0]))));
        int state = Integer.parseInt(json.getAsString().split(",")[1]);
        for (int i = 0; i < state; i++) {
            tile.flood();
        }
        return tile;
    }
}
