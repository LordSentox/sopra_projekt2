package de.sopra.javagame.util.serialize.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.util.CardStack;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Stack;
import java.util.stream.Collectors;

public class FloodCardStackSerializer implements JsonSerializer<CardStack<FloodCard>> {
    @Override
    public JsonElement serialize(CardStack<FloodCard> src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            Field stackField = CardStack.class.getDeclaredField("drawStack");

            stackField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Stack<FloodCard> cards = (Stack<FloodCard>) stackField.get(src);
            stackField.setAccessible(false);

            JsonObject json = new JsonObject();
            json.addProperty("drawStack", cards.stream().map(FloodCard::getTile).map(MapTileProperties::getIndex).map(String::valueOf).collect(Collectors.joining(",")));
            json.addProperty("discardPile", src.getDiscardPile().stream().map(FloodCard::getTile).map(MapTileProperties::getIndex).map(String::valueOf).collect(Collectors.joining(",")));
            return json;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
