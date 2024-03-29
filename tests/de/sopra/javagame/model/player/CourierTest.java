package de.sopra.javagame.model.player;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.control.ControllerChan;
import de.sopra.javagame.model.*;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
import de.sopra.javagame.util.Triple;
import de.sopra.javagame.util.cardstack.CardStack;
import de.sopra.javagame.util.cardstack.CardStackUtil;
import de.sopra.javagame.util.map.MapFull;
import de.sopra.javagame.util.map.MapUtil;
import de.sopra.javagame.view.GamePreparationsViewController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CourierTest {

    private Action action;
    private List<Triple<PlayerType,String, Boolean>> players;
    private MapFull testMap;
    private CardStack<ArtifactCard> artifactCardStack;
    private CardStack<FloodCard> floodCardStack;
    private Triple<MapFull, CardStack<ArtifactCard>, CardStack<FloodCard>> tournamentTriple;

    @Before
    public void setUp() throws Exception {
        ControllerChan controllerChan = TestDummy.getDummyControllerChan();

        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        this.testMap = MapUtil.readFullMapFromString(testMapString);
        String testArtifactCardString = new String(Files.readAllBytes(Paths.get(GamePreparationsViewController.DEV_ARTIFACT_STACK_FOLDER)), StandardCharsets.UTF_8);
        this.artifactCardStack = CardStackUtil.readArtifactCardStackFromString(testArtifactCardString);
        String testFloodCardString = new String(Files.readAllBytes(Paths.get(GamePreparationsViewController.DEV_FLOOD_STACK_FOLDER)), StandardCharsets.UTF_8);
        this.floodCardStack = CardStackUtil.readFloodCardStackFromString(testFloodCardString);
        tournamentTriple = new Triple<>(testMap, artifactCardStack, floodCardStack);

        players = Arrays.asList(
                new Triple<>(PlayerType.COURIER,"", false),
                new Triple<>(PlayerType.EXPLORER,"", false),
                new Triple<>(PlayerType.NAVIGATOR,"", false),
                new Triple<>(PlayerType.PILOT,"", false));

        Pair<JavaGame, Action> pair = JavaGame.newGame("test", tournamentTriple, Difficulty.NORMAL, players);
        TestDummy.injectJavaGame(controllerChan, pair.getLeft());
        TestDummy.injectCurrentAction(controllerChan, pair.getRight());
        action = pair.getRight();

        action.getActivePlayer().setActionsLeft(3);
    }

    @Test
    public void constructorTest() {
        new Courier("Bob", new Point(4, 4), action);
        new Courier("Bob", new Point(4, 4), action, false);
    }

    @Test
    public void legalReceiversTest() {
        Courier courier = (Courier) action.getActivePlayer();
        List<PlayerType> receivers = courier.legalReceivers();

        for (PlayerType type : players.stream().map(Triple::getFirst).collect(Collectors.toList())) {
            if (type != PlayerType.COURIER) Assert.assertTrue("Player missing from legalReceivers: " + type, receivers.contains(type));
        }
    }

    @Test
    public void copyTest() {
        Courier courier = (Courier) action.getActivePlayer();
        Courier copy = (Courier) courier.copy();

        Assert.assertEquals("type not equal", courier.type, copy.type);
        Assert.assertEquals("name not equal", courier.name, copy.name);
        Assert.assertEquals("position not equal", courier.position, copy.position);
        Assert.assertEquals("actionsLeft not equal", courier.actionsLeft, copy.actionsLeft);
        Assert.assertEquals("isAI not equal", courier.isAI, copy.isAI);
        Assert.assertEquals("hand not equal", courier.hand, copy.hand);
    }
}
