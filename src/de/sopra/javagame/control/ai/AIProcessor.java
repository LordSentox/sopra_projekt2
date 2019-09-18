package de.sopra.javagame.control.ai;

import de.sopra.javagame.control.AIController;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 11.09.2019
 * @since 11.09.2019
 */
public interface AIProcessor {

    /**
     * Ermöglicht der KI die Vorbereitung der inneren Strukturen.
     */
    void init();

    /**
     * Fordert die KI auf, mit dem aktuellen Spieler einen automatischen Zug durchzuführen.
     * Dieser Zug kann mehrere Aktionen verbrauchen und das Ausspielen von Spezialkarten beinhalten.
     * MakeStep kann ebenfalls zum Abwerfen von Karten verwendet werden.
     */
    void makeStep(AIController control);

    /**
     * Fordert einen Tipp von der KI an.
     * Der Tipp wird in Befehlsform definiert durch {@link SimpleAction} dargestellt.
     * Die Aktion kann Schritte vorraussetzen, die vorher passieren müssen.
     *
     * @return ein Tipp in Befehlsform
     */
    SimpleAction getTip(AIController control);

}