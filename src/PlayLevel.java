import engine.core.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Supplier;

public class PlayLevel {
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

    public static void runAllLevelsInFolder(String folderPath, int timer, int marioState, boolean visuals, Supplier<MarioAgent> agentSupplier) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: Folder does not exist: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.out.println("No .txt files found in folder: " + folderPath);
            return;
        }

        // Sort files by name for consistent order
        Arrays.sort(files);

        MarioGame game = new MarioGame();
        int passed = 0;
        int coins = 0;
        int kills = 0;

        System.out.println("Running " + files.length + " levels from folder: " + folderPath);
        System.out.println("========================================================================");

        for (File file : files) {
            System.out.println("\n>>> Running: " + file.getName());
            String levelContent = getLevel(file.getAbsolutePath());
            MarioResult result = game.runGame(agentSupplier.get(), levelContent, timer, marioState, visuals);
            printResults(result);

            if (result.getGameStatus().toString().equals("WIN")) {
                passed++;
            }
            coins += result.getCurrentCoins();
            kills += result.getKillsTotal();
        }

        System.out.println("\n========================================================================");
        System.out.println("SUMMARY: Passed: " + passed + "/" + (files.length));
        System.out.println("Success rate: " + String.format("%.2f", (passed * 100.0 / files.length)) + "%");
        System.out.println("Avg. Coins: " + (coins / (double) files.length));
        System.out.println("Avg. Kills: " + (kills / (double) files.length));
    }

    public static void main(String[] args) {
//        String folderPath = "./levels/MuPlusLambdaEvolution1/";
//        String folderPath = "./levels/MuPlusLambdaEvolution2/";
        String folderPath = "./levels/MuPlusLambdaEvolution3/";
        int timer = 20;
        int marioState = 0;
        boolean visuals = true;
//        runAllLevelsInFolder(folderPath, timer, marioState, visuals, agents.robinBaumgarten.Agent::new);
//        runAllLevelsInFolder(folderPath, timer, marioState, visuals, agents.killer.Agent::new);
        runAllLevelsInFolder(folderPath, timer, marioState, false, agents.killer.Agent::new);
    }
}
