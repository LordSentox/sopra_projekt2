package de.sopra.javagame.model.player;

import javafx.scene.image.Image;

/**
 * Der Typ der Spielerfigur
 */
public enum PlayerType {
    /**
     * Der Bote
     *
     * @see Courier
     */
    COURIER("messenger.png"),
    /**
     * Der Taucher
     *
     * @see Diver
     */
    DIVER("diver.png"),
    /**
     * Der Ingenieur
     *
     * @see Engineer
     */
    ENGINEER("engineer.png"),
    /**
     * Der Abenteurer
     *
     * @see Explorer
     */
    EXPLORER("explorer.png"),
    /**
     * Der Navigator
     *
     * @see Navigator
     */
    NAVIGATOR("navigator.png"),
    /**
     * Der Pilot
     *
     * @see Pilot
     */
    PILOT("pilot.png"),
    /**
     * Es liegt keine der Figuren vor
     */
    NONE("");
    
    
    private static String imageRoot = "/textures/default/";
    
    private String imageName;
    
    
    PlayerType(String imageName) {
        this.imageName = imageName;
    }
    
    public PlayerImage getImage() {
        return new PlayerImage(getClass().getResource(imageRoot + imageName).toExternalForm(), this);
    }
    
    public PlayerImage getImage(int requestedWidth, int requestedHeight, boolean preserveRatio, boolean smooth) {
        return new PlayerImage(getClass().getResource(imageRoot + imageName).toExternalForm(), requestedWidth, requestedHeight, preserveRatio, smooth, this);
    }
    
    public static void setImageRoot(String imageRoot) {
        PlayerType.imageRoot = imageRoot;
    }
    
    public static class PlayerImage extends Image {
        
        private PlayerType player;

        PlayerImage(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth, PlayerType player) {
            super(url, requestedWidth, requestedHeight, preserveRatio, smooth);
            this.player = player;
        }

        public PlayerImage(String url, PlayerType player) {
            super(url);
            this.player = player;
        }

        public PlayerType getPlayer() {
            return player;
        }
    }
}
