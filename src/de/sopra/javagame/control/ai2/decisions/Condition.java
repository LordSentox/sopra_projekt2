package de.sopra.javagame.control.ai2.decisions;

import de.sopra.javagame.model.ArtifactType;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.Pair;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.sopra.javagame.model.ArtifactCardType.*;
import static de.sopra.javagame.model.MapTileState.FLOODED;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 12.09.2019
 * @since 12.09.2019
 */
public enum Condition implements ICondition {

    GAME_HAS_ALL_ARTIFACTS {
        @Override
        public boolean isTrue(Decision decision) {
            EnumSet<ArtifactType> discoveredArtifacts = decision.action().getDiscoveredArtifacts();
            return discoveredArtifacts.size() == 4;
        }
    },
    GAME_LANDING_SITE_IS_FLOODED {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.control.getTile(PlayerType.PILOT).getRight().getState() == FLOODED;
        }
    },
    GAME_ANY_LAST_TEMPLE_IN_DANGER {
        @Override
        public boolean isTrue(Decision decision) {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            EnumSet<ArtifactType> discoveredArtifacts = decision.action().getDiscoveredArtifacts();
            List<MapTile> templeTiles = decision.control.getTemples()
                    .stream()
                    .map(Pair::getRight)
                    //Nur Tempel, deren Artefakt noch nicht gefunden wurde
                    .filter(mapTile -> !discoveredArtifacts.contains(mapTile.getProperties().getHidden()))
                    .collect(Collectors.toList());
            templeTiles.forEach(new Consumer<MapTile>() {
                List<ArtifactType> sunkenTemples = templeTiles.stream()
                        .filter(tile -> tile.getState() == MapTileState.GONE)
                        .map(mapTile -> mapTile.getProperties().getHidden())
                        .collect(Collectors.toList());

                @Override
                public void accept(MapTile mapTile) {
                    if (atomicBoolean.get()) return;
                    if (mapTile.getState() == MapTileState.FLOODED) {
                        atomicBoolean.set(sunkenTemples.contains(mapTile.getProperties().getHidden()));
                    }
                }
            });
            return atomicBoolean.get();
        }
    },
    GAME_ANY_PLAYER_HAS_HELICOPTER {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.control.anyPlayerHasCard(HELICOPTER);
        }
    },
    GAME_ANY_PLAYER_HAS_SANDBAGS {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.control.anyPlayerHasCard(SANDBAGS);
        }
    },
    PLAYER_HAS_ANY_ARTIFACT_CARD {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.playerHand().hasAnyCard(WATER, FIRE, EARTH, AIR);
        }
    },
    PLAYER_HAS_SANDBAGS_CARD {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.playerHand().hasSandBags();
        }
    },
    PLAYER_HAS_HELICOPTER_CARD {
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
    PLAYER_CAN_CAPTURE_TREASURE {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.any(decision.all(decision.playerHand().getAmount(FIRE) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.FIRE),
                    decision.all(decision.playerHand().getAmount(EARTH) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.EARTH),
                    decision.all(decision.playerHand().getAmount(WATER) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.WATER),
                    decision.all(decision.playerHand().getAmount(AIR) > decision.THREE_CARDS, decision.tile().getProperties().getHidden() == ArtifactType.AIR));
        }
    },
    PLAYER_HAS_MORE_THAN_1_ACTION_LEFT {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.player().getActionsLeft() > 1;
        }
    },
    PLAYER_NO_ACTION_LEFT {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.player().getActionsLeft() == 0;
        }
    },
    //TODO test by playing: if limit 7 actions is too much, reduce to 4 
    PLAYER_REACHES_LANDINGSITE_WITH_LESS_THAN_SEVEN_ACTIONS {
        @Override
        public boolean isTrue(Decision decision) {
            return decision.control.getMinimumActionsNeededToReachTarget
                    (decision.player().getPosition(), decision.control.getTile(PlayerType.PILOT).getLeft(), decision.player().getType()) < 7;
        }
    }

}