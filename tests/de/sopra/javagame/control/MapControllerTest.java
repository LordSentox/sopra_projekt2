package de.sopra.javagame.control;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.Turn;

public class MapControllerTest {

	private ControllerChan controllerChan; 
	private MapController mapController;
	private JavaGame javaGame;
	private Turn turn;
	
	@Before
	public void setUp() {
		controllerChan = TestDummy.getDummyControllerChan();
		mapController = controllerChan.getMapController();
		javaGame = controllerChan.getJavaGame();
		turn = javaGame.getTurn();
	}
	
	
	@Test
	public void testGenerateMapToEditor() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadMapToEditor() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveMap() throws IllegalArgumentException {
		boolean[][] map = new boolean[12][12];
		String name = "hallo";
		
		
		//teste saveMap ohne map
		mapController.saveMap(name, null);
		
		//teste saveMap mit unvollständiger Map
		mapController.saveMap(name, map);
		
		//teste saveMap mit zu voller map
		for(int i = 1; i<11; i++){
			for(int j = 0; j<12; j++){
				map[i][j]=true;
			}	
		}
		mapController.saveMap(name, map);
		
		map = new boolean[12][12];
		//teste mit unzusammenhängender map
		for(int i = 1; i<11; i+=2){
			for(int j = 1; j<6; j++){
				map[i][j]=true;
			}	
		}
		map[1][1] = false;
		mapController.saveMap(name, map);
		
		map = new boolean[12][12];
		//teste mit erwarteter map
		for(int i = 1; i<11; i++){
			for(int j = 1; j<3; j++){
				map[i][j]=true;
			}	
		}
		for(int i = 1; i<5; i+=2){
			map[i][3]=true;
		}
		mapController.saveMap(name, map);
		
		assert.assertEquals(map, javaGame.getTurn().getTiles());
		
		//teste mit korrekter map ohne Namen
		mapController.saveMap("", map);
	}

}
