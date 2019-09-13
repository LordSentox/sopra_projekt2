package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

public class ActivePlayerControllerTest {
    
    private ControllerChan controllerChan;
    private ActivePlayerController activePlayerController;
    private TestDummy.InGameView inGameView;
    private JavaGame javaGame;
    private int[][] testMapNumbers;
    private MapTile[][] testMap;
    private List moveablePlayers;
    private MapController mapController;
    private Action action;
    private ArtifactCard fireCard;
    private ArtifactCard waterCard;
    private ArtifactCard earthCard;
    private ArtifactCard airCard;
    private ArtifactCard sandCard;
    private ArtifactCard heliCard;
    private Courier courier;
    private Explorer explorer;
    private Navigator navigator;
    private Pilot pilot;
    private CardStack<ArtifactCard> artifactCardStack;
    private List<ArtifactCard> handCardsExpected;
    List<Pair<PlayerType, Boolean>> players;
    
    
    
    @Before
    public void setUp() throws IOException {
        controllerChan = TestDummy.getDummyControllerChan();
        activePlayerController = controllerChan.getActivePlayerController();
        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
        javaGame = controllerChan.getJavaGame();
        action = controllerChan.getCurrentAction();

        boolean [][] tiles = new boolean [12][12];
        for (int y = 1; y < 3; y++) {
            for (int x = 1; x < tiles[y].length-1; x++) {
                tiles[y][x] = true;
            }
        }
        for (int x = 1; x < 5; x++) {
            tiles[3][x] = true;
        }
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap", new String[]{})), StandardCharsets.UTF_8);
        testMapNumbers = MapUtil.readNumberMapFromString(testMapString);
        this.testMap = MapUtil.createMapFromNumbers(testMapNumbers);
        players = Arrays.asList(
                new Pair<>(PlayerType.COURIER, false),
                new Pair<>(PlayerType.EXPLORER, false),
                new Pair<>(PlayerType.NAVIGATOR, false));
        controllerChan.startNewGame("TestMap", tiles, players, Difficulty.NORMAL);
        mapController = controllerChan.getMapController();
        
        artifactCardStack = action.getArtifactCardStack();
        List<ArtifactCard> cardList = artifactCardStack.draw(28, false);
        
        for (ArtifactCard cur : cardList){
            if(fireCard != null && waterCard != null && earthCard != null && airCard != null && sandCard != null && heliCard != null){
                break;
            }
            
            if(cur.getType() == ArtifactCardType.FIRE){
                fireCard = cur;
            }
            if(cur.getType() == ArtifactCardType.WATER){
                waterCard = cur;
            }
            if(cur.getType() == ArtifactCardType.EARTH){
                earthCard = cur;
            }
            if(cur.getType() == ArtifactCardType.AIR){
                airCard = cur;
            }
            if(cur.getType() == ArtifactCardType.SANDBAGS){
                sandCard = cur;
            }
            if(cur.getType() == ArtifactCardType.HELICOPTER){
                heliCard = cur;
            }
        }
        
        pilot = new Pilot("pilot", new Point(4,2), action);
        courier = new Courier("courier", new Point(4, 2), action);
        explorer = new Explorer("explorer", new Point(5,2), action);
        navigator = new Navigator("navigator", new Point(4,2), action);
        
        handCardsExpected = new ArrayList<ArtifactCard>();
        explorer.getHand().add(fireCard);
        handCardsExpected.add(fireCard);
        explorer.getHand().add(waterCard);
        handCardsExpected.add(waterCard);
        explorer.getHand().add(earthCard);
        handCardsExpected.add(earthCard);
        explorer.getHand().add(airCard);
        handCardsExpected.add(airCard);
        

        moveablePlayers = new ArrayList<Player>();
        moveablePlayers.add(navigator);
        moveablePlayers.add(courier);
    }
    

    @Test
    public void testShowMovements() {
        int activePlayerNumber = action.getActivePlayerIndex();
        Pair<PlayerType, Boolean> activeplayer = players.get(activePlayerNumber);
        
        //Optionen für Courier
        if (activeplayer.equals(courier)) {
            activePlayerController.showMovements(false);
        }
        
        //alle Felder müssen angezeigt werden
        if (activeplayer.equals(pilot)) {
            activePlayerController.showMovements(true);
        }
        fail("Not yet implemented");
    }


    @Test
    public void testShowDrainOptions() {
        fail("Not yet implemented");
    }

    @Test
    public void testShowSpecialAbility() {
        fail("Not yet implemented");
    }

    @Test
    public void testCancelSpecialAbility() {
        fail("Not yet implemented");
    }

    @Test
    public void testShowTransferable() {
        fail("Not yet implemented");
    }

    @Test
    public void testTransferCard() {
        fail("Not yet implemented");
    }

    @Test
    public void testCollectArtifact() {
        fail("Not yet implemented");
    }

    @Test
    public void testMove() {
        fail("Not yet implemented");
    }

    @Test
    public void testMoveOther() {
        fail("Not yet implemented");
    }

    @Test
    public void testDrain() {
        fail("Not yet implemented");
    }

    @Test
    public void testShowTip() {
        fail("Not yet implemented");
    }

    @Test
    public void testEndTurn() {
        fail("Not yet implemented");
    }

}
