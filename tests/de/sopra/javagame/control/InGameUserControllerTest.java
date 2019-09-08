package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.*;
import de.sopra.javagame.model.player.*;
import de.sopra.javagame.util.CardStack;
import de.sopra.javagame.util.Pair;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InGameUserControllerTest {

    private ControllerChan controllerChan; 
    private MapController mapController;
    private InGameUserController inGameCont;
    private TestDummy.InGameView inGameView;
    private JavaGame javaGame;
    private Turn turn;
    private ArtifactCard fireCard;
    private ArtifactCard waterCard;
    private ArtifactCard earthCard;
    private ArtifactCard airCard;
    private ArtifactCard sandCard;
    private ArtifactCard heliCard;
    private Courier courier;
    private Explorer explorer;
    private Navigator navigator;
    private CardStack<ArtifactCard> artifactCardStack;
    private List<ArtifactCard> handCardsExpected;
    private List moveablePlayers;
    
    @Before
    public void setUp() {
        controllerChan = TestDummy.getDummyControllerChan();
        boolean [][] tiles = new boolean [12][12];
        List<Pair<PlayerType, Boolean>> players = Arrays.asList(new Pair(PlayerType.COURIER, false), 
                                                                new Pair(PlayerType.EXPLORER, false), 
                                                                new Pair(PlayerType.NAVIGATOR, false));
        controllerChan.startNewGame(tiles, players, Difficulty.NORMAL);
        mapController = controllerChan.getMapController();
        inGameCont = controllerChan.getInGameUserController();
        inGameView = (TestDummy.InGameView) controllerChan.getInGameViewAUI();
        javaGame = controllerChan.getJavaGame();
        turn = controllerChan.getCurrentTurn();
        artifactCardStack = turn.getArtifactCardStack();
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
        
        courier = new Courier("courier", new Point(1,1), turn);
        explorer = new Explorer("explorer", new Point(2,2), turn);
        navigator = new Navigator("navigator", new Point(1,1), turn);
        
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
    public void testPlayHelicopterCard() {
        //teste mit ungültigem Zielfeld(kein maptile)
        
        //teste mit ungültigem Zielfeld(versunkenes maptile)

        //teste ohne helicard
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 1, new Point(1,1), new Point(1,9), moveablePlayers);
        Assert.assertTrue("Die Spieler hätten nicht bewegt werden dürfen", 
                          inGameView.getNotifications().contains("Du hattest keine Helikopter Karte!"));
        
        //teste mit gültigen Daten und einem Spieler (Helicard wurde abgewofen)
        
        //teste mit gültigen Daten und mehreren Spielern (Helicard wurde abgewofen)
        
        //teste mit Start = Ziel
        
        //teste mit HeliCard auf HeliPlatz und nicht alle Artefakte gefunden
        
        //teste mit HeliCard auf HeliPlatz und alle Artefakte gefunden
    }

    @Test
    public void testPlaySandbagCard() {
        //teste mit ungültigem Zielfeld(kein maptile)
        
        //teste mit ungültigem Zielfeld(trockenes maptile)
        
        //teste mit ungültigem Zielfeld(versunkenes maptile)

        //teste ohne Sandcard 
        inGameCont.playHelicopterCard(PlayerType.EXPLORER, 1, new Point(1,1), new Point(1,9), moveablePlayers);
        Assert.assertTrue("Das Feld hätte nicht trockengelegt werden dürfen", 
                          inGameView.getNotifications().contains("Du hattest keine Sandsack Karte!"));
        
       
        //teste mit gültigen Daten und einem Spieler (Sandcard wurde abgewofen)
        
        //teste mit gültigen Daten und mehreren Spielern (Sandcard wurde abgewofen)
        
    }

    @Test
    public void testDiscardCard() {
        
        //teste für zu leere Hand (Anzahl Karten <= 5)
        inGameCont.discardCard(PlayerType.EXPLORER, 1);
        Assert.assertEquals(handCardsExpected, explorer.getHand());
        Assert.assertTrue("Der Spieler hätte benachrichtigt werden sollen.", 
                          inGameView.getNotifications().contains("Es darf keine Karte abgeworfen werden!"));
        
        //teste für Hand mit 6 Karten
        explorer.getHand().add(heliCard);
        handCardsExpected.add(heliCard);
        explorer.getHand().add(sandCard);
        
        inGameCont.discardCard(PlayerType.EXPLORER, 6);
        Assert.assertEquals(handCardsExpected, explorer.getHand());
        
        //teste für komplett leere Hand (= falscher Index)
        inGameCont.discardCard(PlayerType.NAVIGATOR, 1);
        Assert.assertEquals(handCardsExpected, navigator.getHand());
        Assert.assertTrue("Der Spieler hätte benachrichtigt werden sollen.", 
                          inGameView.getNotifications().contains("Es gab keine Karte zum abwerfen!"));
        
        
    }

}
