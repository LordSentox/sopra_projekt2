package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai.ActionQueue;
import de.sopra.javagame.control.ai2.DoAfter;
import de.sopra.javagame.control.ai2.PreCondition;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import java.util.EnumSet;
import java.util.List;

import static de.sopra.javagame.control.ai2.DecisionResult.PLAY_SPECIAL_CARD;
import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_HELICOPTER_CARD;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 10.09.2019
 * @since 10.09.2019
 */
@DoAfter(act = PLAY_SPECIAL_CARD, value = SpecialFlyNextActivePlayerToDrainOrphanedTempleMapTile.class)
@PreCondition(allTrue = PLAYER_HAS_HELICOPTER_CARD)
public class SpecialFlyOutOrphanedPlayers extends Decision {
    private Point start;
    private Point target;
    private EnumSet<PlayerType> rescued;

    @Override
    public Decision decide() {
        List<Player> allPlayers = control.getAllPlayers();

        Pair<Point, MapTile> informationLandingSite = control.getTile(PlayerType.PILOT);
        Point landingSitePosition = informationLandingSite.getLeft();
        //prüfe, ob Spieler auf LandingSite steht, von dort muss er nicht gerettet werden 
        for (Player player : allPlayers) {
            if (player.getPosition().equals(landingSitePosition)) {
                continue;
            }
            //prüfe, ob der Spieler sich noch selbst wegbewegen kann
            if (player.legalMoves(true).isEmpty()) {
                start = player.getPosition();
                target = control.getTile(PlayerType.PILOT).getLeft();
                for (Player otherPlayer : control.getAllPlayers()) {
                    if (otherPlayer.getPosition().equals(player.getPosition())) {
                        rescued.add(otherPlayer.getType());
                    }
                }
                return this;
            }
        }
        return null;
    }

    @Override
    public ActionQueue act() {
        return startActionQueue().helicopterCard(start, target, rescued);
    }

}
