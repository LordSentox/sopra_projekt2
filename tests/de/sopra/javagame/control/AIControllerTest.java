package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static de.sopra.javagame.control.ai.GameAI.DECISION_BASED_AI;
import static org.junit.Assert.*;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 13.09.2019
 * @since 13.09.2019
 */
public class AIControllerTest {

    private AIController aiControl;
    private ControllerChan controllerChan;

    @Before
    public void setup() throws IOException {
        controllerChan = TestDummy.getDummyControllerChan();
        aiControl = controllerChan.getAiController();
        aiControl.setAI(DECISION_BASED_AI); //getestet wird mit der DECISION_BASES_AI
        final String testMapString = new String(Files.readAllBytes(Paths.get("resources/maps/island_of_death.map")), StandardCharsets.UTF_8);
        final MapBlackWhite tiles = MapUtil.readBlackWhiteMapFromString(testMapString);
        List<Pair<PlayerType, Boolean>> players = new LinkedList<Pair<PlayerType, Boolean>>() {{
            add(new Pair<>(PlayerType.EXPLORER, true));
            add(new Pair<>(PlayerType.PILOT, true));
            add(new Pair<>(PlayerType.DIVER, true));
            add(new Pair<>(PlayerType.COURIER, true));
        }};
        controllerChan.startNewGame("testGame", tiles, players, Difficulty.NORMAL);
        //FIXME Prüft bitte ob weitere Vorkehrungen getroffen werden müssen.
        //Es wird ein Zustand erwartet, in welcher der erste Spieler am Zug ist und Aktionen durchführen kann.
    }

    @Test
    public void hasAI() {
        ControllerChan dummyControllerChan = TestDummy.getDummyControllerChan();
        aiControl = dummyControllerChan.getAiController();
        assertFalse("AI auf magische Weise von Beginn an vorhanden", aiControl.hasAI());
        aiControl.setAI(DECISION_BASED_AI);
        assertTrue("Setzen der AI ist fehlgeschlagen", aiControl.hasAI());
    }

    @Test
    public void checkForCardStackTrackers() {
        Action currentAction = controllerChan.getCurrentAction();
        assertEquals(currentAction.getArtifactCardStack().getObserver(), aiControl.getArtifactCardStackTracker());
        assertEquals(currentAction.getFloodCardStack().getObserver(), aiControl.getFloodCardStackTracker());
    }

    @Test
    public void doSteps() {
        fail("Not yet implemented");
        //Maybe dont test, since its a little too heavy
    }

    @Test
    public void isCurrentlyDiscarding() {
        assertFalse(aiControl.isCurrentlyDiscarding());
        Player activePlayer = aiControl.getActivePlayer();
        while (activePlayer.getHand().size() < 6) {
            activePlayer.getHand().add(new ArtifactCard(ArtifactCardType.AIR));
        }
        assertTrue(aiControl.isCurrentlyDiscarding());
    }

    @Test
    public void isCurrentlyRescueingHimself() {
        fail("Not yet implemented");
    }

    @Test
    public void getCurrentAction() {
        fail("Not yet implemented");
    }

    @Test
    public void getActivePlayer() {
        fail("Not yet implemented");
    }

    @Test
    public void getTile() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetTileByPlayerStart() {
        fail("Not yet implemented");
    }

    @Test
    public void getTemples() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetTileByPoint() {
        fail("Not yet implemented");
    }

    @Test
    public void getAllPlayers() {
        fail("Not yet implemented");
    }

    @Test
    public void getDrainablePositionsOneMoveAway() {
        fail("Not yet implemented");
    }

    @Test
    public void anyPlayerHasCard() {
        fail("Not yet implemented");
    }

    @Test
    public void getTotalAmountOfCardsOnHands() {
        fail("Not yet implemented");
    }

    @Test
    public void anyTile() {
        fail("Not yet implemented");
    }

    @Test
    public void makeStep() {
        fail("Not yet implemented");
    }

    @Test
    public void testMakeStep() {
        fail("Not yet implemented");
    }

    @Test
    public void getTip() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetTip() {
        fail("Not yet implemented");
    }

    @Test
    public void landingSiteIsFlooded() {
        fail("Not yet implemented");
    }

    @Test
    public void getMinimumActionsNeededToReachTarget() {
        fail("Not yet implemented");
    }

}