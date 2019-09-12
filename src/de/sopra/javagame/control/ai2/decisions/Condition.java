package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.ArtifactType;

import java.util.EnumSet;

import static de.sopra.javagame.model.ArtifactCardType.*;

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
            return decision.playerHand().hasAnyCard(WATER, FIRE, EARTH, AIR);
        }
    },
    GAME_HAS_ALL_ARTIFACTS {
        @Override
        public boolean isTrue(Decision decision) {
            EnumSet<ArtifactType> discoveredArtifacts = decision.action().getDiscoveredArtifacts();
            return discoveredArtifacts.size() == 4;
        }
    },
    PLAYER_HAS_SANDBAGS_CARD {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.playerHand().hasSandBags();
        }
    },
    PLAYER_HAS_HELICOPTER {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.playerHand().hasHelicopter();
        }
    },
    PLAYER_HAS_FOUR_IDENTICAL_TREASURE_CARDS {
        @Override
        public boolean isTrue(Decision decision) {
            return EnumSet.complementOf(EnumSet.of(ArtifactType.NONE)).stream()
                    .anyMatch(type -> decision.playerHand().getAmount(type) >= 4);
        }
    },
    CAN_CAPTURE_TREASURE {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.any(decision.all(decision.playerHand().getAmount(FIRE) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.FIRE),
                    decision.all(decision.playerHand().getAmount(EARTH) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.EARTH),
                    decision.all(decision.playerHand().getAmount(WATER) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.WATER),
                    decision.all(decision.playerHand().getAmount(AIR) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.AIR));
        }
    }

}