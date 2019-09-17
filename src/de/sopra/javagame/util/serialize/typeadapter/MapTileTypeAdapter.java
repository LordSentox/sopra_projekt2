package de.sopra.javagame.util.serialize.typeadapter;

import com.google.gson.*;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileProperties;

import java.lang.reflect.Type;

public class MapTileTypeAdapter implements JsonSerializer<MapTile>, JsonDeserializer<MapTile> {
    @Override
    public JsonElement serialize(MapTile src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTileIndex() + "," + src.getState().ordinal());
    }

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
