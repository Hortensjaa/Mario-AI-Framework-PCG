package levelGenerators.list3.generator;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements MarioLevelGenerator {
    private static final int BASE_FLOOR_OFFSET = 2;
    private static final int SAFE_START = 5;
    private static final int SAFE_END = 5;

    private final Random random = new Random();
    private MarioLevelModel model;

    private List<Structure> structures;
    private List<Decorator> decorators;

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        this.model = model;

        // Generate lists of structures and decorators
        generateStructuresList(model.getWidth());
        generateDecoratorsList(model.getWidth());

        // Render the level from the lists
        renderLevel(model);

        return model.getMap();
    }

    /**
     * Generate list of structures that cover the entire level width
     */
    private void generateStructuresList(int levelWidth) {
        structures = new ArrayList<>();

        int remainingWidth = levelWidth - SAFE_START - SAFE_END;

        while (remainingWidth > 0) {
            Structure structure = generateRandomStructure();
            structures.add(structure);
            remainingWidth -= structure.getWidth();
        }
    }

    /**
     * Generate list of decorators that match the structures
     */
    private void generateDecoratorsList(int levelWidth) {
        decorators = new ArrayList<>();

        int remainingWidth = levelWidth - SAFE_START - SAFE_END;

        while (remainingWidth > 0) {
            Decorator d = generateRandomDecorator();
            decorators.add(d);
            remainingWidth -= d.getWidth();
        }
    }

    /**
     * Render the level from the structures and decorators lists
     */
    private void renderLevel(MarioLevelModel model) {
        model.clearMap();

        int width = model.getWidth();
        int height = model.getHeight();
        int baseFloorLevel = height - BASE_FLOOR_OFFSET;

        // Place Mario at start
        model.setBlock(0, baseFloorLevel - 1, MarioLevelModel.MARIO_START);

        // Generate initial flat ground for Mario to start on
        for (int i = 0; i < SAFE_START; i++) {
            for (int y = baseFloorLevel; y < height; y++) {
                model.setBlock(i, y, MarioLevelModel.GROUND);
            }
        }

        int currentX = SAFE_START;

        // Render structures
        for (Structure structure : structures) {
            structure.generate(model, currentX, baseFloorLevel);
            currentX += structure.getWidth();
        }

        currentX = SAFE_START;
        for (Decorator decorator : decorators) {
            int w = decorator.getWidth();
            int maxFloorY = Integer.MAX_VALUE;
            for (int i = 0; i < w; i++) {
                maxFloorY = Math.min(maxFloorY, getFloorY(currentX + i));
            }
            decorator.generate(model, currentX, maxFloorY);
            currentX += decorator.getWidth();
        }

        // Generate final flat ground before exit
        int finalGroundWidth = Math.min(5, width - currentX - 1);
        for (int i = 0; i < finalGroundWidth; i++) {
            for (int y = baseFloorLevel; y < height; y++) {
                model.setBlock(currentX + i, y, MarioLevelModel.GROUND);
            }
        }

        // Place exit flag
        currentX += finalGroundWidth;
        if (currentX < width) {
            model.setBlock(currentX, baseFloorLevel - 1, MarioLevelModel.MARIO_EXIT);
        }
    }

    /**
     * Generate a random structure
     */
    private Structure generateRandomStructure() {
        int choice = random.nextInt(4);
        return switch (choice) {
            case 0 -> new Gap();
            case 1 -> new Plain();
            case 2 -> new Pipe();
            case 3 -> new Hill();
            default -> new Plain();
        };
    }

    /**
     * Generate a random decorator
     */
    private Decorator generateRandomDecorator() {
        int choice = random.nextInt(4);
        return switch (choice) {
            case 0 -> new Enemy();
            case 1 -> new Bumpable();
            case 2 -> new Coins();
            case 3 -> new EmptyDecor();
            default -> new EmptyDecor();
        };
    }

    @Override
    public String getGeneratorName() {
        return "RandomLevelGenerator";
    }

    private int getFloorY(int x) {
        for (int y = 0; y < model.getHeight(); y++) {
            if (model.getBlock(x, y) != MarioLevelModel.EMPTY && model.getBlock(x, y) != MarioLevelModel.COIN) {
                return y;
            }
        }
        return model.getHeight() - BASE_FLOOR_OFFSET;
    }
}
