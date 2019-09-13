package de.sopra.javagame.control.ai;

import de.sopra.javagame.control.ai1.ActionType;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.Copyable;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;

/**
 * <h1>projekt2</h1>
 *
 * @author Julius Korweck
 * @version 09.09.2019
 * @since 09.09.2019
 */
public final class SimpleAction implements Copyable<SimpleAction> {

    private final ActionType type;
    private final Point startingPoint;
    private final Point targetPoint;

    private final EnumSet<PlayerType> targetPlayers;
    private final ArtifactCardType cardType;

    //COLLECT_TREASURE
    public SimpleAction(ActionType type) {
        this.type = type;
        this.startingPoint = null;
        this.targetPoint = null;
        this.cardType = null;
        this.targetPlayers = EnumSet.noneOf(PlayerType.class);
    }

    //MOVE, DRAIN
    public SimpleAction(ActionType type, Point targetPoint) {
        this.type = type;
        this.startingPoint = null;
        this.targetPoint = targetPoint;
        this.cardType = null;
        this.targetPlayers = EnumSet.noneOf(PlayerType.class);
    }

    //DISCARD_CARD, TRADE_CARD
    public SimpleAction(ActionType type, EnumSet<PlayerType> targetPlayers, ArtifactCardType cardType) {
        this.type = type;
        this.targetPlayers = targetPlayers;
        this.cardType = cardType;
        this.startingPoint = null;
        this.targetPoint = null;
    }

    //SPECIAL_CARD
    public SimpleAction(Point startingPoint, Point targetPoint, EnumSet<PlayerType> targetPlayers, ArtifactCardType cardType) {
        this.type = ActionType.SPECIAL_CARD;
        this.startingPoint = startingPoint;
        this.targetPoint = targetPoint;
        this.targetPlayers = targetPlayers;
        this.cardType = cardType;
    }

    //SPECIAL_ABILITY
    public SimpleAction(Point startingPoint, Point targetPoint, EnumSet<PlayerType> targetPlayers) {
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

    /**
     * Der Startpunkt der Aktion, falls die Aktion keinen impliziten Startpunkt hat.
     * Die Position des ausführenden Spielers wird beispielsweise bei einer normaler Bewegung nicht festgehalten.
     * Ist primär relevant für Helikopter-Spezialkarten und die Spezialaktion des Navigators.
     *
     * @return der Startpunkt der Aktion
     */
    public Point getStartingPoint() {
        return startingPoint;
    }

    public Point getTargetPoint() {
        return targetPoint;
    }

    @Override
    public SimpleAction copy() {
        return this; //should not actually copy, is here to by usable for
    }
}