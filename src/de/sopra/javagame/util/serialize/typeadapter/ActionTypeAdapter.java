package de.sopra.javagame.util.serialize.typeadapter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.cardstack.CardStack;

import java.lang.reflect.Type;

public class ActionTypeAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {
    @Override
    public JsonElement serialize(Action src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonParser().parse(new GsonBuilder()
                .registerTypeAdapter(new TypeToken<CardStack<FloodCard>>() {}.getType(), new FloodCardStackTypeAdapter())
                .registerTypeAdapter(new TypeToken<CardStack<ArtifactCard>>() {}.getType(), new ArtifactCardStackTypeAdapter())
                .registerTypeAdapter(MapTile.class, new MapTileTypeAdapter())
                .create().toJson(src));
    }

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Action action = new GsonBuilder()
                .registerTypeAdapter(Player.class, new PlayerTypeAdapter())
                .registerTypeAdapter(new TypeToken<CardStack<FloodCard>>() {}.getType(), new FloodCardStackTypeAdapter())
                .registerTypeAdapter(new TypeToken<CardStack<ArtifactCard>>() {}.getType(), new ArtifactCardStackTypeAdapter())
                .registerTypeAdapter(MapTile.class, new MapTileTypeAdapter())
                .create().fromJson(json, Action.class);
        action.getPlayers().forEach(player -> player.setAction(action));
        return action;
    }
}
