package de.sopra.javagame.util.serialize.typeadapter;

import com.google.gson.*;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.util.cardstack.CardStack;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class FloodCardStackTypeAdapter implements JsonSerializer<CardStack<FloodCard>>, JsonDeserializer<CardStack<FloodCard>> {
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

    @Override
    public CardStack<FloodCard> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String stackJson = json.getAsJsonObject().getAsJsonPrimitive("drawStack").getAsString();
        String discardPileJson = json.getAsJsonObject().getAsJsonPrimitive("discardPile").getAsString();

        CardStack<FloodCard> cardStack = new CardStack<>();

        Stack<FloodCard> drawStack = Arrays.stream(stackJson.split(","))
                .filter(string -> !string.isEmpty())
                .map(Integer::parseInt)
                .map(MapTileProperties::getByIndex)
                .map(FloodCard::new)
                .collect(Collectors.toCollection(Stack::new));

        List<FloodCard> discardPile = Arrays.stream(discardPileJson.split(","))
                .filter(string -> !string.isEmpty())
                .map(Integer::parseInt)
                .map(MapTileProperties::getByIndex)
                .map(FloodCard::new)
                .collect(Collectors.toList());


        try {
            Class<CardStack> cardStackClass = CardStack.class;
            Field stackField = cardStackClass.getDeclaredField("drawStack");
            Field discardPileField = cardStackClass.getDeclaredField("discardPile");

            stackField.setAccessible(true);
            stackField.set(cardStack, drawStack);
            stackField.setAccessible(false);

            discardPileField.setAccessible(true);
            discardPileField.set(cardStack, discardPile);
            discardPileField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        return cardStack;
    }
}
