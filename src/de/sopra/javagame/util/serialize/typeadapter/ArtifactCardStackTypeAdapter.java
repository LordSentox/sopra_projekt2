package de.sopra.javagame.util.serialize.typeadapter;

import com.google.gson.*;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.util.CardStack;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class ArtifactCardStackTypeAdapter implements JsonSerializer<CardStack<ArtifactCard>>, JsonDeserializer<CardStack<ArtifactCard>> {
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

    @Override
    public CardStack<ArtifactCard> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String stackJson = json.getAsJsonObject().getAsJsonPrimitive("drawStack").getAsString();
        String discardPileJson = json.getAsJsonObject().getAsJsonPrimitive("discardPile").getAsString();

        CardStack<ArtifactCard> cardStack = new CardStack<>();

        Stack<ArtifactCard> drawStack = Arrays.stream(stackJson.split(","))
                .filter(string -> !string.isEmpty())
                .map(Integer::parseInt)
                .map(index -> ArtifactCardType.values()[index])
                .map(ArtifactCard::new)
                .collect(Collectors.toCollection(Stack::new));

        List<ArtifactCard> discardPile = Arrays.stream(discardPileJson.split(","))
                .filter(string -> !string.isEmpty())
                .map(Integer::parseInt)
                .map(index -> ArtifactCardType.values()[index])
                .map(ArtifactCard::new)
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
