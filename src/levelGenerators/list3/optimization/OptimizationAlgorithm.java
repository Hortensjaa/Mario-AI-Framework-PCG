package levelGenerators.list3.optimization;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;
import levelGenerators.list3.LevelRenderer;
import levelGenerators.list3.RandomLevelCreator;
import levelGenerators.list3.structure.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;


public abstract class OptimizationAlgorithm implements MarioLevelGenerator {
    private final static int LEVEL_WIDTH = 150;
    private final static int LEVEL_HEIGHT = 16;
    private final static int TIMER_INIT = 5 * 60 * 60 * 1000;

    private final LevelRenderer renderer = new LevelRenderer();
    protected final RandomLevelCreator creator;
    protected final Random rng;

    public OptimizationAlgorithm() {
        creator = new RandomLevelCreator();
        rng = new Random(System.nanoTime());
    }

    public abstract LevelStructure getBestLevel();

    protected abstract LevelStructure mutateLevel(LevelStructure level, float mutationChance);

    protected static LevelStructure generateSingleLevel() {
        return new RandomLevelCreator().generateLevelStructure(LEVEL_WIDTH);
    }

    // mutation types
    protected List<Decorator> swapDecorators(List<Decorator> decorators) {
        List<Decorator> newDecorators = new ArrayList<>(decorators);
        swapRandom(newDecorators, rng);
        return newDecorators;
    }

    protected List<Terrain> swapTerrains(List<Terrain> terrains) {
        List<Terrain> newTerrains = new ArrayList<>(terrains);
        swapRandom(newTerrains, rng);
        return newTerrains;
    }

    protected List<Decorator> replaceDecorator(List<Decorator> decorators) {
        List<Decorator> newDecorators = new ArrayList<>(decorators);
        replace(newDecorators, creator::getRandomDecorator, rng);
        return newDecorators;
    }

    protected List<Terrain> replaceTerrain(List<Terrain> terrains) {
        List<Terrain> newTerrains = new ArrayList<>(terrains);
        int id = replace(newTerrains, creator::getRandomTerrain, rng);
        Terrain newTerrain = newTerrains.get(id);
        // special case - pipe has to have width 2
        if (newTerrain instanceof Pipe) {
            int w = newTerrain.getWidth();
            int diff = w - 2;
            if (diff > 0) {
                newTerrains.add(id + 1, new Plain(diff));
                newTerrain.setWidth(2);
            } else {
                newTerrains.set(id, new Plain(w));
            }
        }
        return newTerrains;
    }

    // mutation generics
    private <T extends Mutable> int replace(List<T> list, Supplier<T> supplier, Random rng) {
        int index = rng.nextInt(list.size());
        int w = list.get(index).getWidth();
        T newItem = supplier.get();
        newItem.setWidth(w);
        list.set(index, newItem);
        return index;
    }

    private static <T> void swapRandom(List<T> list, Random rng) {
        int size = list.size();
        if (size < 2) return;
        int i = rng.nextInt(size);
        int j = rng.nextInt(size);
        while (j == i) {
            j = rng.nextInt(size);
        }
        Collections.swap(list, i, j);
    }

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        LevelStructure structure = getBestLevel();
        return renderer.getRenderedLevel(model, structure.getTerrains(), structure.getDecorators());
    }
}
