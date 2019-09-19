package de.sopra.javagame.util.cardstack;

import de.sopra.javagame.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helferfunktionen für den Artefaktkartenstapel und den Flutkartenstapel
 */
public class CardStackUtil {

    /**
     * Erstellt für jedes MapTile in tiles eine Flutkarte, die diese überfluten kann, wenn sie gezogen wird.
     *
     * @param tiles Die Tiles, für die Karten gemacht werden sollen
     * @return Der ungemischte Kartenstapel, der die Flutkarten für {@param tiles} enthält.
     */
    public static CardStack<FloodCard> createFloodCardStack(MapTile[][] tiles) {
        List<FloodCard> cards = new LinkedList<>();
        for (MapTile[] row : tiles) {
            for (MapTile tile : row) {
                if (tile != null) {
                    cards.add(new FloodCard(tile.getProperties()));
                }
            }
        }
        return new CardStack<>(cards);
    }

    /**
     * Erstellt einen neuen Artefaktkartenstapel mit den Artefaktkarten, die im Regelwerk vorgesehen sind und gibt ihn zurück.
     *
     * @return Der neu generierte ungemischte Artefaktkartenstapel
     */
    public static CardStack<ArtifactCard> createArtifactCardStack() {
        List<ArtifactCard> cards = new LinkedList<>();
        for (ArtifactCardType type : ArtifactCardType.values()) {
            for (int i = 0; i < type.getCardsInDeck(); i++)
                cards.add(new ArtifactCard(type));
        }
        return new CardStack<>(cards);
    }

    /**
     * Lade die Karten in der im String vorgegebenen Reihenfolge in den Kartenstapel und gebe ihn zurück
     */
    public static CardStack<FloodCard> readFloodCardStackFromString(String toParse) {
        // Parse die Karten aus dem Flutstapel
        List<String> cardStrings = Arrays.stream(toParse.split(",")).map(String::trim).collect(Collectors.toList());

        // Überprüfe, ob es genau 24 Karten, für jedes Tile eines ist
        if (cardStrings.size() != MapTileProperties.values().length) {
            System.err.println("Korrumpierte Flutkartenstapeldatei. Sie konnte nicht gelesen werden");
            return null;
        }

        Collections.reverse(cardStrings);
        List<FloodCard> floodCards = cardStrings.stream().map(string -> new FloodCard(MapTileProperties.getByIndex(Integer.parseInt(string)%24))).collect(Collectors.toList());

        return new CardStack<>(floodCards);
    }


    /**
     * Lade die Karten in der im String vorgegebenen Reihenfolge in den Kartenstapel und gebe ihn zurück
     */
    public static CardStack<ArtifactCard> readArtifactCardStackFromString(String toParse) {
        // Parse die Karten aus dem Flutstapel
        List<String> cardStrings = Arrays.stream(toParse.split(",")).map(String::trim).collect(Collectors.toList());

        // Überprüfe, ob es genau 28 Karten, für jedes Tile eines ist
        int numArtifactCardsInStack = 28;
        if (cardStrings.size() != numArtifactCardsInStack) {
            System.err.println("Korrumpierte Artefaktkartenstapeldatei. Sie konnte nicht gelesen werden");
            return null;
        }

        Collections.reverse(cardStrings);
        List<ArtifactCard> artifactCards = cardStrings.stream().map(string -> new ArtifactCard(ArtifactCardType.getByIndex(Integer.parseInt(string)))).collect(Collectors.toList());

        return new CardStack<>(artifactCards);
    }
}
