package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.ArtifactType;

import java.util.EnumSet;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 12.09.2019
 * @since 12.09.2019
 */
public enum Condition implements ICondition {

    PLAYER_HAS_ANY_ARTIFACT_CARD {
        @Override
        public boolean isTrue(Decision decision) {
            EnumSet<ArtifactType> discoveredArtifacts = decision.action().getDiscoveredArtifacts();
            return discoveredArtifacts.size() == 4;
        }
    },
    GAME_HAS_ALL_ARTIFACTS {
        @Override
        public boolean isTrue(Decision decision) {
            return false;
        }
    }

}