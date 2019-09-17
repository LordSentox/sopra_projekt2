package de.sopra.javagame.util.serialize.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.util.CardStack;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ArtifactCardStackDeserializer implements JsonDeserializer<CardStack<ArtifactCard>> {
    @Override
    public CardStack<ArtifactCard> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String stackJson = json.getAsJsonObject().getAsJsonPrimitive("drawStack").getAsString();
        String discardPileJson = json.getAsJsonObject().getAsJsonPrimitive("discardPile").getAsString();


        CardStack<ArtifactCard> cardStack = new CardStack<>();

        Stack<ArtifactCard> drawStack = Arrays.stream(stackJson.split(","))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .map(i -> ArtifactCardType.values()[i])
                .map(ArtifactCard::new)
                .collect(Collectors.toCollection(Stack::new));

        List<ArtifactCard> discardPile = Arrays.stream(discardPileJson.split(","))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .map(i -> ArtifactCardType.values()[i])
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
