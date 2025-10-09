import engine.core.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenerateLevel {
    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
        }
        return content;
    }

    public static void main(String[] args) {
        MarioGame game = new MarioGame();

        /* todo choose map generator to create a level (uncomment the one you want to use): */
        MarioLevelGenerator generator = new levelGenerators.notch.LevelGenerator();    // original generator by Notch
//        MarioLevelGenerator generator = new levelGenerators.benWeber.LevelGenerator(); // winner of the 2010 PCG Mario AI Competition: makes multiple passes along the level, in each pass adding a new type of level item
//        MarioLevelGenerator generator = new levelGenerators.linear.LevelGenerator();     // flat ground with holes, occasional pipes and monsters
//        MarioLevelGenerator generator = new levelGenerators.sampler.LevelGenerator();  // creates levels by sampling parts of original levels
//        MarioLevelGenerator generator = new levelGenerators.random.LevelGenerator();   // places objects randomly

        /* todo choose level from generator or file */
        String level = generator.getGeneratedLevel(new MarioLevelModel(150, 16), new MarioTimer(5 * 60 * 60 * 1000));
        //String level = getLevel("./levels/original/lvl-1.txt");

        /* todo choose agent to run (uncomment the one you want to use): */
//        MarioAgent marioagent = new agents.human.Agent();            // Human agent - play by yourself: LEFT/RIGHT arrows to move, S to jump, A to shoot fireballs
        MarioAgent marioagent = new agents.robinBaumgarten.Agent();  // 46564.8 progress; 40/40 levels passed; A*
//        MarioAgent marioagent = new agents.andySloane.Agent();       // 44735.5 progress; 38/40 levels passed; A*
//        MarioAgent marioagent = new agents.trondEllingsen.Agent();   // 20599.2 progress; 11/40 levels passed; Rule-based
//        MarioAgent marioagent = new agents.spencerSchumann.Agent();  // 17010.5 progress;  8/40 levels passed; Rule-based
//        MarioAgent marioagent = new agents.sergeyPolikarpov.Agent(); // 12203.3 progress;  3/40 levels passed; Neural Network
//        MarioAgent marioagent = new agents.michal.Agent();           //  6571.8 progress;  3/40 levels passed; State Machine
//        MarioAgent marioagent = new agents.glennHartmann.Agent();    //  1060.0 progress;  0/40 levels passed; Rule-based
//        MarioAgent marioagent = new agents.sergeyKarakovskiy.Agent();// max run and jump to the right
//        MarioAgent marioagent = new agents.random.Agent();           // random agent (much higher probabilities to run/jump right)
//        MarioAgent marioagent = new agents.doNothing.Agent();        // stays in place

        MarioResult runresult = game.runGame(marioagent, level, 500, 0, true);
        /* todo here you can do something with the result */

        printResults(runresult);
    }
}
