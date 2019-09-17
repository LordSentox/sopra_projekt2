package de.sopra.javagame.util.serialize.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.sopra.javagame.model.MapTile;

import java.lang.reflect.Type;

public class MapTileSerializer implements JsonSerializer<MapTile> {
    @Override
    public JsonElement serialize(MapTile src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTileIndex() + "," + src.getState().ordinal());
    }
}
