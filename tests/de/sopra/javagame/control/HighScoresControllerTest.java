package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.util.HighScore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class HighScoresControllerTest {

    private ControllerChan controllerChan;
    private HighScoresController highScoresController;
    private TestDummy.HighScoreView highScoresView;
    private JavaGame javaGame;


    @Before
    public void setUp() {
        controllerChan = TestDummy.getDummyControllerChan();
        highScoresController = controllerChan.getHighScoresController();
        highScoresView = (TestDummy.HighScoreView) highScoresController.getHighScoresViewAUI();
        javaGame = controllerChan.getJavaGame();
    }

    @Test
    public void testLoadHighScores() throws FileNotFoundException {
        String scoreName = "no1";
        int score = 1000;
        String replayName = "no1Replay";
        String mapName = "newMap";
        String scoreData = scoreName + ";" + score + ";" + replayName;

        HighScore no1 = new HighScore(scoreName, mapName, score, replayName);

        File outFile = new File(MapController.MAP_FOLDER + mapName + ".score");
        PrintWriter out = new PrintWriter(outFile);
        out.println(scoreData);
        out.close();

        highScoresController.loadHighScores(mapName);
        Assert.assertTrue("Ein HighScore wurde gespeichert aber ist nicht vorhanden", highScoresView.getHighScores().contains(no1));

        outFile.delete();


        highScoresController.loadHighScores("notThere");
        Assert.assertTrue("Es gab keine HighScores zu dieser Map", highScoresView.getHighScores().isEmpty());
    }

    @Test
    public void testResetHighScores() throws FileNotFoundException {
        String scoreName = "no1";
        int score = 1000;
        String replayName = "no1Replay";
        String mapName = "newMap";
        String scoreData = scoreName + ";" + score + ";" + replayName;

        // TODO: Add highscore IO util
        // HighScore no1 = new HighScore(scoreName, mapName, score);

        File outFile = new File(MapController.MAP_FOLDER + mapName + ".score");
        PrintWriter out = new PrintWriter(outFile);
        out.println(scoreData);
        out.close();

        highScoresController.resetHighScores(mapName);
        highScoresController.loadHighScores(mapName);
        Assert.assertTrue("Die HighScores zu dieser Map hätten zurückgesetzt werden sollen", highScoresView.getHighScores().isEmpty());

        outFile.delete();

        highScoresController.resetHighScores("notThere");
        highScoresController.loadHighScores("notThere");
        Assert.assertTrue("Es gab keine HighScores zu dieser Map", highScoresView.getHighScores().isEmpty());

    }

}
