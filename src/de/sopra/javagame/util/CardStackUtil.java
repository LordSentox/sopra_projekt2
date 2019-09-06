package de.sopra.javagame.util;

import de.sopra.javagame.model.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Helferfunktionen für den Artefaktkartenstapel und den Flutkartenstapel
 */
public class CardStackUtil {

    /**
     * Erstellt für jedes MapTile in tiles eine Flutkarte, die diese überfluten kann, wenn sie gezogen wird. Erstellt
     * damit einen gemischten Kartenstapel.
     *
     * @param tiles Die Tiles, für die Karten gemacht werden sollen
     * @return Der Kartenstapel, der die Flutkarten für {@param tiles} enthält.
     */
    public static CardStack<FloodCard> createFloodCardStack(MapTile[][] tiles) {
        List<FloodCard> cards = new LinkedList<>();
        for (MapTile[] row : tiles) {
            for (MapTile tile : row) {
                if (tile != null) {
                    cards.add(new FloodCard(tile));
                }
            }
        }
        return new CardStack<>(cards);
    }

    /**
     * Erstellt einen neuen Artefaktkartenstapel mit den Artefaktkarten, die im Regelwerk vorgesehen sind und gibt ihn in
     * gemischter Form zurück.
     *
     * @return Der gemischte Artefaktkartenstapel
     */
    public static CardStack<ArtifactCard> createArtifactCardStack() {
        List<ArtifactCard> cards = new LinkedList<>();
        for (ArtifactCardType type : ArtifactCardType.values()) {
            for (int i = 0; i < type.getCardsInDeck(); i++)
                cards.add(new ArtifactCard(type));
        }
        return new CardStack<>(cards);
    }
}
