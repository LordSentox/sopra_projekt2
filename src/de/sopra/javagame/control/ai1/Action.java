package de.sopra.javagame.control.ai1;

import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.player.PlayerType;

import java.awt.*;
import java.util.EnumSet;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public final class Action {

    private final ActionType type;
    private final Point startingPoint;
    private final Point targetPoint;

    private final EnumSet<PlayerType> targetPlayers;
    private final ArtifactCardType cardType;

    //MOVE, DRAIN
    public Action(ActionType type, Point startingPoint, Point targetPoint) {
        this.type = type;
        this.startingPoint = startingPoint;
        this.targetPoint = targetPoint;
        this.cardType = null;
        this.targetPlayers = EnumSet.noneOf(PlayerType.class);
    }

    //DISCARD_CARD, TRADE_CARD
    public Action(ActionType type, EnumSet<PlayerType> targetPlayers, ArtifactCardType cardType) {
        this.type = type;
        this.targetPlayers = targetPlayers;
        this.cardType = cardType;
        this.startingPoint = null;
        this.targetPoint = null;
    }

    //SPECIAL_CARD
    public Action(Point startingPoint, Point targetPoint, EnumSet<PlayerType> targetPlayers, ArtifactCardType cardType) {
        this.type = ActionType.SPECIAL_CARD;
        this.startingPoint = startingPoint;
        this.targetPoint = targetPoint;
        this.targetPlayers = targetPlayers;
        this.cardType = cardType;
    }

    //SPECIAL_ABILITY
    public Action(Point startingPoint, Point targetPoint, EnumSet<PlayerType> targetPlayers) {
        this.type = ActionType.SPECIAL_ABILITY;
        this.startingPoint = startingPoint;
        this.targetPoint = targetPoint;
        this.targetPlayers = targetPlayers;
        this.cardType = null;
    }

    public ActionType getType() {
        return type;
    }

    public ArtifactCardType getCardType() {
        return cardType;
    }

    public EnumSet<PlayerType> getTargetPlayers() {
        return targetPlayers;
    }

    public Point getStartingPoint() {
        return startingPoint;
    }

    public Point getTargetPoint() {
        return targetPoint;
    }

}