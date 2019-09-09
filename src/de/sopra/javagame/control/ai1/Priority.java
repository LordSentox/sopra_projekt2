package de.sopra.javagame.control.ai1;

import de.sopra.javagame.control.AIController;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public interface Priority {

    /**
     * Soll einen Wert für die gegebene Aktion berechnen, der die Wichtigkeit der Aktion repräsentiert
     *
     * @param action  die zu bewertende Aktion
     * @param control der AIController mit allen wichtigen Inhalten
     * @return ein Bewertung der Aktion. Je größer die Zahl, desto wichtiger
     */
    double getPriority(Action action, AIController control);

    /**
     * Ein Gewichtungswertswert, der als Multiplikator einfließt.
     *
     * @return der Multiplikator zur Feineinstellung von Gewichtung
     */
    default double weight() {
        return 1;
    }

}