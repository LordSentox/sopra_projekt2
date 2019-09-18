package de.sopra.javagame.control;

import de.sopra.javagame.TestDummy;
import de.sopra.javagame.model.JavaGame;
import de.sopra.javagame.util.HighScore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class HighScoresControllerTest {

    private HighScoresController highScoresController;
    private TestDummy.HighScoreView highScoresView;


    @Before
    public void setUp() {
        ControllerChan controllerChan = TestDummy.getDummyControllerChan();
        highScoresController = controllerChan.getHighScoresController();
        highScoresView = (TestDummy.HighScoreView) highScoresController.getHighScoresViewAUI();
        JavaGame javaGame = controllerChan.getJavaGame();
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
    public void testSaveHighscores() throws IOException{
        new File(HighScoresController.SCORE_FOLDER + "arch_of_destiny.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "atoll_of_judgement.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "bridge_of_horrors.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "coral_reef.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "gull_cove.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "island_of_death.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "island_of_shadows.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "skull_island.score").createNewFile();
        new File(HighScoresController.SCORE_FOLDER + "vulcan_island.score").createNewFile();
        
        highScoresController.saveHighScore(new HighScore("SuperPlayer", "bone", 10000, "abc"));
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
