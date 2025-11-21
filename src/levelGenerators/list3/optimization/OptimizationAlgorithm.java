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

    public void reset() {
        rng.setSeed(System.currentTimeMillis());
    }

    protected LevelStructure mutateLevel(LevelStructure level) {
        List<Decorator> decorators = level.getDecorators();
        List<Terrain> terrains = level.getTerrains();
        int r = rng.nextInt(6);
        switch (r) {
            case 0 -> decorators = swapDecorators(decorators);
            case 1 -> terrains = swapTerrains(terrains);
            case 2 -> decorators = replaceDecorator(decorators);
            case 3 -> terrains = replaceTerrain(terrains);
            case 4 -> decorators = mutateRandomDecorators(decorators);
            case 5 -> terrains = mutateRandomTerrains(terrains);
        }
        return new LevelStructure(terrains, decorators);
    }

    protected static LevelStructure generateSingleLevel() {
        return new RandomLevelCreator().generateLevelStructure(LEVEL_WIDTH);
    }

    protected static List<LevelStructure> generateMultipleLevels(int n) {
       List<LevelStructure> levels = new ArrayList<>(n);
       for (int i = 0; i < n; i++) {
           levels.add(generateSingleLevel());
       }
       return levels;
    }

    // ------------------ mutation types ------------------
    /** swap two decorators */
    protected List<Decorator> swapDecorators(List<Decorator> decorators) {
        List<Decorator> newDecorators = new ArrayList<>(decorators);
        swapRandom(newDecorators, rng);
        return newDecorators;
    }

    /** swap two terrain elements */
    protected List<Terrain> swapTerrains(List<Terrain> terrains) {
        List<Terrain> newTerrains = new ArrayList<>(terrains);
        swapRandom(newTerrains, rng);
        return newTerrains;
    }

    /** replace one decorator element by another */
    protected List<Decorator> replaceDecorator(List<Decorator> decorators) {
        List<Decorator> newDecorators = new ArrayList<>(decorators);
        int id = replace(newDecorators, creator::getRandomDecorator, rng);
        Decorator newDecor = newDecorators.get(id);
        if (newDecor instanceof Enemy) {
            int w = newDecor.getWidth();
            int diff = w - 1;
            if (diff > 0) {
                newDecorators.add(id + 1, new EmptyDecor(diff));
                newDecor.setWidth(1);
            }
        }
        return newDecorators;
    }

    /** replace one terrain element by another */
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
        // special case - bullet bill has to have width 1
        if (newTerrain instanceof BulletBill) {
            int w = newTerrain.getWidth();
            if (w > 1) {
                newTerrain.setWidth(1);
                newTerrains.add(id + 1, new Plain(w - 1));
            }
        }
        return newTerrains;
    }

    /** mutate random decorator */
    protected List<Decorator> mutateRandomDecorators(List<Decorator> decorators) {
        List<Decorator> newDecorators = new ArrayList<>(decorators);
        mutateRandomElements(newDecorators, rng);
        return newDecorators;
    }

    /** mutate random terrain */
    protected List<Terrain> mutateRandomTerrains(List<Terrain> terrains) {
        List<Terrain> newTerrains = new ArrayList<>(terrains);
        mutateRandomElements(newTerrains, rng);
        return newTerrains;
    }

    // ------------- mutation generics -------------
    private <T extends Mutable> void mutateRandomElements(List<T> list,  Random rng) {
        int index = rng.nextInt(list.size());
        Mutable elem = list.get(index);
        elem.mutate();
    }

    private <T extends Mutable> int replace(List<T> list, Supplier<T> supplier, Random rng) {
        int index = rng.nextInt(list.size() - 1) + 1;
        int w = list.get(index).getWidth();
        T newItem = supplier.get();
        newItem.setWidth(w);
        list.set(index, newItem);
        return index;
    }

    private static <T> void swapRandom(List<T> list, Random rng) {
        int size = list.size();
        if (size < 2) return;
        int i = rng.nextInt(size - 1) + 1;
        int j = rng.nextInt(size - 1) + 1;
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
