package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.control.ai2.PreCondition;

import static de.sopra.javagame.control.ai2.decisions.Condition.PLAYER_HAS_HELICOPTER;

/**
 * <h1>projekt2</h1>
 *
 * @author Melanie Arnds
 * @version 12.09.2019
 * @since 12.09.2019
 */
@PreCondition(allTrue = PLAYER_HAS_HELICOPTER)
public class SpecialFlyPlayersToLandingSiteForDeparture extends Decision {

    @Override
    public Decision decide() {
        // TODO use BFS to decide if to fly out Players or let them walk
        return null;
    }

    @Override
    public void act() {
        // TODO Auto-generated method stub

    }

}
