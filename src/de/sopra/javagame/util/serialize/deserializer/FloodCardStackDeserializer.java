package de.sopra.javagame.util.serialize.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTileProperties;
import de.sopra.javagame.util.CardStack;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class FloodCardStackDeserializer implements JsonDeserializer<CardStack<FloodCard>> {
    @Override
    public CardStack<FloodCard> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String stackJson = json.getAsJsonObject().getAsJsonPrimitive("drawStack").getAsString();
        String discardPileJson = json.getAsJsonObject().getAsJsonPrimitive("discardPile").getAsString();

        CardStack<FloodCard> cardStack = new CardStack<>();

        Stack<FloodCard> drawStack = Arrays.stream(stackJson.split(","))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .map(MapTileProperties::getByIndex)
                .map(FloodCard::new)
                .collect(Collectors.toCollection(Stack::new));

        List<FloodCard> discardPile = Arrays.stream(discardPileJson.split(","))
                .filter(s -> !s.isEmpty())
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
