package de.sopra.javagame.view;

/**
 * bietet Methoden zum aktualisieren des MapEditors
 *
 * @author Hannah, Lisa
 */
public interface MapEditorViewAUI {
    /**
     * zeigt dem Benutzer die übergebene Nachricht in dem dafür vorgesehenen Fenster
     *
     * @param notification Nachricht an den Benutzer
     */
    void showNotification(String notification);

    /**
     * setzt die Karte auf eine vorher ausgewählte Karte, die in Form eines boolean Arryas übergeben wurde
     *
     * @param mapName
     * @param tiles true: hier kann ein MapTile platziert werden
     */
    void setMap(String mapName, boolean[][] tiles);

}
