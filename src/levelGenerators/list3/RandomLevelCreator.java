package levelGenerators.list3;
import levelGenerators.list3.structure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class RandomLevelCreator {

    private final Random random;

    public RandomLevelCreator() {
        this.random = new Random(System.nanoTime());
    }

    public LevelStructure generateLevelStructure(int levelWidth) {
        List<Terrain> terrains = generateStructuresList(levelWidth);
        List<Decorator> decorators = generateDecoratorsList(levelWidth);
        return new LevelStructure(terrains, decorators);
    }

    /**
     * Generate list of terrain that cover the entire level width
     */
    public List<Terrain> generateStructuresList(int levelWidth) {
        List<Terrain> terrains = new ArrayList<>();

        int remainingWidth = levelWidth;

        while (remainingWidth > 0) {
            Terrain terrain = getRandomTerrain();
            terrains.add(terrain);
            remainingWidth -= terrain.getWidth();
        }

        if (remainingWidth < 0) {
            Terrain last = terrains.getLast();
            last.setWidth(last.getWidth() + remainingWidth);
        }

        return terrains;
    }

    /**
     * Generate list of decorators that cover the entire level width
     */
    public List<Decorator> generateDecoratorsList(int levelWidth) {
        List<Decorator> decorators = new ArrayList<>();

        int remainingWidth = levelWidth;

        while (remainingWidth > 0) {
            Decorator d = getRandomDecorator();
            decorators.add(d);
            remainingWidth -= d.getWidth();
        }

        if (remainingWidth < 0) {
            Decorator last = decorators.getLast();
            last.setWidth(last.getWidth() + remainingWidth);
        }

        return decorators;
    }

    /**
     * Generate a random terrain
     */
    public Terrain getRandomTerrain() {
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
    public Decorator getRandomDecorator() {
        int choice = random.nextInt(4);
        return switch (choice) {
            case 0 -> new Enemy();
            case 1 -> new Bumpable();
            case 2 -> new Coins();
            case 3 -> new EmptyDecor();
            default -> new EmptyDecor();
        };
    }
}
