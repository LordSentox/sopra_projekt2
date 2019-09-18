package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.control.ai.EnhancedPlayerHand;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.Explorer;
import de.sopra.javagame.model.player.Player;
import de.sopra.javagame.model.player.PlayerType;
import de.sopra.javagame.util.*;
import de.sopra.javagame.view.GamePreparationsViewController;

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
import static de.sopra.javagame.util.Direction.*;
import static org.junit.Assert.*;

/**
 * <h1>Projekt2</h1>
 *
 * @author Julius Korweck
 * @version 13.09.2019
 * @since 13.09.2019
 */
@SuppressWarnings("serial")
public class AIControllerTest {

    private AIController aiControl;
    private ControllerChan controllerChan;
    private List<Triple<PlayerType,String, Boolean>> players;

    @Before
    public void setup() throws IOException {
        controllerChan = TestDummy.getDummyControllerChan();
        aiControl = controllerChan.getAiController();
        aiControl.setAI(DECISION_BASED_AI); //getestet wird mit der DECISION_BASES_AI
        final String testMapString = new String(Files.readAllBytes(Paths.get("resources/maps/island_of_death.map")), StandardCharsets.UTF_8);
        final MapBlackWhite tiles = MapUtil.readBlackWhiteMapFromString(testMapString);
        players = new LinkedList<Triple<PlayerType,String, Boolean>>() {{
            add(new Triple<>(PlayerType.EXPLORER,"", true));
            add(new Triple<>(PlayerType.PILOT,"", true));
            add(new Triple<>(PlayerType.DIVER,"", true));
            add(new Triple<>(PlayerType.COURIER,"", true));
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
    public void getDrainablePositionsOneMoveAway() throws IOException {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        MapFull fixMap = MapUtil.readFullMapFromString(testMapString);
        String testArtifactCardString = new String(Files.readAllBytes(Paths.get(GamePreparationsViewController.DEV_ARTIFACT_STACK_FOLDER)), StandardCharsets.UTF_8);
        CardStack<ArtifactCard> artifactCardStack = CardStackUtil.readArtifactCardStackFromString(testArtifactCardString);
        String testFloodCardString = new String(Files.readAllBytes(Paths.get(GamePreparationsViewController.DEV_FLOOD_STACK_FOLDER)), StandardCharsets.UTF_8);
        CardStack<FloodCard>  floodCardStack = CardStackUtil.readFloodCardStackFromString(testFloodCardString);
        Triple<MapFull, CardStack<ArtifactCard>, CardStack<FloodCard>> tournamentTriple = new Triple<>(fixMap, artifactCardStack, floodCardStack);
        
        controllerChan.startNewGame("fixMapFull", tournamentTriple, players, Difficulty.NORMAL);

        MapTile[][] completeCard = aiControl.getCurrentAction().getMap().raw();
        /*
         * Test für Explorer
         */
        Point explorersStartPosition = aiControl.getTile(PlayerType.EXPLORER).getLeft();
        //setze alle MapTiles auf DRY, um den Test zu starten
        for (MapTile[] mapTiles3 : completeCard) {
            for (int j = 0; j < mapTiles3.length; j++) {
                if (mapTiles3[j] != null) {
                    mapTiles3[j].setState(MapTileState.DRY);
                }
            }
        }
        /*
         * untersuche, ob das in einem Schritt nordwestliche MapTile und in zwei Schritten nordwestliche MapTile belegt sind
         * wenn, dann Status nordwestliches MapTile DRY, Status in zwei Schritten nordwestliches MapTile FLOODED        
         */
        
        
        if(aiControl.getTile(explorersStartPosition.add(UP).add(LEFT)) != null 
                && aiControl.getTile(explorersStartPosition.add(UP).add(UP).add(LEFT).add(LEFT)) != null){
            aiControl.getTile(explorersStartPosition.add(UP).add(LEFT)).setState(MapTileState.DRY);
            MapTile northWesternNextNeighbour = aiControl.getTile(explorersStartPosition.add(UP).add(UP).add(LEFT).add(LEFT));
            Point northWesternNextNeighbourPosition = explorersStartPosition.add(UP).add(UP).add(LEFT).add(LEFT);
            northWesternNextNeighbour.setState(MapTileState.FLOODED);
            assertTrue("Explorer kann MapTile zwei Schritte nach Nordwest nicht im nächsten Zug trockenlegen", 
            aiControl.getDrainablePositionsOneMoveAway(explorersStartPosition, PlayerType.EXPLORER).contains(northWesternNextNeighbourPosition));
        }
        /*
         * untersuche, ob das in einem Schritt nordöstliche MapTile und in zwei Schritten nordöstliche MapTile belegt sind
         * wenn, dann Status nordöstliches MapTile DRY, Status in zwei Schritten nordöstliches MapTile FLOODED        
         */
        if(aiControl.getTile(explorersStartPosition.add(UP).add(RIGHT)) != null 
                && aiControl.getTile(explorersStartPosition.add(UP).add(UP).add(RIGHT).add(RIGHT)) != null){
            aiControl.getTile(explorersStartPosition.add(UP).add(RIGHT)).setState(MapTileState.DRY);
            MapTile northEasternNextNeighbour = aiControl.getTile(explorersStartPosition.add(UP).add(UP).add(RIGHT).add(RIGHT));
            Point northEasternNextNeighbourPosition = explorersStartPosition.add(UP).add(UP).add(RIGHT).add(RIGHT);
            northEasternNextNeighbour.setState(MapTileState.FLOODED);
            assertTrue("Explorer kann MapTile zwei Schritte nach Nordost nicht im nächsten Zug trockenlegen", 
            aiControl.getDrainablePositionsOneMoveAway(explorersStartPosition, PlayerType.EXPLORER).contains(northEasternNextNeighbourPosition));
        }
        
        explorersStartPosition = new Point(2,0);
        MapTile current = aiControl.getTile(explorersStartPosition);
        /*
         * untersuche, ob das in einem Schritt südöstliche MapTile und in zwei Schritten südöstliche MapTile belegt sind
         * wenn, dann Status südöstliches MapTile DRY, Status in zwei Schritten südöstliches MapTile FLOODED        
         */
        if(aiControl.getTile(explorersStartPosition.add(DOWN).add(RIGHT)) != null 
                && aiControl.getTile(explorersStartPosition.add(DOWN).add(DOWN).add(RIGHT).add(RIGHT)) != null){
            aiControl.getTile(explorersStartPosition.add(DOWN).add(RIGHT)).setState(MapTileState.DRY);
            MapTile southEasternNextNeighbour = aiControl.getTile(explorersStartPosition.add(DOWN).add(DOWN).add(RIGHT).add(RIGHT));
            Point southEasternNextNeighbourPosition = explorersStartPosition.add(DOWN).add(DOWN).add(RIGHT).add(RIGHT);
            southEasternNextNeighbour.setState(MapTileState.FLOODED);
            assertTrue("Explorer kann MapTile zwei Schritte nach Südost nicht im nächsten Zug trockenlegen", 
            aiControl.getDrainablePositionsOneMoveAway(explorersStartPosition, PlayerType.EXPLORER).contains(southEasternNextNeighbourPosition));
        }
        /*
         * untersuche, ob das in einem Schritt südwestliche MapTile und in zwei Schritten südwestliche MapTile belegt sind
         * wenn, dann Status südwestliches MapTile DRY, Status in zwei Schritten südwestliches MapTile FLOODED        
         */
        if(aiControl.getTile(explorersStartPosition.add(DOWN).add(LEFT)) != null 
                && aiControl.getTile(explorersStartPosition.add(DOWN).add(DOWN).add(LEFT).add(LEFT)) != null){
            aiControl.getTile(explorersStartPosition.add(DOWN).add(LEFT)).setState(MapTileState.DRY);
            MapTile southWesternNextNeighbour = aiControl.getTile(explorersStartPosition.add(DOWN).add(DOWN).add(LEFT).add(LEFT));
            Point southWesternNextNeighbourPosition = explorersStartPosition.add(DOWN).add(DOWN).add(LEFT).add(LEFT);
            southWesternNextNeighbour.setState(MapTileState.FLOODED);
            assertTrue("Explorer kann MapTile zwei Schritte nach Südwest nicht im nächsten Zug trockenlegen", 
            aiControl.getDrainablePositionsOneMoveAway(explorersStartPosition, PlayerType.EXPLORER).contains(southWesternNextNeighbourPosition));
        }
        List<Point> explorersNeighboursPositions = explorersStartPosition.getSurrounding();
        List<MapTile> explorersNeighbours = explorersStartPosition.getSurrounding().stream().map(aiControl::getTile).collect(Collectors.toList());
        List<MapTile> testList = new LinkedList<>();
        //setze alle MapTiles auf DRY, um den Test zurückzusetzen
        for (MapTile[] mapTiles2 : completeCard) {
            for (int j = 0; j < mapTiles2.length; j++) {
                if (mapTiles2[j] != null) {
                    mapTiles2[j].setState(MapTileState.DRY);
                }
            }
        }
        /*
         * setze alle surroundingTiles der Nachbarn vom ExplorerMapTile auf FLOODED
         * dazu gehören auch Nachbarn des ExplorerMapTile
         */
        
        for(Point explorersNeighboursPoint : explorersNeighboursPositions){
            List<MapTile> explorersNextNeighbours = explorersNeighboursPoint.getSurrounding(new Point (0, 0), new Point (MapFull.SIZE_X-1, MapFull.SIZE_Y-1))
                    .stream().map(aiControl::getTile).collect(Collectors.toList());
            for(MapTile explorersNextNeighbour : explorersNextNeighbours){
                if (explorersNextNeighbour != null){
                    explorersNextNeighbour.setState(MapTileState.FLOODED);
                    testList.add(explorersNextNeighbour);
                }
            }
        }
        /*
         * setze alle Nachbarn des ExplorerMapTiles auf DRY, 
         * damit nur die erst in einem Schritt erreichbaren MapTiles FLOODED sind  
         */
        for(MapTile neighbour : explorersNeighbours){
            if (neighbour != null) 
                neighbour.setState(MapTileState.DRY);
                testList.remove(neighbour);
        }
        List<Point> testListPoints = testList.stream().map(tile -> aiControl.getCurrentAction().getMap().getPositionForTile(tile.getProperties())).collect(Collectors.toList());
        assertTrue(testListPoints.containsAll(aiControl.getDrainablePositionsOneMoveAway(explorersStartPosition, PlayerType.EXPLORER)));
        /*
         * Test für Pilot:
         * 
         */
        Point landingSitePosition = aiControl.getTile(PlayerType.PILOT).getLeft();
        MapTile landingSite = aiControl.getTile(PlayerType.PILOT).getRight();

        for (MapTile[] mapTiles1 : completeCard) {
            for (int j = 0; j < mapTiles1.length; j++) {
                if (mapTiles1[j] != null) {
                    mapTiles1[j].setState(MapTileState.DRY);
                }
            }
        }
        assertTrue("Pilot kann Felder trockenlegen, obwohl alle MapTiles DRY sind",
                aiControl.getDrainablePositionsOneMoveAway(landingSitePosition, PlayerType.PILOT).isEmpty());  
//        for (int i = 0; i < completeCard.length; i++){
//            for (int j = 0; j < completeCard[i].length; j++){
//                if (completeCard[i][j] != null){
//                    completeCard[i][j].setState(MapTileState.FLOODED);;
//                }
//            }
//        }
        
      //setze alle MapTiles auf DRY, um den Test zu starten
        for (MapTile[] element : completeCard) {
            for (int j = 0; j < element.length; j++) {
                if (element[j] != null) {
                    element[j].setState(MapTileState.DRY);
                }
            }
        }
        
        Point pilotSpawn = fixMap.getPlayerSpawnPoint(PILOT);
        List<Point> pilotsNeighbours = pilotSpawn.getNeighbours();
        
        for (Point currentNeighbour : pilotsNeighbours) {
            aiControl.getTile(currentNeighbour).setState(FLOODED);
        }
        
        assertTrue("Pilot kann Felder trockenlegen, obwohl er alle MapTiles sofort erreichen kann",
                aiControl.getDrainablePositionsOneMoveAway(landingSitePosition, PlayerType.PILOT).isEmpty());
        for (MapTile[] item : completeCard) {
            for (int j = 0; j < item.length; j++) {
                if (item[j] != null) {
                    item[j].setState(MapTileState.GONE);
                }
            }
        }
        landingSite.setState(MapTileState.DRY);
        assertTrue("Pilot kann Felder trockenlegen, obwohl alle MapTiles bis auf die LandingSite GONE sind",
                aiControl.getDrainablePositionsOneMoveAway(landingSitePosition, PlayerType.PILOT).isEmpty());
        
        ///setze alle MapTiles auf DRY, um den Test zu starten
        Point diversStartPosition = aiControl.getTile(PlayerType.DIVER).getLeft();

        for (MapTile[] value : completeCard) {
            for (int j = 0; j < value.length; j++) {
                if (value[j] != null) {
                    value[j].setState(MapTileState.DRY);
                }
            }
        }
        assertTrue("Taucher kann Felder trockenlegen, obwohl alle MapTiles DRY sind",
                aiControl.getDrainablePositionsOneMoveAway(diversStartPosition, PlayerType.DIVER).isEmpty());
        for (MapTile[] tiles : completeCard) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[j] != null) {
                    tiles[j].setState(FLOODED);
                }
            }
        }        
        assertTrue("Taucher kann Felder trockenlegen, obwohl er alle MapTiles sofort erreichen kann",
                aiControl.getDrainablePositionsOneMoveAway(diversStartPosition, PlayerType.DIVER).isEmpty());

        for (MapTile[] mapTiles : completeCard) {
            for (int j = 0; j < mapTiles.length; j++) {
                if (mapTiles[j] != null) {
                    mapTiles[j].setState(MapTileState.GONE);
                }
            }
        }
        landingSite.setState(MapTileState.DRY);
        assertTrue("Taucher kann Felder trockenlegen, obwohl alle MapTiles bis auf die Position des Tauchers GONE sind",
                aiControl.getDrainablePositionsOneMoveAway(diversStartPosition, PlayerType.DIVER).isEmpty());
       
        
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