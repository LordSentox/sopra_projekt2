package de.sopra.javagame.model.player;

/**
 * Der Typ der Spielerfigur
 */
public enum PlayerType {
    /**
     * Der Bote
     *
     * @see Courier
     */
    COURIER,
    /**
     * Der Taucher
     *
     * @see Diver
     */
    DIVER,
    /**
     * Der Ingenieur
     *
     * @see Engineer
     */
    ENGINEER,
    /**
     * Der Abenteurer
     *
     * @see Explorer
     */
    EXPLORER,
    /**
     * Der Navigator
     *
     * @see Navigator
     */
    NAVIGATOR,
    /**
     * Der Pilot
     *
     * @see Pilot
     */
    PILOT,
    /**
     * Es liegt keine der Figuren vor
     */
    NONE;

}
