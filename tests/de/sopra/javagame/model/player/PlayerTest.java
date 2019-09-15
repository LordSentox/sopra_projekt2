package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.*;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static de.sopra.javagame.util.Direction.*;
import static org.junit.Assert.*;

public class PlayerTest {
    MapFull testMap;

    @Before
    public void setUp() throws Exception {
        String testMapString = new String(Files.readAllBytes(Paths.get("resources/full_maps/test.extmap")), StandardCharsets.UTF_8);
        this.testMap = MapUtil.readFullMapFromString(testMapString);
    }

    @Test
    public void legalMoves() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);

        Player pilot = action.getPlayers().get(0);
        Player navigator = action.getPlayers().get(1);
        Point pilotPos = testMap.getPlayerSpawnPoint(PlayerType.PILOT);

        // teste Rückgabe von legalMoves mit 4 trockenen Inselfeldern
        List<Point> legalMovesPilotNoSpecial = pilot.legalMoves(false);
        for (Point currentPos : legalMovesPilotNoSpecial) {
            assertTrue("legale Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
        }
        assertEquals("der Pilot kann sich zu 4 Feldern bewegen", 4, legalMovesPilotNoSpecial.size());

        //teste mit 4 FLOODED Feldern
        for (Point neighbour : pilotPos.getNeighbours()) {
            testMap.get(neighbour).flood();
        }

        legalMovesPilotNoSpecial = pilot.legalMoves(false);
        for (Point currentPos : legalMovesPilotNoSpecial) {
            assertTrue("legale Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
        }
        assertEquals("der Pilot kann sich zu 4 Feldern bewegen", 4, legalMovesPilotNoSpecial.size());


        //teste mit 2 GONE Feldern
        testMap.get(pilotPos.add(0,  1)).flood();
        testMap.get(pilotPos.add(0, -1)).flood();

        legalMovesPilotNoSpecial = pilot.legalMoves(false);
        for (Point currentPos : legalMovesPilotNoSpecial) {
            assertTrue("legale Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
        }
        assertEquals("der Pilot kann sich zu 2 Feldern bewegen", 2, legalMovesPilotNoSpecial.size());

        //teste mit Pilot Special Move
        List<Point> legalMovesPilotSpecialActive = pilot.legalMoves(true);
        for (Point currentPos : legalMovesPilotSpecialActive) {
            assertTrue("legale Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
        }
        assertEquals("der Pilot kann sich zu 22 Feldern bewegen", 22, legalMovesPilotSpecialActive.size());

        // Teste mit Navigator, 2 null Feldern und einem GONE
        navigator.setPosition(new Point(6, 3));
        List<Point> legalMovesNavigatorNoSpecial = navigator.legalMoves(false);
        for (Point currentPos : legalMovesNavigatorNoSpecial) {
            assertTrue("legale Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
        }
        assertEquals("der Navigator kann sich zu 1 Feld bewegen", 1, legalMovesNavigatorNoSpecial.size());
    }

    @Test
    public void move() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);

        Player pilot = action.getPlayers().get(0);
        Point pilotPos = testMap.getPlayerSpawnPoint(PlayerType.PILOT);

        assertEquals("", 3, pilot.getActionsLeft());

        // teste pilot bewegen ohne Special, gültiges Ziel, alles trocken
        Point oldPilotPos = new Point(pilot.getPosition());
        Point destination = new Point(oldPilotPos.xPos + 1, oldPilotPos.yPos);
        assertTrue("Der Pilot hätte sich bewegen dürfen müssen", pilot.move(destination, true, false));
        assertEquals("Der Pilot hätte laufen müssen", destination, pilot.getPosition());
        assertEquals("", 2, pilot.getActionsLeft());
        pilot.move(oldPilotPos, false, false);

        //teste pilot bewegen ohne Special, gültiges Ziel, alles FLOODED
        for (Point neighbour : pilotPos.getNeighbours()) {
            testMap.get(neighbour).flood();
        }
        destination.setLocation(pilotPos.xPos + 1, pilotPos.yPos);
        pilot = action.getActivePlayer();
        assertTrue("Der Pilot hätte sich bewegen dürfen müssen", pilot.move(destination, true, false));
        assertEquals("Der Pilot hätte laufen müssen", destination, pilot.getPosition());
        pilot.move(oldPilotPos, false, false);
        assertEquals("", 1, pilot.getActionsLeft());

        pilot.setActionsLeft(3);
        //teste pilot bewegen ohne Special, gültiges Ziel, zwei GONE
        testMap.get(pilotPos.add(UP)).flood();
        testMap.get(pilotPos.add(DOWN)).flood();
        destination = oldPilotPos.add(RIGHT);
        pilot = action.getActivePlayer();
        assertTrue("Der Pilot hätte sich bewegen dürfen müssen", pilot.move(destination, true, false));
        assertEquals("Der Pilot hätte laufen müssen", destination, pilot.getPosition());
        pilot.move(oldPilotPos, false, false);
        assertEquals("", 2, pilot.getActionsLeft());

        //teste pilot bewegen ohne Special, ungültiges Ziel (zu weit weg)
        oldPilotPos = new Point(pilotPos);
        destination = new Point(1, 2);
        pilot = action.getActivePlayer();
        assertFalse("Der Pilot hätte sich nicht bewegen dürfen", pilot.move(destination, true, false));
        assertEquals("Der Pilot hätte sich nicht bewegen dürfen", oldPilotPos, pilot.getPosition());
        assertEquals("", 2, pilot.getActionsLeft());

        //teste pilot bewegen ohne Special, ungültiges Ziel (null)
        oldPilotPos = new Point(pilotPos);
        destination = new Point(0, 0);
        pilot = action.getActivePlayer();
        assertFalse("Der Pilot hätte sich nicht bewegen dürfen", pilot.move(destination, true, false));
        assertEquals("Der Pilot hätte sich nicht bewegen dürfen", oldPilotPos, pilot.getPosition());
        assertEquals("", 2, pilot.getActionsLeft());

        //teste pilot bewegen ohne Special, ungültiges Ziel (GONE)
        oldPilotPos = new Point(pilotPos);
        destination = pilotPos.add(DOWN);
        pilot = action.getActivePlayer();
        assertFalse("Der Pilot hätte sich nicht bewegen dürfen", pilot.move(destination, true, false));
        assertEquals("Der Pilot hätte sich nicht bewegen dürfen", oldPilotPos, pilot.getPosition());
        assertEquals("", 2, pilot.getActionsLeft());

        pilot.setActionsLeft(0);
        //teste pilot bewegen ohne Special, keine Actions mehr
        oldPilotPos = new Point(pilotPos);
        pilot.setPosition(testMap.getPlayerSpawnPoint(PlayerType.PILOT));
        pilot = action.getActivePlayer();
        destination = pilotPos.add(RIGHT);
        assertFalse("Der Pilot hätte sich nicht bewegen dürfen", pilot.move(destination, true, false));
        assertEquals("Der Pilot hätte sich nicht bewegen dürfen", oldPilotPos, pilot.getPosition());
        assertEquals("", 0, pilot.getActionsLeft());
    }

    @Test
    public void canMoveOthers() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);
        Action secondAction = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.COURIER, false),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.ENGINEER, false),
                        new Pair<>(PlayerType.EXPLORER, false)),
                this.testMap);

        Pilot pilot = (Pilot) action.getPlayer(PlayerType.PILOT);
        Diver diver = (Diver) secondAction.getPlayer(PlayerType.DIVER);
        Engineer engineer = (Engineer) secondAction.getPlayer(PlayerType.ENGINEER);
        Explorer explorer = (Explorer) secondAction.getPlayer(PlayerType.EXPLORER);
        Courier courier = (Courier) secondAction.getPlayer(PlayerType.COURIER);
        Navigator navigator = (Navigator) action.getPlayer(PlayerType.NAVIGATOR);
        //teste für normale Spieler
        assertFalse("Der Pilot darf keine anderen Spieler bewegen", pilot.canMoveOthers());
        assertFalse("Der Taucher darf keine anderen Spieler bewegen", diver.canMoveOthers());
        assertFalse("Der Ingenier darf keine anderen Spieler bewegen", engineer.canMoveOthers());
        assertFalse("Der Forscher darf keine anderen Spieler bewegen", explorer.canMoveOthers());
        assertFalse("Der Bote darf keine anderen Spieler bewegen", courier.canMoveOthers());

        //teste für Navigator
        assertTrue("Der Navigator soll andere bewegen können", navigator.canMoveOthers());
    }

    @Test
    public void forcePush() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);

        Player pilot = action.getPlayers().get(0);
        Player navigator = action.getPlayers().get(1);

        Point navigatorPos = new Point(navigator.getPosition());
        // Teste force push mit ungültigem Spieler
        assertFalse("Der Pilot darf niemanden einfach bewegen!", pilot.forcePush(DOWN, navigator));
        assertEquals("Der Navigator hätte nicht bewegt werden dürfen!", navigatorPos, navigator.getPosition());

        // Teste einen gültigen force-push des Navigators auf ein Inselfeld der Karte
        action.nextPlayerActive();
        Point oldPos = new Point(pilot.getPosition()); // Position des Piloten
        assertTrue("Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush(UP, pilot));
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.xPos, pilot.getPosition().xPos);
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.yPos - 1, pilot.getPosition().yPos);

        // Der Spieler soll keine Aktionen mehr haben, damit extraPush auch notwendig ist
        navigator.setActionsLeft(0);

        // Der zweite muss auch noch gültig sein, denn es handelt sich um den extra-push
        oldPos = new Point(pilot.getPosition()); // Position des Piloten
        assertTrue("Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush(RIGHT, pilot));
        assertEquals("Der Navigator sollte 0 Actions übrig haben", 0, navigator.getActionsLeft());
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.xPos + 1, pilot.getPosition().xPos);
        assertEquals("Spieler wurde nicht wirklich bewegt", oldPos.yPos, pilot.getPosition().yPos);

        //Der Navigator sollte nun seine zweite forcePush Option, aber keine Aktion verbrauchen
        // Da der Spieler keine Aktionen mehr hat muss der dritte forcePush fehlschlagen
        oldPos = new Point(pilot.getPosition());
        assertFalse("Spieler wurde bewegt, obwohl der Navigator keine Aktion dafür hat", navigator.forcePush(RIGHT, pilot));
        assertEquals("Spieler wurde bewegt, obwohl er nicht sollte", oldPos, pilot.getPosition());

        // Teste force Push vom Navigator mit null-MapTile
        pilot.setPosition(new Point(1, 0));
        oldPos = new Point(pilot.getPosition());
        assertFalse("Das Feld war null! Bewegen war nicht möglich", navigator.forcePush(Direction.LEFT, pilot));
        assertEquals("Das Feld war null. Der Pilot wäre in den Abyss gestürzt", oldPos, pilot.getPosition());

        // Teste force Push vom Navigator mit GONE Maptile
        pilot.setPosition(new Point(1, 1));
        IntStream.range(0, 2).forEach(i -> testMap.get(1, 0).flood());

        oldPos = new Point(pilot.getPosition());
        assertFalse("Das Feld war versunken! Bewegen war nicht möglich", navigator.forcePush(UP, pilot));
        assertEquals("Das Feld war versunken. Der Pilot wäre kläglich ertrunken", oldPos, pilot.getPosition());
    }

    @Test
    public void drainablePositions() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.EXPLORER, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);

        Player explorer = action.getPlayers().get(0);
        Player navigator = action.getPlayers().get(1);
        Point navigatorPos = testMap.getPlayerSpawnPoint(PlayerType.NAVIGATOR);

        // teste Rückgabe von drainablePositions  mit 4 trockenen Inselfeldern
        List<Point> drainablePositionsNavigator = navigator.drainablePositions();
       assertTrue("trockene Felder können nicht trockengelegt werden", drainablePositionsNavigator.isEmpty());

        //teste mit 4 FLOODED Feldern
        for (Point neighbour : navigatorPos.getNeighbours()) {
            testMap.get(neighbour).flood();
        }
        drainablePositionsNavigator = navigator.drainablePositions();
        for (Point currentPos : drainablePositionsNavigator) {
            assertTrue("Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
            assertTrue("Felder sind niemals trocken!", testMap.get(currentPos).getState() != MapTileState.DRY);
        }
        assertEquals("der Navigator kann 4 Felder trockenlegen", 4, drainablePositionsNavigator.size());


        //teste mit 2 GONE Feldern
        testMap.get(navigatorPos.add(UP)).flood();
        testMap.get(navigatorPos.add(DOWN)).flood();

        drainablePositionsNavigator = navigator.drainablePositions();
        for (Point currentPos : drainablePositionsNavigator) {
            assertTrue("Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
            assertTrue("Felder sind niemals trocken!", testMap.get(currentPos).getState() != MapTileState.DRY);
        }
        assertEquals("der Navigator kann 2 Felder trockenlegen", 2, drainablePositionsNavigator.size());

        // Teste mit explorer special move
        testMap.get(navigatorPos.add(DOWN).add(RIGHT)).flood();
        testMap.get(navigatorPos.add(UP).add(LEFT)).flood();
        testMap.get(navigatorPos.add(UP).add(RIGHT)).flood();

        //testMap[navigatorPos.yPos + 1][navigatorPos.xPos - 1].flood();
        explorer.setPosition(navigatorPos);
        List<Point> drainablePositionsExplorerSpecial = explorer.drainablePositions();
        for (Point currentPos : drainablePositionsExplorerSpecial) {
            assertTrue("Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
            assertTrue("Felder sind niemals trocken!", testMap.get(currentPos).getState() != MapTileState.DRY);
        }
        assertEquals("der Explorer kann 5 Felder trockenlegen", 5, drainablePositionsExplorerSpecial.size());


        // Teste mit Navigator, 2 null Feldern und einem GONE
        navigator.setPosition(new Point(6, 3));
        testMap.get(navigator.getPosition()).flood();
        drainablePositionsNavigator = navigator.drainablePositions();
        for (Point currentPos : drainablePositionsNavigator) {
            assertTrue("legale Felder sind niemals versunken!", testMap.get(currentPos).getState() != MapTileState.GONE);
        }
        assertEquals("der Navigator kann 1 Feld trockenlegen", 1, drainablePositionsNavigator.size());
    }

    @Test
    public void drain() {
        fail("Not yet implemented");
    }

    @Test
    public void collectArtifact() {
        fail("Not yet implemented");
    }

    @Test
    public void legalReceivers() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);
        Action secondAction = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.COURIER, false),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.ENGINEER, false),
                        new Pair<>(PlayerType.EXPLORER, false)),
                this.testMap);

        Pilot pilot = (Pilot) action.getPlayer(PlayerType.PILOT);
        Diver diver = (Diver) secondAction.getPlayer(PlayerType.DIVER);
        Engineer engineer = (Engineer) secondAction.getPlayer(PlayerType.ENGINEER);
        Explorer explorer = (Explorer) secondAction.getPlayer(PlayerType.EXPLORER);
        Courier courier = (Courier) secondAction.getPlayer(PlayerType.COURIER);
        Navigator navigator = (Navigator) action.getPlayer(PlayerType.NAVIGATOR);

        //teste courier
        assertTrue("Der Bote sollte an alle Spieler übegeben können", courier.legalReceivers().contains(diver.getType()));
        assertTrue("Der Bote sollte an alle Spieler übegeben können", courier.legalReceivers().contains(engineer.getType()));
        assertTrue("Der Bote sollte an alle Spieler übegeben können", courier.legalReceivers().contains(explorer.getType()));

        //teste Pilot & Navigator nicht gleiches Feld
        assertTrue("Der Pilot sollte niemandem etwas übergeben können", pilot.legalReceivers().isEmpty());
        assertFalse("Der Pilot sollte niemandem etwas übergeben können", pilot.legalReceivers().contains(navigator.getType()));
        assertTrue("Der Navigator sollte niemandem etwas übergeben können", navigator.legalReceivers().isEmpty());
        assertFalse("Der Navigator sollte niemandem etwas übergeben können", navigator.legalReceivers().contains(pilot.getType()));

        //teste Pilot & Navigator gleiches Feld
        pilot.setPosition(navigator.getPosition());
        assertTrue("Der Pilot sollte dem Navigator etwas übergeben können", pilot.legalReceivers().contains(navigator.getType()));
        assertTrue("Der Navigator sollte dem Piloten etwas übergeben können", navigator.legalReceivers().contains(pilot.getType()));

        //teste Engineer, Explorer & Diver alle nicht gleiches Feld
        assertTrue("Der engineer sollte niemandem etwas übergeben können", engineer.legalReceivers().isEmpty());
        assertFalse("Der engineer sollte niemandem etwas übergeben können", engineer.legalReceivers().contains(explorer.getType()));
        assertFalse("Der engineer sollte niemandem etwas übergeben können", engineer.legalReceivers().contains(courier.getType()));
        assertFalse("Der engineer sollte niemandem etwas übergeben können", engineer.legalReceivers().contains(diver.getType()));
        assertTrue("Der explorer sollte niemandem etwas übergeben können", explorer.legalReceivers().isEmpty());
        assertFalse("Der explorer sollte niemandem etwas übergeben können", explorer.legalReceivers().contains(engineer.getType()));
        assertFalse("Der explorer sollte niemandem etwas übergeben können", explorer.legalReceivers().contains(courier.getType()));
        assertFalse("Der explorer sollte niemandem etwas übergeben können", explorer.legalReceivers().contains(diver.getType()));
        assertTrue("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().isEmpty());
        assertFalse("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().contains(explorer.getType()));
        assertFalse("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().contains(engineer.getType()));
        assertFalse("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().contains(courier.getType()));

        //teste Engineer, Explorer & Diver einer nicht gleiches Feld
        engineer.setPosition(explorer.getPosition());
        assertFalse("Der engineer sollte dem Explorer etwas übergeben können", engineer.legalReceivers().isEmpty());
        assertTrue("Der engineer sollte dem Explorer etwas übergeben können", engineer.legalReceivers().contains(explorer.getType()));
        assertFalse("Der engineer sollte niemandem etwas übergeben können", engineer.legalReceivers().contains(courier.getType()));
        assertFalse("Der engineer sollte niemandem etwas übergeben können", engineer.legalReceivers().contains(diver.getType()));
        assertFalse("Der explorer sollte dem Engineer etwas übergeben können", explorer.legalReceivers().isEmpty());
        assertTrue("Der explorer sollte dem Engineer etwas übergeben können", explorer.legalReceivers().contains(engineer.getType()));
        assertFalse("Der explorer sollte niemandem etwas übergeben können", explorer.legalReceivers().contains(courier.getType()));
        assertFalse("Der explorer sollte niemandem etwas übergeben können", explorer.legalReceivers().contains(diver.getType()));
        assertTrue("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().isEmpty());
        assertFalse("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().contains(explorer.getType()));
        assertFalse("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().contains(engineer.getType()));
        assertFalse("Der diver sollte niemandem etwas übergeben können", diver.legalReceivers().contains(courier.getType()));

        //teste Engineer, Explorer & Diver alle gleiches Feld
        diver.setPosition(explorer.getPosition());
        assertFalse("Der engineer sollte allen etwas übergeben können", engineer.legalReceivers().isEmpty());
        assertTrue("Der engineer sollte dem Explorer etwas übergeben können", engineer.legalReceivers().contains(explorer.getType()));
        assertFalse("Der engineer sollte dem Courier nichts übergeben können", engineer.legalReceivers().contains(courier.getType()));
        assertTrue("Der engineer sollte dem Diver etwas übergeben können", engineer.legalReceivers().contains(diver.getType()));
        assertFalse("Der explorer sollte allen etwas übergeben können", explorer.legalReceivers().isEmpty());
        assertTrue("Der explorer sollte dem Engineer etwas übergeben können", explorer.legalReceivers().contains(engineer.getType()));
        assertFalse("Der explorer sollte dem Courier nichts übergeben können", explorer.legalReceivers().contains(courier.getType()));
        assertTrue("Der explorer sollte dem Diver etwas übergeben können", explorer.legalReceivers().contains(diver.getType()));
        assertFalse("Der diver sollte allen etwas übergeben können", diver.legalReceivers().isEmpty());
        assertTrue("Der diver sollte dem Explorer etwas übergeben können", diver.legalReceivers().contains(explorer.getType()));
        assertTrue("Der diver sollte dem Engineer etwas übergeben können", diver.legalReceivers().contains(engineer.getType()));
        assertFalse("Der diver sollte dem Courier nichts übergeben können", diver.legalReceivers().contains(courier.getType()));
    }

    @Test
    public void getType() {
        Action action = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.PILOT, false),
                        new Pair<>(PlayerType.NAVIGATOR, false)),
                this.testMap);
        Action secondAction = Action.createInitialAction(Difficulty.NORMAL,
                Arrays.asList(new Pair<>(PlayerType.COURIER, false),
                        new Pair<>(PlayerType.DIVER, false),
                        new Pair<>(PlayerType.ENGINEER, false),
                        new Pair<>(PlayerType.EXPLORER, false)),
                this.testMap);

        Pilot pilot = (Pilot) action.getPlayer(PlayerType.PILOT);
        Diver diver = (Diver) secondAction.getPlayer(PlayerType.DIVER);
        Engineer engineer = (Engineer) secondAction.getPlayer(PlayerType.ENGINEER);
        Explorer explorer = (Explorer) secondAction.getPlayer(PlayerType.EXPLORER);
        Courier courier = (Courier) secondAction.getPlayer(PlayerType.COURIER);
        Navigator navigator = (Navigator) action.getPlayer(PlayerType.NAVIGATOR);

        assertEquals("Der Pilot hätte vom Typ Pilot sein sollen", PlayerType.PILOT , pilot.getType());
        assertEquals("Der diver hätte vom Typdiver  sein sollen", PlayerType.DIVER , diver.getType());
        assertEquals("Der engineer hätte vom Typ engineer sein sollen", PlayerType.ENGINEER , engineer.getType());
        assertEquals("Der explorer hätte vom Typ explorer sein sollen", PlayerType.EXPLORER , explorer.getType());
        assertEquals("Der courier hätte vom Typ courier sein sollen", PlayerType.COURIER , courier.getType());
        assertEquals("Der navigator hätte vom Typ navigator sein sollen", PlayerType.NAVIGATOR , navigator.getType());
    }

    @Test
    public void copyTest() {
        //TODO test all copy methods of ALL player types, testing each player is unnecessary
        fail("Not yet implemented");
    }

}