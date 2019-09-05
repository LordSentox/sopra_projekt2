package de.sopra.javagame.model;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * 
 * @author Max Bühmann, Melanie Arnds
 * 
 * Ein CardStack implementiert einen Zieh- sowie einen Ablagestapel eines Kartentyps.
 *
 * @param <T> ist der Kartentyp. Entweder {@link ArtifactCard} oder {@link FloodCard}
 */
public class CardStack<T> {

    private Stack drawStack;

    private Collection discardPile;
    
    /**
     * draw nimmt die angegebene Anzahl Karten von oben vom Stack
     * @param amount Anzahl gewünschter Karten
     * @param discard bezeichnet, ob die gezogenen Karten sofort ausgeführt und abgeworfen werden sollen
     * @return gibt eine Liste vom Kartentyp T zurück
     */
    List<T> draw(int amount, boolean discard) {
        return null;
    }
    
    /**
     * shuffleBack mischt den Ablagestapel durch und legt ihn auf den DrawStack, beide Stapel können auch leer sein kann, nicht beide gleichzeitig.
     */
    void shuffleBack() {

    }
    
    /**
     * discard fügt eine beliebige Anzahl an Karten dem discardPile hinzu.
     * @param card ist ein varargs mit beliebiger Anzahl an Karten vom Typ T.
     * @return false, wenn fehlgeschlagen, true, sonst.
     */
    boolean discard(T... card) {
        return false;
    }

}
