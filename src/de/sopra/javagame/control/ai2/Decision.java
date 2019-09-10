package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.AIController;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.Point;

/**
 * <h1>Decision</h1>
 * Ist der Kern der entscheidungbasierten KI.
 * Stellt eine Aktion bereit, die mit einer Entscheidung als Bedingung verknüpft wird.
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public abstract class Decision {

    protected AIController control;

    /**
     * Entscheidet, ob die mit diesem Objekt verbundene Aktion ausgeführt werden soll, oder nicht.
     *
     * @return sich selbst, wenn die Entscheidung positiv ausfiel, andernfalls <code>null</code>
     */
    public abstract Decision decide();

    /**
     * Führt die Aktion aus.
     * Soll nur nach getroffener Entscheidung durch {@link #decide()} geschehen.
     */
    public abstract void act();

    /**
     * Baut aus zwei entscheidungsabhängigen Aktionen einen Turm.
     * Das Argument wird zur priorisierten Aktion gegenüber der Aktuellen.
     *
     * @param moreImportantDecision die Aktion, welche als wichtiger betrachtet wird, als die Aktuelle
     * @return ein neues Decision Objekt, welches keine eigene Aktion enthält,
     * aber mittels {@link #decide()} ein Objekt mit Aktion liefert
     */
    public final Decision onTop(Decision moreImportantDecision) {
        return new Decision() {
            @Override
            public Decision decide() {
                Decision decision = moreImportantDecision.decide();
                if (decision == null) {
                    return decide();
                } else return decision;
            }

            @Override
            public void act() {
                //empty
            }
        };
    }

    public Point translate(Point point, Direction... directions) {
        if (directions == null || directions.length == 0) return point;
        for (Direction direction : directions)
            point = direction.translate(point);
        return point;
    }

    public Player player() {
        return control.getActivePlayer();
    }

    public Turn turn() {
        return control.getActiveTurn();
    }

    public final void setControl(AIController control) {
        this.control = control;
    }
}