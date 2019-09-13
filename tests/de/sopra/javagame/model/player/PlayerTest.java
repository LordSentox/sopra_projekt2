package de.sopra.javagame.model.player;

import de.sopra.javagame.model.Action;
import de.sopra.javagame.model.Difficulty;
import de.sopra.javagame.model.MapTile;
import de.sopra.javagame.model.MapTileState;
import de.sopra.javagame.util.Direction;
import de.sopra.javagame.util.MapUtil;
import de.sopra.javagame.util.Pair;
import de.sopra.javagame.util.Point;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest
{

    MapTile[][] testMap;

    @Before
    public void setUp() throws Exception
    {
        String testMapString = new String( Files.readAllBytes( Paths.get( "resources/full_maps/test.extmap" ) ), StandardCharsets.UTF_8 );
        int[][] testMapNumbers = MapUtil.readNumberMapFromString( testMapString );
        this.testMap = MapUtil.createMapFromNumbers( testMapNumbers );
    }

        int PilotStart = 1;
        int NavigatorStart = 12;
    @Test
    public void legalMoves()
    {
        Action action = Action.createInitialAction( Difficulty.NORMAL,
                Arrays.asList( new Pair<>( PlayerType.PILOT, false ),
                        new Pair<>( PlayerType.NAVIGATOR, false ) ),
                this.testMap );

        Player pilot = action.getPlayers().get( 0 );
        Player navigator = action.getPlayers().get( 1 );
        Point pilotPos = MapUtil.getPlayerSpawnPoint(testMap, PlayerType.PILOT);
        Point navigatorPos = MapUtil.getPlayerSpawnPoint(testMap, PlayerType.PILOT);

        // teste Rückgabe von legalMoves mit 4 trockenen Inselfeldern
       List<Point> legalMovesPilotNoSpecial = pilot.legalMoves(false);
       for (Point currentPos : legalMovesPilotNoSpecial) {
           assertTrue("legale Felder sind niemals versunken!", testMap[currentPos.yPos][currentPos.xPos].getState() != MapTileState.GONE);
       }
       assertEquals("der Pilot kann sich zu 4 Feldern bewegen", 4, legalMovesPilotNoSpecial.size());

       //teste mit 4 FLOODED Feldern
        testMap[pilotPos.yPos+1][pilotPos.xPos].flood();
        testMap[pilotPos.yPos-1][pilotPos.xPos].flood();
        testMap[pilotPos.yPos][pilotPos.xPos+1].flood();
        testMap[pilotPos.yPos][pilotPos.xPos-1].flood();
        legalMovesPilotNoSpecial = pilot.legalMoves(false);
        for (Point currentPos : legalMovesPilotNoSpecial) {
            assertTrue("legale Felder sind niemals versunken!", testMap[currentPos.yPos][currentPos.xPos].getState() != MapTileState.GONE);
        }
        assertEquals("der Pilot kann sich zu 4 Feldern bewegen", 4, legalMovesPilotNoSpecial.size());



        //teste mit 2 GONE Feldern
        testMap[pilotPos.yPos+1][pilotPos.xPos].flood();
        testMap[pilotPos.yPos-1][pilotPos.xPos].flood();
        legalMovesPilotNoSpecial = pilot.legalMoves(false);
        for (Point currentPos : legalMovesPilotNoSpecial) {
            assertTrue("legale Felder sind niemals versunken!", testMap[currentPos.yPos][currentPos.xPos].getState() != MapTileState.GONE);
        }
        assertEquals("der Pilot kann sich zu 2 Feldern bewegen", 2, legalMovesPilotNoSpecial.size());

        //teste mit Pilot Special Move
        List<Point> legalMovesPilotSpecialActive = pilot.legalMoves(true);
        for (Point currentPos : legalMovesPilotSpecialActive) {
            assertTrue("legale Felder sind niemals versunken!", testMap[currentPos.yPos][currentPos.xPos].getState() != MapTileState.GONE);
        }
        assertEquals("der Pilot kann sich zu 22 Feldern bewegen", 22, legalMovesPilotSpecialActive.size());


        // Teste mit Navigator, 2 null Feldern und einem GONE
        navigator.setPosition(new Point(7,4));
        List<Point> legalMovesNavigatorNoSpecial = navigator.legalMoves(false);
        for (Point currentPos : legalMovesNavigatorNoSpecial) {
            assertTrue("legale Felder sind niemals versunken!", testMap[currentPos.yPos][currentPos.xPos].getState() != MapTileState.GONE);
        }
        assertEquals("der Navigator kann sich zu 1 Feld bewegen", 1, legalMovesNavigatorNoSpecial.size());
    }

    @Test
    public void move()
    {

    }

    @Test
    public void canMoveOthers()
    {
        fail( "Not yet implemented" );
    }

    @Test
    public void forcePush()
    {
        Action action = Action.createInitialAction( Difficulty.NORMAL,
                Arrays.asList( new Pair<>( PlayerType.PILOT, false ),
                        new Pair<>( PlayerType.NAVIGATOR, false ) ),
                this.testMap );

        Player pilot = action.getPlayers().get( 0 );
        Player navigator = action.getPlayers().get( 1 );

        Point navigatorPos = new Point(navigator.getPosition());
        // Teste force push mit ungültigem Spieler
        assertFalse("Der Pilot darf niemanden einfach bewegen!", pilot.forcePush(Direction.DOWN, navigator));
        assertEquals("Der Navigator hätte nicht bewegt werden dürfen!", navigatorPos, navigator.getPosition());

        // Teste einen gültigen force-push des Navigators auf ein Inselfeld der Karte
        action.nextPlayerActive();
        Point oldPos = new Point( pilot.getPosition() ); // Position des Piloten
        assertTrue( "Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush( Direction.UP, pilot ) );
        assertEquals( "Spieler wurde nicht wirklich bewegt", oldPos.xPos, pilot.getPosition().xPos );
        assertEquals( "Spieler wurde nicht wirklich bewegt", oldPos.yPos - 1, pilot.getPosition().yPos );

        // Der Spieler soll keine Aktionen mehr haben, damit extraPush auch notwendig ist
        navigator.setActionsLeft( 0 );

        // Der zweite muss auch noch gültig sein, denn es handelt sich um den extra-push
        oldPos = new Point( pilot.getPosition() ); // Position des Piloten
        assertTrue( "Konnte einen Spieler nicht bewegen, obwohl der Zug legal ist", navigator.forcePush( Direction.RIGHT, pilot ) );
        assertEquals("Der Navigator sollte 0 Actions übrig haben", 0, navigator.getActionsLeft());
        assertEquals( "Spieler wurde nicht wirklich bewegt", oldPos.xPos + 1, pilot.getPosition().xPos );
        assertEquals( "Spieler wurde nicht wirklich bewegt", oldPos.yPos, pilot.getPosition().yPos );

        //Der Navigator sollte nun seine zweite forcePush Option, aber keine Aktion verbrauchen
        // Da der Spieler keine Aktionen mehr hat muss der dritte forcePush fehlschlagen
        oldPos = new Point( pilot.getPosition() );
        assertFalse( "Spieler wurde bewegt, obwohl der Navigator keine Aktion dafür hat", navigator.forcePush( Direction.RIGHT, pilot ) );
        assertEquals( "Spieler wurde bewegt, obwohl er nicht sollte", oldPos, pilot.getPosition() );

        // Teste force Push vom Navigator mit null-MapTile
        pilot.setPosition(new Point (2,1));
        oldPos = new Point (pilot.getPosition());
        assertFalse("Das Feld war null! Bewegen war nicht möglich", navigator.forcePush(Direction.LEFT, pilot));
        assertEquals("Das Feld war null. Der Pilot wäre in den Abyss gestürzt", oldPos, pilot.getPosition());

        // Teste force Push vom Navigator mit GONE Maptile
        pilot.setPosition(new Point (2,2));
        testMap[1][2].flood();
        testMap[1][2].flood();
        oldPos = new Point (pilot.getPosition());
        assertFalse("Das Feld war versunken! Bewegen war nicht möglich", navigator.forcePush(Direction.UP, pilot));
        assertEquals("Das Feld war versunken. Der Pilot wäre kläglich ertrunken", oldPos, pilot.getPosition());
    }

    @Test
    public void drainablePositions()
    {
        fail( "Not yet implemented" );
    }

    @Test
    public void drain()
    {
        fail( "Not yet implemented" );
    }

    @Test
    public void collectArtifact()
    {
        fail( "Not yet implemented" );
    }

    @Test
    public void legalReceivers()
    {
        fail( "Not yet implemented" );
    }

    @Test
    public void getType()
    {
        fail( "Not yet implemented" );
    }

    @Test
    public void copyTest()
    {
        //TODO test all copy methods of ALL player types, testing each player is unnecessary
        fail( "Not yet implemented" );
    }

}