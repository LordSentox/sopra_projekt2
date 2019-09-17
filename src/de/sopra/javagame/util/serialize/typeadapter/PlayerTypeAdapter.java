package de.sopra.javagame.util.serialize.typeadapter;

import com.google.gson.*;
import de.sopra.javagame.model.player.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PlayerTypeAdapter implements JsonDeserializer<Player> {

    private static final Map<String, Class<? extends Player>> PLAYER_TYPES = new HashMap<>();

    static {
        PLAYER_TYPES.put("COURIER", Courier.class);
        PLAYER_TYPES.put("DIVER", Diver.class);
        PLAYER_TYPES.put("ENGINEER", Engineer.class);
        PLAYER_TYPES.put("EXPLORER", Explorer.class);
        PLAYER_TYPES.put("NAVIGATOR", Navigator.class);
        PLAYER_TYPES.put("PILOT", Pilot.class);
    }

    @Override
    public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Class<? extends Player> playerClass = PLAYER_TYPES.get(json.getAsJsonObject().get("type").getAsString());
        return new Gson().fromJson(json, playerClass);
    }
}
