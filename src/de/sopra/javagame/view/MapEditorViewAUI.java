package de.sopra.javagame.view;

/**
 * bietet Methoden zum aktualisieren des MapEditors
 * @author Hannah, Lisa
 *
 */
public interface MapEditorViewAUI {

<<<<<<< HEAD

    /**
     * zeigt dem Benutzer die übergebene Nachricht in dem dafür vorgesehenen Fenster
     * @param notification Nachricht an den Benutzer
     */
    public abstract void showNotification(String notification);
=======
    void showNotification(String notification);
>>>>>>> branch 'master' of https://sopra-gitlab.cs.tu-dortmund.de/sopra19B/gruppe04/projekt2.git

<<<<<<< HEAD
    /**
     * setzt die Karte auf eine vorher ausgewählte Karte, die in Form eines boolean Arryas übergeben wurde
     * @param tiles true: hier kann ein MapTile platziert werden
     * 			    false: hier ist immer Wasser
     */
    public abstract void setMap(boolean[][] tiles);
=======
    void setMap(boolean[][] tiles);
>>>>>>> branch 'master' of https://sopra-gitlab.cs.tu-dortmund.de/sopra19B/gruppe04/projekt2.git

}
