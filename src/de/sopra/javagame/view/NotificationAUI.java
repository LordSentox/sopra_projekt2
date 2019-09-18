package de.sopra.javagame.view;

import de.sopra.javagame.view.abstraction.Notification;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 18.09.2019
 * @since 18.09.2019
 */
public interface NotificationAUI {

    /**
     * gibt dem Spieler eine Mitteilung in dem dafür vorgesehenen Fenster
     *
     * @param notification Mitteilung an den Spieler
     */
    void showNotification(Notification notification);

}