package de.sopra.javagame.control;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.TestDummy.MapEditorView;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.model.Turn;
import de.sopra.javagame.view.MapEditorViewAUI;
import junit.framework.Assert;

public class MapControllerTest {

	private ControllerChan controllerChan; 
	private MapController mapController;
	private JavaGame javaGame;
	private Turn turn;
	private MapEditorView mapEditorView;
	private boolean[][] map;
	private String name;
	
	@Before
	public void setUp() {
		controllerChan = TestDummy.getDummyControllerChan();
		mapController = controllerChan.getMapController();
		javaGame = controllerChan.getJavaGame();
		turn = javaGame.getCurrentTurn();
		mapEditorView = mapController.getMapEditorView();
		map = new boolean[12][12];
		name = "hallo";
	}
	
	
	@Test
	public void testGenerateMapToEditor() {
		mapController.generateMapToEditor();
		map = mapEditorView.getTiles();
		
	}

	@Test
	public void testLoadMapToEditor() {
		for(int i = 1; i<11; i++){
			for(int j = 1; j<3; j++){
				map[i][j]=true;
			}	
		}
		for(int i = 1; i<5; i+=2){
			map[i][3]=true;
		}
		
		mapController.loadMapToEditor("map");
	}

	@Test
	public void testSaveMap() throws IllegalArgumentException, UnsupportedEncodingException, IOException {	
		
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
		String mapString = "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;1;1;1;1;1;1;1;1;1;1;-\n"
					  + "-;1;1;1;1;1;1;1;1;1;1;-\n"
					  + "-;1;1;1;1;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n"
					  + "-;-;-;-;-;-;-;-;-;-;-;-\n";
		
	   String content = new String(Files.readAllBytes(Paths.get(name + ".java")), "UTF-8");		  
	   Assert.assertEquals(mapString, content);
		
		//teste mit korrekter map ohne Namen
		mapController.saveMap("", map);
	}

}
