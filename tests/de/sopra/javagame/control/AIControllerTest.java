package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Explorer;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.MapBlackWhite;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static de.sopra.javagame.control.ai.GameAI.DECISION_BASED_AI;
import static de.sopra.javagame.model.ArtifactCardType.SANDBAGS;
import static de.sopra.javagame.model.ArtifactCardType.WATER;
import static de.sopra.javagame.model.MapTileState.FLOODED;
import static de.sopra.javagame.model.player.PlayerType.PILOT;
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
        Player activePlayer = aiControl.getActivePlayer();
        MapTile tile = aiControl.getTile(activePlayer.getPosition());
        tile.flood();
        tile.flood();
        assertTrue(aiControl.isCurrentlyRescueingHimself());
    }

    @Test
    public void getCurrentAction() {
        assertSame(aiControl.getCurrentAction(), controllerChan.getCurrentAction());
    }

    @Test
    public void getActivePlayer() {
        assertSame(aiControl.getActivePlayer(), controllerChan.getCurrentAction().getActivePlayer());
        Explorer otherExplorer = new Explorer("otherExplorer", new Point(0, 0), aiControl.getCurrentAction(), true);
        aiControl.setActivePlayerSupplier(() -> otherExplorer);
        assertSame(aiControl.getActivePlayer(), otherExplorer);
    }

    @Test
    public void getAllPlayers() {
        List<Player> allPlayers = aiControl.getAllPlayers();
        List<Player> players = controllerChan.getCurrentAction().getPlayers();
        assertEquals(allPlayers.size(), players.size());
        assertArrayEquals(allPlayers.toArray(), players.toArray());
    }

    @Test
    public void getDrainablePositionsOneMoveAway() {
        
        /*
         * Test für Explorer
         */
//        Point explorersStartPosition = aiControl.getTile(PlayerType.EXPLORER).getLeft();
//        MapTile explorerStart = aiControl.getTile(PlayerType.EXPLORER).getRight();
//        List<Point> explorersNeighboursPositions = explorersStartPosition.getNeighbours();
//        List<MapTile> explorersNeighbours = explorersStartPosition.getNeighbours().stream().map(aiControl::getTile).collect(Collectors.toList());
//        for(MapTile neighbour : explorersNeighbours){
//            if (neighbour != null) neighbour.drain();
//        }
//        for(Point explorersNeighboursPoint : explorersNeighboursPositions){
//            List<MapTile> explorersNextNeighbours = explorersNeighboursPoint.getNeighbours().stream().map(aiControl::getTile).collect(Collectors.toList());
//            for(MapTile explorersNextNeighbour : explorersNextNeighbours){
//                if (explorersNextNeighbour != null) explorersNextNeighbour.drain();
//                explorersNextNeighbour.drain();
//            }
//        }
        
        /*Test für den Taucher ändert die komplette Map, um sicherzustellen, dass alle 
         *erreichbaren Teile verändert werden
         */
        Point diversStartPosition = aiControl.getTile(PlayerType.DIVER).getLeft();
        MapTile diversStart = aiControl.getTile(PlayerType.DIVER).getRight();
        MapTile[][] completeCard = aiControl.getCurrentAction().getMap().raw();
        for (int i = 0; i < completeCard.length; i++){
            for (int j = 0; j < completeCard[i].length; j++){
                if (completeCard[i][j] != null){
                    completeCard[i][j].drain();
                }
            }
        }
        assertTrue("Taucher kann Felder trockenlegen, obwohl alle MapTiles DRY sind",
                aiControl.getDrainablePositionsOneMoveAway(diversStartPosition, PlayerType.DIVER).isEmpty());        
        for (int i = 0; i < completeCard.length; i++){
            for (int j = 0; j < completeCard[i].length; j++){
                if (completeCard[i][j] != null){
                    completeCard[i][j].flood();
                }
            }
        }        
        assertTrue("Taucher kann Felder trockenlegen, obwohl er alle MapTiles sofort erreichen kann",
                aiControl.getDrainablePositionsOneMoveAway(diversStartPosition, PlayerType.DIVER).isEmpty());
        
        for (int i = 0; i < completeCard.length; i++){
            for (int j = 0; j < completeCard[i].length; j++){
                if (completeCard[i][j] != null){
                    completeCard[i][j].flood();
                }
            }
        }
        diversStart.drain();
        assertTrue("Taucher kann Felder trockenlegen, obwohl alle MapTiles bis auf die Position des Tauchers GONE sind",
                aiControl.getDrainablePositionsOneMoveAway(diversStartPosition, PlayerType.DIVER).isEmpty());
        

        
        
        
//        for(MapTile neighbour : diversNeighbours){
//            neighbour.flood();
//        }
//        for(Point diversNeighboursPoint : diversNeighboursPositions){
//            List<MapTile> diversNextNeighbours = diversNeighboursPoint.getNeighbours().stream().map(aiControl::getTile).collect(Collectors.toList());
//            for(MapTile diversNextNeighbour : diversNextNeighbours){
//                diversNextNeighbour.flood();
//            }
//        }
        
        
        
//        for(MapTile neighbour : diversNeighbours){
//            neighbour.flood();
//        }
//        for(Point diversNeighboursPoint : diversNeighboursPositions){
//            List<MapTile> diversNextNeighbours = diversNeighboursPoint.getNeighbours().stream().map(aiControl::getTile).collect(Collectors.toList());
//            for(MapTile diversNextNeighbour : diversNextNeighbours){
//                diversNextNeighbour.flood();
//            }
//        }
        
        
        
    }

    @Test
    public void anyPlayerHasCard() {
        aiControl.getActivePlayer().getHand().add(new ArtifactCard(SANDBAGS));
        assertTrue(aiControl.anyPlayerHasCard(SANDBAGS));
    }

    @Test
    public void getTotalAmountOfCardsOnHands() {
        int cards = 0;
        aiControl.getActivePlayer().getHand().add(new ArtifactCard(WATER));
        for (Player player : aiControl.getAllPlayers()) {
            cards += EnhancedPlayerHand.ofPlayer(player).getAmount(WATER);
        }
        assertEquals(aiControl.getTotalAmountOfCardsOnHands(WATER), cards);
    }

    @Test
    public void landingSiteIsFlooded() {
        Pair<Point, MapTile> tile = aiControl.getTile(PILOT);
        tile.getRight().setState(FLOODED);
        assertTrue(aiControl.landingSiteIsFlooded());
    }

    @Test
    public void getMinimumActionsNeededToReachTarget() {
        fail("Not yet implemented");
    }

}