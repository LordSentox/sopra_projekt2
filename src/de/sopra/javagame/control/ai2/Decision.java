package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.AIController;

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
     * @param control der AIController mit allen wichtigen Inhalten
     * @return sich selbst, wenn die Entscheidung positiv ausfiel, andernfalls <code>null</code>
     */
    Decision decide(AIController control);

    /**
     * Führt die Aktion aus.
     * Soll nur nach getroffener Entscheidung durch {@link #decide(AIController)} geschehen.
     *
     * @param control der AIController mit allen wichtigen Inhalten
     */
    void act(AIController control);

    /**
     * Baut aus zwei entscheidungsabhängigen Aktionen einen Turm.
     * Das Argument wird zur priorisierten Aktion gegenüber der Aktuellen.
     *
     * @param moreImportantDecision die Aktion, welche als wichtiger betrachtet wird, als die Aktuelle
     * @return ein neues Decision Objekt, welches keine eigene Aktion enthält, aber mittels {@link #decide(AIController)} ein Objekt mit Aktion liefert
     */
    default Decision onTop(Decision moreImportantDecision) {
        return new Decision() {
            @Override
            public Decision decide(AIController control) {
                Decision decision = moreImportantDecision.decide(control);
                if (decision == null) {
                    return decide(control);
                } else return decision;
            }

            @Override
            public void act(AIController control) {
                //empty
            }
        };
    }

}