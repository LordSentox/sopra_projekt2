package de.sopra.javagame.util.serialize.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.util.CardStack;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Stack;
import java.util.stream.Collectors;

public class ArtifactCardStackSerializer implements JsonSerializer<CardStack<ArtifactCard>> {
    @Override
    public JsonElement serialize(CardStack<ArtifactCard> src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            Field stackField = CardStack.class.getDeclaredField("drawStack");
            stackField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Stack<ArtifactCard> cards = (Stack<ArtifactCard>) stackField.get(src);
            stackField.setAccessible(false);

            JsonObject json = new JsonObject();
            json.addProperty("drawStack", cards.stream().map(ArtifactCard::getType).map(ArtifactCardType::ordinal).map(String::valueOf).collect(Collectors.joining(",")));
            json.addProperty("discardPile", src.getDiscardPile().stream().map(ArtifactCard::getType).map(ArtifactCardType::ordinal).map(String::valueOf).collect(Collectors.joining(",")));
            return json;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
