package de.sopra.javagame.control.ai2;

import de.sopra.javagame.control.ai2.decisions.*;
import de.sopra.javagame.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 11.09.2019
 * @since 11.09.2019
 */
public class DecisionMakerTest {

    private DecisionMaker decisionMaker;

    @Before
    public void setup() {
        decisionMaker = new DecisionMaker();
        decisionMaker.init();
    }

    @Test
    public void findDecisionsTest() {
        List<Class<? extends Decision>> decisionClasses = decisionMaker.getDecisionClasses().stream()
                .map(Pair::getRight).collect(Collectors.toList());
        List<Class<? extends Decision>> partOfKnownDecisions = new LinkedList<>();
        partOfKnownDecisions.add(DiscardFlyToTreasurePickupSiteToKeepFourTreasureCards.class);
        partOfKnownDecisions.add(DiscardOddTreasureCardWhenPlayerHasThreeOfSomething.class);
        partOfKnownDecisions.add(DiscardSandbagRatherThanOneOfFourTreasureCards.class);
        partOfKnownDecisions.add(DiscardUseSandbagInsteadOfDiscardingTreasureCard.class);
        partOfKnownDecisions.add(SpecialFlyNextActivePlayerToDrainOrphanedTempleMapTile.class);
        partOfKnownDecisions.add(SpecialFlyOutOrphanedPlayers.class);
        partOfKnownDecisions.add(TurnCaptureTreasure.class);
        partOfKnownDecisions.add(TurnDrainTile.class);
        partOfKnownDecisions.add(TurnDrainTempleMapTileOfUndiscoveredArtifact.class);
        partOfKnownDecisions.add(TurnEndGame.class);
        Assert.assertTrue("Nicht alle Beispiel Decisions Klassen sind vorhanden", decisionClasses.containsAll(partOfKnownDecisions));
    }

    @Test
    public void queueBuildingTest() {
        List<Pair<DoAfter, Class<? extends Decision>>> decisionClasses = decisionMaker.getDecisionClasses();
        Queue<Class<? extends Decision>> queue = decisionMaker.buildingQueue(decisionClasses);
        Class<? extends Decision> current = queue.poll();
        while (!queue.isEmpty()) {
            Class<? extends Decision> polled = queue.poll();
            Assert.assertEquals("Der Decision-Tree ist falsch gebaut", getPrevious(polled), current);
        }
    }

    private Class<? extends Decision> getPrevious(Class<? extends Decision> decisionClass) {
        DoAfter doAfter = decisionClass.getDeclaredAnnotation(DoAfter.class);
        return doAfter.value();
    }

}