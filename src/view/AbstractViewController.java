package model.view;
/*
 * @author Hannah, Lisa

 * 
 */

public class AbstractViewController {
	
	private GameWindow gameWindow;

	private ViewState viewState;

	/**
	 * gibt zurück welches Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) zurzeit angezeigt wird 
	 * @return Typ des Fensters
	 */
	abstract ViewState getType();

	/**
	 * neu initailisieren aller GUIs
	 */
	abstract void reset();

	
	/**
	 * zeigen einer stage
	 * @param stage Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
	 */
	abstract void show(Stage stage);

	/**
	 * Wechsel zur übergebenen Stage
	 * @param next Fenster(Menu, Settings, InGame, MapEditor, GamePreperatios, HighScores) welches angezeigt werden soll
	 */
	void changeState(ViewState next) {

	}

}
