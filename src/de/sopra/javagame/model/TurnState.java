package de.sopra.javagame.model;

/**
 * Beschreibt die drei verschiedenen Phasen eines Spielzugs:
 * Fluten, Spieleraktion, Artefaktkarte nachziehen
 *
 * @author GeEuch Bühmann, Melanie Arnds
 */
public enum TurnState {
    FLOOD,
    PLAYER_ACTION,
    DRAW_ARTIFACT_CARD;
}
