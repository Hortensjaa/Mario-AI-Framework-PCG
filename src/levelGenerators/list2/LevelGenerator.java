package levelGenerators.list2;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

import java.util.*;

/**
 * Grammar-based Mario level generator with weighted production rules.
 */
public class LevelGenerator implements MarioLevelGenerator {
    private final Random random = new Random();
    private final TerrainGenerator grammarGenerator = new TerrainGenerator();
    private final HazardDecorator hazardDecorator = new HazardDecorator();
    private MarioLevelModel model;

    private static final int MAX_PIT_WIDTH = 4;
    private static final int MAX_JUMP_HEIGHT = 4;
    private static int BASE_FLOOR;

    private int currentFloorY;
    private int currentX = 0;

    public LevelGenerator() {}

    @Override
    public String getGeneratedLevel(MarioLevelModel m, MarioTimer timer) {
        model = m;
        model.clearMap();
        BASE_FLOOR = model.getHeight() - 4;

        final int width = model.getWidth();
        final int height = model.getHeight();
        currentFloorY = height - 4;

        model.setBlock(0, 0, MarioLevelModel.MARIO_START);

        // 1. Generate grammar string using weighted rules
        String terrain = grammarGenerator.generate(64);
        System.out.println("Base terrain: " + terrain);

        String hazards = hazardDecorator.decorate(terrain);
        System.out.println("Hazards: " + hazards);

        // 2. Build terrain
        currentX = 0;
        for (int i = 0; i < terrain.length(); i++) {
            if (currentX >= width) break;

            int xBefore = currentX;

            // terrain
            switch (terrain.charAt(i)) {
                case 'P' -> {
                    if (i >= 1 && terrain.charAt(i-1) != 'P')
                        currentX += buildPit();
                    else
                        currentX += buildGround();
                }
                case 'G' -> currentX += buildGround();
                case 'J' -> currentX += buildJump();
                case 'T' -> currentX += buildTube();
                case 'B' -> currentX += buildBulletBill();
            }

            // hazards
            switch (hazards.charAt(i)) {
                case 'E' -> placeEnemy(xBefore);
                case 'C' -> placeCoins(xBefore);
                case 'B' -> placeBlocks(xBefore);
                case 'X' -> {
                    placeCoins(xBefore);
                    placeEnemy(xBefore);
                }
                case 'Y' -> {
                    placeBlocks(xBefore);
                    placeEnemy(xBefore);
                }
                case '0' -> {}
            }
        }

        buildFlatTerrain( currentX-5, 5);
        model.setBlock(currentX, BASE_FLOOR+1, MarioLevelModel.MARIO_EXIT);

        return model.getMap();
    }

    // ==== BUILDERS ====
    private int buildPit() {
        int pitWidth = random.nextInt(MAX_PIT_WIDTH - 1) + 1;
        currentFloorY = BASE_FLOOR;
        return pitWidth;
    }

    private int buildJump() {
        int jumpHeight = random.nextInt(MAX_JUMP_HEIGHT - 1) + 1;
        int jumpLength = random.nextInt(4) + 2;
        currentFloorY = Math.max(3, currentFloorY - jumpHeight);
        buildFlatTerrain(currentX, jumpLength);
        currentFloorY = Math.min(BASE_FLOOR, currentFloorY + jumpHeight);
        return jumpLength;
    }

    private int buildGround() {
        int length = random.nextInt(4) + 2;
        buildFlatTerrain(currentX, length);
        return length;
    }

    private int buildTube() {
        int tubeHeight = random.nextInt(MAX_JUMP_HEIGHT - 2) + 2;
        char tubeType = random.nextDouble() < 0.05 ? MarioLevelModel.PIPE_FLOWER : MarioLevelModel.PIPE;
        for (int x = currentX + 1; x < currentX + 3; x++) {
            for (int y = currentFloorY - tubeHeight; y < currentFloorY; y++) {
                model.setBlock(x, y, tubeType);
            }
        }
        buildFlatTerrain(currentX, 4);
        return 4;
    }

    private int buildBulletBill() {
        int tubeHeight = random.nextInt(MAX_JUMP_HEIGHT - 1) + 1;
        for (int y = currentFloorY - tubeHeight; y < currentFloorY; y++) {
            model.setBlock(currentX + 1, y, MarioLevelModel.BULLET_BILL);
        }
        buildFlatTerrain(currentX, 3);
        return 3;
    }

    // ==== DECORATORS ====
    private void placeCoins(int xBefore) {
        int segmentLength = currentX - xBefore;
        if (segmentLength <= 1) return;

        // Decide rectangle size: width (cols) and height (rows)
        int rectCols = 1 + random.nextInt(Math.max(1, Math.min(segmentLength - 1, 6))); // limit width to keep reasonable
        int rectRows = 1 + random.nextInt(3); // 1..3 rows of coins

        // choose random start so the rectangle fits in the segment
        int startX = xBefore + random.nextInt(Math.max(1, segmentLength - rectCols + 1));
        int endX = startX + rectCols;

        // Height above the floor for the top row of coins
        int topOffset = 1 + random.nextInt(Math.max(1, MAX_JUMP_HEIGHT - 1));

        // Place the rectangle of coins. For each column, compute floor and stack rows above it.
        for (int x = startX; x < endX; x++) {
            int floorY = getFloorY(x);
            // If it's a pit (floorY == 0), skip this column
            if (floorY <= 0) continue;
            int topY = floorY - topOffset; // y coordinate for top coin row
            if (topY < 0) topY = 0; // clamp to map

            for (int r = 0; r < rectRows; r++) {
                int y = topY - r;
                if (y < 0) break;
                model.setBlock(x, y, MarioLevelModel.COIN);
            }
        }
    }

    private void placeBlocks(int xBefore) {
        int segmentLength = currentX - xBefore;
        if (segmentLength <= 1) return;

        int lineHeight = random.nextInt(MAX_JUMP_HEIGHT - 2) + 3;

        int lineLength = 2 + random.nextInt(Math.max(1, segmentLength / 2));

        int startX = xBefore + random.nextInt(Math.max(1, segmentLength - lineLength + 1));
        int endX = startX + lineLength;

        char[] blockTypes =  MarioLevelModel.getBumpableTiles();
        int blockId = random.nextInt(Math.max(1, blockTypes.length));

        for (int x = startX; x < endX; x++) {
            model.setBlock(x, getFloorY(x) - lineHeight, blockTypes[blockId]);
        }
    }

    private void placeEnemy(int xBefore) {
        int segmentLength = currentX - xBefore;
        int enemyX = xBefore + segmentLength / 2;
        model.setBlock(enemyX, getFloorY(enemyX) - 1, MarioLevelModel.GOOMBA);
        if (segmentLength >= 4) {
            model.setBlock(enemyX + 1, getFloorY(enemyX) - 1, MarioLevelModel.GREEN_KOOPA);
        }
    }

    // ==== helpers ====
    private void buildFlatTerrain(int startX, int length) {
        for (int x = startX; x < startX + length && x < model.getWidth(); x++) {
            for (int y = currentFloorY; y < model.getHeight(); y++) {
                model.setBlock(x, y, MarioLevelModel.GROUND);
            }
        }
    }

    private int getFloorY(int x) {
        for (int y = 0; y < model.getHeight(); y++) {
            if (model.getBlock(x, y) != MarioLevelModel.EMPTY) {
                return y;
            }
        }
        return 0; // pit
    }

    @Override
    public String getGeneratorName() {
        return "LinearProbabilisticGrammarGenerator";
    }
}
