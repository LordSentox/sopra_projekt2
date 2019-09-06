package de.sopra.javagame.control;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.ArtifactCard;
import de.sopra.javagame.model.ArtifactCardType;
import de.sopra.javagame.model.CardStack;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.model.player.Courier;
import de.sopra.javagame.model.player.Explorer;

public class InGameUserControllerTest {

    private ControllerChan controllerChan; 
    private MapController mapController;
    private JavaGame javaGame;
    private Turn turn;
    private ArtifactCard airCard;
    private ArtifactCard earthCard;
    private ArtifactCard waterCard;
    private ArtifactCard fireCard;
    private ArtifactCard sandCard;
    private ArtifactCard heliCard;
    private Courier courier;
    private Explorer explorer;
    private CardStack<ArtifactCard> artifactCardStack ;
    
    @Before
    public void setUp() {
        controllerChan = TestDummy.getDummyControllerChan();
        mapController = controllerChan.getMapController();
        javaGame = controllerChan.getJavaGame();
        turn = javaGame.getCurrentTurn();
        artifactCardStack = turn.getArtifactCardStack();
        List<ArtifactCard> cardList = artifactCardStack.draw(28, false);
        
        for (ArtifactCard cur : cardList){
            if(airCard != null && earthCard != null && waterCard != null && fireCard != null && sandCard != null && heliCard != null){
                break;
            }
            
            if(cur.getType() == ArtifactCardType.AIR){
                airCard = cur;
            }
            if(cur.getType() == ArtifactCardType.EARTH){
                earthCard = cur;
            }
            if(cur.getType() == ArtifactCardType.WATER){
                waterCard = cur;
            }
            if(cur.getType() == ArtifactCardType.FIRE){
                fireCard = cur;
            }
            if(cur.getType() == ArtifactCardType.SANDBAGS){
                sandCard = cur;
            }
            if(cur.getType() == ArtifactCardType.HELICOPTER){
                heliCard = cur;
            }
        }
    }
    @Test
    public void testPlayHelicopterCard() {
        fail("Not yet implemented");
    }

    @Test
    public void testPlaySandbagCard() {
        fail("Not yet implemented");
    }

    @Test
    public void testDiscardCard() {
        fail("Not yet implemented");
    }

}
