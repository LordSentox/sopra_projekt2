package de.sopra.javagame.util;

import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.CardStack;
import de.sopra.javagame.model.FloodCard;
import de.sopra.javagame.model.MapTile;

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
        return null;
    }

    /**
     * Erstellt einen neuen Artefaktkartenstapel mit den Artefaktkarten, die im Regelwerk vorgesehen sind und gibt ihn in
     * gemischter Form zurück.
     *
     * @return Der gemischte Artefaktkartenstapel
     */
    public static CardStack<ArtifactCard> createArtifactCardStack() {
        return null;
    }
}
