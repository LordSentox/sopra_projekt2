package de.sopra.javagame.control.ai2;

import de.sopra.javagame.model.JavaGame;

/**
 * <h1>Decision</h1>
 * Ist der Kern der entscheidungbasierten KI.
 * Stellt eine Aktion bereit, die mit einer Entscheidung als Bedingung verknüpft wird.
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public interface Decision {

    /**
     * Entscheidet, ob die mit diesem Objekt verbundene Aktion ausgeführt werden soll, oder nicht.
     *
     * @param javaGame das aktuelle Spiel
     * @return sich selbst, wenn die Entscheidung positiv ausfiel, andernfalls <code>null</code>
     */
    Decision decide(JavaGame javaGame);

    /**
     * Führt die Aktion aus.
     * Soll nur nach getroffener Entscheidung durch {@link #decide(JavaGame)} geschehen.
     *
     * @param javaGame das aktuelle Spiel
     */
    void act(JavaGame javaGame);

    /**
     * Baut aus zwei entscheidungsabhängigen Aktionen einen Turm.
     * Das Argument wird zur priorisierten Aktion gegenüber der Aktuellen.
     *
     * @param moreImportantDecision die Aktion, welche als wichtiger betrachtet wird, als die Aktuelle
     * @return ein neues Decision Objekt, welches keine eigene Aktion enthält, aber mittels {@link #decide(JavaGame)} ein Objekt mit Aktion liefert
     */
    default Decision onTop(Decision moreImportantDecision) {
        return new Decision() {
            @Override
            public Decision decide(JavaGame javaGame) {
                Decision decision = moreImportantDecision.decide(javaGame);
                if (decision == null) {
                    return decide(javaGame);
                } else return decision;
            }

            @Override
            public void act(JavaGame javaGame) {
                //empty
            }
        };
    }

}