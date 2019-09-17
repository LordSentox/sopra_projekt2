package de.sopra.javagame.util.serialize.deserializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.CardStack;

import java.lang.reflect.Type;

public class ActionDeserializer implements JsonDeserializer<Action> {
    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Action action = new GsonBuilder()
                .registerTypeAdapter(Player.class, new PlayerDeserializer())
                .registerTypeAdapter(new TypeToken<CardStack<FloodCard>>(){}.getType(), new FloodCardStackDeserializer())
                .registerTypeAdapter(new TypeToken<CardStack<ArtifactCard>>(){}.getType(), new ArtifactCardStackDeserializer())
                .registerTypeAdapter(MapTile.class, new MapTileDeserializer())
                .create().fromJson(json, Action.class);
        action.getPlayers().forEach(player -> player.setAction(action));
        return action;
    }
}
